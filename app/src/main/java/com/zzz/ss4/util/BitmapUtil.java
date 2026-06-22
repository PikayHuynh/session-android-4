package com.zzz.ss4.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class BitmapUtil {
    /**
     * convert bitmap to byte array
     * @param bitmap Bitmap
     * @return byte[]
     */
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    /**
     * convert byte array to bitmap image
     * @param bytes byte array
     * @return Bitmap
     */
    public static Bitmap getBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
