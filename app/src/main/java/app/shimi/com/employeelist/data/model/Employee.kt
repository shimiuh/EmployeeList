package app.shimi.com.employeelist.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import android.widget.TextView
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.BindingAdapter


@Entity(tableName = "Employees")
data class Employee(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val employee_name: String,
    @ColumnInfo(name = "age")
    val employee_age: Int,
    @ColumnInfo(name = "salary")
    val employee_salary: Int)

