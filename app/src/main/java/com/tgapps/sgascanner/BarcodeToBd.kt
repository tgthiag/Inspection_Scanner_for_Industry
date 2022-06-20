package com.tgapps.sgascanner

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.database.getStringOrNull

class BarcodeToBd() {
    fun checkDatabases(barcode: String?, ctx: Context){
    var database = BarcodeDatabase(ctx)
    var db = database.readableDatabase
    var query = "SELECT * FROM $TABLE_NAME WHERE $UNIDADE = $barcode OR $CONJUNTO = $barcode OR $PACOTE = $barcode"
    var cursor = db.rawQuery(query,null)
        cursor.moveToFirst()

        //Se encontrou algo no banco de dados, abrir activity passando os dados encontrados
        if (cursor != null){
            cursor.moveToFirst()
            var nome = cursor.getString(1)
            var material = cursor.getString(0)
            var unidade = cursor.getString(2)
            var conjunto = cursor.getString(3)
            var pacote = cursor.getString(4)

            val intent = Intent(ctx, ActivityResults::class.java).apply {
                putExtra("nome",nome)
                putExtra("material",material)
                putExtra("unidade",unidade)
                putExtra("conjunto",conjunto)
                putExtra("pacote",pacote)
            }
            ctx.startActivity(intent)
            //startActivity(ctx,ActivityResults(nome,material,unidade,conjunto,pacote)::class.java)
        }
    }
}