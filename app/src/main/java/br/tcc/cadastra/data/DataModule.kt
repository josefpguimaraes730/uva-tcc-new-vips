package br.tcc.cadastra.data

import android.content.Context
import androidx.room.Room
import br.tcc.cadastra.data.dao.ParticipanteDao
import br.tcc.cadastra.data.dao.SessaoUsuarioDao
import br.tcc.cadastra.data.repository.AutenticacaoRepository
import br.tcc.cadastra.data.repository.CelulaRepository
import br.tcc.cadastra.data.repository.ParticipanteRepository

class DataModule(private val context: Context) {

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "cadastra_database"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    private val sessaoUsuarioDao: SessaoUsuarioDao by lazy {
        database.sessaoUsuarioDao()
    }

    private val participanteDao: ParticipanteDao by lazy {
        database.participanteDao()
    }

    val autenticacaoRepository: AutenticacaoRepository by lazy {
        AutenticacaoRepository(sessaoUsuarioDao)
    }

    val participanteRepository: ParticipanteRepository by lazy {
        ParticipanteRepository(participanteDao)
    }

    val celulaRepository: CelulaRepository by lazy {
        CelulaRepository(database.celulaDao())
    }
}