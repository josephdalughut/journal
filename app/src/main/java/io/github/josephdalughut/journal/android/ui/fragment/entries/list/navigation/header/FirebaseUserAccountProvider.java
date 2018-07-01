package io.github.josephdalughut.journal.android.ui.fragment.entries.list.navigation.header;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 30/06/2018
 *
 * A simple interface for handling the users Firebase account
 */
public interface FirebaseUserAccountProvider {

    void getSignedInUser(UserAccountCallback callback);
    void signInUser(GoogleSignInAccount googleSignInAccount, OnCompleteListener<AuthResult> onCompleteListener);
    void signOut();

    /**
     * Simple callback for logged in firebase user.
     */
    interface UserAccountCallback {
        void onUserAccountProvided(FirebaseUser firebaseUser);
    }

}
