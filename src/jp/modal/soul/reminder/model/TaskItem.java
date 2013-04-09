package jp.modal.soul.reminder.model;

import java.io.Serializable;


public class TaskItem implements Comparable<TaskItem>, Serializable {
	/** ログ出力用 タグ */
    public final String TAG = this.getClass().getSimpleName();
        
    /** status */
    public static final int STATUS_TODO = 0;
    public static final int STATUS_DONE = 1;
    
    /** properties */
	public int id;
	public String message;
	public int delay;
	public String start_date;
	public String target_date;
	public int status;
	public String image_url;
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public int compareTo(TaskItem another) {
		return (int)(this.id - another.id);
	}
	

}
