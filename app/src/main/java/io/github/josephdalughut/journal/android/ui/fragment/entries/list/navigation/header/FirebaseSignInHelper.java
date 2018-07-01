package io.github.josephdalughut.journal.android.ui.fragment.entries.list.navigation.header;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

import io.github.josephdalughut.journal.android.R;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 01/07/2018
 *
 * Helper class for Firebase Auth and Google Signin
 */
public class FirebaseSignInHelper {

    private Fragment mFragment;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseUserAccountProvider mAccountProvider;
    private int mRequestCode;

    public FirebaseSignInHelper(Fragment fragment, FirebaseUserAccountProvider provider,
                                int requestCode) {
        this.mAccountProvider = provider;
        this.mFragment = fragment;
        this.mRequestCode = requestCode;

        //create a new sign in client
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mFragment.getString(R.string.firebase_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(mFragment.getActivity(), gso);
    }

    /**
     * I used Google accounts as a provider for Firebase Auth,
     * this authenticates with google
     */
    public void authenticateWithGoogle(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        mFragment.startActivityForResult(signInIntent, mRequestCode);
    }

    /**
     * Logs into firebase using a google account
     * The resulting {@link GoogleSignInAccount} would be passed in the container activity's
     * {@link android.app.Activity#onActivityResult(int, int, Intent)}
     * @param completeListener would be notified when this task complets
     *
     * @see #authenticateWithGoogle()
     */
    public void signInToFirebase(GoogleSignInAccount googleSignInAccount,
                                 OnCompleteListener<AuthResult> completeListener){
        mAccountProvider.signInUser(googleSignInAccount, completeListener);
    }

    /**
     * Signs out the user from firebase
     * @param onCompleteListener would be notified when this task completes
     */
    public void signOut(OnCompleteListener<Void> onCompleteListener){
        mAccountProvider.signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(onCompleteListener);
    }

}
