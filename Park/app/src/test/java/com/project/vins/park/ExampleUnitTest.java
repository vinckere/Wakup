package com.project.vins.park;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.*;


/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void textIsCorrect(){

        EmplacementVoitureView emplacementVoitureView = new EmplacementVoitureView();
        EmplacementVoiturePresenter emplacementVoiturePresenter = new EmplacementVoiturePresenter(emplacementVoitureView);

    }
}