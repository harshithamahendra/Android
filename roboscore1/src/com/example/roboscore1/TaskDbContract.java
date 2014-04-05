package com.example.roboscore1;
import android.provider.BaseColumns;

public class TaskDbContract {
	private TaskDbContract(){}
	
	public static abstract class TblTask implements BaseColumns {
		public static final String TABLE_NAME = "task";

		public static final String COLUMN_NAME_SUBTASK = "subtask";
		public static final String COLUMN_TYPE_SUBTASK = "INTEGER";
		
		public static final String COLUMN_NAME_SCORE_LIMIT = "score_limit";
		public static final String COLUMN_TYPE_SCORE_LIMIT = "INTEGER";
		public static final String COLUMN_NAME_DESCRIPTION = "description";
		public static final String COLUMN_TYPE_DESCRIPTION = "TEXT";
		
		public static final String TABLE_NAME1 = "Scores";

		public static final String COLUMN_NAME_TEAM_NO = "team_no";
		public static final String COLUMN_TYPE_TEAM_NO = "INTEGER";
		public static final String COLUMN_NAME_SUBTASK_NO = "subtask_no";
		public static final String COLUMN_TYPE_SUBTASK_NO = "INTEGER";
		public static final String COLUMN_NAME_SCORE = "score";
		public static final String COLUMN_TYPE_SCORE = "INTEGER";





		
	}
}
