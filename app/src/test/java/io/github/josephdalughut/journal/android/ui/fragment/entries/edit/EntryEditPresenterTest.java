package io.github.josephdalughut.journal.android.ui.fragment.entries.edit;

import android.test.suitebuilder.annotation.SmallTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.github.josephdalughut.journal.android.data.models.entry.Entry;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 29/06/2018
 */
@SmallTest
public class EntryEditPresenterTest {

    @Mock private EntryEditContract.View mView;
    @Mock private EntryEditRepository mRepository;

    @Captor private ArgumentCaptor<EntryEditRepository.LoadEntryCallback> mEntrySupplierCaptor;

    private EntryEditPresenter mPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this); //initialize mocks

        mPresenter = new EntryEditPresenter(mView, mRepository); //create presenter instance
    }

    @After
    public void tearDown() {
    }

    @Test
    public void onBackButtonPressed_navigatesBack() {
        mPresenter.onBackButtonPressed();
        verify(mView).navigateBack();
    }

    @Test
    public void loadEntry_showsLoadingUi(){
        mPresenter.loadEntry(anyLong());
        verify(mView).showContentUi(false);
        verify(mView).showLoadingProgress(true);
    }

    @Test
    public void loadEntry_showsEntry(){

        Entry entry = new Entry();
        mPresenter.loadEntry(anyLong());
        verify(mRepository).loadEntry(anyLong(), mEntrySupplierCaptor.capture());
        mEntrySupplierCaptor.getValue().onEntryLoaded(Mockito.mock(Entry.class));

        //display ui
        verify(mView).showContentUi(true);
        verify(mView).showLoadingProgress(false);

        //show entry
        verify(mView).setEntryTitle(entry.getTitle());
        verify(mView).setEntryContent(entry.getContent());
    }

    @Test
    public void loadEntry_onNewEntry_neverShowsDeleteMenu(){
        Entry entry = new Entry();
        mPresenter.loadEntry(anyLong());
        verify(mRepository).loadEntry(anyLong(), mEntrySupplierCaptor.capture());
        mEntrySupplierCaptor.getValue().onEntryLoaded(entry);
        verify(mView, never()).inflateOptionsMenu(anyInt());
    }

    @Test
    public void loadEntry_onExistingEntry_showsDeleteMenu(){
        Entry entry = new Entry().setId(1L); //an entry with an id exists
        mPresenter.loadEntry(anyLong());
        verify(mRepository).loadEntry(anyLong(), mEntrySupplierCaptor.capture());
        mEntrySupplierCaptor.getValue().onEntryLoaded(entry);
        verify(mView).inflateOptionsMenu(anyInt());
    }

    @Test
    public void saveChanges_savesEntry() {
        mPresenter.saveChanges();
        verify(mRepository).saveEntry();
    }

    @Test
    public void onEntryTitleChanged_changesEntryTitle(){
        mPresenter.onEntryTitleChanged(anyString());
        verify(mRepository).setEntryTitle(anyString());
    }

    @Test
    public void onEntryContentChanged_changesEntryContent(){
        mPresenter.onEntryContentChanged(anyString());
        verify(mRepository).setEntryContent(anyString());
    }

    @Test
    public void onDeleteButtonClicked_showsDeleteDialog(){
        mPresenter.onDeleteButtonClicked();
        verify(mView).showDeleteDialog();
    }

    @Test
    public void onConfirmDeleteButtonClicked_removesEntry(){
        mPresenter.onConfirmDeleteButtonClicked();
        verify(mRepository).deleteEntry();
        verify(mView).navigateBack();
    }

}