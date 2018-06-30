package io.github.josephdalughut.journal.android.ui.fragment.entries.list;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import io.github.josephdalughut.journal.android.R;
import io.github.josephdalughut.journal.android.data.database.Database;
import io.github.josephdalughut.journal.android.data.models.entry.Entry;
import io.github.josephdalughut.journal.android.ui.fragment.abstracts.Fragment;
import io.github.josephdalughut.journal.android.ui.fragment.entries.edit.EntryEditFragment;
import io.github.josephdalughut.journal.android.ui.fragment.entries.list.adapter.EntryAdapter;
import io.github.josephdalughut.journal.android.ui.fragment.entries.list.adapter.PaddingItemDecoration;
import io.github.josephdalughut.journal.android.ui.fragment.entries.list.navigation.header.HeaderFragment;
import io.github.josephdalughut.journal.android.ui.utils.ViewUtils;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 29/06/2018
 *
 * Displays a list of type {@link io.github.josephdalughut.journal.android.data.models.entry.Entry}
 * belonging to the current user
 */
public class EntriesFragment extends Fragment implements EntriesContract.View, EntryAdapter.EntrySelectCallback {

    private static final String LOG_TAG = EntriesFragment.class.getSimpleName();

    /**
     * Use this instead of the default constructor to create an {@link EntriesFragment instance}
     * @return a new {@link EntriesFragment} instance.
     */
    public static EntriesFragment newInstance(){
        return new EntriesFragment();
    }


    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.layDrawer) public DrawerLayout layDrawer;

    @BindView(R.id.fabAddEntry) public FloatingActionButton fabAddEntry;

    @BindView(R.id.laySwipeRefresh) public SwipeRefreshLayout laySwipeRefresh;
    @BindView(R.id.vwRecycler) public RecyclerView vwRecycler; //shows our entries

    @BindView(R.id.vwNavigation) public NavigationView vwNavigation;

    private EntryAdapter mAdapter; //our adapter would handle displaying our entries in recyclerView

    EntriesContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateView() {

        //setup adapter and recyclerView
        mAdapter = new EntryAdapter(this);
        vwRecycler.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL)); //stagger our items

        //items should be decorated with custom padding
        vwRecycler.addItemDecoration(new PaddingItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.margin_page_half) / 2));

        vwRecycler.setAdapter(mAdapter);

        mPresenter = new EntriesPresenter(this,
                ViewModelProviders.of(this).get(EntriesRepositoryImpl.class));

        //inflate menu
        toolbar.inflateMenu(R.menu.menu_entries);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layDrawer.isDrawerOpen(Gravity.START)){
                    layDrawer.closeDrawer(Gravity.START);
                }else{
                    layDrawer.openDrawer(Gravity.START);
                }
            }
        });

        fabAddEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onAddEntryButtonClicked();
            }
        });

        ViewUtils.setDefaultRefreshColors(laySwipeRefresh);

//        add header fragment
        getChildFragmentManager().beginTransaction().replace(R.id.layNavigationHeaderFragmentContainer,
                HeaderFragment.newInstance()).commitAllowingStateLoss();

        laySwipeRefresh.setEnabled(false);
        mPresenter.loadEntries();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_entries;
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
    public void showSearchUi() {

    }
}