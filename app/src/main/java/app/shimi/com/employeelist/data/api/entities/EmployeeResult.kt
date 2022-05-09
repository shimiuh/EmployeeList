package app.shimi.com.employeelist.data.api.entities

import app.shimi.com.employeelist.data.model.Employee
import com.google.gson.annotations.SerializedName



data class RestObject (
    @SerializedName("status"  )
    var status  : String? = "",
    @SerializedName("message" )
    var message : String? = ""
) { fun isSuccess() = status.isSuccess() }

data class EmployeesRestObject (
    @SerializedName("status"  )
    var status  : String? = "",
    @SerializedName("data"    )
    var data : ArrayList<Data> = arrayListOf(),
    @SerializedName("message" )
    var message : String? = ""
) { fun isSuccess() = status.isSuccess()
    fun toEmployeeList() = data.map{it.toEmployee()} }

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

data class EmployeeRestObject (
    @SerializedName("status"  )
    var status  : String? = "",
    @SerializedName("data"    )
    var data : EmployeeData? = EmployeeData(),
    @SerializedName("message" )
    var message : String? = ""
) { fun isSuccess() = status.isSuccess() }

data class EmployeeData (

    @SerializedName("id")
    var id : String? = "",
    @SerializedName("name")
    var employeeName : String? = "",
    @SerializedName("salary")
    var employeeSalary : String? = "",
    @SerializedName("age")
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

private fun String?.isSuccess():Boolean {
    return this == "success"
}
