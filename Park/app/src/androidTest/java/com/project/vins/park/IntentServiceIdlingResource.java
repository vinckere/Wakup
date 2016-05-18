package com.project.vins.park;


import android.content.Context;
import android.support.test.espresso.IdlingResource;

public class IntentServiceIdlingResource implements IdlingResource {
    private final Context context;
    private ResourceCallback resourceCallback;
    private EmplacementVoitureView emplacementVoitureView;

    public IntentServiceIdlingResource(Context context, EmplacementVoitureView emplacementVoitureView) {
        this.context = context;
        this.emplacementVoitureView = emplacementVoitureView;
    }

    @Override
    public String getName() {
        return IntentServiceIdlingResource.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        boolean idle = !isLocationRunning(emplacementVoitureView);
        if (idle && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
        return idle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }

    private boolean isLocationRunning(EmplacementVoitureView emplacementVoitureView) {

        return emplacementVoitureView.isRunning();

    }
}