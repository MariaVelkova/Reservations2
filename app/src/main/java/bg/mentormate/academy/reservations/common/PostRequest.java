package bg.mentormate.academy.reservations.common;

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

/**
 * Created by Student10 on 2/16/2015.
 */
public class PostRequest extends AsyncTask<String, Void, String> {
    private int userId = 0;
    private String email;
    private String pass;
    private String type;
    private String fname;
    private String lname;
    private String city;
    private String phone;
    private String photo;

    public PostRequest(int userId, String email, String pass, String type, String fname, String lname, String city, String phone, String photo) {
        this.userId = userId;
        this.email = email;
        this.pass = pass;
        this.type = type;
        this.fname = fname;
        this.lname = lname;
        this.city = city;
        this.phone = phone;
        this.photo = photo;
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://mma-android.comxa.com/user.php");
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("id", Integer.toString(userId)));
        pairs.add(new BasicNameValuePair("email", email));
        pairs.add(new BasicNameValuePair("pass", pass));
        pairs.add(new BasicNameValuePair("type", type));
        pairs.add(new BasicNameValuePair("fname", fname));
        pairs.add(new BasicNameValuePair("lname", lname));
        pairs.add(new BasicNameValuePair("city", city));
        pairs.add(new BasicNameValuePair("phone", phone));
        pairs.add(new BasicNameValuePair("photo", photo));
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
