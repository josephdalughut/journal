package io.github.josephdalughut.journal.android.ui.fragment.entries.edit;

import java.util.function.Consumer;

import io.github.josephdalughut.journal.android.data.models.entry.Entry;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 29/06/2018
 */
public interface EntryEditRepository {

    public void saveEntry();
    public void loadEntry(Long entryId, LoadEntryCallback loadEntryCallback);
    public void setEntryTitle(String title);
    public void setEntryContent(String content);

    public interface LoadEntryCallback {
        public void onEntryLoaded(Entry entry);
    }

}
