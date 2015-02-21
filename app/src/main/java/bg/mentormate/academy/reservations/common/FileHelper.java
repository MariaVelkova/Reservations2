package bg.mentormate.academy.reservations.common;

import android.content.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Maria on 2/15/2015.
 */
public class FileHelper {
    private static String fileName = "user.txt";

    public static String readFile(Context context) {
        //READ JSON DATA ---------------------------------------------------------------------------
        String jsonString = "";
        InputStream is = null;
        try {
            File file = new File(context.getFilesDir(), fileName);
            is =  context.openFileInput(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
        //READ JSON DATA END -----------------------------------------------------------------------
    }

    public static void writeFile(Context context, String contentString) {
        FileWriter writer = null;
        try {
            File file = new File(context.getFilesDir(), fileName);
            writer = new FileWriter(file);

            /** Saving the contents to the file*/
            writer.write(contentString);

            /** Closing the writer object */
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/*
    public static Bitmap getBitmap(Bitmap bitmap) {
        final String TAG = "getBitmap";
        Charset UTF8_CHARSET = Charset.forName("UTF-8");
        ByteArrayOutputStream streamS = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, streamS);
        byte[] byteArray = streamS.toByteArray();

        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        String yourText = new String(byteArray, UTF8_CHARSET);

        //Uri uri = getImageUri(path);
        InputStream inputStream = null;
        try {
            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
            inputStream = mContentResolver.openInputStream(uri);

            // Decode image size
            BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
            bitmapFactoryOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, bitmapFactoryOptions);
            inputStream.close();


            int scale = 1;
            while ((bitmapFactoryOptions.outWidth * bitmapFactoryOptions.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d(TAG, "scale = " + scale + ", orig-width: " + bitmapFactoryOptions.outWidth + ", orig-height: " + bitmapFactoryOptions.outHeight);

            Bitmap b = null;
            inputStream = mContentResolver.openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                bitmapFactoryOptions = new BitmapFactory.Options();
                bitmapFactoryOptions.inSampleSize = scale;
                b = BitmapFactory.decodeStream(inputStream, null, bitmapFactoryOptions);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                Log.d(TAG, "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(inputStream);
            }
            inputStream.close();

            Log.d(TAG, "bitmap size - width: " + b.getWidth() + ", height: " + b.getHeight());
            return b;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }
    */
}
