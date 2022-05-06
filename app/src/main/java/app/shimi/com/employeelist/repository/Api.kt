package app.shimi.com.employeelist.repository

import app.shimi.com.employeelist.model.dataModel.Employee
import app.shimi.com.employeelist.model.networkModel.EmployeeCall
import app.shimi.com.employeelist.model.networkModel.EmployeeRestObject
import app.shimi.com.employeelist.model.networkModel.EmployeesRestObject
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import retrofit2.http.*

interface EmployeeApi {

    @GET("employees")
    suspend fun getEmployees(): EmployeesRestObject

    @GET("employee/{id}")
    suspend fun getEmployee(@Path("id") EmployeeId: Int) : Flow<Employee>

    @POST("create")
    suspend fun createEmployee(@Body employee: EmployeeCall) : EmployeeRestObject

    @PUT("update/{id}")
    suspend fun updateEmployee(@Path("id") EmployeeId: Int, @Body employee: Employee) : Flow<EmployeeCall>

    @DELETE("delete/{id}")
    suspend fun deleteEmployee(@Path("id") EmployeeId: Int) : Flow<JsonObject>
}
