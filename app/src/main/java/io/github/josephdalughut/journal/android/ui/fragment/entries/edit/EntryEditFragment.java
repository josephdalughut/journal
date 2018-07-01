package io.github.josephdalughut.journal.android.ui.fragment.entries.edit;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import butterknife.BindView;
import io.github.josephdalughut.journal.android.R;
import io.github.josephdalughut.journal.android.data.database.Database;
import io.github.josephdalughut.journal.android.data.models.entry.Entry;
import io.github.josephdalughut.journal.android.ui.fragment.abstracts.Fragment;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 29/06/2018
 *
 * Fragment view for adding / editing an {@link EntryEditFragment}
 */
public class EntryEditFragment extends Fragment implements EntryEditContract.View {

    private static final String ARGUMENT_ENTRY_ID = "entryId";

    /**
     * @return a new {@link EntryEditFragment} instance.
     */
    public static EntryEditFragment newInstance(){
        return new EntryEditFragment();
    }

    /**
     * Use this to edit an existing {@link Entry}
     * @param entryId id of an {@link Entry} to edit
     * @return a new {@link EntryEditFragment} instance
     */
    public static EntryEditFragment newInstance(Long entryId){
        Bundle bundle = new Bundle();
        bundle.putLong(ARGUMENT_ENTRY_ID, entryId);
        EntryEditFragment fragment = newInstance();
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R.id.btnBack) public ImageButton btnBack;
    @BindView(R.id.toolbar) public Toolbar toolbar;

    @BindView(R.id.edtEntryTitle) public EditText edtEntryTitle;
    @BindView(R.id.edtEntryContent) public EditText edtEntryContent;

    @BindView(R.id.laySwipeRefresh) public SwipeRefreshLayout laySwipeRefresh;
    @BindView(R.id.vwContent) public View vwContent;

    EntryEditContract.Presenter mPresenter;
    private Long mEntryId; //entry to either create or edit

    @Override
    public void onCreateView() {

        //get entry id
        Bundle bundle = getArguments();
        if(bundle != null && bundle.containsKey(ARGUMENT_ENTRY_ID)){
            mEntryId = bundle.getLong(ARGUMENT_ENTRY_ID);
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == btnBack){
                    getActivity().onBackPressed(); //back navigation
                }
            }
        };

        laySwipeRefresh.setEnabled(false); //user doesn't need to refresh

        btnBack.setOnClickListener(onClickListener);

        //create new presenter
        mPresenter = new EntryEditPresenter(this,
                new EntryEditRepositoryImpl(getContext()));

        //load the entry
        mPresenter.loadEntry(mEntryId);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_edit_entry;
    }

    @Override
    public void navigateBack() {
        getActivity().onBackPressed();

    }

    @Override
    public void showLoadingProgress(boolean visible) {
        laySwipeRefresh.setRefreshing(visible);
    }

    @Override
    public void showContentUi(boolean visible) {
        vwContent.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void setEntryTitle(String title) {
        edtEntryTitle.setText(null); //clear all text first
        if(title != null) edtEntryTitle.append(title);
    }

    @Override
    public void setEntryContent(String content) {
        edtEntryContent.setText(null); //clear all text first
        if(content != null) {
            edtEntryContent.append(content);
            edtEntryContent.requestFocus();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mPresenter == null)
            return;

        /*
            We'll cache the changes made on pause, so they aren't lost and would be saved if onStop
            is subsequently called
         */
        if(edtEntryTitle != null ) mPresenter.onEntryTitleChanged(edtEntryTitle.getText().toString()
                .trim());
        if(edtEntryContent != null) mPresenter.onEntryContentChanged(edtEntryContent.getText()
                .toString().trim());
    }

    @Override
    public void onStop() {
        super.onStop(); //save the entry when fragment is closed
        if(mPresenter != null) mPresenter.saveChanges();
    }

    @Override
    public void showDeleteDialog() {
        //show dialog to confirm delete
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext())
                .setTitle(R.string.text_delete)
                .setMessage(R.string.text_confirmation_delete_entry)
                .setPositiveButton(R.string.text_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mPresenter.onConfirmDeleteButtonClicked();
                    }
                }).setNegativeButton(R.string.text_back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialogBuilder.show();
    }

    @Override
    public void inflateOptionsMenu(int menuResId) {
        //inflate menu
        toolbar.inflateMenu(menuResId);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_delete:
                        mPresenter.onDeleteButtonClicked();
                        return true;
                }
                return false;
            }
        });
    }
}
