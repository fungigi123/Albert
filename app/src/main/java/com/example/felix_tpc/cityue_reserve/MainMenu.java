package com.example.felix_tpc.cityue_reserve;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;


public class MainMenu extends AppCompatActivity implements Appointment.ContactsFragmentListener,
        DetailFragment.DetailFragmentListener,
        AddEditFragment.AddEditFragmentListener {


    private TextView mTextMessage;

    // key for storing a contact's Uri in a Bundle passed to a fragment
    public static final String CONTACT_URI = "contact_uri";

    private Appointment contactsFragment; // displays contact list


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_appointment:
                    mTextMessage.setText(R.string.title_appointment);
                    selectedFragment = Appointment.newInstance();
                    break;
                case R.id.navigation_medicalHistory:
                    mTextMessage.setText(R.string.title_medicalHistory);
                    selectedFragment = MediHistory.newInstance();
                    break;
                case R.id.navigation_news:
                    mTextMessage.setText(R.string.title_news);
                    selectedFragment = News.newInstance();
                    break;
                case R.id.navigation_aboutUs:
                    mTextMessage.setText(R.string.title_aboutUs);
                    selectedFragment = AboutUs.newInstance();
                    break;

            }
            FragmentTransaction transaction = getSupportFragmentManager().
                    beginTransaction();
            transaction.replace(R.id.mainFrag, selectedFragment);
            transaction.commit();
            return true;
        }

    };



    public void onContactSelected(Uri contactUri) {
            displayContact(contactUri, R.id.mainFrag);

    }

    // display AddEditFragment to add a new contact
    @Override
    public void onAddContact() {
            displayAddEditFragment(R.id.mainFrag, null);
            mTextMessage.setText(R.string.title_new_appointment);

    }

    // display a contact
    private void displayContact(Uri contactUri, int viewID) {
        DetailFragment detailFragment = new DetailFragment();

        // specify contact's Uri as an argument to the DetailFragment
        Bundle arguments = new Bundle();
        arguments.putParcelable(CONTACT_URI, contactUri);
        detailFragment.setArguments(arguments);

        // use a FragmentTransaction to display the DetailFragment
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.replace(viewID, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit(); // causes DetailFragment to display
    }

    // display fragment for adding a new or editing an existing contact
    private void displayAddEditFragment(int viewID, Uri contactUri) {
        AddEditFragment addEditFragment = new AddEditFragment();

        // if editing existing contact, provide contactUri as an argument
        if (contactUri != null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(CONTACT_URI, contactUri);
            addEditFragment.setArguments(arguments);
        }

        // use a FragmentTransaction to display the AddEditFragment
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.replace(viewID, addEditFragment);
        transaction.addToBackStack(null);
        transaction.commit(); // causes AddEditFragment to display
    }

    // return to contact list when displayed contact deleted
    @Override
    public void onContactDeleted() {
        // removes top of back stack
        getSupportFragmentManager().popBackStack();
        contactsFragment.updateContactList(); // refresh contacts
    }

    // display the AddEditFragment to edit an existing contact
    @Override
    public void onEditContact(Uri contactUri) {
            displayAddEditFragment(R.id.mainFrag, contactUri);
    }

    // update GUI after new contact or updated contact saved
    @Override
    public void onAddEditCompleted(Uri contactUri) {
        // removes top of back stack
        getSupportFragmentManager().popBackStack();
        contactsFragment.updateContactList();
        mTextMessage.setText(R.string.title_appointment);// refresh contacts

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        FragmentTransaction transaction = getSupportFragmentManager().
                beginTransaction();
        transaction.replace(R.id.mainFrag, News.newInstance());
        transaction.commit();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }
}
