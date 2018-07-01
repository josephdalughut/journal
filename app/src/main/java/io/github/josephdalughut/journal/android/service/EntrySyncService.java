package io.github.josephdalughut.journal.android.service;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import io.github.josephdalughut.journal.android.data.database.Database;
import io.github.josephdalughut.journal.android.data.models.entry.Entry;
import io.github.josephdalughut.journal.android.ui.fragment.settings.SettingsPreferencesFragment;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 30/06/2018
 *
 * A service for syncing entries to firebase
 * @see Entry
 */
@SuppressLint("StaticFieldLeak")
public class EntrySyncService extends IntentService {

    private static final String TAG = EntrySyncService.class.getSimpleName();

    /**
     * Used to specify the action to be performed by the service. Can be one of:
     * <ul>
     *     <li>
     *         {@link #ACTION_SYNC}: To sync back all entries from firebase
     *     </li>
     * </ul>
     */
    public static final String EXTRA_FIRESTORE_ACTION = TAG + ".ACTION";


    public static final String ACTION_SYNC = TAG + ".SYNC";


    private FirebaseAuth mAuth;
    private Database mDbInstance;
    private FirebaseFirestore mFirestoreInstance;

    public EntrySyncService() {
        super(EntrySyncService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //initialize variables
        mAuth = FirebaseAuth.getInstance();
        mDbInstance = Database.getInstance(this);
        mFirestoreInstance = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        SharedPreferences preferences = getSharedPreferences(SettingsPreferencesFragment.FILENAME_SHARED_PREFERENCES,
                Context.MODE_PRIVATE);

        // We shouldn't sync if it isn't enabled
        boolean isSyncEnabled = preferences.getBoolean(SettingsPreferencesFragment.PREF_SYNC_ENABLED, true);
        if(!isSyncEnabled){
            Log.d(TAG, "Sync disabled, exiting");
            stopSelf();
            return;
        }

        if(intent == null || !intent.hasExtra(EXTRA_FIRESTORE_ACTION)){
            Log.d(TAG, "No action specified, returning");
            stopSelf(); //tell service to quit
            return;
        }

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser == null){
            Log.d(TAG, "No user signed in, stopping service");
            stopSelf();
            return;
        }
        Log.d(TAG, "User signed in: "+ firebaseUser.getEmail());

        String action = intent.getStringExtra(EXTRA_FIRESTORE_ACTION);
        if(action.matches(ACTION_SYNC)){
            performSync(firebaseUser);
        }

    }

    private void performSync(FirebaseUser firebaseUser){
        Log.d(TAG, "Starting sync");
        mFirestoreInstance.collection(Entry.FIREBASE_COLLECTION_NAME)
                .whereEqualTo("firebase_user_id", firebaseUser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snapshots = queryDocumentSnapshots.getDocuments();
                        if(!snapshots.isEmpty()){
                            Log.d(TAG, "Number of entries synced "+snapshots.size());
                            List<Entry> entries = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot: snapshots){
                                Entry entry = documentSnapshot.toObject(Entry.class);
                                entries.add(entry);
                            }

                            /*
                                onSuccess is called by default on main thread I believe,
                                so we'll just cache this quickly on a background thread.
                             */
                            new AsyncTask<List<Entry>, Void, Void>(){
                                @Override
                                protected Void doInBackground(List<Entry>... lists) {
                                    mDbInstance.getEntryDao().insert(lists[0]);
                                    return null;
                                }
                            }.execute(entries);
                        }else{
                            Log.d(TAG, "No entries found");
                        }
                        Log.d(TAG, "Sync finished");
                    }
                });
    }

}
