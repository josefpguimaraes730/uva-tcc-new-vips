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
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["usuarioLocalId"])]
)
data class ParticipanteEntity(
    @PrimaryKey(autoGenerate = true)
    val idLocal: Long = 0,
    val usuarioLocalId: Long? = null,
    val nome: String,
    val telefone: String,
    val estagioFunil: String, // TRIAGEM, ACOMPANHAMENTO, ENCAMINHADO
    val dataCriacao: Long,
    val sincronizado: Boolean = false,
    val idRemoto: Long? = null,

    val bairro: String = "",
    val cidade: String = "",
    val dataNascimento: String = "",
    val participaIgreja: Boolean = false,
    val qualIgreja: String = "",
    val vezesVisitadas: String = "1",
    val comoConheceu: String = "",
    val acompanhantes: String = "",
    val apelo: String = "Não",
    val uGroupIndicado: String = "Nenhum",
    val temWhatsapp: Boolean = true
)