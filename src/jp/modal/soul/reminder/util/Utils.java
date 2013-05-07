package jp.modal.soul.reminder.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.os.Environment;

public class Utils {

    public static String getDateString(String targetDateMillis) {

    	Date target = new Date(Long.valueOf(targetDateMillis));
    	SimpleDateFormat dateFormat = new SimpleDateFormat("M/d HH:mm");
    	
    	return dateFormat.format(target);
    }
    
    public static String getDirPath(Context context) {
        String dirPath = "";
        File photoDir = null;
        File extStorageDir = Environment.getExternalStorageDirectory();
        if (extStorageDir.canWrite()) {
            photoDir = new File(extStorageDir.getPath() + "/" + context.getPackageName());
        }
        if (photoDir != null) {
            if (!photoDir.exists()) {
                photoDir.mkdirs();
            }
            if (photoDir.canWrite()) {
                dirPath = photoDir.getPath();
            }
        }
        return dirPath;
    }
}
