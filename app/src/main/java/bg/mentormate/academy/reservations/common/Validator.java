package bg.mentormate.academy.reservations.common;

/**
 * Created by Maria on 2/15/2015.
 */

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
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Calendar;
import java.util.TimeZone;

public class Validator {

    static final String DATEFORMAT = "E d MMM 'at' HH:mm";

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
        Pattern regexPattern;
        Matcher regMatcher;
        regexPattern = Pattern.compile("^\\+[0-9]{2,3}+-[0-9]{10}$");
        regMatcher   = regexPattern.matcher(mobileNumber);
        if(regMatcher.matches()){
            return true;
        } else {
            return false;
        }
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
        Calendar GMTCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
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
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        final String utcTime = sdf.format(new Date(milliSeconds));

        return utcTime;
    }

    public static Date StringDateToDate(String StrDate)
    {
        Date dateToReturn = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);

        try
        {
            dateToReturn = (Date)dateFormat.parse(StrDate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return dateToReturn;
    }
}
