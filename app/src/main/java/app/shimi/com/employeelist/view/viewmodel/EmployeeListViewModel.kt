package app.shimi.com.employeelist.view.viewmodel

import android.util.Log
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

    private var mCacheList: List<Employee> = emptyList()

    // Backing property to avoid state updates from other classes
    private val _uiState = MutableStateFlow<LatestEmployeeUiState>(LatestEmployeeUiState.Success(mCacheList))

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
        // Do an suspend operation to fetch Employees and post value.
        Log.d("TAG","in loadEmployees ${mCacheList.isEmpty()}")
        if(mCacheList.isEmpty()){
            //TODO: Implement logic when to get from DB or Api
            employeeRepo.getEmployeesFromApi().catch {  }.collect()
        }

        employeeRepo.getEmployees()
                .catch {
                    Log.d("TAG","in loadEmployees catch $it")
                    postNewState(LatestEmployeeUiState.Error(it)) }
                .collect {
                    if(it.isNotEmpty()){
                        Log.d("TAG","in loadEmployees collect $it")
                        mCacheList = it
                        postNewState(LatestEmployeeUiState.Success(mCacheList))
                    }
                }
    }

    suspend fun createEmployee(name: String, salary: String, age: String) {
        // Do an suspend operation to create Employee and post value.
        employeeRepo.createEmployee(name, salary, age).catch {
            postNewState(LatestEmployeeUiState.Error(it))
        }.collect{
            postNewState(LatestEmployeeUiState.ActionSuccess())
            Log.d("TAG","in deleteEmployee VM collect $it.")
        }

    }

    suspend fun editEmployee(employee: Employee) {
        // Do an suspend operation to edit Employee and post value.
        employeeRepo.editEmployee(employee).catch {
            postNewState(LatestEmployeeUiState.Error(it))
        }.collect{
            postNewState(LatestEmployeeUiState.ActionSuccess())
            Log.d("TAG","in deleteEmployee VM collect $it.")
        }
    }

    fun deleteEmployee(employee: Employee) {
        Log.d("TAG","in deleteEmployee VM $employee")
        // Do an suspend operation to remove Employee and post value.
        viewModelScope.launch {
            employeeRepo.deleteEmployee(employee).catch {
                Log.d("TAG","in deleteEmployee VM catch $it")
                postNewState(LatestEmployeeUiState.Error(it))
            }.collect{
                postNewState(LatestEmployeeUiState.ActionSuccess())
                postNewState(LatestEmployeeUiState.Success(mCacheList))
                Log.d("TAG","in deleteEmployee VM collect $it.")
            }
        }
    }

    fun openEmployeeDetails(employee: Employee) {
        postNewState(LatestEmployeeUiState.OpenEmployeeDetails(employee))
        postNewState(LatestEmployeeUiState.Success(mCacheList))
    }

    // Represents different states for the LatestEmployees screen
    sealed class LatestEmployeeUiState {
        class ActionSuccess : LatestEmployeeUiState()
        data class OpenEmployeeDetails(val employee: Employee): LatestEmployeeUiState()
        data class Success(val employees: List<Employee>): LatestEmployeeUiState()
        data class Error(val exception: Throwable): LatestEmployeeUiState()
    }
}

