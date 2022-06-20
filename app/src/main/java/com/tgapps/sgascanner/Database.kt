package com.tgapps.sgascanner

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

class Database {
    //Local onde estará a referência a database para aquisição dos dados.
    fun InsertData(p0: SQLiteDatabase?){
        var cv = ContentValues()
        cv.put(MATERIAL,66261161504)
        cv.put(NOME,"SHS SST 225x275 T223 NOR C280")
        cv.put(UNIDADE,7891792133594)
        cv.put(CONJUNTO,7895316231278)
        cv.put(PACOTE,7891792000933)
        p0?.insert(TABLE_NAME,null,cv)
    }
}