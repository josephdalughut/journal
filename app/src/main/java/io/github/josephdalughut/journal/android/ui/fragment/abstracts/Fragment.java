package io.github.josephdalughut.journal.android.ui.fragment.abstracts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import io.github.josephdalughut.journal.android.ui.activity.main.MainActivity;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 29/06/2018
 *
 * An abstract implementation of androids default {@link android.support.v4.app.Fragment} impl.
 * This class is a reusable component that abstracts most of the code we'd had to implement by
 * default when creating fragments.
 */
public abstract class Fragment extends android.support.v4.app.Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /*
            Here we can inflate the default layout, and call the onCreateView() method in subclasses
            so they don't have to handle inflation themselves.
         */

        //inflate the layout
        int mLayoutId = getLayoutId();
        View mRootView = inflater.inflate(mLayoutId, container, false);

        //bind views with ButterKnife
        ButterKnife.bind(this, mRootView);

        //tell subclasses view has been created
        onCreateView(savedInstanceState);

        //finally, return the root view
        return mRootView;
    }

    /**
     * This method would be called once the fragments view has been inflated. It's safe to find and
     * handle views here.
     */
    public abstract void onCreateView(Bundle savedInstanceState);

    /**
     * @return the resource id to the layout for this fragment.
     */
    public abstract int getLayoutId();

    /**
     * @return parent {@link MainActivity}
     */
    public MainActivity getMainActivity(){
        return (MainActivity) getActivity();
    }

}
