package jp.modal.soul.reminder.task;

import java.io.IOException;

import jp.modal.soul.reminder.util.ImageUtil;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class GetImageTask extends AsyncTask<Uri, Void, Bitmap>{
	/** ログ出力用 タグ */
    public final String TAG = this.getClass().getSimpleName();

	Context context;
	ImageView imageView;
	Boolean isImageAvailable;
	ProgressDialog dialog;
	
	public GetImageTask(Context context, ImageView imageView) {
		this.context = context;
		isImageAvailable = false;
		this.imageView = imageView;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		showDialog();
		
	}

	private void showDialog() {
		dialog = new ProgressDialog(context);
		dialog.setMessage("Now Loading...");
		dialog.setCancelable(false);
		dialog.show();
	}
	
	@Override
	protected Bitmap doInBackground(Uri... uris) {
		
		ImageUtil imageUtil = new ImageUtil(uris[0], context);
		Bitmap bitmap = null;
		try {
			bitmap = imageUtil.createBitmap();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return bitmap;
	}
	@Override
	protected void onPostExecute(Bitmap result) {
		if(result == null) {
			Log.e(TAG, "Get image failed.");
		}
		imageView.setImageBitmap(result);
		dissmissDialog();
	}

	private void dissmissDialog() {
		dialog.dismiss();
		dialog = null;
	}

}
