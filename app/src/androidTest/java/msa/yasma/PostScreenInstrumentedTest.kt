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
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class PostScreenInstrumentedTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun postScreenTest() {

        Thread.sleep(2000)

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

        Thread.sleep(2000)

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
