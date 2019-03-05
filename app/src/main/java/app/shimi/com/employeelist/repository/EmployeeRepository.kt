package app.shimi.com.employeelist.repository

import android.util.Log
import app.shimi.com.employeelist.model.dataModel.Employee
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import app.shimi.com.employeelist.db.EmployeeDao
import app.shimi.com.employeelist.model.networkModel.EmployeeCall
import app.shimi.com.employeelist.model.networkModel.EmployeeResult
import com.google.gson.JsonObject

class EmployeeRepository(val employeeApi: EmployeeApi, val employeeDao: EmployeeDao) {

    fun getEmployees(): Observable<List<Employee>> {
        return Observable.concatArray(
                getEmployeesFromDb(),
                getEmployeesFromApi())
    }

    fun createEmployee(name: String, salary: String, age: String): Observable<EmployeeResult> {
        return createEmployeeFromApi(name, salary, age)
    }

    fun editEmployee(employee: Employee): Observable<EmployeeCall> {
        return employeeApi.updateEmployee(employee.id, employee)
            .doOnNext {
                employeeDao.update(employee)
            }
    }

    fun deleteEmployee(employee: Employee): Observable<JsonObject> {
        return employeeApi.deleteEmployee(employee.id)
            .doOnNext {
                employeeDao.delete(employee)
            }
    }


    fun getEmployeesFromDb(): Observable<List<Employee>> {
        return employeeDao.getEmployees()
                .toObservable()
                .doOnNext {
                }
    }

    fun createEmployeeFromApi(name: String, salary: String, age: String): Observable<EmployeeResult> {
        return employeeApi.createEmployee(EmployeeCall(name, salary, age))
            .doOnNext {
                employeeDao.insert(Employee(it.id, it.name, it.age, it.salary))
            }
    }

    fun getEmployeesFromApi(): Observable<List<Employee>> {
        return employeeApi.getEmployees()
            .doOnNext {
                storeEmployeesInDb(it)
            }.doOnError{

            }
    }

    fun storeEmployeesInDb(employees: List<Employee>) {
        Observable.fromCallable { employeeDao.insertAll(employees) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe {
                }
    }

}
