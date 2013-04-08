package jp.modal.soul.reminder.activity;

import java.util.ArrayList;
import java.util.List;

import jp.modal.soul.reminder.R;
import jp.modal.soul.reminder.model.TaskItem;
import jp.modal.soul.reminder.util.Const;
import jp.modal.soul.reminder.util.Utils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Filter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskListAdapter extends BaseAdapter  {
	/** ログ出力用 タグ */
    public final String TAG = this.getClass().getSimpleName();
    
    // リスト表示のステータス
    public static final int LIST_STATUS_ALL = 0;
    public static final int LIST_STATUS_TODO = 1;
    public static final int LIST_STATUS_DONE = 2;
    public static final int LIST_STATUS_SEARCH = 3;
    
    // リストの初期表示ステータス
    int statusFlag = LIST_STATUS_ALL;

    List<TaskItem> items;
    
	static class ViewHolder {
		TextView hiddenId;
		TextView messageView;
	    TextView timestampView;
	    ImageView statusView;
	}

	private LayoutInflater inflater;
    private int layoutId;
    public TaskListAdapter(Context context, int layoutId) {
    	this.inflater = (LayoutInflater) context
    		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	this.layoutId = layoutId;
    }
    
    void setItems(List<TaskItem> items) {
    	this.items = items;
    }
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	ViewHolder holder;
    	if (convertView == null) {
    		convertView = inflater.inflate(layoutId, parent, false);
    		holder = new ViewHolder();
    		getHolderView(convertView, holder);
    		convertView.setTag(holder);
    	} else {
    		holder = (ViewHolder) convertView.getTag();
    	}
    	TaskItem data = (TaskItem) getItem(position);
    	setHolderView(holder, data);
    	return convertView;
    }

	private void setHolderView(ViewHolder holder, TaskItem data) {
		holder.hiddenId.setText(Integer.toString(data.id));
    	holder.messageView.setText(data.message);
    	holder.timestampView.setText(Utils.getDateString(data.target_date) + Const.TASK_ALART_TIME_STRING);

    	if(data.status == TaskItem.STATUS_DONE) {
    		holder.statusView.setImageResource(R.drawable.tasks);
    	} else if(data.status == TaskItem.STATUS_TODO) {
    		holder.statusView.setImageResource(R.drawable.clock);
    	}
	}

	private void getHolderView(View convertView, ViewHolder holder) {
		holder.hiddenId = (TextView) convertView.findViewById(R.id.hidden_id);
		holder.messageView = (TextView) convertView.findViewById(R.id.message);
		holder.timestampView = (TextView) convertView.findViewById(R.id.timestamp);
		holder.statusView = (ImageView) convertView.findViewById(R.id.status_icon);
	}

	@Override
	public int getCount() {
		return items == null? 0:items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	
	private List<TaskItem> getStatusTaskItems(int status, List<TaskItem> items) {
		List<TaskItem> filteredItems = new ArrayList<TaskItem>();
		for(TaskItem item: items) {
			if(item.status == status) {
				filteredItems.add(item);
			}
		}
		return filteredItems;
	}


    void setTodoItems(List<TaskItem> items) {
    	this.statusFlag = LIST_STATUS_TODO;
    	setItems(getStatusTaskItems(TaskItem.STATUS_TODO, items));
    }

    void setDoneItems(List<TaskItem> items) {
    	this.statusFlag = LIST_STATUS_DONE;
    	setItems(getStatusTaskItems(TaskItem.STATUS_DONE, items));
    }
}

