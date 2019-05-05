package app.anuraj.contacts.adapters;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anuraj.contacts.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import app.anuraj.contacts.fragments.ContactsFragment;
import app.anuraj.contacts.model.Contact;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    private final int mNameColIdx, mIdColIdx, mThumbColIdx, mHasPhoneIdx, mPhotoCol;
    private Cursor mCursor;
    private ContentResolver contentResolver;

    private ContactsFragment.OnFragmentInteractionListener mListener;

    public ContactsAdapter(Cursor cursor, ContentResolver cr,
                           ContactsFragment.OnFragmentInteractionListener listener) {
        mCursor = cursor;
        mNameColIdx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);
        mIdColIdx = cursor.getColumnIndex(ContactsContract.Contacts._ID);
        mThumbColIdx = cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI);
        mPhotoCol = cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI);
        mHasPhoneIdx = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        contentResolver = cr;
        mListener = listener;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int pos) {
        View listItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contacts_list_item, parent, false);

        return new ContactViewHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int pos) {
        // Extract info from cursor
        mCursor.moveToPosition(pos);
        String contactName = mCursor.getString(mNameColIdx);
        long contactId = mCursor.getLong(mIdColIdx);
        String hasPhone = mCursor.getString(mHasPhoneIdx);
        String thumbnail = mCursor.getString(mThumbColIdx);
        String photo = mCursor.getString(mPhotoCol);

        Contact c = new Contact();

        ArrayList<String> numbers = new ArrayList<>();
        if (Integer.parseInt(hasPhone) > 0) {
            Cursor phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                    null, null);
            while (phones.moveToNext()) {
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Log.i("Number", phoneNumber);
                numbers.add(phoneNumber);
            }
            phones.close();
        }

        String email = "";
        Cursor ce = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null);
        if (ce != null && ce.moveToFirst()) {
            email = ce.getString(ce.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            ce.close();
        }

        c.setEmail(email);
        c.setName(contactName);
        c.setNumbers(numbers);

        if (thumbnail == null) {
            c.setThumbnail(null);
        } else {
            c.setThumbnail(Uri.parse(thumbnail));
        }
        if (photo == null) {
            c.setPhoto(null);
        } else {
            c.setPhoto(Uri.parse(photo));
        }

        contactViewHolder.bind(c);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImage;
        private TextView mLabel;
        private Contact mBoundContact; // Can be null

        public ContactViewHolder(final View itemView) {
            super(itemView);
            mImage = (ImageView) itemView.findViewById(R.id.rounded_iv_profile);
            mLabel = (TextView) itemView.findViewById(R.id.tv_label);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBoundContact != null) {
                        mListener.onListFragmentInteraction(mBoundContact);
                    }
                }
            });
        }

        public void bind(Contact contact) {
            mBoundContact = contact;
            mLabel.setText(contact.getName());

            if (contact.getThumbnail() == null) {
                Glide
                        .with(itemView.getContext())
                        .load(R.drawable.person_placeholder)
                        .apply(RequestOptions.circleCropTransform())
                        .into(mImage);
            } else {
                Glide
                        .with(itemView.getContext())
                        .load(contact.getThumbnail())
                        .placeholder(R.drawable.person_placeholder)
                        .apply(RequestOptions.circleCropTransform())
                        .into(mImage);
            }
        }
    }
}