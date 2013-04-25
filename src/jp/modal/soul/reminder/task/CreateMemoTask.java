package jp.modal.soul.reminder.task;

import java.util.Calendar;

import jp.modal.soul.reminder.activity.TaskDetailActivity;
import jp.modal.soul.reminder.model.TaskDao;
import jp.modal.soul.reminder.model.TaskItem;
import jp.modal.soul.reminder.receiver.AlarmReceiver;
import jp.modal.soul.reminder.util.Const;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

public class CreateMemoTask extends AsyncTask<Void, Void, Boolean>{

	// TODO move to correct package
	static final Boolean DB_INSERT_FAILED = false;
	static final Boolean DB_INSERT_SUCCESS = true;
	
	Context context;
	long taskId;
	TaskItem item;
	
	public CreateMemoTask(Context context, TaskItem item) {
		this.context = context;
		this.item = item;
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		
		Boolean result = DB_INSERT_FAILED;
		result = registerTaskItem(item);
		
		return result;
	}

	private Boolean registerTaskItem(TaskItem... params) {
		TaskDao dao = new TaskDao(context);
		SQLiteDatabase db = dao.getWritableDatabase();
		boolean result = DB_INSERT_FAILED;
		try {
			taskId = dao.insertWithoutOpenDb(db, params[0]);
			result = DB_INSERT_SUCCESS;
		} catch (Exception e) {
			// TODO implement exception process
			e.printStackTrace();
		} finally {
			db.close();
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(Boolean insertResult) {
		if(insertResult == DB_INSERT_FAILED) {
		
			
		} else if(insertResult == DB_INSERT_SUCCESS) {
			setupAlarm();
		}
	}

	void setupAlarm() {
		Intent intent = new Intent(context, AlarmReceiver.class);  
		intent.putExtra(TaskDetailActivity.EXTRA_KEY_TASK_ID, taskId);
		Uri uri = Uri.parse("SCHEME://HOSTNAME/" + taskId);
		intent.setData(uri);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);  
  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTimeInMillis(System.currentTimeMillis());  
        calendar.add(Calendar.SECOND, item.delay);  
          
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);  
        
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);  
                  
        Toast.makeText(context, Const.CREATE_TASK_SUCCESS_MESSAGE, Toast.LENGTH_SHORT).show();  
    
	}
	
	

}
