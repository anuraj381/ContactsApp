package app.anuraj.contacts.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anuraj.contacts.R;

import java.util.ArrayList;

import app.anuraj.contacts.fragments.ContactDetailFragment;

public class NumbersAdapter extends RecyclerView.Adapter<NumbersAdapter.NumberViewHolder> {

    ArrayList<String> mNumbers;
    private ContactDetailFragment.OnCallInteractionListener mListener;

    public NumbersAdapter(ArrayList<String> list,
                          ContactDetailFragment.OnCallInteractionListener listener) {
        mNumbers = list;
        mListener = listener;
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup parent, int pos) {
        View listItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.phone_list_item, parent, false);

        return new NumberViewHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(NumberViewHolder contactViewHolder, int pos) {
        contactViewHolder.bind(mNumbers.get(pos));
    }

    @Override
    public int getItemCount() {
        return mNumbers.size();
    }

    class NumberViewHolder extends RecyclerView.ViewHolder {
        private TextView mNumber;
        private ImageView mPhone;
        private ImageView message;
        private String num;

        public NumberViewHolder(final View itemView) {
            super(itemView);
            mNumber = (TextView) itemView.findViewById(R.id.number);
            mPhone = (ImageView) itemView.findViewById(R.id.phoneimage);
            message = (ImageView) itemView.findViewById(R.id.message);
            mNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.OnCallClick(num);
                }
            });
            mPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.OnCallClick(num);
                }
            });
            message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.OnSMSClick(num);
                }
            });
        }

        public void bind(String number) {
            num = number;
            mNumber.setText(number);
        }
    }
}