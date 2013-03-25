package jp.modal.soul.reminder;

import java.util.ArrayList;
import java.util.List;

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

public class Feature1Activity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	    setContentView(R.layout.feature1);

	    setTitle();

	    List<bindData> data = new ArrayList<bindData>();
	    data.add(new bindData("Aラインコート", "税込 4,980円", R.drawable.coat_thumb));
	    data.add(new bindData("Aラインコート", "税込 4,980円", R.drawable.coat_thumb));
	    data.add(new bindData("Aラインコート", "税込 4,980円", R.drawable.coat_thumb));
	    data.add(new bindData("Aラインコート", "税込 4,980円", R.drawable.coat_thumb));
	    data.add(new bindData("Aラインコート", "税込 4,980円", R.drawable.coat_thumb));
	    data.add(new bindData("Aラインコート", "税込 4,980円", R.drawable.coat_thumb));

	    MyAdapter adapter = new MyAdapter(this, R.layout.list_item, data);
	    setListAdapter(adapter);
	}

	private void setTitle() {
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
	    Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/JohnHancockCP.otf");

	    // カスタムタイトルのセットアップ
	    TextView mTitle;
	    mTitle = (TextView) findViewById(R.id.title_text);
	    mTitle.setText(R.string.app_name);
	    mTitle.setTypeface(tf);
	    ImageView mImage;
	    mImage = (ImageView) findViewById(R.id.title_icon);
	    mImage.setImageResource(R.drawable.sym_action_chat);
	}

	class bindData {
		String text1;
	    String text2;
	    int imageResourceId;

	    public bindData(String text1, String text2, int imageResourceId)
	    {
	    	this.text1 = text1;
	    	this.text2 = text2;
	    	this.imageResourceId = imageResourceId;
	    }
	}

	static class ViewHolder {
		TextView textView1;
	    TextView textView2;
	    ImageView imageView;
	}

	public class MyAdapter extends ArrayAdapter<bindData> {
		private LayoutInflater inflater;
	    private int layoutId;

	    public MyAdapter(Context context, int layoutId, List<bindData>objects) {
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
	    		holder.textView1 = (TextView) convertView.findViewById(R.id.textview1);
	    		holder.textView2 = (TextView) convertView.findViewById(R.id.textview2);
	    		holder.imageView = (ImageView) convertView.findViewById(R.id.imageview);
	    		convertView.setTag(holder);
	    	} else {
	    		holder = (ViewHolder) convertView.getTag();
	    	}
	    	bindData data = getItem(position);
	    	holder.textView1.setText(data.text1);
	    	holder.textView2.setText(data.text2);
	    	holder.imageView.setImageResource(data.imageResourceId);
	    	return convertView;
	    }
	}
}