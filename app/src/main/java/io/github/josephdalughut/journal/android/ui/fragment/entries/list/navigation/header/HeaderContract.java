package io.github.josephdalughut.journal.android.ui.fragment.entries.list.navigation.header;

import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 30/06/2018
 *
 * Contract between view and presenter for
 * {@link io.github.josephdalughut.journal.android.ui.fragment.entries.list.EntriesFragment}'s
 * header view
 */
public interface HeaderContract {

    public interface View {
        public void showUnauthenticatedUserUi(boolean visible);
        public void showAuthenticatedUserUi(boolean visible);
        public void showLoadingProgress(boolean visible);
        public void showLoginUi();
        public void showUserDetails(String displayName, String emailAddress, Uri photoUri);
        public void startFirebaseSync();
    }

    public interface Presenter {
        public void initializeUi();
        public void onSignInButtonClicked();
        public void onGoogleAccountProvided(GoogleSignInAccount account);
    }

}
