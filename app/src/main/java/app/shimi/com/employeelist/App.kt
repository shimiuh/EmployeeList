package app.shimi.com.employeelist

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

//    companion object {
////        private lateinit var sRetrofit: Retrofit
////        private lateinit var sEmployeeApi: EmployeeApi
////        private lateinit var sEmployeeRepository: EmployeeRepository
////        private lateinit var sAppDatabase: AppDatabase
////        private lateinit var sBackgroundHandler : Handler
////
////        fun getEmployeeApi()       = sEmployeeApi
////        fun getEmployeeDao()       = sAppDatabase.employeeDao()
////        fun getRepository()        = sEmployeeRepository
////        fun getBackgroundHandler() = sBackgroundHandler
//    }
//


//    override fun onCreate() {
//        super.onCreate()
//        setBackgroundHandler()
////        sRetrofit = Retrofit.Builder()
////                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
////                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
////                .baseUrl("https://dummy.restapiexample.com/api/v1/")
////                .build()
////
////        sEmployeeApi = sRetrofit.create(EmployeeApi::class.java)
////        sAppDatabase = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "mvvm-employee-database").build()
////        sEmployeeRepository = EmployeeRepository(sEmployeeApi, sAppDatabase.employeeDao())
//    }
//
//    private fun setBackgroundHandler() {
//        val thread = HandlerThread("background-thread", Process.THREAD_PRIORITY_BACKGROUND)
//        thread.start()
//        sBackgroundHandler = Handler(thread.looper)
//    }

}
