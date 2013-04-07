package jp.modal.soul.reminder.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String getDateString(String targetDateMillis) {

    	Date target = new Date(Long.valueOf(targetDateMillis));
    	SimpleDateFormat dateFormat = new SimpleDateFormat("M/d HH:mm");
    	
    	return dateFormat.format(target);
    }
}
