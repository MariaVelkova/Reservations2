package bg.mentormate.academy.reservations.activities.admin;

import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.common.SessionData;

public class AdminHomeActivity extends ActionBarActivity {
    SessionData sessionData = SessionData.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        ImageView avatar;
        TextView fullName;
        TextView city;
        TextView email;
        TextView phone;
        String pictureArray = null;

        fullName = (TextView) findViewById(R.id.fullName);
        city = (TextView) findViewById(R.id.city);
        email = (TextView) findViewById(R.id.email);
        phone = (TextView) findViewById(R.id.phone);
        avatar = (ImageView) findViewById(R.id.avatar);
        fullName.setText(sessionData.getUser().getFirstName() + " " + sessionData.getUser().getLastName());
        city.setText(sessionData.getUser().getCity());
        email.setText(sessionData.getUser().getEmail());
        phone.setText(sessionData.getUser().getPhone());
        pictureArray = sessionData.getUser().getAvatar();

        pictureArray = pictureArray.trim();
        byte[] imgbytes = Base64.decode(pictureArray, Base64.URL_SAFE);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imgbytes);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(byteArrayInputStream);

        avatar.setImageDrawable(bitmapDrawable);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
