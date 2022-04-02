package sannikov.a.stonerstopwatch.data

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Pill::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pillDao(): PillDao


}
