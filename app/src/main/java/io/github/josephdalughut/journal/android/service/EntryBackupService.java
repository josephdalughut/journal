package io.github.josephdalughut.journal.android.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import io.github.josephdalughut.journal.android.data.database.Database;
import io.github.josephdalughut.journal.android.data.models.entry.Entry;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 30/06/2018
 *
 * A service which runs backing up and deleting
 * {@link io.github.josephdalughut.journal.android.data.models.entry.Entry}
 * to firestore
 */
public class EntryBackupService extends IntentService {

    private static final String LOG_TAG = EntryBackupService.class.getSimpleName();

    /**
     * Used to specify the action to be performed by the service. Can be one of:
     * <ul>
     *     <li>
     *         {@link #ACTION_BACKUP}: To backup all {@link Entry}(ies) in the database
     *         which haven't been saved to firebase
     *     </li>
     *     <li>
     *         {@link #ACTION_DELETE}: To delete a specific {@link Entry} from the database. Make sure
     *         to supply the id of the entry as {@link #EXTRA_ENTRY_ID} in the intent.
     *     </li>
     * </ul>
     */
    public static final String EXTRA_FIRESTORE_ACTION = "ACTION";


    public static final String ACTION_BACKUP = "BACKUP";
    public static final String ACTION_DELETE = "DELETE";

    /**
     * Should be passed as an extra in an intent with action {@link #ACTION_DELETE}
     */
    public static final String EXTRA_ENTRY_ID = "ENTRY.ID";

    private FirebaseAuth mAuth;
    private Database mDbInstance;
    private FirebaseFirestore mFirestoreInstance;

    public EntryBackupService() {
        super(EntryBackupService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAuth = FirebaseAuth.getInstance();
        mDbInstance = Database.getInstance(this);
        mFirestoreInstance = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if(intent == null || !intent.hasExtra(EXTRA_FIRESTORE_ACTION)){
            Log.d(LOG_TAG, "No action specified, returning");
            stopSelf(); //tell service to quit
            return;
        }

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser == null){
            Log.d(LOG_TAG, "No user signed in, stopping service");
            stopSelf();
            return;
        }
        Log.d(LOG_TAG, "User signed in: "+ firebaseUser.getEmail());

        String action = intent.getStringExtra(EXTRA_FIRESTORE_ACTION);
        if(action.matches(ACTION_BACKUP)){
            performBackup(firebaseUser);
        }else if(action.matches(ACTION_DELETE)){
            if(intent.hasExtra(EXTRA_ENTRY_ID)){
                Long entryId = intent.getLongExtra(EXTRA_ENTRY_ID, -1);
                if(entryId < 0){
                    Log.d(LOG_TAG, "Invalid entry id");
                }else{
                    deleteEntry(firebaseUser, entryId);
                }
            }else{
                Log.d(LOG_TAG, "No entry id provided");
            }
        }

    }

    private void performBackup(FirebaseUser firebaseUser){
        Log.d(LOG_TAG, "Starting sync");
        List<Entry> entries = mDbInstance.getEntryDao().loadUnSyncedEntries();
        if(entries == null || entries.isEmpty()){
            Log.d(LOG_TAG, "Stopping sync, no items");
            return;
        }
        Log.d(LOG_TAG, "Syncing "+entries.size() + " entries");
        CollectionReference entryCollection = mFirestoreInstance.collection(Entry.FIREBASE_COLLECTION_NAME);
        for (Entry entry: entries){
            entry.setFirebase_user_id(firebaseUser.getUid());
            entry.setSynced(true);

            entryCollection.document(Entry.getFirebaseId(firebaseUser, entry)).set(entry);

            //update the entry as synced
            entry.setSynced(true);
            mDbInstance.getEntryDao().insert(entry);
        }

        Log.d(LOG_TAG, "Sync finished");
    }

    private void deleteEntry(FirebaseUser firebaseUser, Long entryId){
        Log.d(LOG_TAG, "Deleting entry");
        String firebaseId = Entry.getFirebaseId(firebaseUser, entryId);
        mFirestoreInstance.collection(Entry.FIREBASE_COLLECTION_NAME) //delete document
                .document(firebaseId).delete();
        Log.d(LOG_TAG, "Deleted entry");
    }

}
