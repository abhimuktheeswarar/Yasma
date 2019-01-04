package msa.yasma


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.nio.charset.Charset

@LargeTest
@RunWith(AndroidJUnit4::class)
class AlbumScreenInstrumentedTest {

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    lateinit var testYasmaApplication: TestYasmaApplication

    @Before
    fun setUp() {

        testYasmaApplication = activityTestRule.activity.application as TestYasmaApplication

    }

    @Test
    fun albumScreenTest() {

        val postsJson =
            InstrumentationRegistry.getInstrumentation().context.resources.openRawResource(msa.yasma.test.R.raw.posts)
                .readBytes()
                .toString(Charset.defaultCharset())
        val mockPostsReponse = MockResponse()
            .setResponseCode(200)
            .setBody(postsJson)
        testYasmaApplication.getMockWebServer().enqueue(mockPostsReponse)

        val usersJson =
            InstrumentationRegistry.getInstrumentation().context.resources.openRawResource(msa.yasma.test.R.raw.users)
                .readBytes()
                .toString(Charset.defaultCharset())
        val mockUsersReponse = MockResponse()
            .setResponseCode(200)
            .setBody(usersJson)
        testYasmaApplication.getMockWebServer().enqueue(mockUsersReponse)

        Thread.sleep(2000)

        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.albumListFragment), withContentDescription("Albums"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigationView),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )

        bottomNavigationItemView.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(2000)

        val progressBar = onView(
            allOf(
                withId(R.id.progressBar_list),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.epoxyRecyclerView),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        progressBar.check(matches(isDisplayed()))


        val albumsJson =
            InstrumentationRegistry.getInstrumentation().context.resources.openRawResource(msa.yasma.test.R.raw.albums)
                .readBytes()
                .toString(Charset.defaultCharset())
        val mockAlbumsReponse = MockResponse()
            .setResponseCode(200)
            .setBody(albumsJson)
        testYasmaApplication.getMockWebServer().enqueue(mockAlbumsReponse)

        Thread.sleep(2000)

        val viewGroup = onView(
            allOf(
                withId(R.id.constraintLayout_item_simple),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.epoxyRecyclerView),
                        4
                    ),
                    0
                ),
                isDisplayed()
            )
        )

        Thread.sleep(2000)

        viewGroup.check(matches(isDisplayed()))

        val constraintLayout = onView(
            allOf(
                withId(R.id.constraintLayout_item_simple),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.epoxyRecyclerView),
                        4
                    ),
                    0
                ),
                isDisplayed()
            )
        )

        constraintLayout.perform(click())

        val albumDetailJson =
            InstrumentationRegistry.getInstrumentation()
                .context.resources.openRawResource(msa.yasma.test.R.raw.albumdetail).readBytes()
                .toString(Charset.defaultCharset())
        val mockAlbumtDetailReponse = MockResponse()
            .setResponseCode(200)
            .setBody(albumDetailJson)
        testYasmaApplication.getMockWebServer().enqueue(mockAlbumtDetailReponse)

        val photosJson =
            InstrumentationRegistry.getInstrumentation().context.resources.openRawResource(msa.yasma.test.R.raw.photos)
                .readBytes()
                .toString(Charset.defaultCharset())
        val mockPhotosReponse = MockResponse()
            .setResponseCode(200)
            .setBody(photosJson)
        testYasmaApplication.getMockWebServer().enqueue(mockPhotosReponse)

        val userDetailJson =
            InstrumentationRegistry.getInstrumentation()
                .context.resources.openRawResource(msa.yasma.test.R.raw.userdetail).readBytes()
                .toString(Charset.defaultCharset())
        val mockUserDetailReponse = MockResponse()
            .setResponseCode(200)
            .setBody(userDetailJson)
        testYasmaApplication.getMockWebServer().enqueue(mockUserDetailReponse)


        Thread.sleep(2000)

        val constraintLayout2 = onView(
            allOf(
                withId(R.id.constraintLayout_item_photo),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.epoxyRecyclerView),
                        3
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        constraintLayout2.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
