package io.github.josephdalughut.journal.android.ui.activity.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.github.josephdalughut.journal.android.R;
import io.github.josephdalughut.journal.android.ui.fragment.entries.list.EntriesFragment;

/**
 * Our Main {@link android.app.Activity}, holds other visual components our users would interact with.
 *
 * Since the UI requirements of this application are fairly simple, we'll make use of only {@link android.app.Fragment}'s
 * instead. They'll be hosted in a container view in this activity.
 */
public class MainActivity extends AppCompatActivity {

    /** Our fragments would be added to this container */
    private static final int LAYOUT_ID_FRAGMENT_CONTAINER = R.id.layFragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //inflate our layout

        showMainUi();
    }

    /**
     * Adds a fragment to the main activity Ui
     * @param fragment a {@link Fragment} to add to the Ui
     * @param replace if false, the fragment would be stacked above all other fragments in the container,
     *                       otherwise it would replace all other fragments in the container.
     */
    public void addFragmentToUi(Fragment fragment, boolean replace){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction(); //get a transaction
        if(replace){
            transaction.replace(LAYOUT_ID_FRAGMENT_CONTAINER, fragment);
        }else{
            transaction.add(LAYOUT_ID_FRAGMENT_CONTAINER, fragment);

            //adding this transaction to backstack would let us navigate
            // back by pressing tbe back button
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    //moves to entries fragment
    private void showMainUi(){
        addFragmentToUi(EntriesFragment.newInstance(), true);
    }

}
