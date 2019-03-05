package app.shimi.com.employeelist.repository

import app.shimi.com.employeelist.model.dataModel.Employee
import app.shimi.com.employeelist.model.networkModel.EmployeeCall
import app.shimi.com.employeelist.model.networkModel.EmployeeResult
import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.http.*

interface EmployeeApi {

    @GET("employees")
    fun getEmployees()
            : Observable<List<Employee>>

    @GET("employee/{id}")
    fun getEmployee(@Path("id") EmployeeId: Int)
            : Observable<Employee>

    @POST("create")
    fun createEmployee(@Body employee: EmployeeCall)
            : Observable<EmployeeResult>

    @PUT("update/{id}")
    fun updateEmployee(@Path("id") EmployeeId: Int, @Body employee: Employee)
            : Observable<EmployeeCall>

    @DELETE("delete/{id}")
    fun deleteEmployee(@Path("id") EmployeeId: Int)
            : Observable<JsonObject>
}
