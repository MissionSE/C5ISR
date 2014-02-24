package com.missionse.mapsexample.utils;
import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.io.IOException;

/**
 *
 * @author Michaël André
 */
public class BitmapHelper {

    public static Bitmap getDrawableBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private static Bitmap getScaledBitmap(Bitmap source, Rect src, int width, int height) {
        System.gc();
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        canvas.drawBitmap(source, src, new Rect(0, 0, width, height), paint);
        return output;
    }

    public static Bitmap getScaledBitmapFillCrop(Bitmap source, int width, int height) {
        if (source.getWidth() < width && source.getHeight() < height) {
            return source;
        }
        int srcWidth = source.getWidth();
        int srcHeight = source.getHeight();
        double scale = Math.max(width * 1d / srcWidth, height * 1d / srcHeight);
        Rect src = new Rect(
                (int) ((srcWidth - width / scale) / 2),
                (int) ((srcHeight - height / scale) / 2),
                (int) ((srcWidth + width / scale) / 2),
                (int) ((srcHeight + height / scale) / 2)
        );
        return getScaledBitmap(source, src, width, height);
    }

    public static Bitmap getScaledBitmapFillCrop(Context context, Uri source, int width, int height) throws IOException {
        return getScaledBitmapFillCrop(getResampledBitmap(context, source, width, height), width, height);
    }

    public static Bitmap getScaledBitmapFill(Bitmap source, int minWidth, int minHeight) {
        if (source.getWidth() < minWidth && source.getHeight() < minHeight) {
            return source;
        }
        int srcWidth = source.getWidth();
        int srcHeight = source.getHeight();
        double scale = Math.max(minWidth * 1d / srcWidth, minHeight * 1d / srcHeight);
        return getScaledBitmap(source, new Rect(0, 0, srcWidth, srcHeight), (int) (srcWidth * scale), (int) (srcHeight * scale));
    }

    public static Bitmap getScaledBitmapFill(Context context, Uri source, int width, int height) throws IOException {
        return getScaledBitmapFill(getResampledBitmap(context, source, width, height), width, height);
    }

    public static int getOrientation(Context context, Uri file) throws IOException {
        if ("content".equals(file.getScheme())) {
            Cursor cursor = context.getContentResolver().query(file, new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                return cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));
            }
        }
        ExifInterface exif = new ExifInterface(file.getPath());
        switch (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            case ExifInterface.ORIENTATION_ROTATE_270: return 270;
            case ExifInterface.ORIENTATION_ROTATE_180: return 180;
            case ExifInterface.ORIENTATION_ROTATE_90: return 90;
            default: return 0;
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
    public static Bitmap getResampledBitmap(Context context, Uri source, int minWidth, int minHeight) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(source), null, options);
        int scaleFactor = (int) Math.min(options.outWidth * 1f / minWidth, options.outHeight * 1f / minHeight);
        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;
        options.inPurgeable = true;
        if (android.os.Build.VERSION.SDK_INT >= 10) {
            options.inPreferQualityOverSpeed = true;
        }
        Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(source), null, options);
        int orientation = getOrientation(context, source);
        if (orientation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        return bitmap;
    }

}