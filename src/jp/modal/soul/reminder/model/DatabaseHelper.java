package jp.modal.soul.reminder.model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    /** ログ用タグ */
    public final String TAG = this.getClass().getSimpleName();

    /** DB名 */
    private static final String DB_NAME = "reminder";

    /**
     * DBのバージョン番号
     */
    private static final int DB_VERSION = 1;

    private static String DB_PATH = "/data/data/jp.modal.soul.reminder/databases/"; 
    
    private SQLiteDatabase mDataBase;  
    
    private final Context mContext; 
    
    private static String DB_NAME_ASSET = DB_NAME + ".db";  
    
    /**
     * コンストラクタ
     * @param context
     */
    public DatabaseHelper(Context context) {
    	super(context, DB_NAME, null, DB_VERSION);
    	this.mContext = context;
    }

    public void createEmptyDataBase() throws IOException{  
        boolean dbExist = checkDataBaseExists();  
  
        if(dbExist){  
            // すでにデータベースは作成されている  
        	Log.e(TAG, "DB is already exist");
        }else{  
            // このメソッドを呼ぶことで、空のデータベースが  
            // アプリのデフォルトシステムパスに作られる  
            this.getReadableDatabase();  
   
            try {  
                // asset に格納したデータベースをコピーする  
                copyDataBaseFromAsset();   
                Log.e(TAG, "COPY_SUCCESS");
            } catch (IOException e) {  
            	Log.e(TAG, "COPY ERROR");
                throw new Error("Error copying database");  
            }  
        }  
    }
    
    private void copyDataBaseFromAsset() throws IOException{  
    	   
        // asset 内のデータベースファイルにアクセス  
        InputStream mInput = mContext.getAssets().open(DB_NAME_ASSET);  
   
        // デフォルトのデータベースパスに作成した空のDB  
        String outFileName = DB_PATH + DB_NAME;  
   
        OutputStream mOutput = new FileOutputStream(outFileName);  
  
        // コピー  
        byte[] buffer = new byte[1024];  
        int size;  
        while ((size = mInput.read(buffer)) > 0){  
            mOutput.write(buffer, 0, size);  
        }  
   
        //Close the streams  
        mOutput.flush();  
        mOutput.close();  
        mInput.close();  
    }  
   
    public SQLiteDatabase openDataBaseReadable() throws SQLException{  
        //Open the database  
        String myPath = DB_PATH + DB_NAME;  
        mDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);  
        return mDataBase;  
    }
    public SQLiteDatabase openDataBaseWritable() throws SQLException {
    	String myPath = DB_PATH + DB_NAME;  
        mDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);  
        return mDataBase;
    }
      
    @Override  
    public void onCreate(SQLiteDatabase arg0) {  
    }  
  
    @Override  
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
    }  
  
    @Override  
    public synchronized void close() {  
        if(mDataBase != null)  
            mDataBase.close();  
      
        super.close();  
    }  
    
    private boolean checkDataBaseExists() {  
        SQLiteDatabase checkDb = null;  
   
        try{  
            String dbPath = DB_PATH + DB_NAME;  
            checkDb = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);  
        }catch(SQLiteException e){  
            // データベースはまだ存在していない  
        }  
   
        if(checkDb != null){  
            checkDb.close();  
        }  
        return checkDb != null ? true : false;  
    }
    
//    /**
//     * テーブル定義用メソッド
//     */
//    @Override
//    public void onCreate(SQLiteDatabase db) {
////    	db.beginTransaction();
////    	try {
////    		db.execSQL(BusStopDao.CREATE_TABLE);
////    		db.execSQL(RouteDao.CREATE_TABLE);
////    		db.execSQL(TimeTableDao.CREATE_TABLE);
////    		db.execSQL(HistoryDao.CREATE_TABLE);
////    		db.setTransactionSuccessful();
////    	} finally {
////    		db.endTransaction();
////    	}
//
//    }
//    /**
//     * マイグレーション用メソッド
//     */
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//    }
}