package app.shimi.com.employeelist.data.api.endpoints

import app.shimi.com.employeelist.data.api.entities.EmployeeCall
import app.shimi.com.employeelist.data.api.entities.EmployeeRestObject
import app.shimi.com.employeelist.data.api.entities.EmployeesRestObject
import app.shimi.com.employeelist.data.api.entities.RestObject
import app.shimi.com.employeelist.data.model.Employee
import kotlinx.coroutines.flow.Flow
import retrofit2.http.*

interface EmployeeApi {

    @GET("employees")
    suspend fun getEmployees(): EmployeesRestObject

    @GET("employee/{id}")
    suspend fun getEmployee(@Path("id") EmployeeId: Int) : Employee

    @POST("create")
    suspend fun createEmployee(@Body employee: EmployeeCall) : EmployeeRestObject

    @PUT("update/{id}")
    suspend fun updateEmployee(@Path("id") EmployeeId: Int) : RestObject

    @DELETE("delete/{id}")
    suspend fun deleteEmployee(@Path("id") EmployeeId: Int) : RestObject
}
