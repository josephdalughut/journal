package io.github.josephdalughut.journal.android.ui.fragment.entries.list.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 30/06/2018
 */
public class PaddingItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpacing;

    public PaddingItemDecoration(int spacing) {
        this.mSpacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if (parent.getPaddingLeft() != mSpacing) {
            parent.setPadding(mSpacing, mSpacing, mSpacing, mSpacing);
            parent.setClipToPadding(false);
        }

        outRect.top = mSpacing;
        outRect.bottom = mSpacing;
        outRect.left = mSpacing;
        outRect.right = mSpacing;
    }
}
