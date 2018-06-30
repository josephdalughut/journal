package io.github.josephdalughut.journal.android.ui.fragment.entries.list.navigation.header;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import io.github.josephdalughut.journal.android.R;
import io.github.josephdalughut.journal.android.ui.fragment.abstracts.Fragment;

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

    private static final String LOG_TAG = HeaderFragment.class.getSimpleName();
    private static final int RC_SIGN_IN = 777;

    @BindView(R.id.progress) public ProgressBar progress;
    @BindView(R.id.layAuthenticatedUser) public View layAuthenticatedUser;
    @BindView(R.id.layUnauthenticatedUser) public View layUnauthenticatedUser;

    @BindView(R.id.imgUserAvatar) public ImageView imgUserAvatar;
    @BindView(R.id.txtUserName) public TextView txtUserName;
    @BindView(R.id.txtUserEmail) public TextView txtUserEmail;

    @BindView(R.id.btnSignIn) public SignInButton btnSignIn;

    private HeaderPresenter mPresenter;

    private GoogleSignInClient mGoogleSignInClient;

    /**
     * @return a new {@link HeaderFragment} instance.
     */
    public static HeaderFragment newInstance(){
        return new HeaderFragment();
    }

    @Override
    public void onCreateView() {
        mPresenter = new HeaderPresenter(this, new FirebaseUserAccountProviderImpl());
        mPresenter.initializeUi();
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onSignInButtonClicked();
            }
        });
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
        initializeGoogleSigninClient();
        signInWithGoogle();
    }

    private void initializeGoogleSigninClient(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.firebase_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
        if(photoUri != null ) {
            Picasso.with(getContext())
                    .load(photoUri.toString())
                    .placeholder(R.drawable.ic_account_circle)
                    .error(R.drawable.ic_account_circle)
                    .into(imgUserAvatar);
        }
    }
}
