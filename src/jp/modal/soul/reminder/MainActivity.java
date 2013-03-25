package jp.modal.soul.reminder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.dashboard);

		setTitle();
	}

	private void setTitle() {
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/JohnHancockCP.otf");

		// Set up the custom title
		TextView mTitle;
		mTitle = (TextView) findViewById(R.id.title_text);
	    mTitle.setText(R.string.app_name);
	    mTitle.setTypeface(tf);
	    ImageView mImage;
	    mImage = (ImageView) findViewById(R.id.title_icon);
	    mImage.setImageResource(R.drawable.sym_action_chat);
	}

	public void moveFeature1(View v) {
		Intent intent = new Intent(this, Feature1Activity.class);
	    startActivity(intent);
	}

	public void moveFeature2(View v) {
	}
}