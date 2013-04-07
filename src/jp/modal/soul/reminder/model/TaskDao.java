package jp.modal.soul.reminder.model;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TaskDao extends Dao{
	/** ログ出力用 タグ */
    public final String TAG = this.getClass().getSimpleName();
        
    public static final int MILLI = 1000;
	
	public static final String TABLE_NAME = "task";
	
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_MESSAGE = "message";
	public static final String COLUMN_DELAY = "delay";
	public static final String COLUMN_START_DATE = "start_date"; 
	public static final String COLUMN_TARGET_DATE = "target_date";
	public static final String COLUMN_STATUS = "status";

	public static final String[] COLUMNS = {
		COLUMN_ID,
		COLUMN_MESSAGE,
		COLUMN_DELAY,
		COLUMN_START_DATE,
		COLUMN_TARGET_DATE,
		COLUMN_STATUS
	};
	
	public static final String CREATE_TABLE;
	static {
		String columnDefine = COLUMN_ID + " integer primary key, "
							+ COLUMN_MESSAGE + " text not null, "
							+ COLUMN_START_DATE + " text not null, "
							+ COLUMN_TARGET_DATE + " text not null, "
							+ COLUMN_STATUS + " integer not null"
							;
		CREATE_TABLE = createTable(TABLE_NAME, columnDefine);
	}
	
	public TaskDao(Context context) {
		super(context);
	}
	
	public static TaskItem getTaskItem(Cursor cursor) {
		TaskItem taskItem = new TaskItem();
		taskItem.id = cursor.getInt(0);
		taskItem.message = cursor.getString(1);
		taskItem.delay = cursor.getInt(2);
		taskItem.start_date = cursor.getString(3);
		taskItem.target_date = cursor.getString(4);
		taskItem.status = cursor.getInt(5);
		return taskItem;
	}
	
	private ArrayList<TaskItem> queryList(String[] columns, String selection, String[] selectionArgs,  String groupBy, 
			String having, String orderBy, String limit) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = null;
		cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
		ArrayList<TaskItem> itemList = new ArrayList<TaskItem>();
		while (cursor.moveToNext()) {
			TaskItem item = getTaskItem(cursor);
			itemList.add(item);
		}
		cursor.close();
		db.close();
		
		return itemList;
	}
	
	public TaskItem queryTaskByTaskId(int id) {
		String selection = COLUMN_ID + " = ?";
		String[] selectionArgs = new String[1];
		selectionArgs[0] = String.valueOf(id);
		ArrayList<TaskItem> taskItems = queryList(COLUMNS, selection, selectionArgs, null, null, null, null);
		
		if(taskItems.size() != 1) {
			return null;
		}
		return taskItems.get(0);
	}
	
	public ArrayList<TaskItem> queryAllTask() {
		String orderBy = COLUMN_TARGET_DATE + " desc ";
		return queryList(COLUMNS, null, null, null, null, orderBy, null);
	}
	public ArrayList<TaskItem> queryTaskByStatus(int status) {
		String selection = COLUMN_STATUS + " = ?";
		String[] selectionArgs = new String[1];
		selectionArgs[0] = String.valueOf(status);
		Log.e(TAG, selectionArgs[0]);
		ArrayList<TaskItem> taskItems = queryList(COLUMNS, selection, selectionArgs, null, null, null, null);
		
		return taskItems;
	}
	public ArrayList<TaskItem> queryTodoTask() {
		return queryTaskByStatus(TaskItem.STATUS_TODO);
	}
	public ArrayList<TaskItem> queryDoneTask() {
		return queryTaskByStatus(TaskItem.STATUS_DONE);
	}

	public long insertWithoutOpenDb(SQLiteDatabase db, TaskItem item) throws Exception {
		ContentValues values = getContentValues(item);		
		
		long result = db.insert(TABLE_NAME, null, values);
		if(result == Dao.RETURN_CODE_INSERT_FAIL) {
			throw new Exception("insert exception");
		}
		return result;
	}
	
    public int update(int id, TaskItem item) {
        // WritableモードでDBオープン
        SQLiteDatabase db = getWritableDatabase();

        // 更新系処理の実行
        int result = updateWithoutOpenDb(db, id, item);

        // DBクローズ(必須)
        db.close();
        return result;
    }
    public int updateWithoutOpenDb(SQLiteDatabase db, int id, TaskItem item) {
        // 更新系処理の実行
        ContentValues values = getContentValues(item);

        // 更新条件の設定
        String whereClause = COLUMN_ID + "=?";
        String[] whereArgs = { String.valueOf(id) };

        // updateに成功するとupdateした更新件数
        int result = db.update(TABLE_NAME, values, whereClause, whereArgs);
        if (result <= Dao.RETURN_CODE_UPDATE_FAIL) {
            
        }
        return result;
    }
    
    public int updateStatusDone(TaskItem item) {
    	item.status = TaskItem.STATUS_DONE;
    	return update(item.id, item);
    }
	
	ContentValues getContentValues(TaskItem item) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_MESSAGE, item.message);
		values.put(COLUMN_DELAY, item.delay);
		
		if(item.start_date == null) {
			long startDate = new Date().getTime();
			item.start_date = String.valueOf(startDate);
		}
		values.put(COLUMN_START_DATE, item.start_date);
		
		if(item.target_date == null) {
			long targetDate = Long.valueOf(item.start_date) + item.delay*MILLI;
			item.target_date = String.valueOf(targetDate);
		}
		values.put(COLUMN_TARGET_DATE, item.target_date);
		values.put(COLUMN_STATUS, item.status);
		
		return values;
	}
}