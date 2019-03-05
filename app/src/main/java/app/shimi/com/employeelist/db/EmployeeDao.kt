package app.shimi.com.employeelist.db


import androidx.room.*
import app.shimi.com.employeelist.model.dataModel.Employee
import io.reactivex.Single

@Dao
interface EmployeeDao {

    @Query("SELECT * FROM employees")
    fun getEmployees(): Single<List<Employee>>

    @Query("SELECT * FROM employees WHERE id = :id")
    fun getEmployee(id: Int): Employee

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(employee: Employee)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(employees: List<Employee>)

    @Update
    fun update(employee: Employee)

    @Delete
    fun delete(employee: Employee)


}