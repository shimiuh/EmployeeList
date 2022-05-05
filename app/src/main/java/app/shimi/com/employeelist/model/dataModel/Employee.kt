package app.shimi.com.employeelist.model.dataModel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import android.widget.TextView
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.BindingAdapter



@Entity(tableName = "employees")
data class Employee(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "employee_name")
    val employee_name: String,
    @ColumnInfo(name = "employee_age")
    val employee_age: Int,
    @ColumnInfo(name = "employee_salary")
    val employee_salary: Int)

@BindingAdapter("android:text")
fun setText(view: TextView, value: Int) {
    view.text = value.toString()
}

@InverseBindingAdapter(attribute = "android:text")
fun getText(view: TextView): Int {
    return view.text.toString().toIntOrNull() ?: 0
}
