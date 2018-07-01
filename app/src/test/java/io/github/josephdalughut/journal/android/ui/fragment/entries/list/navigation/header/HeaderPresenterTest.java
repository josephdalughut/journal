package io.github.josephdalughut.journal.android.ui.fragment.entries.list.navigation.header;

import android.net.Uri;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.test.suitebuilder.annotation.SmallTest;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.internal.firebase_auth.zzao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.UserInfo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 30/06/2018
 */
@SmallTest
public class HeaderPresenterTest {

    @Mock private HeaderContract.View mView;
    @Mock private FirebaseUserAccountProvider mProvider;

    @Captor private ArgumentCaptor<FirebaseUserAccountProvider.UserAccountCallback> mUserCallbackCaptor;
    @Captor private ArgumentCaptor<OnCompleteListener<AuthResult>> mCompleteListenerCaptor;

    private HeaderPresenter mPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this); //init mocks
        mPresenter = new HeaderPresenter(mView, mProvider); //initialize presenter
    }

    @Test
    public void checkNotNull(){
        assertNotNull(mPresenter.mProvider);
        assertNotNull(mPresenter.mView);
    }

    @Test
    public void initializeUi_showsLoadingProgress() {
        mPresenter.initializeUi();
        verify(mView).showAuthenticatedUserUi(false);
        verify(mView).showUnauthenticatedUserUi(false);
        verify(mView).showLoadingProgress(true);
    }

    @Test
    public void onUserAccount_showsAuthedUserUi(){
        mPresenter.initializeUi();
        //couldn't mock FirebaseUser, sorry.
        FirebaseUser firebaseUser = mock(FirebaseUser.class);
        verify(mProvider).getSignedInUser(mUserCallbackCaptor.capture());
        mUserCallbackCaptor.getValue().onUserAccountProvided(firebaseUser);

        InOrder inOrder = Mockito.inOrder(mView);

        inOrder.verify(mView).showUnauthenticatedUserUi(false);
        inOrder.verify(mView).showAuthenticatedUserUi(true);
        inOrder.verify(mView).showLoadingProgress(false);
        inOrder.verify(mView).showUserDetails(firebaseUser.getDisplayName(), firebaseUser.getEmail(),
                firebaseUser.getPhotoUrl());

    }

    @Test
    public void onNoUserAccountProvided_showsUnauthenticatedUserUi(){
        mPresenter.initializeUi();

        InOrder inOrder = Mockito.inOrder(mView);

        verify(mProvider).getSignedInUser(mUserCallbackCaptor.capture());
        mUserCallbackCaptor.getValue().onUserAccountProvided(null);

        inOrder.verify(mView).showAuthenticatedUserUi(false);
        inOrder.verify(mView).showUnauthenticatedUserUi(true);
        inOrder.verify(mView).showLoadingProgress(false);
    }

    @Test
    public void onSignInButtonClicked() {
        mPresenter.onSignInButtonClicked();
        InOrder inOrder = Mockito.inOrder(mView);
        inOrder.verify(mView).showLoadingProgress(true);
        inOrder.verify(mView).showAuthenticatedUserUi(false);
        inOrder.verify(mView).showUnauthenticatedUserUi(false);
        inOrder.verify(mView).showLoginUi();
    }

    @Test
    public void onGoogleAccountProvided_performsFirebaseLogin(){
        GoogleSignInAccount account = Mockito.mock(GoogleSignInAccount.class);
        mPresenter.onGoogleAccountProvided(account);
        verify(mProvider).signInUser(any(GoogleSignInAccount.class), mCompleteListenerCaptor.capture());
    }


}