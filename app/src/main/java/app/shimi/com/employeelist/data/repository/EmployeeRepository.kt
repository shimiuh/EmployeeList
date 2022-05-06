package app.shimi.com.employeelist.data.repository

import android.util.Log
import app.shimi.com.employeelist.data.api.endpoints.EmployeeApi
import app.shimi.com.employeelist.data.persistence.db.EmployeeDao
import app.shimi.com.employeelist.data.api.entities.EmployeeCall
import app.shimi.com.employeelist.data.model.Employee
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.*


class EmployeeRepository(private val employeeApi: EmployeeApi, val employeeDao: EmployeeDao) {

    suspend fun getEmployees(): Flow<List<Employee>> {
        return merge(getEmployeesFromDb(), getEmployeesFromApi())
    }

    suspend fun createEmployee(name: String, salary: String, age: String){
        return createEmployeeOnRemoteApi(name, salary, age)
    }

    suspend fun editEmployee(employee: Employee): Flow<EmployeeCall> {
        return employeeApi.updateEmployee(employee.id, employee)
    }

    suspend fun deleteEmployee(employee: Employee): Flow<JsonObject> {
        return employeeApi.deleteEmployee(employee.id)
    }

    private suspend fun getEmployeesFromDb(): Flow<List<Employee>> {
        return employeeDao.getEmployees()
    }

    private suspend fun createEmployeeOnRemoteApi(name: String, salary: String, age: String) {
        employeeApi.createEmployee(EmployeeCall(name, salary, age)).let {
            if(it.isSuccess()){
                Log.d("TAG","in createEmployeeOnRemoteApi ${it.data} ${it.data!!.toEmployee()}")
                employeeDao.insert(it.data!!.toEmployee())
            }
        }
    }

    private suspend fun getEmployeesFromApi(): Flow<List<Employee>> {
        return flow {
            employeeApi.getEmployees().let {
                var res = listOf<Employee>()
                if(it.isSuccess()){
                    res = it.data.map {employee -> employee.toEmployee()}
                    storeEmployeesInDb(it.data.map {employee -> employee.toEmployee()})
                }
                emit(res)
            }
        }

    }

    private suspend fun storeEmployeesInDb(employees: List<Employee>) {
        employeeDao.insertAll(employees)
    }

}
