package com.example.kampus2.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CardDB(val context: Context, val factory: SQLiteDatabase.CursorFactory?): SQLiteOpenHelper(context, "card_db", factory, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE cards (number TEXT, data TEXT, cvp TEXT)"
        db!!.execSQL(query)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db!!.execSQL("DROP TABLE IF EXISTS cards")
        onCreate(db)
    }

    fun addCard(card: Card){
        val values = ContentValues()
        values.put("number", card.number)
        values.put("data", card.data)
        values.put("cvp", card.cvp)

        val db = this.writableDatabase
        db.insert("cards", null, values)

        db.close()
    }

    fun getCard(): MutableList<Card>{
        val list = mutableListOf<Card>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM cards", null)

        if (cursor.moveToFirst()) {
            do {
                val card = Card(
                    cursor.getString(0), // number
                    cursor.getString(1), // data
                    cursor.getString(2)  // cvp
                )
                list.add(card)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun delCard(card: Card){
        val db = this.writableDatabase
        db.delete("cards", "number = ?", arrayOf(card.number))
        db.close()
    }
}