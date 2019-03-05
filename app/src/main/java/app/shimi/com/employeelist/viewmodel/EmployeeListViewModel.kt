package app.shimi.com.employeelist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.shimi.com.employeelist.App
import app.shimi.com.employeelist.model.dataModel.Employee
import io.reactivex.schedulers.Schedulers




class EmployeeListViewModel : ViewModel() {

    private var employees: MutableLiveData<List<Employee>>? = null
    fun getEmployeeList(): LiveData<List<Employee>> {
        if (employees == null) {
            employees = MutableLiveData()
            loadEmployees()
        }
        return employees as MutableLiveData<List<Employee>>
    }

    fun loadEmployees() {
        // Do an asynchronous operation to fetch Employees and post value.
        //should load from web and db with call getEmployees but api dos not support new Employees list
        App.getRepository().getEmployeesFromDb().subscribeOn(Schedulers.io()).subscribe {
            employees?.postValue(it)
        }

    }

    fun createEmployee(name: String, salary: String, age: String) {
        // Do an asynchronous operation to create Employee and post value.
        App.getRepository().createEmployee(name, salary, age).subscribeOn(Schedulers.io()).subscribe {
            loadEmployees()
        }
    }

    fun editEmployee(employee: Employee) {
        // Do an asynchronous operation to edit Employee and post value.
        App.getRepository().editEmployee(employee).subscribeOn(Schedulers.io()).subscribe {
            loadEmployees()
        }

    }

    fun deleteEmployee(employee: Employee) {
        // Do an asynchronous operation to remove Employee and post value.
        App.getRepository().deleteEmployee(employee).subscribeOn(Schedulers.io()).subscribe {
            loadEmployees()
        }
    }
}

