package intro.android.smartcity.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import intro.android.smartcity.entities.Notas

@Dao
abstract interface NotasDao {
    @Query("SELECT * FROM notas_table ORDER BY id ASC")
    fun getIdOrder(): LiveData<List<Notas>>

    @Query("SELECT * FROM notas_table ORDER BY titulo ASC")
    fun getTituloOrder(): LiveData<List<Notas>>

    //@Query("SELECT * FROM notas_table ORDER BY data ASC")
    //fun getDateOrder(): LiveData<List<Notas>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notas: Notas)

    @Query("UPDATE notas_table SET titulo = :titulo, observacao = :observacao WHERE id = :id")
    suspend fun update(id: Int?, titulo: String, observacao: String)

    @Query("DELETE FROM notas_table WHERE id = :id")
    suspend fun delete(id: Int?)

    @Query("DELETE FROM notas_table")
    suspend fun deleteAll()
}