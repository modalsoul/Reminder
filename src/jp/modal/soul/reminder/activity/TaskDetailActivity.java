package jp.modal.soul.reminder.activity;

import java.util.Date;

import jp.modal.soul.reminder.R;
import jp.modal.soul.reminder.model.TaskDao;
import jp.modal.soul.reminder.model.TaskItem;
import jp.modal.soul.reminder.util.Utils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TaskDetailActivity extends Activity {

	
    /** intent key string */
    public static final String EXTRA_KEY_TASK_ID = "task_id";
    
	TaskDao taskDao;
	TaskItem taskItem;
	
	int id;
	
	TextView registeredTime;
	TextView alartTime;
	TextView message;
	Button deleteTaskButton;
	Button backButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_detail);
		
		Intent intent = getIntent();
		id =  intent.getExtras().getInt(EXTRA_KEY_TASK_ID);
		
		setupDao();
		
		setupView();
		
	}
	
	void setupDao() {
		taskDao = new TaskDao(this);
	}
	
	void setupView() {
		taskItem = taskDao.queryTaskByTaskId(id);
		
		registeredTime = (TextView)findViewById(R.id.registered_time);
		registeredTime.setText(Utils.getDateString(taskItem.start_date));
		
		alartTime = (TextView)findViewById(R.id.alart_time);
		alartTime.setText(Utils.getDateString(taskItem.target_date));
		
		message = (TextView)findViewById(R.id.message_string);
		message.setText(taskItem.message);
		
		deleteTaskButton = (Button)findViewById(R.id.delete_task);
		deleteTaskButton.setOnClickListener(OnDeleteTaskButtonClickListener);
		
		backButton = (Button)findViewById(R.id.back_to_list);
		backButton.setOnClickListener(OnBackToListButtonClickListener);
	}
	
	
	View.OnClickListener OnDeleteTaskButtonClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			deleteTask();
		}
	};
	
	void deleteTask() {
		finish();
	}
	
	View.OnClickListener OnBackToListButtonClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			backToList();
		}
	};
	void backToList() {
		finish();
	}
}
