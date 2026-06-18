package br.tcc.cadastra.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.tcc.cadastra.data.entity.ParticipanteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ParticipanteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirOuAtualizar(participante: ParticipanteEntity): Long

    @Query("""
        SELECT * FROM tabela_participantes 
        WHERE usuarioLocalId = :usuarioAtivoId 
        ORDER BY dataCriacao DESC
    """)
    fun listarParticipantesPorUsuario(usuarioAtivoId: String): Flow<List<ParticipanteEntity>>

    @Query("""
        SELECT estagioFunil AS estagio, COUNT(*) AS quantidade 
        FROM tabela_participantes 
        WHERE usuarioLocalId = :usuarioId 
        GROUP BY estagioFunil
    """)
    fun obterMetricasFunilLocal(usuarioId: String): Flow<List<MetricaFunil>>

    @Query("""
        DELETE FROM tabela_participantes 
        WHERE sincronizado = 1 
          AND usuarioLocalId = :usuarioAtivoId
          AND idLocal NOT IN (
              SELECT idLocal FROM tabela_participantes 
              WHERE sincronizado = 1 AND usuarioLocalId = :usuarioAtivoId
              ORDER BY dataCriacao DESC 
              LIMIT 50
          )
    """)
    suspend fun executarExpurgoDadosSincronizados(usuarioAtivoId: String)
}
