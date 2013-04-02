package jp.modal.soul.reminder.activity;

import java.util.ArrayList;
import java.util.List;

import jp.modal.soul.reminder.R;
import jp.modal.soul.reminder.model.TaskDao;
import jp.modal.soul.reminder.model.TaskItem;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskListActivity extends ListActivity {
	/** ログ出力用 タグ */
    public final String TAG = this.getClass().getSimpleName();
    
	int taskId;
	
	TaskDao dao;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//	    requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	    setContentView(R.layout.activity_task_list);

//	    Intent intent = getIntent();
//	    taskId = intent.getExtras().getInt(TaskItem.TASK_ID);
	    Log.e(TAG, taskId + ":hoge");
	    
	    dao = new TaskDao(this);
	    
	    ArrayList<TaskItem> items = dao.queryAllTask();
	    

	    TaskListAdapter adapter = new TaskListAdapter(this, R.layout.list_item, items);
	    setListAdapter(adapter);
	}



}