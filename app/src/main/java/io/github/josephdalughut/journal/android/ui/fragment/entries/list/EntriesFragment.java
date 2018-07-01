package io.github.josephdalughut.journal.android.ui.fragment.entries.list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import io.github.josephdalughut.journal.android.R;
import io.github.josephdalughut.journal.android.data.models.entry.Entry;
import io.github.josephdalughut.journal.android.ui.fragment.abstracts.Fragment;
import io.github.josephdalughut.journal.android.ui.fragment.entries.edit.EntryEditFragment;
import io.github.josephdalughut.journal.android.ui.fragment.entries.list.adapter.EntryAdapter;
import io.github.josephdalughut.journal.android.ui.fragment.entries.list.adapter.PaddingItemDecoration;
import io.github.josephdalughut.journal.android.ui.fragment.entries.list.navigation.header.HeaderFragment;
import io.github.josephdalughut.journal.android.ui.fragment.entries.search.EntrySearchFragment;
import io.github.josephdalughut.journal.android.ui.fragment.settings.SettingsFragment;
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

    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.layDrawer) public DrawerLayout layDrawer;

    @BindView(R.id.fabAddEntry) public FloatingActionButton fabAddEntry;

    @BindView(R.id.laySwipeRefresh) public SwipeRefreshLayout laySwipeRefresh;
    @BindView(R.id.vwRecycler) public RecyclerView vwRecycler; //shows our entries
    @BindView(R.id.layEmpty) public View layEmpty;

    @BindView(R.id.vwNavigation) public NavigationView vwNavigation;

    private EntryAdapter mAdapter; //our adapter would handle displaying our entries in recyclerView

    protected EntriesContract.Presenter mPresenter;

    /**
     * Use this instead of the default constructor to create an {@link EntriesFragment instance}
     * @return a new {@link EntriesFragment} instance.
     */
    public static EntriesFragment newInstance(){
        return new EntriesFragment();
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {

        //setup adapter and recyclerView
        mAdapter = new EntryAdapter(this);
        setupAdapterLayoutManager();

        //items should be decorated with custom padding
        vwRecycler.addItemDecoration(new PaddingItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.margin_page_half) / 2));

        vwRecycler.setAdapter(mAdapter);

        mPresenter = new EntriesPresenter(this,
                ViewModelProviders.of(this).get(EntriesRepositoryImpl.class));

        //inflate menu
        toolbar.inflateMenu(R.menu.menu_entries);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_search:
                        mPresenter.onSearchButtonClicked();
                        return true;
                        default:
                            return false;
                }
            }
        });
        vwNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_settings:
                        layDrawer.closeDrawers();
                        getMainActivity().addFragmentToUi(SettingsFragment.newInstance(), false);
                        return true;
                }
                return false;
            }
        });

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
    }

    @Override
    public void onResume() {
        super.onResume();
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
        getMainActivity().addFragmentToUi(EntrySearchFragment.newInstance(), false);
    }

    @Override
    public void showEmptyItemsPlaceholder(boolean visible) {
        layEmpty.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    //sets the layout manager to show two or three columns in portrait or landscape respectively
    private void setupAdapterLayoutManager(){
        if(mAdapter == null || vwRecycler == null) return;

        int currentOrientation = getResources().getConfiguration().orientation;
        boolean landscape = currentOrientation == Configuration.ORIENTATION_LANDSCAPE;
        vwRecycler.setLayoutManager(new StaggeredGridLayoutManager(landscape ? 3 : 2,
                StaggeredGridLayoutManager.VERTICAL)); //stagger our items
    }

    //we'll change the layout manager to show three rows here
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setupAdapterLayoutManager();
    }
}
