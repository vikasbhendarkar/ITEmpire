/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.jobPortal.library;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jobPortal.bean.SearchFilterForCompany;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "jobportal_api";

	// Login table name
	private static final String TABLE_LOGIN = "login";
	private static final String TABLE_FILTER_COMPANY = "filter";

	// Login Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_UID = "uid";
	private static final String KEY_CREATED_AT = "created_at";

	// filter_for_cmny Table Columns names
	private static final String KEY_SSC = "ssc";
	private static final String KEY_HSC = "hsc";
	private static final String KEY_DIPLOMA = "diploma";
	private static final String KEY_BE = "be";

	private static final String CREATE_LOGIN_TABLE = "CREATE TABLE "
			+ TABLE_LOGIN + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME
			+ " TEXT," + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
			+ KEY_CREATED_AT + " TEXT" + ")";

	private static final String CREATE_FILTER_TABLE_COMPANY = "CREATE TABLE "
			+ TABLE_FILTER_COMPANY + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_EMAIL + " TEXT UNIQUE," + KEY_SSC + " REAL," + KEY_HSC
			+ " REAL," + KEY_DIPLOMA + " REAL," + KEY_BE + " REAL" + ")";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_LOGIN_TABLE);
		db.execSQL(CREATE_FILTER_TABLE_COMPANY);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILTER_COMPANY);
		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */
	public void addUser(String name, String email, String uid, String created_at) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name); // Name
		values.put(KEY_EMAIL, email); // Email
		values.put(KEY_UID, "uid"); // Email
		values.put(KEY_CREATED_AT, created_at); // Created At

		// Inserting Row
		db.insert(TABLE_LOGIN, null, values);
		db.close(); // Closing database connection
	}

	/**
	 * Getting user data from database
	 * */
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			user.put("name", cursor.getString(1));
			user.put("email", cursor.getString(2));
			user.put("uid", cursor.getString(3));
			user.put("created_at", cursor.getString(4));
		}
		cursor.close();
		db.close();
		// return user
		return user;
	}

	/**
	 * Getting user login status return true if rows are there in table
	 * */
	public int getRowCount() {
		String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int rowCount = cursor.getCount();
		db.close();
		cursor.close();

		// return row count
		return rowCount;
	}

	/**
	 * Re crate database Delete all tables and create them again
	 * */
	public void resetTables() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_LOGIN, null, null);
		db.close();
	}

	/**
	 * Storing filter details for company in database
	 * */
	public void addFilterCmny(SearchFilterForCompany filter) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_EMAIL, filter.getEmail());
		values.put(KEY_SSC, filter.getSsc_marks());
		values.put(KEY_HSC, filter.getHsc_marks());
		values.put(KEY_DIPLOMA, filter.getDiploma_marks());
		values.put(KEY_BE, filter.getBe_marks());

		// Inserting Row
		db.insert(TABLE_FILTER_COMPANY, null, values);
		db.close(); // Closing database connection
	}

	/**
	 * Getting filter comny data from database
	 * */
	public HashMap<String, Double> getFilterDetailsCompanyUsingEmail(String mail) {
		HashMap<String, Double> filter = new HashMap<String, Double>();
		String selectQuery = "SELECT  * FROM " + TABLE_FILTER_COMPANY
				+ " WHERE " + KEY_EMAIL + " = '" + mail + "'";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			filter.put("ssc", Double.parseDouble(cursor.getString(2)));
			filter.put("hsc", Double.parseDouble(cursor.getString(3)));
			filter.put("diploma", Double.parseDouble(cursor.getString(4)));
			filter.put("be", Double.parseDouble(cursor.getString(5)));
		}
		cursor.close();
		db.close();
		// return user
		return filter;
	}

	public void updateFilterTable(SearchFilterForCompany filter) {
		SQLiteDatabase db = this.getWritableDatabase();

		String where_caluse = KEY_EMAIL + "=" + "\'" + filter.getEmail() + "\'";
		ContentValues values = new ContentValues();
		values.put(KEY_SSC, filter.getSsc_marks());
		values.put(KEY_HSC, filter.getHsc_marks());
		values.put(KEY_DIPLOMA, filter.getDiploma_marks());
		values.put(KEY_BE, filter.getBe_marks());

		// Inserting Row
		db.update(TABLE_FILTER_COMPANY, values, where_caluse, null);
		db.close(); // Closing database connection
	}

	public void resetFilterTables() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_FILTER_COMPANY, null, null);
		db.close();
	}

//	public int getRowCountForFilterTable(String email) {
//		String countQuery = "SELECT  * FROM " + TABLE_FILTER_COMPANY
//				+ " WHERE email=" + "\'" + email + "\'";
//		SQLiteDatabase db = this.getReadableDatabase();
//		Cursor cursor = db.rawQuery(countQuery, null);
//		int rowCount = cursor.getCount();
//		db.close();
//		cursor.close();
//
//		// return row count
//		return rowCount;
//	}

}
