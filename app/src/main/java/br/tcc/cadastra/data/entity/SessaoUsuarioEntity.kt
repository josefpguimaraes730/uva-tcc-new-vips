package br.tcc.cadastra.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa a tabela de controle de perfis locais armazenados no dispositivo.
 * Essencial para o gerenciamento de múltiplos usuários compartilhando o mesmo aparelho.
 */
@Entity(tableName = "sessao_usuario")
data class SessaoUsuarioEntity(
    @PrimaryKey
    val usuarioId: String,          // UUID gerado localmente ou ID retornado pelo servidor na v2
    val nome: String,               // Nome do operador/assistente social
    val email: String,              // Email para identificação do login
    val perfil: String,             // Nível de acesso (ex: "ADMIN", "VOLUNTARIO")
    val dataUltimoAcesso: Long      // Timestamp do último login para ordenação na troca de usuário
)