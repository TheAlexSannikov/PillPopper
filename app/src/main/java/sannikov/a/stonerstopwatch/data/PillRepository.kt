package sannikov.a.stonerstopwatch.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PillRepository @Inject constructor(
    private val pillDao : PillDao
){
    suspend fun popPill(pill : Pill) {
        pillDao.popPill(pill)
    }
}