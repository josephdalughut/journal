package io.github.josephdalughut.journal.android.ui.fragment.entries.list;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.util.List;

import io.github.josephdalughut.journal.android.data.database.Database;
import io.github.josephdalughut.journal.android.data.models.entry.Entry;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 30/06/2018
 *
 * Repository impl for {@link io.github.josephdalughut.journal.android.data.models.entry.Entry} items
 */
@SuppressLint("StaticFieldLeak")
public class EntriesRepositoryImpl extends AndroidViewModel implements EntriesRepository {

    private Database mDatabase;
    private LiveData<List<Entry>> mEntries;

    public EntriesRepositoryImpl(@NonNull Application application) {
        super(application);
        mDatabase = Database.getInstance(application);

    }

    @Override
    public void loadEntries(LifecycleOwner lifecycleOwner, Observer<List<Entry>> observer) {
        mEntries = mDatabase.getEntryDao().loadEntries();
        mEntries.observe(lifecycleOwner, observer);
    }
}
