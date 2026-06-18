package br.tcc.cadastra.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.tcc.cadastra.data.dao.CelulaDao
import br.tcc.cadastra.data.dao.ParticipanteDao
import br.tcc.cadastra.data.dao.SessaoUsuarioDao
import br.tcc.cadastra.data.entity.ParticipanteEntity
import br.tcc.cadastra.data.entity.SessaoUsuarioEntity
import br.tcc.cadastra.data.entity.CelulaEntity

@Database(
    entities = [
        SessaoUsuarioEntity::class, 
        ParticipanteEntity::class,
        CelulaEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun sessaoUsuarioDao(): SessaoUsuarioDao
    abstract fun participanteDao(): ParticipanteDao
    abstract fun celulaDao(): CelulaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "instituicao_social_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}