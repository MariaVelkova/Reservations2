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
 * Created by Student09 on 2/21/2015.
 */
public class PostLogin extends AsyncTask<String, Void, String> {

    private String email;
    private String pass;


    public PostLogin(String email, String pass) {
            this.email = email;
            this.pass = pass;
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://mma-android.comxa.com/login.php");
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("email", email));
        pairs.add(new BasicNameValuePair("pass", pass));
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
