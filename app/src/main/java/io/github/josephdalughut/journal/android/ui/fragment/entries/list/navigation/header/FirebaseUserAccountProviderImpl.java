package io.github.josephdalughut.journal.android.ui.fragment.entries.list.navigation.header;

import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 30/06/2018
 *
 * A {@link FirebaseUserAccountProvider} which provides a logged in {@link FirebaseUser} account.
 */
public class FirebaseUserAccountProviderImpl implements FirebaseUserAccountProvider {

    private static final String LOG_TAG = FirebaseUserAccountProviderImpl.class.getSimpleName();

    private FirebaseAuth mAuth;

    public FirebaseUserAccountProviderImpl() {
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void getSignedInUser(final UserAccountCallback callback) {
        callback.onUserAccountProvided(mAuth.getCurrentUser());
    }

    @Override
    public void signInUser(GoogleSignInAccount googleSignInAccount, OnCompleteListener<AuthResult> onCompleteListener) {
        Log.d(LOG_TAG, "Logging in user");
        AuthCredential credential = GoogleAuthProvider
                .getCredential(googleSignInAccount.getIdToken(), null); //get google credential
        mAuth.signInWithCredential(credential)  //sign in with google
                .addOnCompleteListener(onCompleteListener);
    }

    @Override
    public void signOut() {
        mAuth.signOut();
    }
}
