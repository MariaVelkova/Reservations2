package bg.mentormate.academy.reservations.common;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import bg.mentormate.academy.reservations.R;

/**
 * Created by Student09 on 2/19/2015.
 */
public class PostMakeReservation extends AsyncTask<String, Void, String> {
    private int user;
    private int venue;
    private long date;
    private int count;
    private String comment;

    Context context;
    public PostMakeReservation(Context context, int user, int venue, long date, int count, String comment) throws NetworkErrorException {
        this.user = user;
        this.venue = venue;
        this.date = date;
        this.count = count;
        this.comment = comment;

        this.context = context;

        if (!Validator.hasNetworkConnection(context)) {
            throw new NetworkErrorException(context.getResources().getString(R.string.no_network));
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://mma-android.comxa.com/reservation.php");
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("user", Integer.toString(user)));
        pairs.add(new BasicNameValuePair("venue", Integer.toString(venue)));
        pairs.add(new BasicNameValuePair("date", Long.toString(date)));
        pairs.add(new BasicNameValuePair("count", Integer.toString(count)));
        pairs.add(new BasicNameValuePair("comment", comment));
        try {
            post.setEntity(new UrlEncodedFormEntity(pairs));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpResponse response = null;
        try {
            response = client.execute(post);
            result = Validator.readHttpResponse(response);
            int i = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
