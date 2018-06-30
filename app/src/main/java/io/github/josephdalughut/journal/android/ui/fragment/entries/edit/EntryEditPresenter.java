package io.github.josephdalughut.journal.android.ui.fragment.entries.edit;

import io.github.josephdalughut.journal.android.R;
import io.github.josephdalughut.journal.android.data.models.entry.Entry;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 29/06/2018
 *
 * Implementation of {@link EntryEditContract.Presenter}
 */
public class EntryEditPresenter implements EntryEditContract.Presenter, EntryEditRepository.LoadEntryCallback {

    EntryEditContract.View mView; //view impl
    EntryEditRepository mRepository; //repo impl

    /**
     * Default constructor
     * @param mView implementation of {@link EntryEditContract.View}
     * @param mRepository implementation of {@link EntryEditRepository}
     */
    public EntryEditPresenter(EntryEditContract.View mView, EntryEditRepository mRepository) {
        this.mView = mView;
        this.mRepository = mRepository;
    }

    @Override
    public void onBackButtonPressed() {
        if(mView != null) mView.navigateBack();
    }

    @Override
    public void saveChanges() {
        if(mRepository != null) mRepository.saveEntry();
    }

    @Override
    public void loadEntry(Long entryId) {
        if(mView == null || mRepository == null)
            return;

        mView.showLoadingProgress(true);
        mView.showContentUi(false);

        mRepository.loadEntry(entryId, this);
    }

    @Override
    public void onEntryLoaded(Entry entry) {
        if(mView == null || mRepository == null)
            return;

        mView.setEntryTitle(entry.getTitle());
        mView.setEntryContent(entry.getContent());

        mView.showLoadingProgress(false);
        mView.showContentUi(true);

        if(entry.getId() != null) mView.inflateOptionsMenu(R.menu.menu_entry);
    }

    @Override
    public void onEntryTitleChanged(String newTitle) {
        if(mRepository == null)
            return;
        mRepository.setEntryTitle(newTitle);
    }

    @Override
    public void onEntryContentChanged(String newContent) {
        if(mRepository == null)
            return;
        mRepository.setEntryContent(newContent);
    }

    @Override
    public void onDeleteButtonClicked() {
        if(mView == null) return;
        mView.showDeleteDialog();
    }

    @Override
    public void onConfirmDeleteButtonClicked() {
        if(mView == null || mRepository == null) return;
        mRepository.deleteEntry();
        mView.navigateBack();
    }
}
