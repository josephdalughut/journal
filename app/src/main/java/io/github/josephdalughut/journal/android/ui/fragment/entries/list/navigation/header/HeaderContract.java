package io.github.josephdalughut.journal.android.ui.fragment.entries.list.navigation.header;

import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 30/06/2018
 *
 * Contract between view and presenter for header view in
 * {@link io.github.josephdalughut.journal.android.ui.fragment.entries.list.EntriesFragment}
 */
public interface HeaderContract {

    interface View {
        void showUnauthenticatedUserUi(boolean visible);
        void showAuthenticatedUserUi(boolean visible);
        void showLoadingProgress(boolean visible);
        void showLoginUi();
        void showUserDetails(String displayName, String emailAddress, Uri photoUri);
        void startFirebaseSync();
    }

    interface Presenter {
        void initializeUi();
        void onSignInButtonClicked();
        void onGoogleAccountProvided(GoogleSignInAccount account);
    }

}
