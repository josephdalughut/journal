package io.github.josephdalughut.journal.android.ui.fragment.entries.list;

import android.test.suitebuilder.annotation.MediumTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentController;

import io.github.josephdalughut.journal.android.BuildConfig;

import static org.junit.Assert.*;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 29/06/2018
 */
@MediumTest
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class EntriesFragmentTest {

    private EntriesFragment mEntriesFragment;
    private SupportFragmentController<EntriesFragment> mFragmentController;

    @Before
    public void setUp() {
        mFragmentController = SupportFragmentController.of(EntriesFragment.newInstance());
        mEntriesFragment = mFragmentController.create().start().resume().get();
    }

    @After
    public void tearDown() {
        mFragmentController.pause().stop().destroy();
    }

    @Test
    public void checkPresenterNotNull(){
        assertNotNull(mEntriesFragment.mPresenter);
    }
}