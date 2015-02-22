package bg.mentormate.academy.reservations.http;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by PC on 16.2.2015 г..
 */
public class ServerTask extends AsyncTask<String, Void, String> {

    protected Context mContext;

    public ServerTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("DOWNLOAD", "Starting now...");
    }

    @Override
    protected String doInBackground(String... params) {

        String resultString = "";

        try {

            URL url = new URL(params[0]);

            HttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
            HttpGet httpGet = new HttpGet(String.valueOf(url));
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            InputStream inputStream = entity.getContent();
            InputStreamReader byteReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader reader = new BufferedReader(byteReader, 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            resultString = sb.toString();

            if(inputStream != null) {
                inputStream.close();
            }

        } catch (IOException e) {
            return e.toString();
        }

        return resultString;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("Tag", result);
    }
}
