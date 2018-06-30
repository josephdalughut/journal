package io.github.josephdalughut.journal.android.ui.fragment.entries.list.navigation.header;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 30/06/2018
 *
 * Implementation of {@link HeaderContract}.
 * This class helps the navigation header display the users account as well as handle UI actions.
 */
public class HeaderPresenter implements HeaderContract.Presenter, FirebaseUserAccountProvider.UserAccountCallback, OnCompleteListener<AuthResult> {

    private static final String LOG_TAG = HeaderPresenter.class.getSimpleName();

    protected HeaderContract.View mView; //view impl
    protected FirebaseUserAccountProvider mProvider; //provides us with user account details

    public HeaderPresenter(HeaderContract.View mView, FirebaseUserAccountProvider mProvider) {
        this.mView = mView;
        this.mProvider = mProvider;
    }

    @Override
    public void initializeUi() {
        if(mView == null || mProvider == null) return;
        mView.showLoadingProgress(true);
        mView.showAuthenticatedUserUi(false);
        mView.showUnauthenticatedUserUi(false);
        mProvider.getLoggedInUser(this);
    }

    @Override
    public void onSignInButtonClicked() {
        if(mView == null)
            return;
        mView.showLoadingProgress(true);
        mView.showAuthenticatedUserUi(false);
        mView.showUnauthenticatedUserUi(false);
        mView.showLoginUi();
    }

    @Override
    public void onUserAccountProvided(FirebaseUser firebaseUser) {
        if(mView == null) return;
        boolean loggedIn = (firebaseUser != null);

        mView.showAuthenticatedUserUi(loggedIn);
        mView.showUnauthenticatedUserUi(!loggedIn);
        mView.showLoadingProgress(false);

        if(loggedIn){
            mView.showUserDetails(firebaseUser.getDisplayName(), firebaseUser.getEmail(),
                    firebaseUser.getPhotoUrl());
        }
    }

    @Override
    public void onGoogleAccountProvided(GoogleSignInAccount account) {
        if(mProvider == null) return;
        mProvider.loginUser(account, this);
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if(mView == null) return;
        if(task.isSuccessful()){
            Log.d(LOG_TAG, "Sign in success");
            mProvider.getLoggedInUser(this);
        }else{
            Log.d(LOG_TAG, "Sign in exception");
            mView.showAuthenticatedUserUi(false);
            mView.showUnauthenticatedUserUi(true);
            mView.showLoadingProgress(false);
        }
    }
}
