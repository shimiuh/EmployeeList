package app.shimi.com.employeelist.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import app.shimi.com.employeelist.App
import app.shimi.com.employeelist.R
import app.shimi.com.employeelist.model.dataModel.Employee
import app.shimi.com.employeelist.viewmodel.EmployeeListViewModel
import kotlinx.android.synthetic.main.employee_fragment.*
import kotlinx.android.synthetic.main.item_employee.*

class EmployeeFragment : androidx.fragment.app.Fragment() {


    private lateinit var employeeViewModel: EmployeeListViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.employee_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            EmployeeListFragment.showEmployee(view.context, object : EmployeeListFragment.Companion.OnDone {
                override fun onDone(name: String, salary: String, age: String) {
                    mEmployee?.let { employeeViewModel.editEmployee(Employee(it.id, name, age.toInt(), salary.toInt())) }
                }
            }, mEmployee)
        }
        deleteItem.visibility = View.GONE
        initObserver()
        extractIdAndUpdateUi()

    }

    private fun initObserver() {
        employeeViewModel = activity?.let { ViewModelProviders.of(it).get(EmployeeListViewModel::class.java) }!!
        employeeViewModel.getEmployeeList().observe(this, Observer {
            extractIdAndUpdateUi()
        })
    }

    private var mEmployee: Employee? = null

    private fun extractIdAndUpdateUi() {
        val bundle = arguments
        if (bundle != null && bundle.containsKey(KEY_EMPLOYEE_ID)) {
            val id = bundle.getInt(KEY_EMPLOYEE_ID)
            App.getBackgroundHandler().post {
                mEmployee = App.getEmployeeDao().getEmployee(id)
                updateUi()
            }

        }
    }
    private fun updateUi() {
        activity?.runOnUiThread({
            employee_name.text   = mEmployee?.employee_name
            employee_salary.text = mEmployee?.employee_salary.toString()
            employee_age.text    = mEmployee?.employee_age.toString()
        })
    }


    companion object {

        private val KEY_EMPLOYEE_ID = "employee_id"

        /** Creates employee fragment for specific Employee ID  */
        fun forEmployee(employeeId: Int): EmployeeFragment {
            val fragment = EmployeeFragment()
            val args = Bundle()
            args.putInt(KEY_EMPLOYEE_ID, employeeId)
            fragment.setArguments(args)
            return fragment
        }
    }

}
