package jp.modal.soul.reminder.action;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public abstract class BaseAction implements Runnable{
	public static final String PACKAGE_NAME = "jp.modal.soul.reminder.action.";

	
	Context context;
	Bundle extra;
	BroadcastSender broBroadcastSender;
	
	public BaseAction(Context context, Bundle extra) {
		this.context = context;
		this.extra = extra;
		this.broBroadcastSender = new BroadcastSender(context);
	}
	
	void sendBroadcast(Intent intent) {
		broBroadcastSender.send(intent);
	}
	
	Intent createBaseBroadcastIntent(String actionName) {
		Intent intent = new Intent();
		intent.setAction(actionName);
		return intent;
	}
	
	public static class BroadcastSender {
		private Context context;
		public BroadcastSender(Context context) {
			this.context = context;
		}
		public void send(Intent intent) {
			context.sendBroadcast(intent);
		}
	}
}
