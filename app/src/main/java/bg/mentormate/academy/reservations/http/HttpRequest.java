package bg.mentormate.academy.reservations.http;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.activities.BroadcastNotifier;
import bg.mentormate.academy.reservations.common.Validator;

/**
 * Created by Maria on 2/15/2015.
 */
public class HttpRequest {


    private static final String DOWNLOAD_URL             = "http://mma-android.comxa.com/";

    public static final String LOGIN_ACTION                = "login";
    private static final String REGISTER_ACTION             = "register";
    private static final String ACCOUNT_ACTION              = "account";
    private static final String CREATE_VENUE_ACTION         = "create_venue";
    private static final String VENUES_ACTION               = "venues";
    private static final String MAKE_RESERVATION_ACTION     = "make_reservation";
    private static final String APPROVE_RESERVATION_ACTION  = "approve_reservation";
    private static final String REJECT_RESERVATION_ACTION   = "reject_reservation";
    private static final String RESERVATIONS_ACTION         = "reservations";
    private static final String VENUE_RESERVATIONS_ACTION   = "venue_reservations";
    private static final String CITIES_ACTION               = "cities";

    final String DEBUG_TAG = "bg.mentormate.academy.reservations.http.HttpRequest";

    private Context context;
    private BroadcastNotifier mBroadcaster;

    public HttpRequest(Context context) {

        this.context = context;
        this.mBroadcaster = new BroadcastNotifier(context);
    }


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

        resultString = Validator.createString(byteReader);

        if(inputStream != null && inputStream.available() != 0) {
            inputStream.close();
        }

        return resultString;
        //return "Success";
    }

    public JSONObject getData(URL url, String action) throws NetworkErrorException, IOException {

        if (!Validator.hasNetworkConnection(context)) {
            throw new NetworkErrorException(context.getResources().getString(R.string.no_network));
        }
        //new HttpAsyncTask(context, action).execute(url.toString());
        String result = "";
        try {
            result =  downloadUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("DOWNLOAD", "Just finished - " + result);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private URL getLoginUrl(String userEmail, String userPass) throws IOException {
        Log.d("E",userEmail+"\n");
        Log.d("P",userPass+"\n");
        String errors = "";
        if (Validator.isEmpty(userEmail)) {
            errors+= context.getResources().getString(R.string.empty_email_error);
        } else if (!Validator.validateEmailAddress(userEmail)) {
            errors+= context.getResources().getString(R.string.invalid_email_error);
        }
        if (Validator.isEmpty(userPass)) {
            errors+= context.getResources().getString(R.string.empty_pass_error);
        }
        if (errors.length() > 0) {
            throw new IOException(errors);
        }

        String url = DOWNLOAD_URL + "?action=" + LOGIN_ACTION + "&email=" + userEmail + "&pass=" + userPass;
        return new URL(url);
    }

    private URL getRegisterUrl(String userEmail, String userPass, String userPass2, String userFName, String userLNname, int userCity, String userPhone, int userType) throws IOException {
        String errors = "";
        if (Validator.isEmpty(userEmail)) {
            errors+= context.getResources().getString(R.string.empty_email_error);
        } else if (!Validator.validateEmailAddress(userEmail)) {
            errors+= context.getResources().getString(R.string.invalid_email_error);
        }
        if (Validator.isEmpty(userPass)) {
            errors+= context.getResources().getString(R.string.empty_pass_error);
        } else if (!userPass.equals(userPass2)) {
            errors+= context.getResources().getString(R.string.different_pass_error);
        }
        if (Validator.isEmpty(userFName)) {
            errors+= context.getResources().getString(R.string.empty_fname_error);
        }
        if (Validator.isEmpty(userLNname)) {
            errors+= context.getResources().getString(R.string.empty_lname_error);
        }
        if (Validator.isEmpty(userPhone)) {
            errors+= context.getResources().getString(R.string.empty_phone_error);
        }
        if (0 == userCity) {
            userCity = 1;
        }
        if (0 == userType) {
            userType = 1;
        }
        if (errors.length() > 0) {
            throw new IOException(errors);
        }

        String url = DOWNLOAD_URL + "?action=" + REGISTER_ACTION + "&email=" + userEmail + "&pass=" + userPass +
                "&fname=" + userFName + "&lname=" + userLNname + "&phone=" + userPhone + "&city=" + userCity + "&type=" + userType;
        return new URL(url);
    }

    private URL getCitiesUrl() throws IOException {
        String url = DOWNLOAD_URL + "?action=" + CITIES_ACTION;
        return new URL(url);
    }
    public JSONObject login(String userEmail, String userPass) throws IOException, NetworkErrorException{
        Log.d("userEmail",userEmail);
        return getData(getLoginUrl(userEmail,userPass), LOGIN_ACTION);
    }

    public JSONObject getCities() throws IOException, NetworkErrorException{
        return getData(getCitiesUrl(), CITIES_ACTION);
    }

    private URL getAccountUrl(int userId) throws IOException {
        String url = DOWNLOAD_URL + "?action=" + ACCOUNT_ACTION + "&id=" + userId;
        return new URL(url);
    }
}
