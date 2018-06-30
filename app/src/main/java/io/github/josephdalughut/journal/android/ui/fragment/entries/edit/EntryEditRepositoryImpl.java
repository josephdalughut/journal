package io.github.josephdalughut.journal.android.ui.fragment.entries.edit;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import io.github.josephdalughut.journal.android.data.database.Database;
import io.github.josephdalughut.journal.android.data.models.entry.Entry;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 29/06/2018
 */
@SuppressLint("StaticFieldLeak")
public class EntryEditRepositoryImpl implements EntryEditRepository {

    private static final String LOG_TAG = EntryEditRepositoryImpl.class.getSimpleName();

    private Database mDatabase; //database instance
    private Entry mEntry;

    public EntryEditRepositoryImpl(Database mDatabase) {
        this.mDatabase = mDatabase;
    }

    @Override
    public void saveEntry() {
        if(mEntry.getId() == null && (mEntry.isTitleEmpty() && mEntry.isContentEmpty())){
            Log.d(LOG_TAG, "Ignoring empty entry");
            return;
        }

        new AsyncTask<Entry, Void, Void>(){
            @Override
            protected Void doInBackground(Entry... entries) {
                Entry entry = entries[0];

                Date now = Calendar.getInstance().getTime();
                if(entry.getCreatedAt() == null) entry.setCreatedAt(now); //set the date created if null
                entry.setUpdatedAt(now);

                Log.d(LOG_TAG, "Adding entry into database");
                Long rowsAffected = mDatabase.getEntryDao().insert(entries[0]);
                Log.d(LOG_TAG, "" +rowsAffected +" entries added into database");
                return null;
            }
        }.execute(mEntry);
    }

    @Override
    public void loadEntry(Long entryId, final LoadEntryCallback loadEntryCallback) {
        new AsyncTask<Long, Void, Entry>(){
            @Override
            protected Entry doInBackground(Long... longs) {
                Entry entry = mDatabase.getEntryDao().getEntry(longs[0]);
                return entry == null ? new Entry() : entry;
            }

            @Override
            protected void onPostExecute(Entry entry) {
                super.onPostExecute(entry);
                if(loadEntryCallback == null)
                    return;
                EntryEditRepositoryImpl.this.mEntry = entry;
                loadEntryCallback.onEntryLoaded(entry);
            }
        }.execute(entryId);
    }

    @Override
    public void setEntryTitle(String title) {
        if(mEntry == null)
            return;
        mEntry.setTitle(title);
    }

    @Override
    public void setEntryContent(String content) {
        if(mEntry == null)
            return;
        mEntry.setContent(content);
    }
}
