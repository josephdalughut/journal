package io.github.josephdalughut.journal.android.ui.fragment.settings;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.github.josephdalughut.journal.android.R;
import io.github.josephdalughut.journal.android.data.database.Database;
import io.github.josephdalughut.journal.android.service.EntryBackupService;
import io.github.josephdalughut.journal.android.service.EntrySyncService;
import io.github.josephdalughut.journal.android.ui.fragment.entries.list.navigation.header.FirebaseSignInHelper;
import io.github.josephdalughut.journal.android.ui.fragment.entries.list.navigation.header.FirebaseUserAccountProviderImpl;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 01/07/2018
 *
 * Application settings screen.
 */
@SuppressLint("StaticFieldLeak")
public class SettingsPreferencesFragment extends PreferenceFragmentCompat{

    private static final String LOG_TAG = SettingsPreferencesFragment.class.getSimpleName();

    private static final int RC_SIGN_IN = 778;
    public static final String FILENAME_SHARED_PREFERENCES = "journalprefs";

    /*
        Intent actions
     */
    public static final String ACTION_GOOGLE_SIGN_IN = LOG_TAG + ".SIGN_IN";

    /*
        A list of constants for preferences
     */
    public static final String PREF_SYNC_ENABLED = "pref_sync_enabled";

    /**
     * Use this to create a new {@link SettingsPreferencesFragment}
     * @return a new {@link SettingsPreferencesFragment} instance
     */
    public static SettingsPreferencesFragment newInstance(){
        return new SettingsPreferencesFragment();
    }

    private FirebaseSignInHelper mFirebaseSigninHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define the settings file to use by this settings fragment
        getPreferenceManager().setSharedPreferencesName(FILENAME_SHARED_PREFERENCES);

        mFirebaseSigninHelper = new FirebaseSignInHelper(this,
                new FirebaseUserAccountProviderImpl(), RC_SIGN_IN);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        // Load preferences from an XML resource
        setPreferencesFromResource(R.xml.pref_settings, rootKey);

        SwitchPreference firebaseSyncPreference = (SwitchPreference)
                findPreference(PREF_SYNC_ENABLED);

        firebaseSyncPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean enabled = (Boolean) newValue;
                if(enabled){
                    // fire up sync service
                    startEntrySyncService();
                    // fire up backup service
                    startEntryBackupService();

                }
                return true;
            }
        });

        Preference clearDataPreference = findPreference("pref_clear_data");
        clearDataPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                clearData();
                return true;
            }
        });

        refreshSignInPreference();
    }

    private void clearData(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.text_delete)
                .setMessage(R.string.text_confirmation_delete_cache)
                .setNegativeButton(R.string.text_back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton(R.string.text_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), R.string.text_cache_cleared, Toast.LENGTH_SHORT).show();
                        new AsyncTask<Void, Void, Void>(){
                            @Override
                            protected Void doInBackground(Void... voids) {
                                Database.getInstance(getActivity().getApplicationContext()).getEntryDao().delete();
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                            }
                        }.execute();
                    }
                });
        builder.show();
    }

    private void refreshSignInPreference(){

        /*
            We want to show the users account if they've signed in, or let them sign in if not
         */

        Preference sigininPreference = findPreference("pref_sign_in");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){ //no user yet
            sigininPreference.setTitle(R.string.text_sign_in_with_google);
            sigininPreference.setSummary(R.string.text_signin_info);
            sigininPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    //we'll sign in here
                    mFirebaseSigninHelper.signInWithGoogle();
                    return true;
                }
            });
        }else{
            // update preference with user info
            sigininPreference.setTitle(user.getEmail());
            sigininPreference.setSummary(R.string.text_sign_out);
            sigininPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    //sign out
                    mFirebaseSigninHelper.signOut(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(getActivity() == null) return;
                            if(task.isSuccessful()){
                                Toast.makeText(getActivity(), R.string.text_signed_out, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    // update ui
                    refreshSignInPreference();

                    // notify broadcast listeners about sign out
                    sendSignInIntent();
                    return true;
                }
            });
        }

    }

    /**
     * Fires up {@link EntryBackupService} to start backing up saved
     * {@link io.github.josephdalughut.journal.android.data.models.entry.Entry}
     */
    private void startEntryBackupService(){
        Intent intent = new Intent(getActivity(), EntryBackupService.class);
        intent.putExtra(EntryBackupService.EXTRA_FIRESTORE_ACTION, EntryBackupService.ACTION_BACKUP);
        getActivity().startService(intent);
    }

    /**
     * Fires up {@link EntrySyncService} to start syncing saved
     * {@link io.github.josephdalughut.journal.android.data.models.entry.Entry}ies
     */
    private void startEntrySyncService(){
        Intent intent = new Intent(getActivity(), EntrySyncService.class);
        intent.putExtra(EntrySyncService.EXTRA_FIRESTORE_ACTION, EntrySyncService.ACTION_SYNC);
        getActivity().startService(intent);
    }

    /**
     * Broadcasts {@link #ACTION_GOOGLE_SIGN_IN} when the user signs in or signs out.
     */
    private void sendSignInIntent(){
        Intent intent = new Intent(ACTION_GOOGLE_SIGN_IN);
        getActivity().sendBroadcast(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                Log.d(LOG_TAG, "Google sign in success");
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(LOG_TAG, "Account: "+account.getEmail());
                mFirebaseSigninHelper.loginToFirebase(account, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(getActivity() == null) return;
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), R.string.text_signed_in, Toast.LENGTH_LONG).show();
                            refreshSignInPreference();

                            //start sync
                            startEntrySyncService();

                            //start backup
                            startEntryBackupService();

                        }else{
                            Toast.makeText(getActivity(), R.string.text_sign_in_failed, Toast.LENGTH_LONG).show();
                        }

                        // notify broadcast listeners about signin
                        sendSignInIntent();
                    }
                });

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.d(LOG_TAG, "Google sign in failed", e);
                Toast.makeText(getActivity(), R.string.text_sign_in_failed, Toast.LENGTH_LONG).show();
            }
        }
    }


}
