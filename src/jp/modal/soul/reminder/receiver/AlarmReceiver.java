package jp.modal.soul.reminder.receiver;


import jp.modal.soul.reminder.R;
import jp.modal.soul.reminder.activity.TaskDetailActivity;
import jp.modal.soul.reminder.activity.TaskListActivity;
import jp.modal.soul.reminder.configure.SettingItem;
import jp.modal.soul.reminder.configure.Settings;
import jp.modal.soul.reminder.model.TaskDao;
import jp.modal.soul.reminder.model.TaskItem;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {  
	/** ログ出力用 タグ */
    public final String TAG = this.getClass().getSimpleName();
	  
	Context context;
	TaskDao dao;
	TaskItem item;
	int taskId;
	
	Intent notifyIntent;
	PendingIntent contentIntent;
	Notification notification;
	NotificationManager notificationManager;
	
	
    @Override  
    public void onReceive(Context context, Intent intent) {  
    	this.context = context;
    	
    	getTaskId(intent);
    	
    	setupDao(context);
    	
    	getTaskItem();
    	    	
    	if(item != null) {
    		updateTaskStatus();
    		executeNotification();
    	}
    	
    }

	private void getTaskItem() {
		item = dao.queryTaskByTaskId(taskId);
		Log.e(TAG, "receive:"+ taskId);
	}

	private void setupDao(Context context) {
		dao = new TaskDao(context);
	}

	private void getTaskId(Intent intent) {
//		taskId = Integer.valueOf(intent.getExtras().getSerializable(TaskDetailActivity.EXTRA_KEY_TASK_ID).toString());
		taskId = intent.getExtras().getInt(TaskDetailActivity.EXTRA_KEY_TASK_ID);
	}

	private void executeNotification() {
		setupNotification();
		notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
    	notificationManager.notify(taskId, notification);
	}  
    
    void setupNotification() {
    	setupPendingIntent();
    	    	
    	setupNotificationContent();
    	
    	setupNotificationAction();
    }

	private void setupNotificationContent() {
		Resources res = context.getResources();
		String tickerString = res.getString(R.string.app_name);
		notification = new Notification(R.drawable.tasks, tickerString, System.currentTimeMillis());
		String contentTitle = res.getString(R.string.notification_title);
    	notification.setLatestEventInfo(context, contentTitle, item.message, contentIntent);
	}

	private void setupNotificationAction() {
		
		Settings settings = new Settings(context);
		SettingItem settingItem = settings.getAllSetting();
		
		if(settingItem.soundSetting == Settings.SETTING_ON) notification.defaults |= Notification.DEFAULT_SOUND;
    	if(settingItem.lightSetting == Settings.SETTING_ON) notification.defaults |= Notification.DEFAULT_LIGHTS;
    	if(settingItem.vibrationSetting == Settings.SETTING_ON) notification.defaults |= Notification.DEFAULT_VIBRATE;

    	notification.flags = Notification.FLAG_AUTO_CANCEL;
	}

	private void setupPendingIntent() {
		setupIntent();
    	contentIntent = PendingIntent.getActivity(context, 0, notifyIntent, 0);
	}

	private void setupIntent() {
		notifyIntent = new Intent(context, TaskListActivity.class);
		notifyIntent.putExtra(TaskDetailActivity.EXTRA_KEY_TASK_ID, taskId);
		Uri uri = Uri.parse("SCHEME://HOSTNAME/" + taskId);
		notifyIntent.setData(uri);
	}
	
	// TODO 更新失敗時にどうするか？
	void updateTaskStatus() {
		dao.updateStatusDone(item);
	}
} 