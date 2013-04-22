package jp.modal.soul.reminder.activity;

import jp.modal.soul.reminder.R;
import jp.modal.soul.reminder.configure.SettingItem;
import jp.modal.soul.reminder.configure.Settings;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckedTextView;

public class SettingActivity extends Activity {
	/** ログ出力用 タグ */
    public final String TAG = this.getClass().getSimpleName();
    
    CheckedTextView soundCheckedTextView;
    CheckedTextView lightCheckedTextView;
    CheckedTextView vibrationCheckedTextView;
    Settings settings;
    final boolean OFF = false;
    final boolean ON = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	    setContentView(R.layout.activity_setting);
	    
	    setupView();
	    
	    setupCheckState();
	    
	    setupEventHandling();
	}
	
	void setupView() {
		soundCheckedTextView = (CheckedTextView)findViewById(R.id.notification_sound);
		lightCheckedTextView = (CheckedTextView)findViewById(R.id.notification_light);
		vibrationCheckedTextView = (CheckedTextView)findViewById(R.id.notification_vibration);
	}
	
	void setupCheckState() {
		settings = new Settings(this);
		SettingItem item = settings.getAllSetting();
		Log.e(TAG, "SOUND:" + item.soundSetting);
		Log.e(TAG, "LIGHT:" + item.lightSetting);
		Log.e(TAG, "VIB:" + item.vibrationSetting);
		
		if(item.soundSetting == Settings.SETTING_ON) {
			soundCheckedTextView.setChecked(ON);
		} else {
			soundCheckedTextView.setChecked(OFF);
		}
		if(item.lightSetting == Settings.SETTING_ON) {
			lightCheckedTextView.setChecked(ON);
		} else {
			lightCheckedTextView.setChecked(OFF);
		}
		if(item.vibrationSetting == Settings.SETTING_ON) {
			vibrationCheckedTextView.setChecked(ON);
		} else {
			vibrationCheckedTextView.setChecked(OFF);
		}
	}
	void setupEventHandling() {
		soundCheckedTextView.setOnClickListener(onSoundCheckedTextViewClickListener);
		lightCheckedTextView.setOnClickListener(onLightCheckedTextViewClickListener);
		vibrationCheckedTextView.setOnClickListener(onVibrationCheckedTextViewClickListener);
	}
	
	View.OnClickListener onSoundCheckedTextViewClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View checkedView) {
			if(((CheckedTextView)checkedView).isChecked()) {
				disableSound((CheckedTextView)checkedView);
			} else {
				enableSound((CheckedTextView)checkedView);
			}
		}
	};
	View.OnClickListener onLightCheckedTextViewClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View checkedView) {
			if(((CheckedTextView)checkedView).isChecked()) {
				disableLight((CheckedTextView)checkedView);
			} else {
				enableLight((CheckedTextView)checkedView);
			}
		}
	};
	View.OnClickListener onVibrationCheckedTextViewClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View checkedView) {
			if(((CheckedTextView)checkedView).isChecked()) {
				disableVibration((CheckedTextView)checkedView);
			} else {
				enableVibration((CheckedTextView)checkedView);
			}
		}
	};
	

	void disableSound(CheckedTextView view) {
		disableSetting(view);
		settings.setSoundOff();
	}
	void enableSound(CheckedTextView view) {
		enableSetting(view);
		settings.setSoundOn();
	}
	void disableLight(CheckedTextView view) {
		disableSetting(view);
		settings.setLightOff();
	}
	void enableLight(CheckedTextView view) {
		enableSetting(view);
		settings.setLightOn();
	}
	void disableVibration(CheckedTextView view) {
		disableSetting(view);
		settings.setVibrationOff();
	}
	void enableVibration(CheckedTextView view) {
		enableSetting(view);
		settings.setVibrationOn();
	}
	
	
	void disableSetting(CheckedTextView view) {
		setSettingStatus(view, OFF);
	}
	void enableSetting(CheckedTextView view) {
		setSettingStatus(view, ON);
	}
	void setSettingStatus(CheckedTextView view, boolean status) {
		view.setChecked(status);
	}
}

