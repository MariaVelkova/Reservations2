package bg.mentormate.academy.reservations.common;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import java.io.IOException;

import bg.mentormate.academy.reservations.models.User;

/**
 * Created by Student09 on 2/21/2015.
 */
public class GetVenues  extends AsyncTask<String, Void, String> {

    private Context context;
    SessionData sessionData = SessionData.getInstance();
    User user = sessionData.getUser();

    public GetVenues(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        // params comes from the execute() call: params[0] is the url.
        String result = "";
        HttpResponse response = null;
        HttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
        HttpGet httpGet = new HttpGet("http://mma-android.comxa.com/index.php?action=venues&city="+user.getCity());
        try {
            response = httpclient.execute(httpGet);
            result = Validator.readHttpResponse(response);
            int i = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }
}
