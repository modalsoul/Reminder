package jp.modal.soul.reminder.activity;

import java.util.Calendar;

import jp.modal.soul.reminder.R;
import jp.modal.soul.reminder.receiver.AlarmReceiver;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class CreateTaskActivity extends Activity {
	
	EditText messageView;
	Button setTimeButton;
	Button createTaskButton;
	TimePickerDialog dialog;
	
	
	int hour = 0;
	int minutes = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_task);
		
		setupView();
		
	}
	
	private void setupView() {
		messageView = (EditText)findViewById(R.id.message);
		
		setTimeButton = (Button)findViewById(R.id.set_time);
		
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
			setupAlarm();
		}
	};
	
	TimePickerDialog.OnTimeSetListener onTimeSetListener = new OnTimeSetListener(){

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			hour = hourOfDay;
			minutes = minute;
		}
		
	};
	

	void setupAlarm() {
		Intent intent = new Intent(CreateTaskActivity.this, AlarmReceiver.class);  
		intent.putExtra("hoge", hour + ":" + minutes);
        PendingIntent sender = PendingIntent.getBroadcast(CreateTaskActivity.this, 0, intent, 0);  
  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTimeInMillis(System.currentTimeMillis());  
        calendar.add(Calendar.SECOND, 3);  
          
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);  
        // one shot  
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);  
                  
        Toast.makeText(CreateTaskActivity.this, "Start Alarm!", Toast.LENGTH_SHORT).show();  
    
	}

}
