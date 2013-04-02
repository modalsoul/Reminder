package jp.modal.soul.reminder.activity;

import java.util.List;

import jp.modal.soul.reminder.R;
import jp.modal.soul.reminder.model.TaskItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskListAdapter extends ArrayAdapter<TaskItem>  {

	static class ViewHolder {
		TextView messageView;
	    TextView timestampView;
	    ImageView statusView;
	}

	private LayoutInflater inflater;
    private int layoutId;
    public TaskListAdapter(Context context, int layoutId, List<TaskItem>objects) {
    	super(context, 0, objects);
    	this.inflater = (LayoutInflater) context
    		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	this.layoutId = layoutId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	ViewHolder holder;
    	if (convertView == null) {
    		convertView = inflater.inflate(layoutId, parent, false);
    		holder = new ViewHolder();
    		holder.messageView = (TextView) convertView.findViewById(R.id.message);
    		holder.timestampView = (TextView) convertView.findViewById(R.id.timestamp);
    		holder.statusView = (ImageView) convertView.findViewById(R.id.status_icon);
    		convertView.setTag(holder);
    	} else {
    		holder = (ViewHolder) convertView.getTag();
    	}
    	TaskItem data = getItem(position);
    	holder.messageView.setText(data.message);
    	holder.timestampView.setText(data.target_date);
    	if(data.status == TaskItem.STATUS_DONE) {
    		holder.statusView.setImageResource(R.drawable.tasks);
    	} else if(data.status == TaskItem.STATUS_TODO) {
    		holder.statusView.setImageResource(R.drawable.clock);
    	}
    	return convertView;
    }
}

