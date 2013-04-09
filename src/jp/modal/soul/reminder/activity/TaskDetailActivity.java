package jp.modal.soul.reminder.activity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import jp.modal.soul.reminder.R;
import jp.modal.soul.reminder.model.TaskDao;
import jp.modal.soul.reminder.model.TaskItem;
import jp.modal.soul.reminder.util.Const;
import jp.modal.soul.reminder.util.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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
		
		InputStream in;
		try {
			in = getContentResolver().openInputStream(uri);
			Bitmap img = BitmapFactory.decodeStream(in);
			in.close();
			imageView.setImageBitmap(img); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
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
		deleteConfirmDialogBuilder.setTitle("メモの削除");
		deleteConfirmDialogBuilder.setMessage("削除しますか？");
		deleteConfirmDialogBuilder.setPositiveButton(R.string.delete_confirm_dialog_ok, OnDeleteOkButtonClickListener);
		deleteConfirmDialogBuilder.setNegativeButton(R.string.delete_confirm_dialog_cancel, null);
		deleteConfirmDialogBuilder.show();
	}
	DialogInterface.OnClickListener OnDeleteOkButtonClickListener = new DialogInterface.OnClickListener() {

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
