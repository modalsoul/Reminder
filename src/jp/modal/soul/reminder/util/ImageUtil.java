package jp.modal.soul.reminder.util;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

public class ImageUtil {
    private static final int IMAGE_HEIGHT = 640;
    private static final int IMAGE_WIDTH = 480;
	
	Context context;
	Uri uri;
	public ImageUtil(Uri uri, Context context) {
		this.context = context;
		this.uri = uri;
	}
	public Bitmap createBitmap() throws IOException {
		Bitmap bitmap = null;
		InputStream is = null;
		
		try {
			BitmapFactory.Options options = getBitmapOptions();
			
			is = context.getContentResolver().openInputStream(uri);
			bitmap = BitmapFactory.decodeStream(is, null, options);
		} finally {
			if(is != null) {
				is.close();
			}
		}
		return bitmap;
	}
	
	private BitmapFactory.Options getBitmapOptions() throws IOException {
		BitmapFactory.Options options = new BitmapFactory.Options();
		
		InputStream is = null;
		
		try {
			// set attribute to get just image attribute
			options.inJustDecodeBounds = true;
			
			is = context.getContentResolver().openInputStream(uri);
			BitmapFactory.decodeStream(is, null, options);
			
			boolean landscape = options.outWidth > options.outHeight;
			int height = landscape ? IMAGE_HEIGHT : IMAGE_WIDTH;
			int width = landscape ? IMAGE_WIDTH : IMAGE_HEIGHT;
			
			int scale1 = (int)Math.floor(options.outWidth / width);
			int scale2 = (int)Math.floor(options.outHeight / height);
			
			int scale = Math.max(scale1, scale2);
			
			options.inSampleSize = scale + (scale % 2);
			options.inJustDecodeBounds = false;
			
		} finally {
			if(is != null) {
				is.close();
			}
		}
		return options;
	}
}
