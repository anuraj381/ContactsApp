package app.anuraj.contacts.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anuraj.contacts.R;

import app.anuraj.contacts.adapters.ContactsAdapter;
import app.anuraj.contacts.model.Contact;


public class ContactsFragment extends Fragment {

    private static final String[] PROJECTION = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
            ContactsContract.Contacts.PHOTO_URI,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.HAS_PHONE_NUMBER
    };
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    OnFragmentInteractionListener mListener;
    private RecyclerView mContactListView;

    public ContactsFragment() {
    }

    @SuppressWarnings("unused")
    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.contacts_list, container, false);

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        toolbar.setTitle("Contacts");

        mContactListView = (RecyclerView) root.findViewById(R.id.rv_contact_list);
        mContactListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mContactListView.setItemAnimator(new DefaultItemAnimator());
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestContacts();
    }

    private void requestContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            showContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showContacts();
            } else {
                Log.e("Permissions", "Access denied");
            }
        }
    }

    private void showContacts() {
        getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                Uri contactsUri = ContactsContract.Contacts.CONTENT_URI;
                return new CursorLoader(
                        getActivity(),
                        contactsUri,
                        PROJECTION,
                        null,
                        null,
                        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC");
            }

            @Override
            public void onLoadFinished(Loader<Cursor> objectLoader, Cursor c) {
                // Put the result Cursor in the adapter for the ListView
                mContactListView.setAdapter(new ContactsAdapter(c, getActivity().getContentResolver(), mListener));
            }

            @Override
            public void onLoaderReset(Loader<Cursor> cursorLoader) {
            }
        });
    }

    public interface OnFragmentInteractionListener {
        void onListFragmentInteraction(Contact contact);
    }

}
