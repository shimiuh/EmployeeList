package app.shimi.com.employeelist.module

import android.content.Context
import androidx.room.Room
import app.shimi.com.employeelist.data.api.endpoints.EmployeeApi
import app.shimi.com.employeelist.data.persistence.db.AppDatabase
import app.shimi.com.employeelist.data.persistence.db.EmployeeDao
import app.shimi.com.employeelist.data.repository.EmployeeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(appContext, AppDatabase::class.java, "mvvm-employee-database").build()
    }

    @Provides
    fun provideChannelDao(appDatabase: AppDatabase): EmployeeDao {
        return appDatabase.employeeDao()
    }

}



