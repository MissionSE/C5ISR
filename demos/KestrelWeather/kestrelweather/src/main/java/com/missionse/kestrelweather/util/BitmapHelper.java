package com.missionse.kestrelweather.util;

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
 * Provides utility functions for loading bitmaps.
 */
public final class BitmapHelper {
	private static final int ROTATE_0 = 0;
	private static final int ROTATE_90 = 90;
	private static final int ROTATE_180 = 180;
	private static final int ROTATE_270 = 270;

	private BitmapHelper() {
	}

	/**
	 * Creates a bitmap out of a drawable.
	 * @param drawable The drawable.
	 * @return A bitmap representation out of a drawable.
	 */
	public static Bitmap getDrawableBitmap(final Drawable drawable) {
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

	/**
	 * Scales a bitmap to a requested size.
	 * @param source The source bitmap to scale.
	 * @param width The width of the scaled bitmap in pixels.
	 * @param height The height of the scaled bitmap in pixels.
	 * @return A bitmap that has been scaled to the requested size.
	 */
	public static Bitmap getScaledBitmapFillCrop(final Bitmap source, final int width, final int height) {
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

	/**
	 * Gets a scaled bitmap from a Uri.
	 * @param context The current context.
	 * @param source The source from which a bitmap is created.
	 * @param width The width of the scaled bitmap in pixels.
	 * @param height The height of the scaled bitmap in pixels.
	 * @return A bitmap that has been scaled to the requested size.
	 * @throws IOException When the Uri is not found.
	 */
	public static Bitmap getScaledBitmapFillCrop(final Context context, final Uri source,
			final int width, final int height) throws IOException {
		return getScaledBitmapFillCrop(getResampledBitmap(context, source, width, height), width, height);
	}

	/**
	 * Gets a filled scaled bitmap out of another bitmap.
	 * @param source The source bitmap to scale and fill.
	 * @param minWidth The minimum width of the bitmap in pixels.
	 * @param minHeight The minimum height of the bitmap in pixels.
	 * @return A filled and scaled bitmap.
	 */
	public static Bitmap getScaledBitmapFill(final Bitmap source, final int minWidth, final int minHeight) {
		if (source.getWidth() < minWidth && source.getHeight() < minHeight) {
			return source;
		}
		int srcWidth = source.getWidth();
		int srcHeight = source.getHeight();
		double scale = Math.max(minWidth * 1d / srcWidth, minHeight * 1d / srcHeight);
		return getScaledBitmap(source, new Rect(0, 0, srcWidth, srcHeight), (int) (srcWidth * scale), (int) (srcHeight * scale));
	}

	/**
	 * Gets a filled scaled bitmap out of a Uri.
	 * @param context The current context.
	 * @param source The source from which a bitmap is created.
	 * @param width The width of the scaled bitmap in pixels.
	 * @param height The height of the scaled bitmap in pixels.
	 * @return A filled and scaled bitmap.
	 * @throws IOException When the Uri is not found.
	 */
	public static Bitmap getScaledBitmapFill(final Context context, final Uri source,
			final int width, final int height) throws IOException {
		return getScaledBitmapFill(getResampledBitmap(context, source, width, height), width, height);
	}

	/**
	 * Gets the orientation of a file using the exif data.
	 * @param context The current context.
	 * @param file The file from which orientation is determined.
	 * @return The orientation of the file.
	 * @throws IOException When the Uri is not found.
	 */
	public static int getOrientation(final Context context, final Uri file) throws IOException {
		if ("content".equals(file.getScheme())) {
			Cursor cursor = context.getContentResolver().query(
					file, new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				return cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));
			}
		}
		ExifInterface exif = new ExifInterface(file.getPath());
		switch (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
			case ExifInterface.ORIENTATION_ROTATE_270:
				return ROTATE_270;
			case ExifInterface.ORIENTATION_ROTATE_180:
				return ROTATE_180;
			case ExifInterface.ORIENTATION_ROTATE_90:
				return ROTATE_90;
			default:
				return ROTATE_0;
		}
	}

	/**
	 * Gets a re-sampled bitmap from a Uri.
	 * @param context The current context.
	 * @param source The source from which a bitmap is created.
	 * @param minWidth The minimum width of the bitmap in pixels.
	 * @param minHeight The minimum height of the bitmap in pixels.
	 * @return A bitmap that has been re-sampled from the Uri.
	 * @throws IOException When the Uri is not found.
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
	public static Bitmap getResampledBitmap(final Context context, final Uri source,
			final int minWidth, final int minHeight) throws IOException {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(context.getContentResolver().openInputStream(source), null, options);
		int scaleFactor = (int) Math.min(options.outWidth * 1f / minWidth, options.outHeight * 1f / minHeight);
		options.inJustDecodeBounds = false;
		options.inSampleSize = scaleFactor;
		options.inPurgeable = true;
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
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
