package io.github.josephdalughut.journal.android.ui.utils;

import android.support.v4.widget.SwipeRefreshLayout;

import io.github.josephdalughut.journal.android.R;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 30/06/2018
 *
 * Helper class for handling views
 */
public class ViewUtils {

    /**
     * Sets the refresh state of a {@link SwipeRefreshLayout}
     */
    public static void setRefreshing(final SwipeRefreshLayout swipeRefreshLayout, final boolean isRefreshing){
        //sometimes without a runnable, the loading animation behaves unexpectedly
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(isRefreshing);
            }
        });
    }

    /**
     * Global helper method to set the color of the progress animation in {@link SwipeRefreshLayout}'s
     */
    public static void setDefaultRefreshColors(SwipeRefreshLayout swipeRefreshLayout){
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
    }

}
