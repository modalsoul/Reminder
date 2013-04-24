package jp.modal.soul.reminder.configure;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {
	public static final String SETTING_PREFERENCE_NAME = "SettingPreference";
	
	public static final String SOUND_SETTING_KEY = "SoundSetting";
	
	public static final String LIGHT_SETTING_KEY = "LightSetting";
	
	public static final String VIBRATION_SETTING_KEY = "VibrationSetting";

	public static final int SETTING_OFF = 0;
	public static final int SETTING_ON = 1;
	
	Context context;
	
	public Settings(Context context) {
		this.context = context;
	}
	
	public void initSettings() {
		setSharedPreferenceValue(SOUND_SETTING_KEY, SETTING_ON);
		setSharedPreferenceValue(LIGHT_SETTING_KEY, SETTING_ON);
		setSharedPreferenceValue(VIBRATION_SETTING_KEY, SETTING_ON);	
	}
	
	public SettingItem getAllSetting() {
		SettingItem item = new SettingItem();
		item.soundSetting = getSharedPreferenceValue(SOUND_SETTING_KEY);
		item.lightSetting = getSharedPreferenceValue(LIGHT_SETTING_KEY);
		item.vibrationSetting = getSharedPreferenceValue(VIBRATION_SETTING_KEY);
		return item;
	}
	
	public void setSoundOn() {
		setSharedPreferenceValue(SOUND_SETTING_KEY, SETTING_ON);
	}
	public void setSoundOff() {
		setSharedPreferenceValue(SOUND_SETTING_KEY, SETTING_OFF);
	}
	public void setLightOn() {
		setSharedPreferenceValue(LIGHT_SETTING_KEY, SETTING_ON);
	}
	public void setLightOff() {
		setSharedPreferenceValue(LIGHT_SETTING_KEY, SETTING_OFF);
	}
	public void setVibrationOn() {
		setSharedPreferenceValue(VIBRATION_SETTING_KEY, SETTING_ON);
	}
	public void setVibrationOff() {
		setSharedPreferenceValue(VIBRATION_SETTING_KEY, SETTING_OFF);
	}
	
	void setSharedPreferenceValue(String target, int value) {
		SharedPreferences sp = getSharedPreference();
		sp.edit().putInt(target, value).commit();
	}
	int getSharedPreferenceValue(String target) {
		SharedPreferences sp = getSharedPreference();
		return sp.getInt(target, SETTING_OFF);
	}
	SharedPreferences getSharedPreference() {
		SharedPreferences sp = context.getSharedPreferences(SETTING_PREFERENCE_NAME, Context.MODE_PRIVATE);
		return sp;
	}
	
}
