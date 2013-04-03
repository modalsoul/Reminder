package jp.modal.soul.reminder.activity;

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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CreateTaskActivity extends Activity {
	/** ログ出力用 タグ */
    public final String TAG = this.getClass().getSimpleName();
    
    public static final int ONE_MINUTE_SECONDS = 60;
    public static final int ONE_HOUR_MINUTES = 60;
    
	EditText messageView;
	View setTimeButton;
	Button createTaskButton;
	TimePickerDialog dialog;
	TextView displayTime;
	
	TaskDao taskDao;
	TaskItem item;
	
	int hour = 0;
	int minutes = 0;
	int taskId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_task);
		
		setupView();
		
		setupDao();
		
	}
	
	void setupDao() {
		taskDao = new TaskDao(this);
	}
	
	private void setupView() {
		displayTime = (TextView)findViewById(R.id.display_time);
		
		messageView = (EditText)findViewById(R.id.message);
		
		setTimeButton = findViewById(R.id.select_time);
		
		setTimeButton.setOnClickListener(onSetTimeButtonClickListener);
		
		createTaskButton = (Button)findViewById(R.id.create_task);
		
		createTaskButton.setOnClickListener(onCreateTaskButtonClickListener);

		dialog = new TimePickerDialog(this, android.R.style.Theme_Black, onTimeSetListener, 0, 10, true);
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
			
			SQLiteDatabase db = taskDao.getWritableDatabase();
			try {
				taskId = (int)taskDao.insertWithoutOpenDb(db, item);
				if(taskId != -1) {
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
	

	void setupAlarm() {
		Intent intent = new Intent(CreateTaskActivity.this, AlarmReceiver.class);  
		intent.putExtra(TaskItem.TASK_ID, taskId);
        PendingIntent sender = PendingIntent.getBroadcast(CreateTaskActivity.this, 0, intent, 0);  
  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTimeInMillis(System.currentTimeMillis());  
        calendar.add(Calendar.SECOND, item.delay);  
          
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);  
        // one shot  
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);  
                  
        Toast.makeText(CreateTaskActivity.this, Const.CREATE_TASK_SUCCESS_MESSAGE, Toast.LENGTH_SHORT).show();  
    
	}

}
