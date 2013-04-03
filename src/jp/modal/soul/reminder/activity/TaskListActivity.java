package jp.modal.soul.reminder.activity;

import java.util.ArrayList;

import jp.modal.soul.reminder.R;
import jp.modal.soul.reminder.model.TaskDao;
import jp.modal.soul.reminder.model.TaskItem;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

public class TaskListActivity extends Activity {
	/** ログ出力用 タグ */
    public final String TAG = this.getClass().getSimpleName();
    
	Integer taskId;
	
	TaskDao dao;
	
	ArrayList<TaskItem> items;
	
	TaskListAdapter adapter;
	
	ListView listView;
	
	Intent intent = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	    setContentView(R.layout.activity_task_list);

	    setupDao();
	    
	    setupView();
	    
	}

	void setupDao() {
		dao = new TaskDao(this);		
	}

	void setupView() {
		listView = (ListView)findViewById(R.id.task_list);
		
		items = dao.queryAllTask();
		
		adapter = new TaskListAdapter(this, R.layout.list_item, items);

		listView.setAdapter(adapter);
		
//		listView.setOnItemClickListener(listItemOnClickListener);
		
	}
	
	AdapterView.OnItemClickListener listItemOnClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
//			Log.e(TAG, parent.getChildAt(position));
			
		}
		
	};

}