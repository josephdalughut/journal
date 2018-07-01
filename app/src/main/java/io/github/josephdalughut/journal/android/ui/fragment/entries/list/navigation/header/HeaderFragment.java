package io.github.josephdalughut.journal.android.ui.fragment.entries.list.navigation.header;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import io.github.josephdalughut.journal.android.R;
import io.github.josephdalughut.journal.android.service.EntrySyncService;
import io.github.josephdalughut.journal.android.ui.fragment.abstracts.Fragment;
import io.github.josephdalughut.journal.android.ui.fragment.settings.SettingsPreferencesFragment;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 30/06/2018
 *
 * Header, for the navigation view in
 * {@link io.github.josephdalughut.journal.android.ui.fragment.entries.list.EntriesFragment}
 */
public class HeaderFragment extends Fragment implements HeaderContract.View {

    private static final int RC_SIGN_IN = 777;
    private static final String LOG_TAG = HeaderFragment.class.getSimpleName();

    @BindView(R.id.progress) public ProgressBar progress;
    @BindView(R.id.layAuthenticatedUser) public View layAuthenticatedUser;
    @BindView(R.id.layUnauthenticatedUser) public View layUnauthenticatedUser;

    @BindView(R.id.imgUserAvatar) public ImageView imgUserAvatar;
    @BindView(R.id.txtUserName) public TextView txtUserName;
    @BindView(R.id.txtUserEmail) public TextView txtUserEmail;

    @BindView(R.id.btnSignIn) public SignInButton btnSignIn;

    private HeaderPresenter mPresenter;
    private FirebaseSignInHelper mFirebaseSignInHelper;

    private BroadcastReceiver mGoogleSigninBroadcastReceiver;

    /**
     * @return a new {@link HeaderFragment} instance.
     */
    public static HeaderFragment newInstance(){
        return new HeaderFragment();
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {

        FirebaseUserAccountProvider firebaseAccountProvider = new FirebaseUserAccountProviderImpl();

        mPresenter = new HeaderPresenter(this, firebaseAccountProvider);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onSignInButtonClicked();
            }
        });

        mFirebaseSignInHelper = new FirebaseSignInHelper(this, firebaseAccountProvider, RC_SIGN_IN);

        // listen for sign-in changes to update ui
        mGoogleSigninBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // initialize ui
                mPresenter.initializeUi();
            }
        };
        getContext().registerReceiver(mGoogleSigninBroadcastReceiver,
                new IntentFilter(SettingsPreferencesFragment.ACTION_GOOGLE_SIGN_IN));

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.initializeUi();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mGoogleSigninBroadcastReceiver != null) {
            getContext().unregisterReceiver(mGoogleSigninBroadcastReceiver);
            mGoogleSigninBroadcastReceiver = null;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_entries_navigation_header;
    }

    @Override
    public void showUnauthenticatedUserUi(boolean visible) {
        layUnauthenticatedUser.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showAuthenticatedUserUi(boolean visible) {
        layAuthenticatedUser.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showLoadingProgress(boolean visible) {
        progress.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showLoginUi() {
        mFirebaseSignInHelper.authenticateWithGoogle();
    }

    @Override
    public void startFirebaseSync() {
        Intent intent = new Intent(getActivity(), EntrySyncService.class);
        intent.putExtra(EntrySyncService.EXTRA_FIRESTORE_ACTION, EntrySyncService.ACTION_SYNC);
        getActivity().startService(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(LOG_TAG, "Activity result: ");
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                Log.d(LOG_TAG, "Google sign in success");
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(LOG_TAG, "Account: "+account.getEmail());
                mPresenter.onGoogleAccountProvided(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.d(LOG_TAG, "Google sign in failed", e);
                mPresenter.onGoogleAccountProvided(null);
            }
        }
    }

    @Override
    public void showUserDetails(String displayName, String emailAddress, Uri photoUri) {
        txtUserName.setText(displayName);
        txtUserEmail.setText(emailAddress);
        if(photoUri != null ) { //load users avatar
            Picasso.with(getContext())
                    .load(photoUri.toString())
                    .placeholder(R.drawable.ic_account_circle)
                    .error(R.drawable.ic_account_circle)
                    .into(imgUserAvatar);
        }
    }
}
