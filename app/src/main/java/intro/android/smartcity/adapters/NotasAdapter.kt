package intro.android.smartcity.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import intro.android.smartcity.NotasPessoais.AddNotas
import intro.android.smartcity.NotasPessoais.CellClickListener
import intro.android.smartcity.NotasPessoais.EditNotas
import intro.android.smartcity.R
import intro.android.smartcity.entities.Notas

class NotasAdapter internal constructor(context: Context, private val cellClickListener: CellClickListener) : RecyclerView.Adapter<NotasAdapter.NotasViewHolder>(){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notas = emptyList<Notas>()


    class NotasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tituloItemView: TextView = itemView.findViewById(R.id.titulo)
        val observacaoItemView: TextView = itemView.findViewById(R.id.observacao)
        //val dataItemView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotasViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item,parent,false)
        return NotasViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotasViewHolder, position: Int) {
        val current = notas[position]
        holder.tituloItemView.text = "Título: " + current.titulo
        holder.observacaoItemView.text = "Observação:" + current.observacao
        //holder.dataItemView.text = current.data.toString()
        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(notas[position])
        }

    }

    internal fun setNotas(notas : List<Notas>){
        this.notas = notas
        notifyDataSetChanged()
    }

    override fun getItemCount() : Int {
        return notas.size
    }
}

