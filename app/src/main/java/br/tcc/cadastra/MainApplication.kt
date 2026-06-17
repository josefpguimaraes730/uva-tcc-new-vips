package br.tcc.cadastra

import android.app.Application
import br.tcc.cadastra.data.DataModule

class MainApplication : Application() {

    // Instância única do módulo de dados que ficará viva em todo o ciclo do app
    lateinit var dataModule: DataModule
        private set

    override fun onCreate() {
        super.onCreate()
        // Inicializa o banco de dados uma única vez no início do processo
        dataModule = DataModule(this)
    }
}