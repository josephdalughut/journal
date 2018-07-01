package io.github.josephdalughut.journal.android.ui.fragment.entries.edit;

import io.github.josephdalughut.journal.android.data.models.entry.Entry;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 29/06/2018
 */
public interface EntryEditRepository {

    void saveEntry();
    void loadEntry(Long entryId, LoadEntryCallback loadEntryCallback);
    void setEntryTitle(String title);
    void setEntryContent(String content);
    void deleteEntry();

    interface LoadEntryCallback {
        void onEntryLoaded(Entry entry);
    }

}
