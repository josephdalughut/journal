package io.github.josephdalughut.journal.android.ui.fragment.entries.search;

import io.github.josephdalughut.journal.android.ui.fragment.entries.list.EntriesContract;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 01/07/2018
 */
public interface EntrySearchContract {

    interface View extends EntriesContract.View {
        void clearSearch();
        void navigateBack();
        void showCloseSearchButton(boolean visible);
    }

    public interface Presenter extends EntriesContract.Presenter {
        void onBackButtonPressed();
        void onClearSearchButtonClicked();
        void loadEntries(String searchQuery);
    }

}
