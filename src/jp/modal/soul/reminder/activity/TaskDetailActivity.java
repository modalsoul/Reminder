package jp.modal.soul.reminder.activity;

import jp.modal.soul.reminder.R;
import jp.modal.soul.reminder.model.TaskDao;
import jp.modal.soul.reminder.model.TaskItem;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TaskDetailActivity extends Activity {

	TaskDao taskDao;
	TaskItem taskItem;
	
	int id;
	
	TextView alartTime;
	TextView message;
	Button deleteTaskButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_detail);
		
		Intent intent = getIntent();
		id =  Integer.valueOf(intent.getExtras().getString(TaskItem.TASK_ID));
		
		setupDao();
		
		setupView();
		
	}
	
	void setupDao() {
		taskDao = new TaskDao(this);
	}
	
	void setupView() {
		taskItem = taskDao.queryTaskByTaskId(id);
		
		alartTime = (TextView)findViewById(R.id.alart_time);
		alartTime.setText(taskItem.target_date);
		
		message = (TextView)findViewById(R.id.message_string);
		message.setText(taskItem.message);
		
		deleteTaskButton = (Button)findViewById(R.id.delete_task);
		deleteTaskButton.setOnClickListener(OnDeleteTaskButtonClickListener);
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
}
