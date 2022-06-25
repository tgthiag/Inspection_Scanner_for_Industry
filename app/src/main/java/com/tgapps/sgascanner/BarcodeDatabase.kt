package com.tgapps.sgascanner

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

var TABLE_NAME = "barcode"
var VERSION = 2
var MATERIAL = "material"
var CODIGO = "codigo"
var NOME = "nome"
var SET = "cod_set"

//Criação do banco de dados
class BarcodeDatabase(ctx: Context) : SQLiteOpenHelper(ctx, TABLE_NAME,null, VERSION) {
    override fun onCreate(p0: SQLiteDatabase?) {
        var CREATE_TABLE = """CREATE TABLE $TABLE_NAME($MATERIAL INT, $NOME VARCHAR, $SET VARCHAR, $CODIGO INT)""".trimMargin()
        p0?.execSQL(CREATE_TABLE)
        //Inserção dos dados
        Database().InsertData(p0)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        p0?.execSQL(DROP_TABLE)
    }
}