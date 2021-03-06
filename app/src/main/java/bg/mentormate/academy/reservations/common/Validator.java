package bg.mentormate.academy.reservations.common;

/**
 * Created by Maria on 2/15/2015.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    static final String DATEFORMAT = "E d MMM 'at' HH:mm";
    static final String DATEFORMATSTRING = "yyyy-MM-dd HH:mm";

    public static boolean isEmpty(String value) {
        if (value == null) {
            return true;
        }
        String nullString =  new String("null");
        if (nullString.equals(value)) {
            return true;
        }
        String emptyString = new String("");
        if (emptyString.equals(value)) {
            return true;
        }
        return false;

    }

    public static boolean validateEmailAddress(String emailAddress) {
        Pattern regexPattern;
        Matcher regMatcher;
        regexPattern = Pattern.compile("^[(a-zA-Z-0-9-\\_\\+\\.)]+@[(a-z-A-z)]+\\.[(a-zA-z)]{2,3}$");
        regMatcher   = regexPattern.matcher(emailAddress);
        if(regMatcher.matches()){
            return true;
        } else {
            return false;
        }
    }

    public static boolean validateMobileNumber(String mobileNumber) {
        //validate phone numbers of format "1234567890"
        if (mobileNumber.matches("\\d{10}")) return true;
        else return false;
    }

    public static String md5(String input) {

        String md5 = null;

        if(null == input) return null;

        try {

            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");

            //Update input string in message digest
            digest.update(input.getBytes(), 0, input.length());

            //Converts message digest value in base 16 (hex)
            md5 = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }
        return md5;
    }


    public static long getCurrentGMTTime() {
        //Calendar GMTCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        Calendar GMTCalendar = Calendar.getInstance();
        long currentGMTTime = GMTCalendar.getTimeInMillis(); //or getTime() //.get(Calendar.HOUR_OF_DAY)
        return currentGMTTime;
    }


    public static String createString(InputStreamReader byteReader) throws IOException {
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

    public static String readHttpResponse(HttpResponse response) throws IOException {
        String resultString = "";
        HttpEntity entity = response.getEntity();
        InputStream inputStream = null;
        try {
            inputStream = entity.getContent();
        } catch (IOException e) {
            e.printStackTrace();
            return resultString;
        }
        InputStreamReader byteReader = null;
        try {
            byteReader = new InputStreamReader(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return resultString;
        }

        try {
            resultString = Validator.createString(byteReader);
        } catch (IOException e) {
            e.printStackTrace();
            return resultString;
        }

        if(inputStream != null && inputStream.available() != 0) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    public static Date GetDatetimeAsDate(long milliSeconds)
    {
        //note: doesn't check for null
        return StringDateToDate(GetDatetimeAsString(milliSeconds));
    }

    public static String GetDatetimeAsString(long milliSeconds)
    {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
        //sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        final String utcTime = sdf.format(new Date(milliSeconds));
        return utcTime;
    }

    public static Date StringDateToDate(String StrDate)
    {
        Date dateToReturn = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMATSTRING);

        try
        {
            dateToReturn = (Date)dateFormat.parse(StrDate);
            long time = dateToReturn.getTime();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return dateToReturn;
    }


    public static boolean hasNetworkConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
//        networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        boolean isWifiConn = networkInfo.isConnected();
//        networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//        boolean isMobileConn = networkInfo.isConnected();
//        Log.d(DEBUG_TAG, "Wifi connected: " + isWifiConn);
//        Log.d(DEBUG_TAG, "Mobile connected: " + isMobileConn);

        networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();

        matrix.postRotate(degrees);

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int maxTargetWidth = 120; // your arbitrary fixed limit
        int maxTargetHeight = 120;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(originalWidth/maxTargetWidth, originalHeight/maxTargetHeight);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, originalWidth/scaleFactor, originalHeight/scaleFactor, true);

        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);

        return rotatedBitmap;
    }
}
