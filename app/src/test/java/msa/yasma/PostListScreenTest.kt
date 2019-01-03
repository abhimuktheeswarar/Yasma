package msa.yasma

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject
import kotlinx.serialization.json.JSON
import kotlinx.serialization.list
import msa.data.DataRepository
import msa.data.DataStoreFactory
import msa.domain.actionstate.PostAction
import msa.domain.actionstate.PostListState
import msa.domain.core.State
import msa.domain.entities.Post
import msa.domain.entities.User
import msa.domain.statemachine.PostListStateMachine
import msa.domain.usecases.GetPosts
import msa.yasma.common.*
import msa.yasma.post.list.PostListViewModel
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import timber.log.Timber

/**
 * Created by Abhi Muktheeswarar.
 */

class PostListScreenTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var mockWebServer: MockWebServer
    private lateinit var postListViewModel: PostListViewModel

    @Before
    @Throws
    fun setUp() {

        mockWebServer = MockWebServer()
        mockWebServer.start()

        val httpUrl = mockWebServer.url("/")

        val dataStoreFactory = DataStoreFactory(Mockito.mock(Context::class.java), httpUrl.toString())
        val dataRepository = DataRepository(dataStoreFactory)

        val getPosts = GetPosts(dataRepository, Schedulers.trampoline(), Schedulers.trampoline())

        val postListStateMachine = PostListStateMachine(getPosts)

        postListViewModel = PostListViewModel(postListStateMachine)

    }

    @Test
    fun runTests() {

        Timber.plant(object : Timber.Tree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                println(message)
                t?.printStackTrace()
            }
        })

        val screen = PostListScreen(postListViewModel)

        postListViewModel.state.observeForever {
            screen.stateSubject.onNext(it!!)
        }

        mockWebServer.use {
            PostListStateMachineSpec(
                config = ScreenConfig(it),
                screen = screen,
                stateHistory = StateHistory(screen)
            ).runTests()
        }
    }

    @After
    @Throws
    fun tearDown() {
        mockWebServer.shutdown()
    }

    class PostListScreen(
        private val viewModel: PostListViewModel
    ) : Screen, StateRecorder {

        val stateSubject: Subject<State> = ReplaySubject.create()

        fun loadData() {
            Observable.just(PostAction.LoadPostsAction).subscribe(viewModel.input)
        }

        fun pullToRefresh() {
            Observable.just(PostAction.RefreshPostsAction).subscribe(viewModel.input)
        }

        override fun renderedStates(): Observable<State> = stateSubject

    }
}

class PostListStateMachineSpec(
    private val screen: PostListScreenTest.PostListScreen,
    private val stateHistory: StateHistory,
    private val config: ScreenConfig
) {

    private fun given(message: String, block: Given.() -> Unit) {
        val given = Given(screen, stateHistory, message)
        given.block()
    }

    fun runTests() {

        val server = config.mockWebServer

        given("the user opens the app") {

            val posts = JSON.stringify(Post.serializer().list, DummyData.getPosts())

            val mockPostsReponse = MockResponse()
                .setResponseCode(200)
                .setBody(posts)

            val users = JSON.stringify(User.serializer().list, DummyData.getUsers())

            val mockUsersReponse = MockResponse()
                .setResponseCode(200)
                .setBody(users)

            server.enqueue(mockPostsReponse)
            server.enqueue(mockUsersReponse)

            Thread.sleep(2000)

            on("user opens app") {

                screen.loadData()

                "shows loading" byRendering PostListState(loading = true)

                "shows posts data" byRendering PostListState(loading = false, posts = DummyData.getPostsWithUser())
            }
        }

        given("the user pulls to refresh") {

            val posts = JSON.stringify(Post.serializer().list, DummyData.getPosts())

            val mockPostsReponse = MockResponse()
                .setResponseCode(200)
                .setBody(posts)

            val users = JSON.stringify(User.serializer().list, DummyData.getUsers())

            val mockUsersReponse = MockResponse()
                .setResponseCode(200)
                .setBody(users)

            server.enqueue(mockPostsReponse)
            server.enqueue(mockUsersReponse)

            Thread.sleep(2000)

            on("refreshing") {

                screen.pullToRefresh()

                "shows refreshing" byRendering PostListState(
                    loading = false,
                    refreshing = true,
                    posts = DummyData.getPostsWithUser()
                )

                "shows refreshed data" byRendering PostListState(
                    loading = false,
                    refreshing = false,
                    posts = DummyData.getPostsWithUser()
                )
            }
        }
    }
}