package app.anuraj.contacts.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anuraj.contacts.R;

import app.anuraj.contacts.adapters.NumbersAdapter;
import app.anuraj.contacts.model.Contact;

public class ContactDetailFragment extends Fragment {

    static Contact contact;
    ContactDetailFragment.OnCallInteractionListener mListener;

    public ContactDetailFragment() {
    }

    @SuppressWarnings("unused")
    public static ContactDetailFragment newInstance(Contact c) {
        contact = c;
        return new ContactDetailFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ContactsFragment.OnFragmentInteractionListener) {
            mListener = (ContactDetailFragment.OnCallInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.contact_detail_layout, container, false);

        ImageView imageView = (ImageView) root.findViewById(R.id.contact_image);
        TextView textView = (TextView) root.findViewById(R.id.contact_name);
        final TextView emailText = (TextView) root.findViewById(R.id.email_text);
        RelativeLayout emailView = (RelativeLayout) root.findViewById(R.id.email_view);

        imageView.setImageURI(contact.getPhoto());
        textView.setText(contact.getName());
        if (!contact.getEmail().equals("")) {
            emailText.setText(contact.getEmail());
        } else {
            emailView.setVisibility(View.GONE);
        }

        emailText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.OnEmailClick(emailText.getText().toString());
            }
        });

        RecyclerView mContactListView;
        mContactListView = (RecyclerView) root.findViewById(R.id.number_list);
        mContactListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mContactListView.setItemAnimator(new DefaultItemAnimator());

        mContactListView.setAdapter(new NumbersAdapter(contact.getNumbers(), mListener));

        return root;
    }

    public interface OnCallInteractionListener {
        void OnCallClick(String number);
        void OnSMSClick(String number);
        void OnEmailClick(String email);
    }

}
