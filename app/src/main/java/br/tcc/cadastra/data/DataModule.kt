package br.tcc.cadastra.data

import android.content.Context
import br.tcc.cadastra.data.dao.ParticipanteDao
import br.tcc.cadastra.data.dao.SessaoUsuarioDao
import br.tcc.cadastra.data.repository.AutenticacaoRepository
import br.tcc.cadastra.data.repository.ParticipanteRepository

/**
 * Módulo centralizador de dependências do ecossistema de dados.
 * Fornece instâncias únicas (Singletons) dos Repositórios e do Banco de Dados
 * para serem consumidas pelas ViewModels da camada de interface.
 */
class DataModule(private val context: Context) {

    // Inicialização preguiçosa (Lazy) do Banco de Dados Room
    private val database: AppDatabase by lazy {
        AppDatabase.getDatabase(context)
    }

    // Fornecimento dos DAOs a partir da instância do banco
    private val sessaoUsuarioDao: SessaoUsuarioDao by lazy {
        database.sessaoUsuarioDao()
    }

    private val participanteDao: ParticipanteDao by lazy {
        database.participanteDao()
    }

    /**
     * Fornece a instância única do Repositório de Autenticação e Perfis Locais.
     */
    val autenticacaoRepository: AutenticacaoRepository by lazy {
        AutenticacaoRepository(sessaoUsuarioDao)
    }

    /**
     * Fornece a instância única do Repositório de Participantes,
     * que gerencia as queries isoladas e o algoritmo de expurgo pós-sincronização.
     */
    val participanteRepository: ParticipanteRepository by lazy {
        ParticipanteRepository(participanteDao)
    }
}