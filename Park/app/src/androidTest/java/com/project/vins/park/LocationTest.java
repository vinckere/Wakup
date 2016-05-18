package com.project.vins.park;

import android.app.Instrumentation;
import android.location.Location;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.google.android.gms.internal.zzir.runOnUiThread;
import static org.hamcrest.Matchers.not;

/**
 * Created by Vins on 10/05/2016.
 */
public class LocationTest {

    private IntentServiceIdlingResource idlingResource;

    @Rule
    public ActivityTestRule<EmplacementVoitureView> mActivityRule = new ActivityTestRule(EmplacementVoitureView.class);


//    @Before
//    public void registerIntentServiceIdlingResource() {
//        Instrumentation instrumentation
//                = InstrumentationRegistry.getInstrumentation();
//        idlingResource = new IntentServiceIdlingResource(
//                instrumentation.getTargetContext(), mActivityRule.getActivity());
//        IdlingPolicies.setMasterPolicyTimeout(60, TimeUnit.SECONDS);
//        IdlingPolicies.setIdlingResourceTimeout(60,TimeUnit.SECONDS);
//        Espresso.registerIdlingResources(idlingResource);
//
//    }



    @Test
    public void checkReussi(){
        onView(withId(R.id.jeMeGare)).perform(click());
        onView(withId(R.id.validerConfirmation)).perform(click());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Location loc = new Location("loc");
                loc.setLatitude(41.8919300);
                loc.setLongitude(12.5113300);
                mActivityRule.getActivity().getPresenter().setLastLocation(loc,178521);
            }
        });

        onView(withId(R.id.toast_reussi))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void checkPopupReussi(){
        onView(withId(R.id.jeMeGare)).perform(click());
        onView(withId(R.id.validerConfirmation)).perform(click());
        onView(withId(R.id.progressCancelProgress)).perform(pressBack());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Location loc = new Location("loc");
                loc.setLatitude(41.8919300);
                loc.setLongitude(12.5113300);
                mActivityRule.getActivity().getPresenter().setLastLocation(loc,178521);
            }
        });

        onView(withId(R.id.toast_reussi))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

//    @After
//    public void unregisterIntentServiceIdlingResource() {
//        Espresso.unregisterIdlingResources(idlingResource);
//    }


}
