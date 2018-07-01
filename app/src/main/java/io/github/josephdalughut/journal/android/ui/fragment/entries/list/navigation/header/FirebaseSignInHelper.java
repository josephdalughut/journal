package io.github.josephdalughut.journal.android.ui.fragment.entries.list.navigation.header;

import android.app.Activity;
import android.content.Context;
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
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mFragment.getString(R.string.firebase_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(mFragment.getActivity(), gso);
    }

    public void signInWithGoogle(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        mFragment.startActivityForResult(signInIntent, mRequestCode);
    }

    public void loginToFirebase(GoogleSignInAccount googleSignInAccount,
                                OnCompleteListener<AuthResult> completeListener){
        mAccountProvider.loginUser(googleSignInAccount, completeListener);
    }

    public void signOut(OnCompleteListener<Void> onCompleteListener){
        mAccountProvider.logout();
        mGoogleSignInClient.signOut().addOnCompleteListener(onCompleteListener);
    }

}
