package app.shimi.com.employeelist.data.persistence.db

import androidx.room.Database
import androidx.room.RoomDatabase
import app.shimi.com.employeelist.data.model.Employee


@Database(entities = [Employee::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao
}