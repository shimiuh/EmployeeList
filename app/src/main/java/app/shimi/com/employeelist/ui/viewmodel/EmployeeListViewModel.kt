package app.shimi.com.employeelist.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.shimi.com.employeelist.data.model.Employee
import app.shimi.com.employeelist.data.repository.EmployeeRepository
import app.shimi.com.employeelist.ui.view.CreateEmployeeDialog
import app.shimi.com.employeelist.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EmployeeListViewModel @Inject constructor(
    private val employeeRepo: EmployeeRepository) : ViewModel() {

    private val openEmployeeDetailsEvent = SingleLiveEvent<Employee>()
    val isRefreshing = ObservableBoolean(false)

    // Backing property to avoid state updates from other classes
    private val _uiState = MutableStateFlow<EmployeeUiState>(EmployeeUiState.Success(emptyList()))

    // The UI collects from this StateFlow to get its state updates
    val employeeUiState: StateFlow<EmployeeUiState> = _uiState

    private fun postNewState(newState: EmployeeUiState) {
        _uiState.value = newState
    }
    private fun getState() = _uiState.value

    init {
        loadEmployees()
    }

    fun loadEmployees() {
        viewModelScope.launch {
            // Do an suspend operation to fetch Employees and post value.
            Log.d("TAG","in loadEmployees")
            employeeRepo.getEmployeesFromApi().catch{  }.collect()
            employeeRepo.getEmployees()
                .onStart {
                    Log.d("TAG","in getEmployees onStart")
                    isRefreshing.set(true) }
                .catch {
                    Log.d("TAG","in loadEmployees catch $it")
                    postNewState(EmployeeUiState.Error(it)) }
                .collect {
                    if(it.isNotEmpty()){
                        Log.d("TAG","in loadEmployees collect $it")
                        postNewState(EmployeeUiState.Success(it))
                    }
                    isRefreshing.set(false)
                }
        }
    }

    suspend fun createEmployee(name: String, salary: String, age: String) {
        // Do an suspend operation to create Employee and post value.
        employeeRepo.createEmployee(name, salary, age)
            .onStart { isRefreshing.set(true) }
            .catch { postNewState(EmployeeUiState.Error(it)) }
            .collect{
            isRefreshing.set(false)
            Log.d("TAG","in createEmployee VM collect $it.")
        }

    }

    suspend fun editEmployee(employee: Employee) {
        // Do an suspend operation to edit Employee and post value.
        employeeRepo.editEmployee(employee)
            .onStart { isRefreshing.set(true) }
            .catch {
            postNewState(EmployeeUiState.Error(it))
        }.collect{
            isRefreshing.set(false)
            Log.d("TAG","in deleteEmployee VM collect $it.")
        }
    }

    fun deleteEmployee(employee: Employee) {
        Log.d("TAG","in deleteEmployee VM $employee")
        // Do an suspend operation to remove Employee and post value.
        viewModelScope.launch {
            employeeRepo.deleteEmployee(employee)
                .onStart { isRefreshing.set(true) }
                .catch {
                    Log.d("TAG","in deleteEmployee VM catch $it")
                    postNewState(EmployeeUiState.Error(it))
                }.collect{
                    isRefreshing.set(false)
                    Log.d("TAG","in deleteEmployee VM collect $it.")
                }
        }
    }

    fun openEmployeeDetails(employee: Employee) {
        openEmployeeDetailsEvent.value = employee
    }

    fun createEmployeeDialog(context: Context) {
        CreateEmployeeDialog.showEmployee(context, object: CreateEmployeeDialog.OnDone {
            override fun onDone(name: String, salary: String, age: String) {
                viewModelScope.launch{
                    createEmployee(name,salary,age)
                }
            }
        }, null)
    }

    // Represents different states for the Employees screen
    sealed class EmployeeUiState {
        data class Success(val employees: List<Employee>): EmployeeUiState()
        data class Error(val exception: Throwable): EmployeeUiState()
    }
}

