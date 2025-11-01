package app.grocery.list.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InitializeData @Inject internal constructor(
    private val appRepository: AppRepositoryImpl,
) {
    suspend fun execute() {
        appRepository.runMigrations()
    }
}
