package jp.modal.soul.reminder.activity;

import jp.modal.soul.reminder.R;
import jp.modal.soul.reminder.model.TaskDao;
import jp.modal.soul.reminder.model.TaskItem;
import jp.modal.soul.reminder.task.GetImageTask;
import jp.modal.soul.reminder.util.Const;
import jp.modal.soul.reminder.util.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TaskDetailActivity extends Activity {
	/** ログ出力用 タグ */
    public final String TAG = this.getClass().getSimpleName();
	
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
	ImageView imageView;
	
	AlertDialog.Builder deleteConfirmDialogBuilder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_task_detail);
		
		Intent intent = getIntent();
		id =  intent.getExtras().getInt(EXTRA_KEY_TASK_ID);
		
		setupDao();
		
		setupView();
		
		setupEventHandling();
	}
	
	void setupDao() {
		taskDao = new TaskDao(this);
	}
	
	void setupView() {
		
		getView();

		setView();
		
		setupButton();
	}

	private void setupButton() {
		deleteTaskButton = (Button)findViewById(R.id.delete_task);
		backButton = (Button)findViewById(R.id.back_to_list);
	}

	private void setView() {
		taskItem = taskDao.queryTaskByTaskId(id);
		if(taskItem == null) {
			finish();
		}
		registeredTime.setText(Utils.getDateString(taskItem.start_date));		
		alartTime.setText(Utils.getDateString(taskItem.target_date));
		message.setText(taskItem.message);

		if(taskItem.image_url != "") {
			setImage();
		}

	}

	private void setImage() {
		Uri uri = Uri.parse(taskItem.image_url);
		GetImageTask task = new GetImageTask(this, imageView);
		Uri[] uris = {uri};
		task.execute(uris);

	}

	private void getView() {
		registeredTime = (TextView)findViewById(R.id.registered_time);
		alartTime = (TextView)findViewById(R.id.alart_time);
		message = (TextView)findViewById(R.id.message_string);
		imageView = (ImageView)findViewById(R.id.image_detail);
	}
	
	void setupEventHandling() {
		deleteTaskButton.setOnClickListener(OnDeleteTaskButtonClickListener);
		backButton.setOnClickListener(OnBackToListButtonClickListener);
		
	}
	
	
	View.OnClickListener OnDeleteTaskButtonClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			showDeleteTaskDialog();
		}
	};
	
	void showDeleteTaskDialog() {
		deleteConfirmDialogBuilder = new AlertDialog.Builder(this);
		deleteConfirmDialogBuilder.setTitle(Const.TASK_DELETE_DIALOG_TITLE);
		deleteConfirmDialogBuilder.setMessage(Const.TASK_DELETE_DIALOG_MESSAGE);
		deleteConfirmDialogBuilder.setPositiveButton(R.string.delete_confirm_dialog_ok, onDeleteOkButtonClickListener);
		deleteConfirmDialogBuilder.setNegativeButton(R.string.delete_confirm_dialog_cancel, null);
		deleteConfirmDialogBuilder.show();
	}
	DialogInterface.OnClickListener onDeleteOkButtonClickListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			deleteTask();
		}
	};
	void deleteTask() {
		int result = taskDao.delete(id);
		if(result != 1) {
			
		} else {
			Toast.makeText(TaskDetailActivity.this, Const.TASK_DELETE_SUCCESS_MESSAGE, Toast.LENGTH_SHORT).show();
			backToList();
		}
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
