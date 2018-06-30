package io.github.josephdalughut.journal.android.ui.fragment.entries.list.navigation.header;

import android.view.View;

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
 * 30/06/2018
 *
 *
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class HeaderFragmentTest {

    private HeaderFragment mFragment;
    private SupportFragmentController<HeaderFragment> mFragmentController;

    @Before
    public void setUp() {
        mFragmentController = SupportFragmentController.of(HeaderFragment.newInstance());
        mFragment = mFragmentController.create().start().resume().get();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void showUnauthenticatedUserUi() {
        mFragment.showUnauthenticatedUserUi(true);
        assert mFragment.layUnauthenticatedUser.getVisibility() == View.VISIBLE;
        mFragment.showUnauthenticatedUserUi(false);
        assert mFragment.layUnauthenticatedUser.getVisibility() != View.VISIBLE;
    }

    @Test
    public void showAuthenticatedUserUi() {
        mFragment.showAuthenticatedUserUi(true);
        assert mFragment.layAuthenticatedUser.getVisibility() == View.VISIBLE;
        mFragment.showAuthenticatedUserUi(false);
        assert mFragment.layAuthenticatedUser.getVisibility() != View.VISIBLE;
    }

    @Test
    public void showLoadingProgress() {
        mFragment.showLoadingProgress(true);
        assert mFragment.progress.getVisibility() == View.VISIBLE;
        mFragment.showLoadingProgress(false);
        assert mFragment.progress.getVisibility() != View.VISIBLE;
    }
}