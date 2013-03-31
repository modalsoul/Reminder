package jp.modal.soul.reminder.activity;

import java.io.IOException;

import jp.modal.soul.reminder.R;
import jp.modal.soul.reminder.model.DatabaseHelper;
import jp.modal.soul.reminder.util.InitState;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;


public class MainActivity extends Activity {
	/** ログ出力用 タグ */
    public final String TAG = this.getClass().getSimpleName();
    
	View task;
	View list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);

		setupInit();
		
		setupView();

	}
	


	void setupView() {
		task = findViewById(R.id.item1);
		task.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getApplicationContext(), CreateTaskActivity.class);
				startActivity(intent);

			}
		});
	}
	
	/**
	 * アプリ初回起動時の初期化処理
	 */
	public void setupInit() {
		InitState initState = new InitState(this);
		// 初回起動の判定
		if(initState.getStatus() == InitState.PREFERENCE_INIT) {
			// 初回起動の場合、初期データをセット
			DatabaseHelper dbHelper = new DatabaseHelper(this);
			try {
				Log.e(TAG, "DO INIT");
				dbHelper.createEmptyDataBase();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			}
			dbHelper.close();
			// 起動状態を変更
			initState.setStatus(InitState.PREFERENCE_BOOTED);
		}
	}

}