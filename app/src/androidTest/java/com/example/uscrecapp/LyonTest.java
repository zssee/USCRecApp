package com.example.uscrecapp;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LyonTest {
    /**
     * Perform action of waiting for a specific view id.
     * @param viewId The id of the view to wait for.
     * @param millis The timeout of until when to wait for.
     */
//    public static ViewAction waitId(final int viewId, final long millis) {
//        return new ViewAction() {
//            @Override
//            public Matcher<View> getConstraints() {
//                return isRoot();
//            }
//
//            @Override
//            public String getDescription() {
//                return "wait for a specific view with id <" + viewId + "> during " + millis + " millis.";
//            }
//
//            @Override
//            public void perform(final UiController uiController, final View view) {
//                uiController.loopMainThreadUntilIdle();
//                final long startTime = System.currentTimeMillis();
//                final long endTime = startTime + millis;
//                final Matcher<View> viewMatcher = withId(viewId);
//
//                do {
//                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
//                        // found view with required ID
//                        if (viewMatcher.matches(child)) {
//                            return;
//                        }
//                    }
//
//                    uiController.loopMainThreadForAtLeast(50);
//                }
//                while (System.currentTimeMillis() < endTime);
//
//                // timeout happens
//                throw new PerformException.Builder()
//                        .withActionDescription(this.getDescription())
//                        .withViewDescription(HumanReadables.describe(view))
//                        .withCause(new TimeoutException())
//                        .build();
//            }
//        };
//    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void lyonTest() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.userName),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.userName),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("Syuen See"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.logIn), withText("Log In"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton.perform(click());
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.home), withContentDescription("Home"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomNav),
                                        0),
                                1),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.lyonBtn),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
