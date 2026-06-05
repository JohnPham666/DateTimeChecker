package com.trieupk.dateTimeCheckerMobile;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@Config(sdk = 33, qualifiers = "w1080dp-h2400dp")
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testValidDate_Espresso() {
        onView(withId(R.id.etDay)).perform(replaceText("29"));
        onView(withId(R.id.etMonth)).perform(replaceText("2"));
        onView(withId(R.id.etYear)).perform(replaceText("2024"));

        onView(withId(R.id.btnCheck)).perform(click());

        onView(withId(R.id.tvResult))
                .check(matches(isDisplayed()))
                .check(matches(withText("29/02/2024 is correct date time!")));
    }

    @Test
    public void testInvalidDate_Espresso() {
        onView(withId(R.id.etDay)).perform(replaceText("29"));
        onView(withId(R.id.etMonth)).perform(replaceText("2"));
        onView(withId(R.id.etYear)).perform(replaceText("2023"));

        onView(withId(R.id.btnCheck)).perform(click());

        onView(withId(R.id.tvResult))
                .check(matches(isDisplayed()))
                .check(matches(withText("29/02/2023 is not a valid date!")));
    }

    @Test
    public void testEmptyData_Espresso() {
        // Chỉ bấm nút chứ không gõ gì cả
        onView(withId(R.id.btnCheck)).perform(click());

        onView(withId(R.id.tvResult))
                .check(matches(isDisplayed()))
                .check(matches(withText("Invalid input! Please enter integers only.")));
    }
}
