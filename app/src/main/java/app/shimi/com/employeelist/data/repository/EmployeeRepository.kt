package app.shimi.com.employeelist.data.repository

import android.util.Log
import app.shimi.com.employeelist.data.api.endpoints.EmployeeApi
import app.shimi.com.employeelist.data.persistence.db.EmployeeDao
import app.shimi.com.employeelist.data.api.entities.EmployeeCall
import app.shimi.com.employeelist.data.api.entities.EmployeeRestObject
import app.shimi.com.employeelist.data.api.entities.EmployeesRestObject
import app.shimi.com.employeelist.data.api.entities.RestObject
import app.shimi.com.employeelist.data.model.Employee
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import java.lang.Exception


class EmployeeRepository(private val employeeApi: EmployeeApi, val employeeDao: EmployeeDao) {

    suspend fun getEmployees(): Flow<List<Employee>> {
        Log.d("TAG","in getEmployees")
        return getEmployeesFromDb()
    }

    suspend fun editEmployee(employee: Employee): Flow<RestObject> {
        return flow{
            val res = employeeApi.updateEmployee(employee.id)
            Log.d("TAG","in editEmployee res =  ${res.isSuccess()}")
            if(res.isSuccess()){
                employeeDao.update(employee)
            }
            emit(res)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun createEmployee(name: String, salary: String, age: String) : Flow<EmployeeRestObject> {
        Log.d("TAG","in createEmployee")
        return flow{
            val res = employeeApi.createEmployee(EmployeeCall(name, salary, age))
            Log.d("TAG","in createEmployee isSuccess =  ${res.isSuccess()} res = $res")
            if(res.isSuccess()){
                employeeDao.insert(res.data!!.toEmployee())
            }
            emit(res)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun deleteEmployee(employee: Employee): Flow<RestObject> {
        return flow {
            val res = employeeApi.deleteEmployee(employee.id)
            Log.d("TAG","in deleteEmployee res =  ${res.isSuccess()} $employee")
            if(res.isSuccess()){
                employeeDao.delete(employee)
            }
            emit(res)
        }.flowOn(Dispatchers.IO)
    }

    private fun getEmployeesFromDb(): Flow<List<Employee>> {
        return employeeDao.getEmployees()
    }

    suspend fun getEmployeesFromApi():  Flow<List<Employee>> {
        return flow {
            val res = employeeApi.getEmployees()
            Log.d("TAG","in getEmployeesFromApi res =  ${res.isSuccess()}")
            if(res.isSuccess()){
                storeEmployeesInDb(res.toEmployeeList())
            }
            emit(res.toEmployeeList())
        }.flowOn(Dispatchers.IO)
    }

    private suspend fun storeEmployeesInDb(employees: List<Employee>) {
        Log.d("TAG","in storeEmployeesInDb $employees")
        employeeDao.insertAll(employees)
    }

}
