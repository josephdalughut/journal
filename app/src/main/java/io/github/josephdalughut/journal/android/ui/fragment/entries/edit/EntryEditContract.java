package io.github.josephdalughut.journal.android.ui.fragment.entries.edit;

import io.github.josephdalughut.journal.android.data.models.entry.Entry;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 29/06/2018
 *
 * Contract between view and presenter for editing entries.
 */
public interface EntryEditContract {

    public interface View {
        public void navigateBack();
        public void showLoadingProgress(boolean visible);
        public void showContentUi(boolean visible);
        public void setEntryTitle(String title);
        public void setEntryContent(String content);
        public void showDeleteDialog();
        public void inflateOptionsMenu(int menuResId);
    }

    public interface Presenter {

        public void onBackButtonPressed();
        public void saveChanges();
        public void loadEntry(Long entryId);
        public void onEntryTitleChanged(String newTitle);
        public void onEntryContentChanged(String content);
        public void onDeleteButtonClicked();
        public void onConfirmDeleteButtonClicked();

    }

}
