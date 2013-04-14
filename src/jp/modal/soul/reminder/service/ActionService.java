package jp.modal.soul.reminder.service;

import jp.modal.soul.reminder.action.BaseAction;
import jp.modal.soul.reminder.action.HogeAction;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;

public class ActionService extends Service{

	Handler actionHandler;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		if(actionHandler == null) {
			HandlerThread ht = new HandlerThread(this.getClass().getSimpleName());
			ht.setPriority(Thread.MIN_PRIORITY);
			ht.start();
			actionHandler = new Handler(ht.getLooper());
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	void startThread(BaseAction action) {
		if(action != null) {
			
		}
	}
	
	void startAction(Intent intent) {
		try {
			BaseAction action = dispatchAction(intent);
			startThread(action);
		} catch (Exception e) {
			// TODO 
		}
		
	}
	
	BaseAction dispatchAction(Intent intent) throws Exception {
		String actionName = intent.getAction();
		Bundle extra = intent.getExtras();
		
		if(HogeAction.ACTION_NAME.equals(actionName)) {
			return new HogeAction(this, extra);
		} else {
			throw new Exception();
		}
	}
	

}
