package bg.mentormate.academy.reservations.common;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import bg.mentormate.academy.reservations.http.HttpRequest;

/**
 * Created by Student09 on 2/21/2015.
 */
public class GetUserAccount  extends AsyncTask<String, Void, String> {
    private static final String DEBUG_TAG = "HttpAsyncTask";

    private Context context;
    private int userId;

    public GetUserAccount(Context context, int userId) {
        this.context = context;
        this.userId = userId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("DOWNLOAD", "Starting now...");
    }

    @Override
    protected String doInBackground(String... params) {
        // params comes from the execute() call: params[0] is the url.
        String result = "";
        try {
            URL url = new URL(params[0]);
            Log.d("URL", params[0]);
            result =  downloadUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
            return "Unable to retrieve web page. URL may be invalid.";
        } catch (NetworkErrorException e) {
            e.printStackTrace();
        }
        return  result;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        Log.d("DOWNLOAD", "Just finished - " + result);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            int code = jsonObject.getInt("code");
            String message = jsonObject.getString("message");
            JSONObject data = jsonObject.getJSONObject("data");
//            if (1 == code) {
//                switch (action) {
//                    case HttpRequest.LOGIN_ACTION:
//                        FileHelper.writeFile(context, data.toString());
//                        Log.d("Redirect","Main");
//                        break;
//                }
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*
        ArrayList<Day> days = new ArrayList<Day>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            Double message = jsonObject.getDouble("message");
            JSONObject city = jsonObject.getJSONObject("city");
            JSONObject coord = city.getJSONObject("coord");
            Double lat = coord.getDouble("lat");
            Double lon = coord.getDouble("lon");
            int fff= 0;
            JSONArray list = jsonObject.getJSONArray("list");
            for(int i = 0; i < list.length(); i++) {
                JSONObject day = list.getJSONObject(i);
                int dt = day.getInt("dt");
                int humidity = day.getInt("humidity");
                Double pressure = day.getDouble("pressure");
                Double speed = day.getDouble("speed");
                JSONArray weather = day.getJSONArray("weather");
                String icon = "";
                String description = "";
                String main = "";
                for(int ii = 0; ii < weather.length(); ii++) {
                    JSONObject item = weather.getJSONObject(ii);
                    icon = item.getString("icon");
                    description = item.getString("description");
                    main = item.getString("main");
                    int ppp = 0;
                }
                JSONObject temp = day.getJSONObject("temp");
                Double min = temp.getDouble("min");
                Double max = temp.getDouble("max");
                int pp = 0;
                Day dayModel = new Day(dt,lat, lon, humidity, pressure, speed, icon, description, main, min, max );
                days.add(dayModel);
            }
            int cnt = jsonObject.getInt("cnt");
            String cod = jsonObject.getString("cod");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomAdapter adapter = new CustomAdapter(context, days);
        listView.setAdapter(adapter);
        */
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private String downloadUrl(URL url) throws IOException, NetworkErrorException {
        InputStreamReader byteReader;
        InputStream inputStream = null;
        String resultString = "";

        Log.d("READ FROM", "WEB");
        HttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
        HttpGet httpGet = new HttpGet(String.valueOf(url));
        HttpResponse response = httpclient.execute(httpGet);
        HttpEntity entity = response.getEntity();

        inputStream = entity.getContent();
        byteReader = new InputStreamReader(inputStream, "UTF-8");

        resultString = createString(byteReader);

        if(inputStream != null && inputStream.available() != 0) {
            inputStream.close();
        }

        return resultString;
        //return "Success";
    }


    public String createString(InputStreamReader byteReader) throws IOException {
        String resultString = "";
        BufferedReader reader = new BufferedReader(byteReader, 8);
        StringBuilder sb = new StringBuilder();

        String line = null;
        while ((line = reader.readLine()) != null)
        {
            sb.append(line + "\n");
        }
        resultString = sb.toString();
        return resultString;
    }


}