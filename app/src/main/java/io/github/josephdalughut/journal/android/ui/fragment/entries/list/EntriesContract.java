package io.github.josephdalughut.journal.android.ui.fragment.entries.list;

import android.arch.lifecycle.LifecycleOwner;

import java.util.List;

import io.github.josephdalughut.journal.android.data.models.entry.Entry;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 29/06/2018
 *
 * Contract between view and presenter for {@link EntriesFragment}
 *
 */
public interface EntriesContract {

    /**
     * Our view implementation here would typically be a {@link android.support.v4.app.Fragment},
     * which already is a {@link LifecycleOwner}
     */
    interface View extends LifecycleOwner {
        void showAddEntryUi();
        void showLoadingProgress(boolean visible);
        void showEntries(List<Entry> entries);
        void showEntry(Long entryId);
        void showSearchUi();
        void showEmptyItemsPlaceholder(boolean visible);
    }

    interface Presenter {
        void onAddEntryButtonClicked();
        void loadEntries();
        void onEntryItemSelected(Entry entry);
        void onSearchButtonClicked();
    }

}
