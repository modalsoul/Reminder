package jp.modal.soul.reminder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {  
	  
    @Override  
    public void onReceive(Context context, Intent intent) {  
        Toast.makeText(context, "Alarm Received!" + intent.getExtras().getSerializable("hoge"), Toast.LENGTH_SHORT).show();  
        Log.d("AlarmReceiver", "Alarm Received! : " + intent.getIntExtra(Intent.EXTRA_ALARM_COUNT, 0));  
    }  
} 