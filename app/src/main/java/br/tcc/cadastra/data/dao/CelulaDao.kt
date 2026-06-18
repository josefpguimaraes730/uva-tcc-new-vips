package br.tcc.cadastra.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.tcc.cadastra.data.entity.CelulaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CelulaDao {

    @Query("SELECT * FROM tabela_celula")
    fun listarTodas(): Flow<List<CelulaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirVarias(celulas: List<CelulaEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(celula: CelulaEntity)
}