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
public class GetReservations extends AsyncTask<String, Void, String> {

    int status;
    SessionData sessionData = SessionData.getInstance();
    User user = sessionData.getUser();

    public GetReservations(int status) {
        this.status = status;
    }

    @Override
    protected String doInBackground(String... params) {
        // params comes from the execute() call: params[0] is the url.
        String result = "";
        HttpResponse response = null;
        HttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
        HttpGet httpGet = new HttpGet("http://mma-android.comxa.com/index.php?action=reservations&id="+Integer.toString(user.getId()) + "&status=" + Integer.toString(status));
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
