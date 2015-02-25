package bg.mentormate.academy.reservations.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.activities.admin.AdminActivity;
import bg.mentormate.academy.reservations.common.SessionData;

public class AboutActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        WebView webView = (WebView) findViewById(R.id.about_content);
        String summary = "<html>\n" +
                "<body style=\"background: #f9f9f9\">\n" +
                "<h2>MentorMate Academy</h2>\n" +
                "<h3>Android App</h3>\n" +
                "<p><strong>Developers:</strong> Maria Velkova and Emil Kolev</p>\n" +
                "<p><strong>Credits:</strong></p>\n" +
                "<div>Icons made by \n" +
                "<a href=\"http://www.freepik.com\" title=\"Freepik\">Freepik</a> and " +
                "<a href=\"http://www.sm-artists.com\" title=\"spovv\">spovv</a> \n" +
                "from <a href=\"http://www.flaticon.com\" title=\"Flaticon\">www.flaticon.com</a> is licensed by <a href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY 3.0\">CC BY 3.0</a></div>\n" +
                "</body></html>";
        webView.loadData(summary, "text/html", null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_blank, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent;
                if (SessionData.getInstance().getUser().getType() == 1) {
                    upIntent = new Intent(this,MainActivity.class);
                } else {
                    upIntent = new Intent(this,AdminActivity.class);
                }
                startActivity(upIntent);
                //NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
