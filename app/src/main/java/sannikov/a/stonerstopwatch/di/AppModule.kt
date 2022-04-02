package sannikov.a.stonerstopwatch.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import javax.inject.Named
import dagger.Provides


import android.content.Context
//import dagger.hilt.android.components.ActivityComponent
//import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import sannikov.a.stonerstopwatch.data.DataStoreManager
import sannikov.a.stonerstopwatch.viewmodels.StateViewModel

@Module
@InstallIn(SingletonComponent::class) // Installs FooModule in the generate SingletonComponent.
internal object AppModule {

    @Singleton // repeated calls to this will return the same object
    @Provides
    fun provideTestString() = "This is a string we will inject"


    @Singleton
    @Provides
    @Named("dataStoreManager")
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager = DataStoreManager(context)

    @Singleton
    @Provides
//    @Named("stateViewModel")
    fun provideStateViewModel(@Named("dataStoreManager") dataStoreManager : DataStoreManager): StateViewModel = StateViewModel(dataStoreManager)


    /*
     dataStoreManager = DataStoreManager.getInstance(this@MainActivity)
    stateViewModel =
        StateViewModel(dataStoreManager = dataStoreManager) // to interact with MutableLiveData (now StateFlow)
     */
}