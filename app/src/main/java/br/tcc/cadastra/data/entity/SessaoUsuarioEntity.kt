package br.tcc.cadastra.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessao_usuario")
data class SessaoUsuarioEntity(
    @PrimaryKey
    val usuarioId: String,
    val nome: String,
    val email: String,
    val perfil: String,
    val dataUltimoAcesso: Long
)