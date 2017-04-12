package com.mutualmobile.barricade.sample;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import com.mutualmobile.barricade.Barricade;
import com.mutualmobile.barricade.BarricadeConfig;
import com.mutualmobile.barricade.activity.BarricadeActivity;
import com.mutualmobile.barricade.response.BarricadeResponse;
import com.mutualmobile.barricade.response.BarricadeResponseSet;
import com.mutualmobile.barricade.utils.TestAssetFileManager;
import java.util.HashMap;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class) @LargeTest public class BarricadeActivityTest {
  @Rule public ActivityTestRule<BarricadeActivity> mActivityRule =
      new ActivityTestRule<>(BarricadeActivity.class);

  private static Barricade barricade;

  @BeforeClass public static void setup() {
    barricade =
        new Barricade.Builder(BarricadeConfig.getInstance(), new TestAssetFileManager()).install();
  }

  @Test public void verifyEndpoints() {
    int count = 0;
    for (String endpoint : barricade.getConfig().keySet()) {
      onView(withRecyclerView(com.mutualmobile.barricade.R.id.endpoint_rv).atPosition(count)).check(
          matches(hasDescendant(withText(endpoint))));
      count++;
    }
  }

  @Test public void verifyResponseForEndpoints() {
    int endpointCount = 0;
    HashMap<String, BarricadeResponseSet> hashMap = barricade.getConfig();
    for (String endpoint : hashMap.keySet()) {
      int responseCount = 0;
      onView(withId(R.id.endpoint_rv)).perform(
          RecyclerViewActions.actionOnItemAtPosition(endpointCount, click()));
      for (BarricadeResponse response : hashMap.get(endpoint).responses) {
        onView(withRecyclerView(com.mutualmobile.barricade.R.id.endpoint_responses_rv).atPosition(
            responseCount)).check(matches(hasDescendant(withText(response.responseFileName))));
        responseCount++;
      }
      Espresso.pressBack();
    }
  }

  @Test public void verifyDelayTimeDialogShowsCorrectDelay() {
    onView(withId(R.id.menu_delay)).perform(click());
    onView(withId(R.id.delay_value_edittext)).check(matches(withText(Long.toString(barricade.getDelay()))));
    Espresso.pressBack();
  }

  @Test public void verifyResetDialogIsDisplayed() {
    onView(withId(R.id.menu_reset)).perform(click());
    onView(withText(R.string.reset_message)).check(matches(isDisplayed()));
    Espresso.pressBack();
  }

  public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
    return new RecyclerViewMatcher(recyclerViewId);
  }
}
