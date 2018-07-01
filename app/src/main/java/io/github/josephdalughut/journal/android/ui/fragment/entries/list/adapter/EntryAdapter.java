package io.github.josephdalughut.journal.android.ui.fragment.entries.list.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.github.josephdalughut.journal.android.R;
import io.github.josephdalughut.journal.android.data.models.entry.Entry;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 29/06/2018
 *
 * An adapter which shows a list of entries in a {@link RecyclerView}
 * @see Entry
 * @see RecyclerView.Adapter
 *
 */
public class EntryAdapter extends RecyclerView.Adapter<EntryViewHolder> {

    private List<Entry> mEntryList; //list of entries to be shown
    private EntrySelectCallback mCallback;

    public EntryAdapter(EntrySelectCallback mCallback) {
        this.mCallback = mCallback;
    }

    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EntryViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_entry, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final EntryViewHolder holder, int position) {
        Entry entry = mEntryList.get(position);
        holder.setup(entry);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCallback == null)
                    return;
                int position = holder.getAdapterPosition();
                Entry selectedEntry = mEntryList.get(position);
                mCallback.onEntrySelected(selectedEntry);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEntryList == null  ? 0 : mEntryList.size();
    }

    public void setEntries(List<Entry> entries){
        this.mEntryList = entries;
        notifyDataSetChanged(); //refresh the ui
    }

    public interface EntrySelectCallback {
        void onEntrySelected(Entry entry);
    }

}
