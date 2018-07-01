package io.github.josephdalughut.journal.android.ui.fragment.entries.search;

import io.github.josephdalughut.journal.android.ui.fragment.entries.list.EntriesPresenter;
import io.github.josephdalughut.journal.android.ui.fragment.entries.list.EntriesRepository;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 01/07/2018
 */
public class EntrySearchPresenter extends EntriesPresenter implements EntrySearchContract.Presenter {

    private EntrySearchContract.View mView;

    EntrySearchPresenter(EntrySearchContract.View view, EntriesRepository repository) {
        super(view, repository);
        this.mView = view;
    }

    @Override
    public void onClearSearchButtonClicked() {
        if(mView == null) return;
        mView.clearSearch();
    }

    @Override
    public void loadEntries(String searchQuery) {
        if(searchQuery == null || searchQuery.isEmpty()){
            //clear items
            onChanged(null);
            mView.showCloseSearchButton(false);
        }else{
            String formattedQuery = "%"+searchQuery +"%";
            mRepository.loadEntries(formattedQuery, mView, this);
            mView.showCloseSearchButton(true);
        }
    }

    @Override
    public void onBackButtonPressed() {
        if(mView == null) return;
        mView.navigateBack();

    }
}
