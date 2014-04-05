package edu.psu.mob_apps.lab5.persistence;

import android.provider.BaseColumns;

public class JokeDbContract {
	private JokeDbContract() {};
	
	public static abstract class TblJoke implements BaseColumns {
		public static final String TABLE_NAME = "jokes";

		public static final String COLUMN_NAME_SETUP = "setup";
		public static final String COLUMN_TYPE_SETUP = "TEXT";
		
		public static final String COLUMN_NAME_PUNCHLINE = "punchline";
		public static final String COLUMN_TYPE_PUNCHLINE = "TEXT";
		public static final String COLUMN_NAME_USED = "used";
		public static final String COLUMN_TYPE_USED = "INTEGER";
		
	}
	
}
