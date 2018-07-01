package io.github.josephdalughut.journal.android.ui.fragment.entries.list;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.test.suitebuilder.annotation.SmallTest;

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
import io.github.josephdalughut.journal.android.ui.fragment.entries.list.EntriesContract;
import io.github.josephdalughut.journal.android.ui.fragment.entries.list.EntriesPresenter;
import io.github.josephdalughut.journal.android.ui.fragment.entries.list.EntriesRepository;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 29/06/2018
 */
@SmallTest
public class EntriesPresenterTest {

    @Mock private EntriesContract.View mView;
    @Mock private EntriesRepository mRepository;
    @Captor private ArgumentCaptor<Observer<List<Entry>>> mObserverCaptor;

    private EntriesPresenter mPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mPresenter = new EntriesPresenter(mView, mRepository);
    }

    @Test
    public void checkNotNull(){
        assertNotNull(mPresenter.mView);
        assertNotNull(mPresenter.mRepository);
    }

    @Test
    public void onAddEntryButtonClicked_ShowsAddEntryUi() {
        mPresenter.onAddEntryButtonClicked();
        verify(mView).showAddEntryUi();
    }

    @Test
    public void loadEntries_showsEntries(){
        mPresenter.loadEntries(); //load entries

        verify(mRepository).loadEntries(any(LifecycleOwner.class), mObserverCaptor.capture());
        List<Entry> stubEntries = new ArrayList<>();
        stubEntries.add(new Entry());

        mObserverCaptor.getValue().onChanged(stubEntries);

        verify(mView).showEntries(stubEntries);
        verify(mView).showEmptyItemsPlaceholder(false);
    }

    @Test
    public void onEntriesLoaded_onEmpty_showsEmptyPlaceholder(){
        mPresenter.loadEntries(); //load entries

        verify(mRepository).loadEntries(any(LifecycleOwner.class), mObserverCaptor.capture());
        List<Entry> stubEntries = new ArrayList<>();

        mObserverCaptor.getValue().onChanged(stubEntries);

        verify(mView).showEntries(stubEntries);
        verify(mView).showEmptyItemsPlaceholder(true);
    }


    @Test
    public void onEntryItemSelected_showsEntryItem(){
        Entry entry = new Entry().setId(1L);
        mPresenter.onEntryItemSelected(entry);
        verify(mView).showEntry(entry.getId());
    }

    @Test
    public void onSearchButtonClicked_showsSearchUi(){
        mPresenter.onSearchButtonClicked();
        verify(mView).showSearchUi();
    }

}