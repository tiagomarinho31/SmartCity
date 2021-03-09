package intro.android.smartcity.NotasPessoais

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import intro.android.smartcity.R

class AddNotas : AppCompatActivity() {

    private lateinit var editTituloView: EditText
    private lateinit var editObservacaoView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notas)

        editTituloView = findViewById(R.id.titulo)
        editObservacaoView = findViewById(R.id.observacao)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editTituloView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val titulo = editTituloView.text.toString()
                replyIntent.putExtra(TITULO, titulo)
                setResult(Activity.RESULT_OK, replyIntent)
            }

            if (TextUtils.isEmpty(editObservacaoView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val observacao = editObservacaoView.text.toString()
                replyIntent.putExtra(OBSERVACAO, observacao)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val TITULO = "com.example.android.wordlistsql.TITULO"
        const val OBSERVACAO = "com.example.android.wordlistsql.OBSERVACAO"
    }

}