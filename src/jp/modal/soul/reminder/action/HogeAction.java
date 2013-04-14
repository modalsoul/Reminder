package jp.modal.soul.reminder.action;

import jp.modal.soul.reminder.service.ActionService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class HogeAction extends BaseAction {

	public static final String CLASS_NAME = "LoadImageAction";
	
	public static final String EXTRA_KEY_CONTENT_URI = "";
	
	public static final String ACTION_NAME = PACKAGE_NAME + CLASS_NAME;
	
	
    public HogeAction(Context context, Bundle extra) {
		super(context, extra);
	}


	@Override
	public void run() {
		
		
	}
	
	public static void startAction(Context context, String uri) {
		Intent intent = createIntent(context, uri);
		context.startService(intent);
	}
	
	public static Intent createIntent(Context context, String uri) {
        Intent intent = new Intent(context.getApplicationContext(), ActionService.class);
        intent.setAction(HogeAction.ACTION_NAME);
        intent.putExtra(HogeAction.EXTRA_KEY_CONTENT_URI, uri);

        return intent;
    }
}
