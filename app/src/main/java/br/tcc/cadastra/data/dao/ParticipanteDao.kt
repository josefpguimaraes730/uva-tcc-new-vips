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

    // CORRIGIDO: Se usuarioAtivoId for 0 ou nulo, busca registros onde usuarioLocalId IS NULL
    @Query("""
        SELECT * FROM tabela_participantes 
        WHERE (:usuarioAtivoId = 0 AND usuarioLocalId IS NULL) 
           OR (usuarioLocalId = :usuarioAtivoId)
        ORDER BY dataCriacao DESC
    """)
    fun listarParticipantesPorUsuario(usuarioAtivoId: Long): Flow<List<ParticipanteEntity>>

    // CORRIGIDO: Ajustado para contabilizar as métricas do painel mesmo sem usuário logado
    @Query("""
        SELECT estagioFunil AS estagio, COUNT(*) AS quantidade 
        FROM tabela_participantes 
        WHERE (:usuarioId = 0 AND usuarioLocalId IS NULL) 
           OR (usuarioLocalId = :usuarioId)
        GROUP BY estagioFunil
    """)
    fun obterMetricasFunilLocal(usuarioId: Long): Flow<List<MetricaFunil>>

    // Função para vincular os registros retroativamente quando o primeiro usuário logar
    @Query("""
        UPDATE tabela_participantes 
        SET usuarioLocalId = :novoUsuarioId 
        WHERE usuarioLocalId IS NULL
    """)
    suspend fun associarRegistrosOrfaosAoPrimeiroUsuario(novoUsuarioId: Long)

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
    suspend fun executarExpurgoDadosSincronizados(usuarioAtivoId: Long)
}