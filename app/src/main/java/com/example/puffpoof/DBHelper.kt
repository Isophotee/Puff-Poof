package com.example.puffpoof

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "mydatabase.db"
        private const val DATABASE_VERSION = 1

        // Doll table constants
        private const val TABLE_DOLLS = "dolls"
        private const val COLUMN_DOLL_ID = "id"
        private const val COLUMN_DOLL_NAME = "name"
        private const val COLUMN_DOLL_SIZE = "size"
        private const val COLUMN_DOLL_RATING = "rating"
        private const val COLUMN_DOLL_PRICE = "price"
        private const val COLUMN_DOLL_IMAGE = "image"
        private const val COLUMN_DOLL_DESCRIPTION = "description"

        private const val TABLE_USER = "user"
        private const val COLUMN_ID = "userid"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "userpassword"
        private const val COLUMN_PHONE = "phonenumber"
        private const val COLUMN_GENDER = "gender"
        private const val COLUMN_EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUserTable = ("CREATE TABLE $TABLE_USER (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USERNAME TEXT, " +
                "$COLUMN_PASSWORD TEXT, " +
                "$COLUMN_PHONE TEXT)")

        val createDollTable = ("CREATE TABLE $TABLE_DOLLS (" +
                "$COLUMN_DOLL_ID INTEGER PRIMARY KEY, " +
                "$COLUMN_DOLL_NAME TEXT, " +
                "$COLUMN_DOLL_SIZE TEXT, " +
                "$COLUMN_DOLL_RATING REAL, " +
                "$COLUMN_DOLL_PRICE INTEGER, " +
                "$COLUMN_DOLL_IMAGE TEXT, " +
                "$COLUMN_DOLL_DESCRIPTION TEXT)")

        db.execSQL(createUserTable)
        db.execSQL(createDollTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DOLLS")
        onCreate(db)
    }

    // User-related methods
    fun insertUser(username: String, email: String, password: String, phone: String, gender: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_PHONE, phone)
            put(COLUMN_GENDER, gender)
        }
        val db = writableDatabase
        return db.insert(TABLE_USER,  null, values)
    }

    fun readUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor = db.query(TABLE_USER , null, selection, selectionArgs, null, null, null)
        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }

    fun isUsernameExist(username: String): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ?"
        val selectionArgs = arrayOf(username)
        val cursor = db.query(TABLE_USER , null, selection, selectionArgs, null, null, null)
        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }
    fun insertDoll(doll: Doll): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DOLL_ID, doll.id)
            put(COLUMN_DOLL_NAME, doll.name)
            put(COLUMN_DOLL_SIZE, doll.size)
            put(COLUMN_DOLL_RATING, doll.rating)
            put(COLUMN_DOLL_PRICE, doll.price)
            put(COLUMN_DOLL_IMAGE, doll.image)
            put(COLUMN_DOLL_DESCRIPTION, doll.description)
        }
        return db.insert(TABLE_DOLLS, null, values)
    }

    fun getAllDolls(): List<Doll> {
        val dolls = mutableListOf<Doll>()
        val db = readableDatabase
        val cursor = db.query(TABLE_DOLLS, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val doll = Doll(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DOLL_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DOLL_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DOLL_SIZE)),
                    cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_DOLL_RATING)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DOLL_PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DOLL_IMAGE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DOLL_DESCRIPTION))
                )
                dolls.add(doll)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return dolls
    }
}

