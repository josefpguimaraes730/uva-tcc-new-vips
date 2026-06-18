package br.tcc.cadastra.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tabela_participantes",
    foreignKeys = [
        ForeignKey(
            entity = SessaoUsuarioEntity::class,
            parentColumns = ["usuarioId"],
            childColumns = ["usuarioLocalId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["usuarioLocalId"])]
)
data class ParticipanteEntity(
    @PrimaryKey(autoGenerate = true)
    val idLocal: Long = 0,
    val usuarioLocalId: String,
    val nome: String,
    val estagioFunil: String,
    val dataCriacao: Long,
    
    val sincronizado: Boolean = false,
    val idRemoto: String? = null
)