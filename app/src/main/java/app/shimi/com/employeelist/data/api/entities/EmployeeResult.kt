package app.shimi.com.employeelist.data.api.entities

import app.shimi.com.employeelist.data.model.Employee
import com.google.gson.annotations.SerializedName

data class EmployeeRestObject (
    @SerializedName("status"  )
    var status  : String? = "",
    @SerializedName("data"    )
    var data : Data? = Data(),
    @SerializedName("message" )
    var message : String? = ""
) {
    fun isSuccess(): Boolean {
        return  this.status == "success"
    }
}

data class EmployeesRestObject (
    @SerializedName("status"  )
    var status  : String? = "",
    @SerializedName("data"    )
    var data : ArrayList<Data> = arrayListOf(),
    @SerializedName("message" )
    var message : String? = ""
){
    fun isSuccess(): Boolean {
        return  this.status == "success"
    }
}

data class Data (

    @SerializedName("id")
    var id : String? = "",
    @SerializedName("employee_name")
    var employeeName : String? = "",
    @SerializedName("employee_salary")
    var employeeSalary : String? = "",
    @SerializedName("employee_age")
    var employeeAge : String? = "",
    @SerializedName("profile_image")
    var profileImage : String? = ""
){
    fun toEmployee() : Employee{
        return Employee(
            id = this.id!!.toIntOrNull() ?: 0,
            employee_name = this.employeeName!!,
            employee_age = this.employeeAge!!.toIntOrNull() ?: 0,
            employee_salary = this.employeeSalary!!.toIntOrNull() ?: 0
        )
    }
}
