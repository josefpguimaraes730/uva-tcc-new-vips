package br.tcc.cadastra.data.repository

import br.tcc.cadastra.data.dao.CelulaDao
import br.tcc.cadastra.data.entity.CelulaEntity
import kotlinx.coroutines.flow.Flow

class CelulaRepository(private val celulaDao: CelulaDao) {

    val todasCelulas: Flow<List<CelulaEntity>> = celulaDao.listarTodas()

    suspend fun salvarCelulas(celulas: List<CelulaEntity>) {
        celulaDao.inserirVarias(celulas)
    }
}
