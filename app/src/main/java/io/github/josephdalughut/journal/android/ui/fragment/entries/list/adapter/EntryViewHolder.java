package io.github.josephdalughut.journal.android.ui.fragment.entries.list.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.josephdalughut.journal.android.R;
import io.github.josephdalughut.journal.android.data.models.entry.Entry;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 29/06/2018
 *
 * An implementation of {@link RecyclerView.ViewHolder} for displaying an
 * {@link io.github.josephdalughut.journal.android.data.models.entry.Entry}
 */
public class EntryViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.txtTitle) public TextView txtTitle;
    @BindView(R.id.txtContent) public TextView txtContent;

    EntryViewHolder(View itemView) {
        super(itemView);

        //bind with butterknife
        ButterKnife.bind(this, itemView);

    }

    public void setup(Entry entry){
        if(entry.getTitle() == null || entry.getTitle().isEmpty()){
            txtTitle.setVisibility(View.GONE);
        }else {
            txtTitle.setVisibility(View.VISIBLE);
            txtTitle.setText(entry.getTitle());
        }

        if(entry.getContent() == null || entry.getContent().isEmpty()){
            txtContent.setVisibility(View.GONE);
        }else {
            txtContent.setVisibility(View.VISIBLE);
            txtContent.setText(entry.getContent());
        }
    }

}
