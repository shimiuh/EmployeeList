package app.shimi.com.employeelist.ui.view

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import app.shimi.com.employeelist.R
import app.shimi.com.employeelist.data.model.Employee
import kotlinx.android.synthetic.main.employee_dialog.view.*

object CreateEmployeeDialog {
    const val TAG: String = "EmployeeListFragment"
    interface OnDone {
        fun onDone(name: String, salary: String,age: String)
    }
    fun showEmployee(context: Context, onDone: OnDone, employee: Employee?){

        val mDialogView = LayoutInflater.from(context).inflate(R.layout.employee_dialog, null)
        //AlertDialogBuilder
        val title = if (employee != null) "Edit Employee" else "Create Employee"
        val buttonTextId = if (employee != null) R.string.update else R.string.create
        if(employee != null){
            mDialogView.dialogName.setText(employee.employee_name)
            mDialogView.dialogSalary.setText(employee.employee_salary.toString())
            mDialogView.dialogAge.setText(employee.employee_age.toString())
        }
        AlertDialog.Builder(context)
            .setView(mDialogView)
            .setTitle(title)
            .setPositiveButton(buttonTextId) { dialog, which ->
                dialog.dismiss()
                val name = mDialogView.dialogName.text.toString()
                val salary = mDialogView.dialogSalary.text.toString()
                val age = mDialogView.dialogAge.text.toString()
                onDone.onDone(name, salary, age)
            }
            .setNegativeButton(android.R.string.cancel,null).show()
    }
}