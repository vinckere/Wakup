package com.project.vins.park;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by Vins on 10/05/2016.
 */

@RunWith(AndroidJUnit4.class)
public class RecupererVoiture {

    @Rule
    public ActivityTestRule<EmplacementVoitureView> mActivityRule = new ActivityTestRule(EmplacementVoitureView.class);

    @Test
    public void recupererVoitureIntent(){
        Intents.init();

        onView(withId(R.id.jeRecupere)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        intended(hasAction(equalTo(Intent.ACTION_VIEW)));

        Intents.release();
    }

}
