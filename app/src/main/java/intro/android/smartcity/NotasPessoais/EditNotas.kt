package intro.android.smartcity.NotasPessoais

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import intro.android.smartcity.R

class EditNotas : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_notas)

        var editTituloView: EditText = findViewById(R.id.titulo_edit)
        var editObservacaoView: EditText = findViewById(R.id.observacao_edit)

        var id = intent.getStringExtra(PARAM_ID)
        var titulo = intent.getStringExtra(PARAM1_TITULO)
        var observacao = intent.getStringExtra(PARAM2_OBSERVACAO)
        editTituloView.setText(titulo.toString())
        editObservacaoView.setText(observacao.toString())

        val button = findViewById<Button>(R.id.button_edit)
        button.setOnClickListener {
            val replyIntent = Intent()
            replyIntent.putExtra(EDIT_ID, id)
            if (TextUtils.isEmpty(editTituloView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val edit_titulo = editTituloView.text.toString()
                replyIntent.putExtra(EDIT_TITULO, edit_titulo)
                replyIntent.putExtra(STATUS, "EDIT")
                setResult(Activity.RESULT_OK, replyIntent)
            }

            if (TextUtils.isEmpty(editObservacaoView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val edit_observacao = editObservacaoView.text.toString()
                replyIntent.putExtra(EDIT_OBSERVACAO, edit_observacao)
                replyIntent.putExtra(STATUS, "EDIT")
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }

        val button_delete = findViewById<Button>(R.id.button_delete)
        button_delete.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editTituloView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra(DELETE_ID, id)
                replyIntent.putExtra(STATUS, "DELETE")
                setResult(Activity.RESULT_OK, replyIntent)
            }

            if (TextUtils.isEmpty(editObservacaoView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra(DELETE_ID, id)
                replyIntent.putExtra(STATUS, "DELETE")
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }

    }

    companion object {
        const val STATUS = ""
        const val DELETE_ID = "DELETE_ID"
        const val EDIT_ID = "EDIT_ID"
        const val EDIT_TITULO = "EDIT_TITULO"
        const val EDIT_OBSERVACAO = "EDIT_OBSERVACAO"
    }
}
