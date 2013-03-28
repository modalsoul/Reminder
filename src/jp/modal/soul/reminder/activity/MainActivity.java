package jp.modal.soul.reminder.activity;

import java.util.Calendar;

import jp.modal.soul.reminder.R;
import jp.modal.soul.reminder.receiver.AlarmReceiver;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;


public class MainActivity extends Activity {

	View task;
	View list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);

		setupView();

	}
	


	void setupView() {
		task = findViewById(R.id.item1);
		task.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getApplicationContext(), CreateTaskActivity.class);
				startActivity(intent);

			}
		});
	}
	
}