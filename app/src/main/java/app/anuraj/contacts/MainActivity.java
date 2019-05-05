package app.anuraj.contacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.anuraj.contacts.R;

import app.anuraj.contacts.fragments.ContactDetailFragment;
import app.anuraj.contacts.fragments.ContactsFragment;
import app.anuraj.contacts.model.Contact;

public class MainActivity extends AppCompatActivity implements ContactsFragment.OnFragmentInteractionListener,
        ContactDetailFragment.OnCallInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_host, new ContactsFragment())
                .commit();

    }

    @Override
    public void onListFragmentInteraction(Contact contact) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_host, ContactDetailFragment.newInstance(contact))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void OnCallClick(String number) {
        Toast.makeText(this, "calling " + number, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    @Override
    public void OnSMSClick(String number) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
    }

    @Override
    public void OnEmailClick(String email) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");
        startActivity(Intent.createChooser(intent, "Send Email"));
    }
}
