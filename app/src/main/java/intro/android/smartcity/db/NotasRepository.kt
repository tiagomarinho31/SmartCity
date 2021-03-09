package intro.android.smartcity.db

import androidx.lifecycle.LiveData
import intro.android.smartcity.dao.NotasDao
import intro.android.smartcity.entities.Notas

class NotasRepository(private val notasDao : NotasDao){
    val allNotasId: LiveData<List<Notas>> = notasDao.getIdOrder()
    val allNotasTitulo: LiveData<List<Notas>> = notasDao.getTituloOrder()
    //val allNotasData: LiveData<List<Notas>> = notasDao.getDateOrder()

    suspend fun insert(notas: Notas){
        notasDao.insert(notas)
    }

    suspend fun update(id: Int?, titulo: String, observacao: String){
        notasDao.update(id, titulo, observacao)
    }

    suspend fun delete(id: Int?){
        notasDao.delete(id)
    }
}