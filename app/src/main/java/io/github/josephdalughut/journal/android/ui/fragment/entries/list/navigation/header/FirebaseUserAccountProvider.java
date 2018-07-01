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
 * A simple user interface providing the account of the Logged in user
 */
public interface FirebaseUserAccountProvider {

    public void getLoggedInUser(UserAccountCallback callback);
    public void loginUser(GoogleSignInAccount googleSignInAccount, OnCompleteListener<AuthResult> onCompleteListener);
    public void logout();

    /**
     * Simple callback for logged in firebase user.
     */
    public interface UserAccountCallback {
        public void onUserAccountProvided(FirebaseUser firebaseUser);
    }

}
