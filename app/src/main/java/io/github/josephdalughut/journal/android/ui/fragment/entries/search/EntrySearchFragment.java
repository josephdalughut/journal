package io.github.josephdalughut.journal.android.ui.fragment.entries.search;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.List;

import butterknife.BindView;
import io.github.josephdalughut.journal.android.R;
import io.github.josephdalughut.journal.android.data.models.entry.Entry;
import io.github.josephdalughut.journal.android.ui.fragment.abstracts.Fragment;
import io.github.josephdalughut.journal.android.ui.fragment.entries.edit.EntryEditFragment;
import io.github.josephdalughut.journal.android.ui.fragment.entries.list.EntriesRepositoryImpl;
import io.github.josephdalughut.journal.android.ui.fragment.entries.list.adapter.EntryAdapter;
import io.github.josephdalughut.journal.android.ui.fragment.entries.list.adapter.PaddingItemDecoration;
import io.github.josephdalughut.journal.android.ui.utils.ViewUtils;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 29/06/2018
 *
 * Displays a list of type {@link Entry}
 * belonging to the current user
 */
public class EntrySearchFragment extends Fragment implements EntrySearchContract.View, EntryAdapter.EntrySelectCallback {

    private static final String LOG_TAG = EntrySearchFragment.class.getSimpleName();

    /**
     * Use this instead of the default constructor to create an {@link EntrySearchFragment instance}
     * @return a new {@link EntrySearchFragment} instance.
     */
    public static EntrySearchFragment newInstance(){
        return new EntrySearchFragment();
    }


    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.btnBack) public ImageButton btnBack;
    @BindView(R.id.edtSearch) public EditText edtSearch;
    @BindView(R.id.btnCloseSearch) public ImageButton btnCloseSearch;

    @BindView(R.id.laySwipeRefresh) public SwipeRefreshLayout laySwipeRefresh;
    @BindView(R.id.vwRecycler) public RecyclerView vwRecycler; //shows our entries
    @BindView(R.id.layEmpty) public View layEmpty;

    private EntryAdapter mAdapter; //our adapter would handle displaying our entries in recyclerView

    EntrySearchContract.Presenter mPresenter;

    @Override
    public void onCreateView(Bundle savedInstanceState) {

        //setup adapter and recyclerView
        mAdapter = new EntryAdapter(this);
        vwRecycler.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL)); //stagger our items

        //items should be decorated with custom padding
        vwRecycler.addItemDecoration(new PaddingItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.margin_page_half) / 2));

        vwRecycler.setAdapter(mAdapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onBackButtonPressed();
            }
        });
        btnCloseSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onClearSearchButtonClicked();
            }
        });

        mPresenter = new EntrySearchPresenter(this,
                ViewModelProviders.of(this).get(EntriesRepositoryImpl.class));

        //when text changes, start search
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPresenter.loadEntries(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnCloseSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onClearSearchButtonClicked();
            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        ViewUtils.setDefaultRefreshColors(laySwipeRefresh);


        laySwipeRefresh.setEnabled(false);

    }


    @Override
    public void navigateBack() {
        getActivity().onBackPressed();
    }

    @Override
    public void clearSearch() {
        edtSearch.setText(null);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_entry_search;
    }

    @Override
    public void showAddEntryUi() {
        getMainActivity().addFragmentToUi(EntryEditFragment.newInstance(), false);
    }

    @Override
    public void showLoadingProgress(boolean visible) {
        ViewUtils.setRefreshing(laySwipeRefresh, visible);
    }

    @Override
    public void showEntries(List<Entry> entries) {
        mAdapter.setEntries(entries);
    }

    @Override
    public void onEntrySelected(Entry entry) {
        mPresenter.onEntryItemSelected(entry);
    }

    @Override
    public void showEntry(Long entryId) {
        //show entry edit fragment
        getMainActivity().addFragmentToUi(EntryEditFragment.newInstance(entryId), false);
    }

    @Override
    public void showSearchUi() { }

    @Override
    public void showEmptyItemsPlaceholder(boolean visible) {
        layEmpty.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showCloseSearchButton(boolean visible) {
        btnCloseSearch.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }
}
