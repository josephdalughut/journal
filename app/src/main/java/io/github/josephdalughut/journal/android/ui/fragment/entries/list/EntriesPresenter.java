package io.github.josephdalughut.journal.android.ui.fragment.entries.list;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import java.util.List;

import io.github.josephdalughut.journal.android.data.models.entry.Entry;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 29/06/2018
 *
 * Implementation of {@link EntriesContract.Presenter}
 *
 */
public class EntriesPresenter implements EntriesContract.Presenter, Observer<List<Entry>> {

    EntriesContract.View mView; //view controlled by this presenter
    EntriesRepository mRepository; //repo providing entries

    /**
     * Default constructor
     * @param view a {@link EntriesContract.View} directed by this presenter
     * @param repository a {@link EntriesRepository} where this presenter would fetch
     * {@link io.github.josephdalughut.journal.android.data.models.entry.Entry}(ies)
     */
    public EntriesPresenter(EntriesContract.View view, EntriesRepository repository) {
        this.mView = view;
        this.mRepository = repository;
    }

    @Override
    public void onAddEntryButtonClicked() {
        if(mView == null)
            return;
        mView.showAddEntryUi();
    }

    @Override
    public void loadEntries() {
        if(mRepository == null || mView == null)
            return;
        mView.showLoadingProgress(true);
        mRepository.loadEntries(mView, this);
    }

    @Override
    public void onEntryItemSelected(Entry entry) {
        if(mView == null)
            return;
        mView.showEntry(entry.getId());
    }

    @Override
    public void onChanged(@Nullable List<Entry> entries) {
        if(mView == null)
            return;
        mView.showEntries(entries);
        mView.showLoadingProgress(false);
    }
}
