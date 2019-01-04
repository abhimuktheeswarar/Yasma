package msa.yasma


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
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
class PostScreenInstrumentedTest {

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java)

    lateinit var testYasmaApplication: TestYasmaApplication


    @Before
    fun setUp() {

        testYasmaApplication = activityTestRule.activity.application as TestYasmaApplication

    }

    @Test
    fun postScreenTest() {

        val postsJson = getInstrumentation().context.resources.openRawResource(msa.yasma.test.R.raw.posts).readBytes()
            .toString(Charset.defaultCharset())
        val mockPostsReponse = MockResponse()
            .setResponseCode(200)
            .setBody(postsJson)
        testYasmaApplication.getMockWebServer().enqueue(mockPostsReponse)

        val usersJson = getInstrumentation().context.resources.openRawResource(msa.yasma.test.R.raw.users).readBytes()
            .toString(Charset.defaultCharset())
        val mockUsersReponse = MockResponse()
            .setResponseCode(200)
            .setBody(usersJson)
        testYasmaApplication.getMockWebServer().enqueue(mockUsersReponse)


        Thread.sleep(1000)

        val viewGroup = onView(
            allOf(
                withId(R.id.constraintLayout_item_simple),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.epoxyRecyclerView),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )

        viewGroup.check(matches(isDisplayed()))

        val constraintLayout = onView(
            allOf(
                withId(R.id.constraintLayout_item_simple),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.epoxyRecyclerView),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        constraintLayout.perform(click())


        val postDetailJson =
            getInstrumentation().context.resources.openRawResource(msa.yasma.test.R.raw.postdetail).readBytes()
                .toString(Charset.defaultCharset())
        val mockPostDetailReponse = MockResponse()
            .setResponseCode(200)
            .setBody(postDetailJson)
        testYasmaApplication.getMockWebServer().enqueue(mockPostDetailReponse)

        val postCommentsJson =
            getInstrumentation().context.resources.openRawResource(msa.yasma.test.R.raw.postcomments).readBytes()
                .toString(Charset.defaultCharset())
        val mockPostCommentsReponse = MockResponse()
            .setResponseCode(200)
            .setBody(postCommentsJson)
        testYasmaApplication.getMockWebServer().enqueue(mockPostCommentsReponse)

        val userDetailJson =
            getInstrumentation().context.resources.openRawResource(msa.yasma.test.R.raw.userdetail).readBytes()
                .toString(Charset.defaultCharset())
        val mockUserDetailReponse = MockResponse()
            .setResponseCode(200)
            .setBody(userDetailJson)
        testYasmaApplication.getMockWebServer().enqueue(mockUserDetailReponse)


        Thread.sleep(1000)

        val linearLayout = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.epoxyRecyclerView),
                        childAtPosition(
                            withId(R.id.swipeRefreshLayout),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        linearLayout.check(matches(isDisplayed()))

        val linearLayout2 = onView(
            allOf(
                childAtPosition(
                    childAtPosition(
                        withId(R.id.epoxyRecyclerView),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )

        linearLayout2.check(matches(isDisplayed()))

        val linearLayout3 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.epoxyRecyclerView),
                        childAtPosition(
                            withId(R.id.swipeRefreshLayout),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        linearLayout3.check(matches(isDisplayed()))

        val linearLayout4 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.epoxyRecyclerView),
                        childAtPosition(
                            withId(R.id.swipeRefreshLayout),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )

        linearLayout4.check(matches(isDisplayed()))
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
