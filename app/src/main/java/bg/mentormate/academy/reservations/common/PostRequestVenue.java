package bg.mentormate.academy.reservations.common;

import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by PC on 16.2.2015 г..
 */
public class PostRequestVenue extends AsyncTask<String, Void, String> {
    private String name;
    private String type;
    private String phone;
    private String city;
    private String address;
    private String lat;
    private String lon;
    private String worktime;
    private String capacity;
    private String owner;

    public PostRequestVenue(String name, String type, String phone, String city, String address, String lat, String lon, String worktime, String capacity, String owner, String photo) {
        this.name = name;
        this.type = type;
        this.phone = phone;
        this.city = city;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
        this.worktime = worktime;
        this.capacity = capacity;
        this.owner = owner;
        this.photo = photo;
    }

    private String photo;



    @Override
    protected String doInBackground(String... params) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://mma-android.comxa.com/venue.php");
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("name", name));
        pairs.add(new BasicNameValuePair("type", type));
        pairs.add(new BasicNameValuePair("phone", phone));
        pairs.add(new BasicNameValuePair("city", city));
        pairs.add(new BasicNameValuePair("address", address));
        pairs.add(new BasicNameValuePair("lat", lat));
        pairs.add(new BasicNameValuePair("lon", lon));
        pairs.add(new BasicNameValuePair("worktime", worktime));
        pairs.add(new BasicNameValuePair("capacity", capacity));
        pairs.add(new BasicNameValuePair("owner", owner));
        pairs.add(new BasicNameValuePair("image", photo));
        try {
            post.setEntity(new UrlEncodedFormEntity(pairs));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            HttpResponse response = client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d("New Venue:  ", "DONE");
    }
}
