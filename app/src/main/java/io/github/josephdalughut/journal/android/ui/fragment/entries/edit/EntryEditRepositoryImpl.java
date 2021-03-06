package io.github.josephdalughut.journal.android.ui.fragment.entries.edit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import io.github.josephdalughut.journal.android.data.database.Database;
import io.github.josephdalughut.journal.android.data.models.entry.Entry;
import io.github.josephdalughut.journal.android.service.EntryBackupService;

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
    private Context mContext;

    public EntryEditRepositoryImpl(Context context) {
        this.mContext = context;
        this.mDatabase = Database.getInstance(mContext.getApplicationContext());
    }

    @Override
    public void saveEntry() {
        if(mEntry == null || mEntry.getId() == null && (mEntry.isTitleEmpty() && mEntry.isContentEmpty())){
            Log.d(LOG_TAG, "Ignoring empty entry");
            return;
        }

        new AsyncTask<Entry, Void, Long>(){
            @Override
            protected Long doInBackground(Entry... entries) {
                Entry entry = entries[0];

                Date now = Calendar.getInstance().getTime();
                if(entry.getCreatedAt() == null) entry.setCreatedAt(now); //set the date created if null
                entry.setSynced(false).setUpdatedAt(now);

                Log.d(LOG_TAG, "Adding entry into database");
                Long entryId = mDatabase.getEntryDao().insert(entries[0]);
                Log.d(LOG_TAG, "Entry with id "+entryId + " added");

                return entryId;
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                Log.d(LOG_TAG, "Starting sync");
                Intent intent = new Intent(mContext, EntryBackupService.class);
                intent.putExtra(EntryBackupService.EXTRA_FIRESTORE_ACTION, EntryBackupService.ACTION_BACKUP);
                mContext.getApplicationContext().startService(intent);
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
                EntryEditRepositoryImpl.this.mEntry = entry; // reference entry instance for future use
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

    @Override
    public void deleteEntry() {
        Log.d(LOG_TAG, "Delete entry method called");
        if(mEntry == null)
            return;
        Entry deletedEntry = mEntry; //cache entry to delete, set to null, if not it gets saved again.
        this.mEntry = null;
        Log.d(LOG_TAG, "Deleting entry: "+deletedEntry.getId());
        new AsyncTask<Entry, Void, Long>(){
            @Override
            protected Long doInBackground(Entry... entries) {
                mDatabase.getEntryDao().delete(entries[0]);
                Log.d(LOG_TAG, "Entry deleted");
                return entries[0].getId();
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);

                // start service to delete entry on firebase
                Intent intent = new Intent(mContext, EntryBackupService.class);
                intent.putExtra(EntryBackupService.EXTRA_FIRESTORE_ACTION, EntryBackupService.ACTION_DELETE);
                intent.putExtra(EntryBackupService.EXTRA_ENTRY_ID, aLong);
                mContext.startService(intent);
            }
        }.execute(deletedEntry);
    }
}
