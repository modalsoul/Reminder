package jp.modal.soul.reminder.configure;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {
	public static final String SOUND_SETTING_PREFERENCE_NAME = "SoundSetting";
	
	public static final String LIGHT_SETTING_PREFERENCE_NAME = "LightSetting";
	
	public static final String VIBRATION_SETTING_PREFERENCE_NAME = "VibrationSetting";

	public static final int SETTING_OFF = 0;
	public static final int SETTING_ON = 1;
	
	Context context;
	
	public Settings(Context context) {
		this.context = context;
	}
	
	public void initSettings() {
		setSharedPreferenceValue(SOUND_SETTING_PREFERENCE_NAME, SETTING_ON);
		setSharedPreferenceValue(LIGHT_SETTING_PREFERENCE_NAME, SETTING_ON);
		setSharedPreferenceValue(VIBRATION_SETTING_PREFERENCE_NAME, SETTING_ON);	
	}
	
	public SettingItem getAllSetting() {
		SettingItem item = new SettingItem();
		item.soundSetting = getSharedPreferenceValue(SOUND_SETTING_PREFERENCE_NAME);
		item.lightSetting = getSharedPreferenceValue(LIGHT_SETTING_PREFERENCE_NAME);
		item.vibrationSetting = getSharedPreferenceValue(VIBRATION_SETTING_PREFERENCE_NAME);
		return item;
	}
	
	public void setSoundOn() {
		setSharedPreferenceValue(SOUND_SETTING_PREFERENCE_NAME, SETTING_ON);
	}
	public void setSoundOff() {
		setSharedPreferenceValue(SOUND_SETTING_PREFERENCE_NAME, SETTING_OFF);
	}
	public void setLightOn() {
		setSharedPreferenceValue(LIGHT_SETTING_PREFERENCE_NAME, SETTING_ON);
	}
	public void setLightOff() {
		setSharedPreferenceValue(LIGHT_SETTING_PREFERENCE_NAME, SETTING_OFF);
	}
	public void setVibrationOn() {
		setSharedPreferenceValue(VIBRATION_SETTING_PREFERENCE_NAME, SETTING_ON);
	}
	public void setVibrationOff() {
		setSharedPreferenceValue(VIBRATION_SETTING_PREFERENCE_NAME, SETTING_OFF);
	}
	
	void setSharedPreferenceValue(String target, int value) {
		SharedPreferences sp = getSharedPreference(target);
		sp.edit().putInt(target, value);
	}
	int getSharedPreferenceValue(String target) {
		SharedPreferences sp = getSharedPreference(target);
		return sp.getInt(target, SETTING_OFF);
	}
	SharedPreferences getSharedPreference(String target) {
		SharedPreferences sp = context.getSharedPreferences(target, Context.MODE_PRIVATE);
		return sp;
	}
	
}
