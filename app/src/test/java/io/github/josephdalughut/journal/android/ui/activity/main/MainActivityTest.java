package io.github.josephdalughut.journal.android.ui.activity.main;

import android.support.v4.app.Fragment;
import android.test.suitebuilder.annotation.MediumTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import io.github.josephdalughut.journal.android.BuildConfig;

import static org.junit.Assert.*;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 28/06/2018
 *
 * Unit testing {@link MainActivity}
 */
@MediumTest
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainActivityTest {

    private MainActivity mActivity;
    private ActivityController<MainActivity> mActivityController;

    @Before
    public void setUp() {
        mActivityController = Robolectric.buildActivity(MainActivity.class);
        mActivity = mActivityController.create().start().resume().get();
    }

    @After
    public void tearDown() {
        mActivityController.pause().stop().destroy();
    }

    @Test
    public void notNull(){
        assertNotNull(mActivity);
    }

    @Test
    public void addFragmentToUi_ShowsFragment(){

        //initialize and add a new fragment
        mActivity.addFragmentToUi(new Fragment(), false);
        assert mActivity.getSupportFragmentManager().getFragments().size() == 1;
    }

    @Test
    public void addFragmentToUi_WithoutReplacing_KeepsPreviousFragment(){

        //initialize, add two fragments
        mActivity.addFragmentToUi(new Fragment(), false);
        mActivity.addFragmentToUi(new Fragment(), false);

        assert mActivity.getSupportFragmentManager().getFragments().size() == 2;

    }

    @Test
    public void addFragmentToUi_WhenReplacing_ReplacesPreviousFragment(){

        //initialize, add two fragments
        mActivity.addFragmentToUi(new Fragment(), true);
        mActivity.addFragmentToUi(new Fragment(), true);
        assert mActivity.getSupportFragmentManager().getFragments().size() == 1;

    }


}