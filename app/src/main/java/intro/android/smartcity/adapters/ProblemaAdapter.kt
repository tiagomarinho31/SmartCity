package intro.android.smartcity.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import intro.android.smartcity.NotasPessoais.CellClickListener
import intro.android.smartcity.R
import intro.android.smartcity.api.Problema
import intro.android.smartcity.entities.Notas

class ProblemaAdapter (val problema: List<Problema>): RecyclerView.Adapter<ProblemaAdapter.ProblemaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProblemaViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_problema, parent, false)
        return ProblemaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProblemaViewHolder, position: Int) {
        return holder.bind(problema[position])

    }

    override fun getItemCount() : Int {
        return problema.size
    }

    class ProblemaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val latitudeItemView: TextView = itemView.findViewById(R.id.latitude)
        val longitudeItemView: TextView = itemView.findViewById(R.id.longitude)
        val tipoItemView: TextView = itemView.findViewById(R.id.tipo)
        val descricaoItemView: TextView = itemView.findViewById(R.id.descricao)

        fun bind(problema : Problema){
            latitudeItemView.text = "Latitude: " + problema.latitude.toString()
            longitudeItemView.text = "Longitude: " + problema.longitude.toString()
            tipoItemView.text = "Tipo: " + problema.tipo
            descricaoItemView.text = "Descrição: " +  problema.descricao
        }
    }
}