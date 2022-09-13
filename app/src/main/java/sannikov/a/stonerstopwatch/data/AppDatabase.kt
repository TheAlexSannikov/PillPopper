package sannikov.a.stonerstopwatch.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Pill::class, Drug::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pillDao(): PillDao
    abstract fun drugDao(): DrugDao
}
