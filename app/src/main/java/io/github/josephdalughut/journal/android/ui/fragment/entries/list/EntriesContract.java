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
    public interface View extends LifecycleOwner {
        public void showAddEntryUi();
        public void showLoadingProgress(boolean visible);
        public void showEntries(List<Entry> entries);
        public void showEntry(Long entryId);
    }

    public interface Presenter {
        public void onAddEntryButtonClicked();
        public void loadEntries();
        public void onEntryItemSelected(Entry entry);
    }

}
