package com.tgapps.sgascanner

import android.content.Context
import android.content.Intent
import android.widget.Toast

class BarcodeToBd() {
    fun checkDatabases(barcode: String?, ctx: Context){
    var database = BarcodeDatabase(ctx)
    var db = database.readableDatabase
    var query = "SELECT * FROM $TABLE_NAME WHERE $CODIGO = $barcode OR $MATERIAL = $barcode"
    var cursor = db.rawQuery(query,null)
        cursor.moveToFirst()
        //adquirindo número do material para puxar os outros códigos, caso código do produto seja escaneado.
        var materialCursor = cursor.getString(0)
        var queryPC = "SELECT *,(SELECT COUNT(*) FROM $TABLE_NAME WHERE $MATERIAL = $materialCursor AND $SET = 'PC') FROM $TABLE_NAME WHERE $MATERIAL = $materialCursor AND $SET = 'PC'"
        var queryCJ = "SELECT *, (SELECT COUNT(*) FROM $TABLE_NAME WHERE $MATERIAL = $materialCursor AND $SET = 'SET') FROM $TABLE_NAME WHERE $MATERIAL = $materialCursor AND $SET = 'SET'"
        var queryPK = "SELECT *, (SELECT COUNT(*) FROM $TABLE_NAME WHERE $MATERIAL = $materialCursor AND $SET != 'PC' AND $SET != 'SET') FROM $TABLE_NAME WHERE $MATERIAL = $materialCursor AND $SET != 'PC' AND $SET != 'SET'"
        //pegando os códigos da peça, conjunto e pack
        var cursorPC = db.rawQuery(queryPC,null)
        var cursorCJ = db.rawQuery(queryCJ,null)
        var cursorPK = db.rawQuery(queryPK,null)


        //Se encontrou algo no banco de dados, abrir activity passando os dados encontrados
        if (cursor != null){
            cursor.moveToFirst()
            var nome = cursor.getString(1)
            var material = cursor.getString(0)
            cursor.close()
            //PUXANDO OS DADOS DE CADA QUERY
            cursorPC.moveToFirst()
            var codigoPC : String? = try{if (cursorPC.getInt(4) < 2){cursorPC.getString(2) + ": " + cursorPC.getString(3)}else{"Inconclusivo, solicite correção."}}catch (e: Exception){""}
            cursorPC.close()
            cursorCJ.moveToFirst()
            var codigoCJ : String? = try{if (cursorCJ.getInt(4) < 2){cursorCJ.getString(2) + ": " + cursorCJ.getString(3)}else{"Inconclusivo, solicite correção."}}catch (e: Exception){""}
            cursorCJ.close()
            cursorPK.moveToFirst()
            var codigoPK : String? = try{if (cursorPK.getInt(4) < 2){cursorPK.getString(2) + ": " + cursorPK.getString(3)}else{"Inconclusivo, solicite correção."}}catch (e: Exception){""}
            cursorPK.close()



            //ABRIR NOVA TELA PARA MOSTRAR OS RESULTADOS
            val intent = Intent(ctx, ActivityResults::class.java).apply {
                putExtra("nome",nome)
                putExtra("material",material)
                putExtra("PC",codigoPC)
                putExtra("CJ",codigoCJ)
                putExtra("PK",codigoPK)

            }
            ctx.startActivity(intent)
        }
    }
}