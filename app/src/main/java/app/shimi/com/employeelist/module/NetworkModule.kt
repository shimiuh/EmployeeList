package app.shimi.com.employeelist.module

import android.content.Context
import androidx.room.Room
import app.shimi.com.employeelist.data.persistence.db.AppDatabase
import app.shimi.com.employeelist.data.persistence.db.EmployeeDao
import app.shimi.com.employeelist.data.api.endpoints.EmployeeApi
import app.shimi.com.employeelist.data.repository.EmployeeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

class NetworkModule {

    @InstallIn(SingletonComponent::class)
    @Module
    class AppDatabaseModule {
        @Provides
        fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
            return Room.databaseBuilder(appContext, AppDatabase::class.java, "mvvm-employee-database").build()
        }
    }

    @InstallIn(SingletonComponent::class)
    @Module
    class DatabaseModule {
        @Provides
        fun provideChannelDao(appDatabase: AppDatabase): EmployeeDao {
            return appDatabase.employeeDao()
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object ApiModule {
        private const val BASE_URL = "https://dummy.restapiexample.com/api/v1/"

        @Singleton
        @Provides
        fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
            .apply {
                level = HttpLoggingInterceptor.Level.BODY
        }

        @Singleton
        @Provides
        fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
            OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build()

        @Singleton
        @Provides
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()

        @Singleton
        @Provides
        fun provideApiService(retrofit: Retrofit): EmployeeApi = retrofit.create(EmployeeApi::class.java)

        @Singleton
        @Provides
        fun providesRepository(apiService: EmployeeApi, employeeDao: EmployeeDao) = EmployeeRepository(apiService,employeeDao)
    }

}