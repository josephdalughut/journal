package io.github.josephdalughut.journal.android.ui.fragment.settings;

import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import io.github.josephdalughut.journal.android.R;
import io.github.josephdalughut.journal.android.ui.fragment.abstracts.Fragment;

/**
 * Joey Dalu (Joseph Dalughut)
 * <a href="http://joeydalu.herokuapp.com">joeydalu.herokuapp.com</a>
 * JournalApp
 * 01/07/2018
 */
public class SettingsFragment extends Fragment {

    @BindView(R.id.toolbar) public Toolbar toolbar;

    public static SettingsFragment newInstance(){
        return new SettingsFragment();
    }

    @Override
    public void onCreateView() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.layFragmentContainer, SettingsPreferencesFragment.newInstance())
                .commitAllowingStateLoss();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_settings;
    }
}
