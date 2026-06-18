package br.tcc.cadastra

import android.app.Application
import br.tcc.cadastra.data.DataModule

class MainApplication : Application() {

    lateinit var dataModule: DataModule
        private set

    override fun onCreate() {
        super.onCreate()
        dataModule = DataModule(this)
    }
}