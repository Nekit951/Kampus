package com.example.kampus2.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, "app", factory, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE users (fio TEXT, email TEXT, pass TEXT)"
        db!!.execSQL(query)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db!!.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun addUser(user: User){
        val values = ContentValues()
        values.put("fio", user.fio)
        values.put("email", user.email)
        values.put("pass", user.pass)

        val db = this.writableDatabase
        db.insert("users", null, values)

        db.close()
    }

    fun getUser(email: String, pass: String): User?{
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM users WHERE email = ? AND pass = ?", arrayOf(email, pass))

        var user: User? = null
        if(result.moveToFirst()){
            user = User(
                result.getString(0),
                result.getString(1),
                result.getString(2)
            )
        }
        result.close()
        return user
    }
}