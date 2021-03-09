package intro.android.smartcity.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import intro.android.smartcity.db.NotasDB
import intro.android.smartcity.db.NotasRepository
import intro.android.smartcity.entities.Notas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotasViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : NotasRepository
    val allNotas : LiveData<List<Notas>>

    init {
        val notasDao = NotasDB.getDatabase(application, viewModelScope).notasDao()
        repository = NotasRepository(notasDao)
        allNotas = repository.allNotasId
    }

    fun insert(notas: Notas) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(notas)
    }

    fun update(id: Int?, titulo: String, observacao: String) = viewModelScope.launch{
        repository.update(id, titulo, observacao)
    }

    fun delete(id: Int?) = viewModelScope.launch{
        repository.delete(id)
    }
}