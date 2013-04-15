package jp.modal.soul.reminder.task;

import java.lang.ref.WeakReference;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

public class LoadImageTask extends AsyncTask<String, Void, Bitmap>{
	private final WeakReference<ImageView> mImageViewReference;  
    int mWidth;  
    int mHeight;  
    String mFilePath;
    
    public LoadImageTask(ImageView imageView) {
        mImageViewReference = new WeakReference<ImageView>(imageView);  
        mWidth = imageView.getWidth();  
        mHeight = imageView.getHeight();  
	}
    
	@Override
	protected Bitmap doInBackground(String... params) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void onPostExecute(Bitmap bitmap) {
		final ImageView imageView = mImageViewReference.get();  
        if (imageView != null) {  
            // ImageView からタスクを取り出す  
            final LoadImageTask loadImageTask = getBitmapWorkerTask(imageView);  
            if (this == loadImageTask && imageView != null) {  
                // 同じタスクなら ImageView に Bitmap をセット  
                imageView.setImageBitmap(bitmap);  
            }  
        }   
	}

	private static LoadImageTask getBitmapWorkerTask(ImageView imageView) {  
		if (imageView != null) {  
			final Drawable drawable = imageView.getDrawable();  
			if (drawable instanceof AsyncDrawable) {  
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;  
				return asyncDrawable.getBitmapWorkerTask();  
			}  
		}  
		return null;  
	} 
	
	static class AsyncDrawable extends BitmapDrawable {  
	    private final WeakReference<LoadImageTask> loadImageTaskReference;  
	  
	    public AsyncDrawable(Resources res, Bitmap bitmap, LoadImageTask bitmapWorkerTask) {  
	        super(res, bitmap);  
	        loadImageTaskReference = new WeakReference<LoadImageTask>(bitmapWorkerTask);  
	    }  
	  
	    public LoadImageTask getBitmapWorkerTask() {  
	        return loadImageTaskReference.get();  
	    }  
	}
}
