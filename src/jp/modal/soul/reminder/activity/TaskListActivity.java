package jp.modal.soul.reminder.activity;

import java.util.ArrayList;

import jp.modal.soul.reminder.R;
import jp.modal.soul.reminder.model.TaskDao;
import jp.modal.soul.reminder.model.TaskItem;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TaskListActivity extends Activity {
	/** ログ出力用 タグ */
    public final String TAG = this.getClass().getSimpleName();
    
	int taskId;
	Intent intent = null;
	
	TaskDao dao;
	
	ArrayList<TaskItem> items;
	
	TaskListAdapter adapter;
	ListView listView;

	ImageView listAll;
	ImageView listStatus;
	ImageView search;
	ImageView add;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	    setContentView(R.layout.activity_task_list);

	    verifyNotification();
	    
	    setupDao();
	    
	    setupView();
	    
	    setupFooter();
	}
	void verifyNotification() {
		Bundle extra = getIntent().getExtras();
		if(extra != null) {
			taskId = extra.getInt(TaskDetailActivity.EXTRA_KEY_TASK_ID);
			moveToDetail();
		}
	}

	void setupDao() {
		dao = new TaskDao(this);		
	}

	void setupView() {
		getListView();
		setListView();
		setListEventHandling();
		
	}
	private void setListEventHandling() {
		listView.setOnItemClickListener(listItemOnClickListener);
	}
	private void setListView() {
		items = dao.queryAllTask();
		adapter = new TaskListAdapter(this, R.layout.list_item);
		adapter.setItems(items);
		adapter.notifyDataSetChanged();
		listView.setAdapter(adapter);
	}
	private void getListView() {
		listView = (ListView)findViewById(R.id.task_list);
	}
	
	
	AdapterView.OnItemClickListener listItemOnClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			TextView hiddenId = (TextView)parent.getChildAt(position).findViewById(R.id.hidden_id);
			taskId = Integer.valueOf(hiddenId.getText().toString());
			moveToDetail();
		}

		
	};
	void moveToDetail() {
		Intent intent = new Intent(getApplicationContext(), TaskDetailActivity.class);
		intent.putExtra(TaskDetailActivity.EXTRA_KEY_TASK_ID, taskId);
		startActivity(intent);
	}

	void setupFooter() {
		getFooterView();
		
		
	}
	private void getFooterView() {
		listAll = (ImageView)findViewById(R.id.list_footer_all);
		listStatus = (ImageView)findViewById(R.id.list_footer_status);
		search = (ImageView)findViewById(R.id.list_footer_search);
		add = (ImageView)findViewById(R.id.list_footer_add);
	}
	
	View.OnClickListener listAllImageClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			
		}
	};
	View.OnClickListener listStatusImageClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			
		}
	};
	View.OnClickListener listSearchImageClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			
		}
	};
	View.OnClickListener listAddImageClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getApplicationContext(), CreateTaskActivity.class);
			startActivity(intent);
			
		}
	};
	
}