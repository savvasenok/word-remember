package xyz.savvamirzoyan.wordremember.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import xyz.savvamirzoyan.wordremember.data.database.dao.AdjectiveWordDao
import xyz.savvamirzoyan.wordremember.data.database.dao.NounWordDao
import xyz.savvamirzoyan.wordremember.data.database.dao.VerbFormDao
import xyz.savvamirzoyan.wordremember.data.database.dao.VerbWordDao
import xyz.savvamirzoyan.wordremember.data.database.model.AdjectiveWord
import xyz.savvamirzoyan.wordremember.data.database.model.NounWord
import xyz.savvamirzoyan.wordremember.data.database.model.VerbForm
import xyz.savvamirzoyan.wordremember.data.database.model.VerbWord

@Database(
    entities = [
        NounWord::class,
        VerbWord::class,
        AdjectiveWord::class,
        VerbForm::class
    ],
    exportSchema = true,
    version = 1,
    autoMigrations = []
)
abstract class AppDatabase : RoomDatabase() {
    abstract val nounWordDao: NounWordDao
    abstract val verbWordDao: VerbWordDao
    abstract val verbFormDao: VerbFormDao
    abstract val adjectiveWordDao: AdjectiveWordDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val lock = Any()

        fun getInstance(context: Context? = null): AppDatabase {
            synchronized(this) {
                var localInstance = instance

                if (localInstance == null) {
                    localInstance = Room
                        .databaseBuilder(
                            context!!.applicationContext,
                            AppDatabase::class.java,
                            "main"
                        ).build()

                    instance = localInstance
                }

                return localInstance
            }
        }

        operator fun invoke(context: Context? = null) = instance ?: synchronized(lock) {
            instance ?: createDatabase(context!!).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "main.db"
        ).build()
    }
}