package io.github.josephdalughut.journal.android.ui.fragment.entries.search;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.github.josephdalughut.journal.android.data.models.entry.Entry;
import io.github.josephdalughut.journal.android.ui.fragment.entries.edit.EntryEditRepository;
import io.github.josephdalughut.journal.android.ui.fragment.entries.list.EntriesRepository;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 01/07/2018
 */
public class EntrySearchPresenterTest {

    @Mock private EntrySearchContract.View mView;
    @Mock private EntriesRepository mRepository;
    @Captor private ArgumentCaptor<Observer<List<Entry>>> mObserverCaptor;

    private EntrySearchPresenter mPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mPresenter = new EntrySearchPresenter(mView, mRepository);
    }

    @Test
    public void onClearSearchButtonClicked_clearsSearch() {
        mPresenter.onClearSearchButtonClicked();
        verify(mView).clearSearch();
    }

    @Test
    public void loadEntries_withSearchQuery_showsEntries() {
        mPresenter.loadEntries("Search"); //load entries

        verify(mRepository).loadEntries(anyString(), any(LifecycleOwner.class), mObserverCaptor.capture());
        List<Entry> stubEntries = new ArrayList<>();
        stubEntries.add(new Entry());

        mObserverCaptor.getValue().onChanged(stubEntries);

        verify(mView).showEntries(stubEntries);
        verify(mView).showEmptyItemsPlaceholder(false);
    }

    @Test
    public void onBackButtonPressed_navigatesBack(){
        mPresenter.onBackButtonPressed();
        verify(mView).navigateBack();
    }

}