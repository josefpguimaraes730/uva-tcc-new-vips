package br.tcc.cadastra.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Representa os registros de participantes capturados em campo.
 * Possui relacionamento de integridade referencial com a tabela de usuários,
 * além de travas para garantir a integridade do ecossistema Offline-First.
 */
@Entity(
    tableName = "tabela_participantes",
    foreignKeys = [
        ForeignKey(
            entity = SessaoUsuarioEntity::class,
            parentColumns = ["usuarioId"],
            childColumns = ["usuarioLocalId"],
            onDelete = ForeignKey.CASCADE // Se o perfil for removido do app, apaga os dados locais dele
        )
    ],
    indices = [Index(value = ["usuarioLocalId"])] // Otimiza a velocidade de consulta por usuário ativo
)
data class ParticipanteEntity(
    @PrimaryKey(autoGenerate = true)
    val idLocal: Long = 0,               // ID incremental interno do SQLite para controle físico local
    val usuarioLocalId: String,          // ID do usuário que realizou o cadastro (Barreira de Isolamento)
    val nome: String,                    // Nome do participante da instituição social
    val estagioFunil: String,            // Status atual no funil de atendimento
    val dataCriacao: Long,               // Timestamp do cadastro para filtros e algoritmo de expurgo
    
    // Metadados de Arquitetura Transiente e Sincronização (Preparados para a v2)
    val sincronizado: Boolean = false,   // v1 sempre grava como false; v2 mudará para true após upload bem-sucedido
    val idRemoto: String? = null         // ID correspondente no banco de dados em nuvem na v2
)