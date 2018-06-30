package io.github.josephdalughut.journal.android.ui.fragment.entries.list;

import android.support.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.josephdalughut.journal.android.R;
import io.github.josephdalughut.journal.android.ui.activity.main.MainActivity;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.junit.Assert.*;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 29/06/2018
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class EntriesFragmentTest {

    /**
     * Launches our {@link MainActivity} under test
     */
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp(){

    }

    @After
    public void tearDown(){
    }

    @Test
    public void showAddEntryUi_opensAddEntryUi() {
        // click the add entry ui
        onView(withId(R.id.fabAddEntry)).perform(click());

        //check if view in AddEntry fragment is displayed
        onView(withId(R.id.edtEntryTitle)).check(matches(isDisplayed()));
    }
}