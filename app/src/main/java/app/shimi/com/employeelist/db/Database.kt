package app.shimi.com.employeelist.db

import androidx.room.Database
import androidx.room.RoomDatabase
import app.shimi.com.employeelist.model.dataModel.Employee


@Database(entities = [Employee::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao
}