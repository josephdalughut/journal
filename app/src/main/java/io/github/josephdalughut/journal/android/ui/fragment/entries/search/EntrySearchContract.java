package io.github.josephdalughut.journal.android.ui.fragment.entries.search;

import io.github.josephdalughut.journal.android.ui.fragment.entries.list.EntriesContract;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 01/07/2018
 */
public interface EntrySearchContract {

    public interface View extends EntriesContract.View {
        public void clearSearch();
        public void navigateBack();
        public void showCloseSearchButton(boolean visible);
    }

    public interface Presenter extends EntriesContract.Presenter {
        public void onBackButtonPressed();
        public void onClearSearchButtonClicked();
        public void loadEntries(String searchQuery);
    }

}
