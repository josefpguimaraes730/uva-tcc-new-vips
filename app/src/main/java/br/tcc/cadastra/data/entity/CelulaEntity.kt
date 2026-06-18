package br.tcc.cadastra.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabela_celula")
data class CelulaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nome: String,
    val lider: String,
    val whatsappLider: String,
    val anfitriao: String,
    val whatsappAnfitriao: String,
    val bairro: String
)
