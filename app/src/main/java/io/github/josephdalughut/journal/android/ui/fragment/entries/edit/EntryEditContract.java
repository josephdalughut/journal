package io.github.josephdalughut.journal.android.ui.fragment.entries.edit;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 29/06/2018
 *
 * Contract between view and presenter for editing entries.
 */
public interface EntryEditContract {

    interface View {
        void navigateBack();
        void showLoadingProgress(boolean visible);
        void showContentUi(boolean visible);
        void setEntryTitle(String title);
        void setEntryContent(String content);
        void showDeleteDialog();
        void inflateOptionsMenu(int menuResId);
    }

    interface Presenter {

        void onBackButtonPressed();
        void saveChanges();
        void loadEntry(Long entryId);
        void onEntryTitleChanged(String newTitle);
        void onEntryContentChanged(String content);
        void onDeleteButtonClicked();
        void onConfirmDeleteButtonClicked();

    }

}
