package com.project.vins.park;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

/**
 * Created by Vins on 09/05/2016.
 */
@RunWith(AndroidJUnit4.class)
public class EmplacementVoitureViewTest {


    @Rule
    public ActivityTestRule<EmplacementVoitureView> mActivityRule = new ActivityTestRule(EmplacementVoitureView.class);


    @Test
    public void checkDebut() {
        onView(withId(R.id.jeRecupere)).check(matches(isDisplayed()));
        onView(withId(R.id.jeMeGare)).check(matches(isDisplayed()));
        onView(withId(R.id.txtDernierEmplacement)).check(matches(isDisplayed()));
    }

    @Test
    public void checkConfirmation(){
        onView(withId(R.id.jeMeGare)).perform(click());

        onView(withId(R.id.txtHautConfirmation)).check(matches(isDisplayed()));
        onView(withId(R.id.txtHautConfirmation)).check(matches(isDisplayed()));
        onView(withId(R.id.validerConfirmation)).check(matches(isDisplayed()));
        onView(withId(R.id.annulerConfirmation)).check(matches(isDisplayed()));
    }

    @Test
    public void cancelConfirmation(){
        onView(withId(R.id.jeMeGare)).perform(click());
        onView(withId(R.id.annulerConfirmation)).perform(click());

        onView(withId(R.id.txtDernierEmplacement)).check(matches(isDisplayed()));
    }

    @Test
    public void checkProgress(){
        onView(withId(R.id.jeMeGare)).perform(click());
        onView(withId(R.id.validerConfirmation)).perform(click());

        onView(withId(R.id.titreLocalisation)).check(matches(isDisplayed()));
        onView(withId(R.id.imageLocalisation)).check(matches(isDisplayed()));
        onView(withId(R.id.progressCancelProgress)).check(matches(isDisplayed()));
    }

    @Test
    public void cancelProgress(){
        onView(withId(R.id.jeMeGare)).perform(click());
        onView(withId(R.id.validerConfirmation)).perform(click());
        onView(withId(R.id.progressCancelProgress)).perform(click());

        onView(withId(R.id.titreLocalisation)).check(doesNotExist());
        onView(withId(R.id.imageLocalisation)).check(doesNotExist());
        onView(withId(R.id.progressCancelProgress)).check(doesNotExist());
        onView(withId(R.id.txtDernierEmplacement)).check(matches(isDisplayed()));
    }

    @Test
    public void checkPopupProgress(){
        onView(withId(R.id.jeMeGare)).perform(click());
        onView(withId(R.id.validerConfirmation)).perform(click());
        onView(withId(R.id.progressCancelProgress)).perform(pressBack());

        onView(withId(R.id.layoutProgress)).check(matches(isDisplayed()));
        onView(withId(R.id.imgLoadingBarre)).check(matches(isDisplayed()));
        onView(withId(R.id.cancelProgress)).check(matches(isDisplayed()));
    }

    @Test
    public void popupCancelProgress(){
        onView(withId(R.id.jeMeGare)).perform(click());
        onView(withId(R.id.validerConfirmation)).perform(click());
        onView(withId(R.id.progressCancelProgress)).perform(pressBack());
        onView(withId(R.id.cancelProgress)).perform(click());

        onView(withId(R.id.layoutProgress)).check(matches(not(isDisplayed())));
        onView(withId(R.id.imgLoadingBarre)).check(matches(not(isDisplayed())));
        onView(withId(R.id.cancelProgress)).check(matches(not(isDisplayed())));
    }

}
