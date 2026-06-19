package br.tcc.cadastra.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import br.tcc.cadastra.data.dao.ParticipanteDao
import br.tcc.cadastra.data.dao.CelulaDao
import br.tcc.cadastra.data.dao.SessaoUsuarioDao
import br.tcc.cadastra.data.entity.ParticipanteEntity
import br.tcc.cadastra.data.entity.CelulaEntity
import br.tcc.cadastra.data.entity.SessaoUsuarioEntity

@Database(
    entities = [ParticipanteEntity::class, CelulaEntity::class, SessaoUsuarioEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun participanteDao(): ParticipanteDao
    abstract fun celulaDao(): CelulaDao
    abstract fun sessaoUsuarioDao(): SessaoUsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun obterBanco(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sistema_cadastra_db"
                )
                .fallbackToDestructiveMigration() 
                .build()
                
                INSTANCE = instance
                instance
            }
        }
    }
}