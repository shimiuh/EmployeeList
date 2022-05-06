package app.shimi.com.employeelist.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.shimi.com.employeelist.data.model.Employee
import app.shimi.com.employeelist.data.repository.EmployeeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EmployeeListViewModel @Inject constructor(
    private val employeeRepo: EmployeeRepository) : ViewModel() {

    // Backing property to avoid state updates from other classes
    private val _uiState = MutableStateFlow<LatestEmployeeUiState>(LatestEmployeeUiState.Success(emptyList()))

    // The UI collects from this StateFlow to get its state updates
    val employeeUiState: StateFlow<LatestEmployeeUiState> = _uiState

    private fun postNewState(newState: LatestEmployeeUiState) {
        _uiState.value = newState
    }
    private fun getState() = _uiState.value

    init {
        viewModelScope.launch {
            loadEmployees()
        }
    }

    suspend fun loadEmployees() {
        // Do an asynchronous operation to fetch Employees and post value.
            employeeRepo.getEmployees()
                .catch { postNewState(LatestEmployeeUiState.Error(it)) }
                .collect {
                    postNewState(LatestEmployeeUiState.Success(it)) }
    }

    suspend fun createEmployee(name: String, salary: String, age: String) {
        // Do an asynchronous operation to create Employee and post value.
        employeeRepo.createEmployee(name, salary, age)
        loadEmployees()
        //Log.d("TAG", "onError ${e.localizedMessage}"
    }

    suspend fun editEmployee(employee: Employee) {
        // Do an asynchronous operation to edit Employee and post value.
        employeeRepo.editEmployee(employee)
        loadEmployees()
    }

    suspend fun deleteEmployee(employee: Employee) {
        // Do an asynchronous operation to remove Employee and post value.
        employeeRepo.deleteEmployee(employee)
        loadEmployees()
    }

    // Represents different states for the LatestEmployees screen
    sealed class LatestEmployeeUiState {
        data class Success(val employees: List<Employee>): LatestEmployeeUiState()
        data class Error(val exception: Throwable): LatestEmployeeUiState()
    }
}

