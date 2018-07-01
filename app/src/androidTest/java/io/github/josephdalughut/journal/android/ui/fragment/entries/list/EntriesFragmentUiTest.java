package io.github.josephdalughut.journal.android.ui.fragment.entries.list;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.DrawerMatchers;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.Gravity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.josephdalughut.journal.android.R;
import io.github.josephdalughut.journal.android.ui.activity.main.MainActivity;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
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
public class EntriesFragmentUiTest {

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

    @Test
    public void onSettingsNavigationItemClicked_showsSettingsScreen(){

        //open the drawer
        onView(withId(R.id.layDrawer))
                .perform(DrawerActions.open(Gravity.LEFT));

        //click settings item
        onView(withId(R.id.vwNavigation))
                .perform(NavigationViewActions.navigateTo(R.id.nav_settings));

        //verify settings screen shown
        onView(withContentDescription(R.string.text_settings))
                .check(matches(isDisplayed()));
    }

    @Test
    public void showSearchUi_opensSearchUi(){
        //click the search menu item
        onView(withId(R.id.nav_search)).perform(click());

        //check if search ui is shown
        onView(withId(R.id.edtSearch)).check(matches(isDisplayed()));
    }


}