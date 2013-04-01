package jp.modal.soul.reminder.receiver;

import jp.modal.soul.reminder.R;
import jp.modal.soul.reminder.activity.MainActivity;
import jp.modal.soul.reminder.model.TaskDao;
import jp.modal.soul.reminder.model.TaskItem;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {  
	  
	Context context;
	TaskDao dao;
	TaskItem item;
	int taskId;
    @Override  
    public void onReceive(Context context, Intent intent) {  
    	this.context = context;
    	taskId = Integer.valueOf(intent.getExtras().getSerializable(TaskItem.TASK_ID).toString());
    	
    	dao = new TaskDao(context);
    	item = dao.queryTaskByTaskId(taskId);
    	
    	if(item != null) {
    		setupNotification();
    	}
    	
    }  
    
    void setupNotification() {
    	Intent intent = new Intent(context, MainActivity.class);
    	intent.putExtra(TaskItem.TASK_ID, taskId);
    	PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);
    	
    	Notification notification;
//    	try {
//    		Class.forName("android.app.Notification$Builder");
//    		notification = new Notification.Builder(context);
//    	} catch (ClassNotFoundException e) {
    		notification = new Notification(R.drawable.tasks, "Hoge", System.currentTimeMillis());
    		notification.setLatestEventInfo(context, "Title", item.message, contentIntent);
//    	}
    	
    	NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    	notificationManager.cancelAll();
    	notificationManager.notify(taskId, notification);
    	
    	
    }
} 