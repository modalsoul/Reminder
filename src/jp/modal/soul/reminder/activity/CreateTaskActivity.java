package jp.modal.soul.reminder.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.modal.soul.reminder.R;
import jp.modal.soul.reminder.model.TaskDao;
import jp.modal.soul.reminder.model.TaskItem;
import jp.modal.soul.reminder.task.CreateMemoTask;
import jp.modal.soul.reminder.task.GetImageTask;
import jp.modal.soul.reminder.util.Const;
import jp.modal.soul.reminder.util.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CreateTaskActivity extends Activity {
	/** ログ出力用 タグ */
    public final String TAG = this.getClass().getSimpleName();
    
    public static final int ONE_MINUTE_SECONDS = 60;
    public static final int ONE_HOUR_MINUTES = 60;
    
    private static final int REQUEST_GALLERY = 0;
    private static final int REQUEST_CAMERA = 1;  
	EditText messageView;
	View setTimeButton;
	Button createTaskButton;
	Button cancelButton;
	TimePickerDialog dialog;
	TextView displayTime;
	Button setImageButton;
	ImageView image;
	
	TaskDao taskDao;
	TaskItem item;
	String imageUri = "";
	Uri mImageUri;
	
	int hour = 0;
	int minutes = 0;
	int taskId;
	
	private boolean isSetImage = false;
	
	AlertDialog.Builder imagePickerDialogBuilder;
	AlertDialog imagePickerDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_create_task);
		
		setupView();
		
		setupDao();
		
	}
	
	void setupDao() {
		taskDao = new TaskDao(this);
	}
	
	private void setupView() {
		getView();
		
		setupEventHandling();

		setupTimepicker();
	}

	private void setupTimepicker() {
		// TODO change TimePickerDialog to NumberPickerDialog
		// TODO increase maximum settable time.
		dialog = new TimePickerDialog(this, android.R.style.Theme_Black, onTimeSetListener, 0, 10, true);
	}

	private void setupEventHandling() {
		setTimeButton.setOnClickListener(onSetTimeButtonClickListener);
		createTaskButton.setOnClickListener(onCreateTaskButtonClickListener);
		cancelButton.setOnClickListener(onCancelButtonClickListener);
		setImageButton.setOnClickListener(onSetImageButtonClickListener);
	}

	private void getView() {
		displayTime = (TextView)findViewById(R.id.display_time);
		messageView = (EditText)findViewById(R.id.message);
		setTimeButton = findViewById(R.id.select_time);
		createTaskButton = (Button)findViewById(R.id.create_task);
		cancelButton = (Button)findViewById(R.id.cancel);
		setImageButton = (Button)findViewById(R.id.set_image_button);
		image = (ImageView)findViewById(R.id.image_view);
	}
	
	OnClickListener onSetTimeButtonClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			dialog.show();
		}
	};
	
	OnClickListener onCreateTaskButtonClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			String messageBody = messageView.getText().toString();
			
			if(messageBody.equals("")) {
				Toast.makeText(CreateTaskActivity.this, Const.EMPTY_BODY_TOAST_MESSAGE, Toast.LENGTH_SHORT).show();
			} else if(hour == 0 && minutes == 0) {
				Toast.makeText(CreateTaskActivity.this, Const.TIME_NOT_SELECTED_TOAST_MESSAGE, Toast.LENGTH_SHORT).show();
			} else {			
			
				setupTaskItem();
			
				registerMemo(item);
			
				finish();
			}
		}

		private void setupTaskItem() {
			item = new TaskItem();
			item.message = messageView.getText().toString();
			item.delay = (hour * ONE_HOUR_MINUTES + minutes) * ONE_MINUTE_SECONDS;
			item.status = TaskItem.STATUS_TODO;
			item.image_url = imageUri;
		}

	};
	void registerMemo(TaskItem item) {
		CreateMemoTask createMemoTask = new CreateMemoTask(this, item);
		
		createMemoTask.execute();
	}
	
	TimePickerDialog.OnTimeSetListener onTimeSetListener = new OnTimeSetListener(){

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			hour = hourOfDay;
			minutes = minute;
			if(minute < 10) {
				displayTime.setText(getDisplayTimeStringWithPostfix(hourOfDay + ":0" + minute));
			} else {
				displayTime.setText(getDisplayTimeStringWithPostfix(hourOfDay + ":" + minute));
			}
			displayTime.setTextSize(20);
		}
		
		String getDisplayTimeStringWithPostfix(String text) {
			return text + Const.SELECTED_TIME_POST_FIX_STRING;
		}
		
	};
	
	View.OnClickListener onCancelButtonClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			cancelCreateTask();
		}
	};
	void cancelCreateTask() {
		finish();
	}
	
	View.OnClickListener onSetImageButtonClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			
			setImageButtonAction();
		}
	};
	
	void showImagePickerDialog() {
		imagePickerDialogBuilder = new AlertDialog.Builder(CreateTaskActivity.this);
		imagePickerDialogBuilder.setCancelable(true);
		imagePickerDialogBuilder.setNegativeButton("Cancel", null);
		imagePickerDialogBuilder.setItems(Const.IMAGE_PICK_ITEMS, onImagePickerDialogItemClickListener);
		imagePickerDialog = imagePickerDialogBuilder.create();
		imagePickerDialog.show();
	}
	
	DialogInterface.OnClickListener onImagePickerDialogItemClickListener = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			imagePickerDialog.dismiss();
			
			if(which == 0) {
				callCamera();
			}else if(which == 1) {
				callImageGallary();
			}
		}
	};
	
	void callCamera() {
		mImageUri = getPhotoUri();
		Intent intent = new Intent();
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
		startActivityForResult(intent, REQUEST_CAMERA);
	}
	
	void setImageButtonAction() {
		 if(isSetImage) {
			image.setImageBitmap(null);
			isSetImage = false;
			imageUri = "";
			setImageButton.setText(R.string.add_image_button);
		 } else {
			showImagePickerDialog();
				
		}
	 }
	 
	 void callImageGallary() {
		 Intent intent = new Intent();
		 intent.setType("image/*");
		 intent.setAction(Intent.ACTION_GET_CONTENT);
		 startActivityForResult(intent, REQUEST_GALLERY);		 
	 }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(resultCode != RESULT_OK) {
			return;
		} else if(requestCode == REQUEST_GALLERY) {
			try {
				Uri uri = data.getData();
				showImage(uri);
				setImageButton.setText(R.string.delete_image_button);
			} catch (IOException e) {
				showFailedGetImageToast();
			}
		} else if(requestCode == REQUEST_CAMERA) {
			Uri uri = data.getData();
			if(uri != null) {
				try {
					showImage(uri);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				image.setImageURI(mImageUri);
			}
			setImageButton.setText(R.string.delete_image_button);
			
		}
	}
	
	private void showFailedGetImageToast() {
		Toast.makeText(CreateTaskActivity.this, Const.FAILED_GET_IMAGE_MESSAGE, Toast.LENGTH_SHORT).show(); 
	}

	private void showImage(Uri uri) throws IOException {

		imageUri = uri.toString();
		GetImageTask task = new GetImageTask(this, image);
		Uri uris[] = {uri};
		task.execute(uris);
		isSetImage = true;
	}
	
	private Uri getPhotoUri() {
        long currentTimeMillis = System.currentTimeMillis();
        Date today = new Date(currentTimeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String title = dateFormat.format(today);
        String dirPath = Utils.getDirPath(this);
        String fileName = "samplecameraintent_" + title + ".jpg";
        String path = dirPath + "/" + fileName;
        File file = new File(path);
        ContentValues values = new ContentValues();
        values.put(Images.Media.TITLE, title);
        values.put(Images.Media.DISPLAY_NAME, fileName);
        values.put(Images.Media.MIME_TYPE, "image/jpeg");
        values.put(Images.Media.DATA, path);
        values.put(Images.Media.DATE_TAKEN, currentTimeMillis);
        if (file.exists()) {
            values.put(Images.Media.SIZE, file.length());
        }
        Uri uri = getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values);
        return uri;
    }

}
