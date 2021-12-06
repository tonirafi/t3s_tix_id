package com.tes_tix_id.android.ui.home


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.tes_tix_id.android.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class HomeActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(HomeActivity::class.java)

    @Test
    fun homeActivityTest() {
        val constraintLayout = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.swipe_target),
                        childAtPosition(
                            withId(R.id.swipeToLoadLayout),
                            2
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        constraintLayout.perform(click())

        val constraintLayout2 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.swipe_target),
                        childAtPosition(
                            withId(R.id.swipeToLoadLayout),
                            2
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        constraintLayout2.perform(click())

        val constraintLayout3 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.swipe_target),
                        childAtPosition(
                            withId(R.id.swipeToLoadLayout),
                            2
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        constraintLayout3.perform(click())

        val f = onView(
            allOf(
                withId(R.id.btn_reload), withText("Reload"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.FrameLayout")),
                        1
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        f.perform(click())

        val f2 = onView(
            allOf(
                withId(R.id.btn_reload), withText("Reload"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.FrameLayout")),
                        1
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        f2.perform(click())
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
