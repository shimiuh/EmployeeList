package app.shimi.com.employeelist.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import app.shimi.com.employeelist.R
import app.shimi.com.employeelist.data.model.Employee
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Add employee list fragment if this is first creation
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container,EmployeeListFragment(), EmployeeListFragment.TAG).commit()
        }
    }

    /** Shows the employee detail fragment  */
    fun showEmployee(employee: Employee) {
        supportFragmentManager.beginTransaction().addToBackStack("employee").replace(
            R.id.fragment_container, EmployeeFragment.forEmployee(employee.id), null).commit()
    }
}
