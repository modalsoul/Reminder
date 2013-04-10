package jp.modal.soul.reminder.activity;

import java.io.InputStream;
import java.util.Calendar;

import jp.modal.soul.reminder.R;
import jp.modal.soul.reminder.model.TaskDao;
import jp.modal.soul.reminder.model.TaskItem;
import jp.modal.soul.reminder.receiver.AlarmReceiver;
import jp.modal.soul.reminder.util.Const;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
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
			item = new TaskItem();
			item.message = messageView.getText().toString();
			item.delay = (hour * ONE_HOUR_MINUTES + minutes) * ONE_MINUTE_SECONDS;
			item.status = TaskItem.STATUS_TODO;
			item.image_url = imageUri;
			
			SQLiteDatabase db = taskDao.getWritableDatabase();
			try {
				taskId = (int)taskDao.insertWithoutOpenDb(db, item);
				if(taskId != -1) {
					
					Log.e(TAG,"insert:" + taskId);
					setupAlarm();
				} else {
					throw(new Exception());
				}
			} catch (Exception e) {
				Log.e(TAG, "INSERT FAILED:" + e.getMessage());
			} finally {
				db.close();
			}
			finish();
		}
	};
	
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

	// TODO 外出し
	void setupAlarm() {
		Intent intent = new Intent(CreateTaskActivity.this, AlarmReceiver.class);  
		intent.putExtra(TaskDetailActivity.EXTRA_KEY_TASK_ID, taskId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(CreateTaskActivity.this, 0, intent, 0);  
  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTimeInMillis(System.currentTimeMillis());  
        calendar.add(Calendar.SECOND, item.delay);  
          
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);  
        // one shot  
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);  
                  
        Toast.makeText(CreateTaskActivity.this, Const.CREATE_TASK_SUCCESS_MESSAGE, Toast.LENGTH_SHORT).show();  
    
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
			try {
				Uri uri = data.getData();
				
				InputStream in = getContentResolver().openInputStream(uri);
				Bitmap img = BitmapFactory.decodeStream(in);
				in.close();
				image.setImageBitmap(img); 
				isSetImage = true;
				imageUri = uri.toString();
			} catch (Exception e) {
				
			}
		}
	}

}
