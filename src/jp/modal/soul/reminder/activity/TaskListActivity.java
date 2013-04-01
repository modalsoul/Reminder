package jp.modal.soul.reminder.activity;

import java.util.ArrayList;
import java.util.List;

import jp.modal.soul.reminder.R;
import jp.modal.soul.reminder.R.drawable;
import jp.modal.soul.reminder.R.id;
import jp.modal.soul.reminder.R.layout;
import jp.modal.soul.reminder.R.string;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskListActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	    setContentView(R.layout.activity_task_list);


	    List<bindData> data = new ArrayList<bindData>();
	    data.add(new bindData("A���C���R�[�g", "�ō� 4,980�~", R.drawable.coat_thumb));
	    data.add(new bindData("A���C���R�[�g", "�ō� 4,980�~", R.drawable.coat_thumb));
	    data.add(new bindData("A���C���R�[�g", "�ō� 4,980�~", R.drawable.coat_thumb));
	    data.add(new bindData("A���C���R�[�g", "�ō� 4,980�~", R.drawable.coat_thumb));
	    data.add(new bindData("A���C���R�[�g", "�ō� 4,980�~", R.drawable.coat_thumb));
	    data.add(new bindData("A���C���R�[�g", "�ō� 4,980�~", R.drawable.coat_thumb));

	    TaskListAdapter adapter = new TaskListAdapter(this, R.layout.list_item, data);
	    setListAdapter(adapter);
	}



	class bindData {
		String message;
	    String timestamp;
	    int imageResourceId;

	    public bindData(String text1, String text2, int imageResourceId)
	    {
	    	this.message = text1;
	    	this.timestamp = text2;
	    	this.imageResourceId = imageResourceId;
	    }
	}

	static class ViewHolder {
		TextView textView1;
	    TextView textView2;
	    ImageView statusView;
	}

	public class TaskListAdapter extends ArrayAdapter<bindData> {
		private LayoutInflater inflater;
	    private int layoutId;

	    public TaskListAdapter(Context context, int layoutId, List<bindData>objects) {
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
	    		holder.textView1 = (TextView) convertView.findViewById(R.id.message);
	    		holder.textView2 = (TextView) convertView.findViewById(R.id.timestamp);
	    		holder.statusView = (ImageView) convertView.findViewById(R.id.status_icon);
	    		convertView.setTag(holder);
	    	} else {
	    		holder = (ViewHolder) convertView.getTag();
	    	}
	    	bindData data = getItem(position);
	    	holder.textView1.setText(data.message);
	    	holder.textView2.setText(data.timestamp);
	    	holder.statusView.setImageResource(data.imageResourceId);
	    	return convertView;
	    }
	}
}