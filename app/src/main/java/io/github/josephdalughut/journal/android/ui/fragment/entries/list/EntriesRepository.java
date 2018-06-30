package io.github.josephdalughut.journal.android.ui.fragment.entries.list;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.support.v4.app.Fragment;

import java.util.List;

import io.github.josephdalughut.journal.android.data.models.entry.Entry;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 29/06/2018
 *
 * A simple interface for a repo containing
 * {@link io.github.josephdalughut.journal.android.data.models.entry.Entry}
 */
public interface EntriesRepository {

    public interface LoadedEntriesCallback{
        public void onEntriesLoaded(List<Entry> loadedEntries);
    }

    public void loadEntries(LifecycleOwner lifecycleOwner, Observer<List<Entry>> observer);

}
