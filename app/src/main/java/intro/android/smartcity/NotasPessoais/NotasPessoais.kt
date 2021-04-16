package intro.android.smartcity.NotasPessoais

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import intro.android.smartcity.R
import intro.android.smartcity.adapters.NotasAdapter
import intro.android.smartcity.entities.Notas
import intro.android.smartcity.viewModel.NotasViewModel

const val PARAM_ID: String = "id"
const val PARAM1_TITULO: String = "titulo"
const val PARAM2_OBSERVACAO: String = "observacao"

class NotasPessoais : AppCompatActivity(), CellClickListener {

    private lateinit var notasViewModel: NotasViewModel
    private val newNotasActivityRequestCode = 1
    private val newNotasActivityRequestCode1 = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas_pessoais)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NotasAdapter(this, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        notasViewModel = ViewModelProvider(this).get(NotasViewModel::class.java)
        notasViewModel.allNotas.observe(this, Observer { notas ->
            notas?.let { adapter.setNotas(it) }
        })

        //fab
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, AddNotas::class.java)
            startActivityForResult(intent, newNotasActivityRequestCode)
        }
    }

    override fun onCellClickListener(data: Notas) {
        val intent = Intent(this@NotasPessoais, EditNotas::class.java)
        intent.putExtra(PARAM_ID, data.id.toString())
        intent.putExtra(PARAM1_TITULO, data.titulo.toString())
        intent.putExtra(PARAM2_OBSERVACAO, data.observacao.toString())
        startActivityForResult(intent, newNotasActivityRequestCode1)
        Log.e("***ID", data.id.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // ADICIONAR NOTA
        if (requestCode == newNotasActivityRequestCode && resultCode == Activity.RESULT_OK) {
            var titulo = data?.getStringExtra(AddNotas.TITULO).toString()
            var observacao = data?.getStringExtra(AddNotas.OBSERVACAO).toString()
            val notas = Notas(titulo = titulo, observacao = observacao)
            notasViewModel.insert(notas)
            Toast.makeText(this, R.string.save_note, Toast.LENGTH_SHORT).show()

        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, R.string.save_note_error, Toast.LENGTH_SHORT).show()
        }

        // EDITAR E APAGAR NOTA
        if (requestCode == newNotasActivityRequestCode1 && resultCode == Activity.RESULT_OK) {
            var edit_titulo = data?.getStringExtra(EditNotas.EDIT_TITULO).toString()
            var edit_observacao = data?.getStringExtra(EditNotas.EDIT_OBSERVACAO).toString()
            var id = data?.getStringExtra(EditNotas.EDIT_ID)
            var id_delete = data?.getStringExtra(EditNotas.DELETE_ID)
            if(data?.getStringExtra(EditNotas.STATUS) == "EDIT"){
                notasViewModel.update(id?.toIntOrNull(), edit_titulo, edit_observacao)
                Toast.makeText(this, R.string.edit_note, Toast.LENGTH_SHORT).show()
            } else if(data?.getStringExtra(EditNotas.STATUS) == "DELETE"){
                notasViewModel.delete(id_delete?.toIntOrNull())
                Toast.makeText(this, R.string.delete_note, Toast.LENGTH_SHORT).show()
            }
            //notasViewModel.update(id?.toIntOrNull(), edit_titulo, edit_observacao)
            //notasViewModel.delete(id_delete?.toIntOrNull())
            //Toast.makeText(this, "Nota editada!", Toast.LENGTH_LONG).show()
        } else if (resultCode == Activity.RESULT_CANCELED) {
            if(data?.getStringExtra(EditNotas.STATUS) == "EDIT"){
                Toast.makeText(this, R.string.edit_note_error, Toast.LENGTH_SHORT).show()
            } else if(data?.getStringExtra(EditNotas.STATUS) == "DELETE"){
                Toast.makeText(this, R.string.delete_note_error, Toast.LENGTH_SHORT).show()
            }
        }
    }


}