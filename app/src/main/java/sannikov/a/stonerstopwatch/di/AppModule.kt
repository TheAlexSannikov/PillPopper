package sannikov.a.stonerstopwatch.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import javax.inject.Named
import dagger.Provides
import android.content.Context
import androidx.room.Room
import dagger.hilt.android.qualifiers.ApplicationContext
import sannikov.a.stonerstopwatch.data.AppDatabase
import sannikov.a.stonerstopwatch.data.DataStoreManager
import sannikov.a.stonerstopwatch.data.PillDao
import sannikov.a.stonerstopwatch.data.PillRepository
import sannikov.a.stonerstopwatch.viewmodels.PillViewModel
import sannikov.a.stonerstopwatch.viewmodels.StopwatchViewModel

@Module
@InstallIn(SingletonComponent::class) // Installs FooModule in the generate SingletonComponent.
internal object AppModule {

    @Singleton // repeated calls to this will return the same object
    @Provides
    fun provideTestString() = "This is a string we will inject"


    @Singleton
    @Provides
    @Named("dataStoreManager")
    fun provideDataStoreManager(@ApplicationContext appContext: Context): DataStoreManager =
        DataStoreManager(appContext)

    @Singleton
    @Provides
//    @Named("stateViewModel")
    fun provideStopwatchViewModel(@Named("dataStoreManager") dataStoreManager: DataStoreManager): StopwatchViewModel =
        StopwatchViewModel(dataStoreManager)

    @Singleton
    @Provides
    fun providePillPopperViewModel(
        @ApplicationContext appContext: Context,
        pillRepository: PillRepository
    ): PillViewModel =
        PillViewModel(pillRepository, appContext)

    @Singleton
    @Provides
    fun providePillDao(appDatabase: AppDatabase): PillDao {
        return appDatabase.pillDao()
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(appContext, AppDatabase::class.java, "stoner-stopwatch-db")
            .fallbackToDestructiveMigration()
            .build()

    /*
     dataStoreManager = DataStoreManager.getInstance(this@MainActivity)
    stateViewModel =
        StateViewModel(dataStoreManager = dataStoreManager) // to interact with MutableLiveData (now StateFlow)
     */
}