package jp.modal.soul.reminder.activity;

import java.io.IOException;

import jp.modal.soul.reminder.R;
import jp.modal.soul.reminder.model.TaskDao;
import jp.modal.soul.reminder.model.TaskItem;
import jp.modal.soul.reminder.task.CreateMemoTask;
import jp.modal.soul.reminder.task.GetImageTask;
import jp.modal.soul.reminder.util.Const;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
	
	int hour = 0;
	int minutes = 0;
	int taskId;
	
	private boolean isSetImage = false;
	
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
			setupTaskItem();
			
			registerMemo(item);
			
			finish();
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
				displayTime.setText(hourOfDay + ":0" + minute);
			} else {
				displayTime.setText(hourOfDay + ":" + minute);
			}
			displayTime.setTextSize(20);
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
	 void setImageButtonAction() {
		 if(isSetImage) {
			image.setImageBitmap(null);
			isSetImage = false;
			imageUri = "";
		 } else {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent, REQUEST_GALLERY);
		}
	 }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(resultCode != RESULT_OK) {
			return;
		} else if(requestCode == REQUEST_GALLERY) {
			try {
				Uri uri = data.getData();
				showImage(uri);
			} catch (IOException e) {
				showFailedGetImageToast();
			}
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
	

}
