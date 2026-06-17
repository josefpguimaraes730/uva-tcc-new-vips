package br.tcc.cadastra.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.tcc.cadastra.data.entity.SessaoUsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SessaoUsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvarUsuarioLocal(usuario: SessaoUsuarioEntity)

    @Query("SELECT * FROM sessao_usuario WHERE usuarioId = :usuarioId LIMIT 1")
    suspend fun buscarUsuarioPorId(usuarioId: String): SessaoUsuarioEntity?

    @Query("SELECT * FROM sessao_usuario ORDER BY nome ASC")
    fun listarTodosOsPerfisLocais(): Flow<List<SessaoUsuarioEntity>>

    @Query("DELETE FROM sessao_usuario WHERE usuarioId = :usuarioId")
    suspend fun removerPerfilLocal(usuarioId: String)
}