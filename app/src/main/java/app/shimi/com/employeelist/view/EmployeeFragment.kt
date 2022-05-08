package app.shimi.com.employeelist.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.shimi.com.employeelist.R
import app.shimi.com.employeelist.data.model.Employee
import app.shimi.com.employeelist.data.persistence.db.EmployeeDao
import app.shimi.com.employeelist.view.viewmodel.EmployeeListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.employee_fragment.*
import kotlinx.android.synthetic.main.item_employee.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EmployeeFragment : Fragment() {

    @Inject lateinit var employeeDao: EmployeeDao
    private val employeeViewModel: EmployeeListViewModel by activityViewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.employee_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            EmployeeListFragment.showEmployee(view.context, object : EmployeeListFragment.Companion.OnDone {
                override fun onDone(name: String, salary: String, age: String) {
                    mEmployee?.let { editEmployee(it.id, name, age.toInt(), salary.toInt()) }
                }
            }, mEmployee)
        }
        deleteItem.visibility = View.GONE
        initObserver()
        extractIdAndUpdateUi()
    }

    private fun initObserver() {
        lifecycleScope.launchWhenResumed {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            //repeatOnLifecycle(Lifecycle.State.STARTED) {}
            employeeViewModel.employeeUiState.collect {
                // New value received
                when (it) {
                    is EmployeeListViewModel.LatestEmployeeUiState.Success -> extractIdAndUpdateUi()
                    is EmployeeListViewModel.LatestEmployeeUiState.Error -> showError(it.exception)
                }
            }


        }
    }

    private fun showError(exception: Throwable) {

    }

    fun editEmployee(id: Int, name: String, age: Int, salary: Int) {
        lifecycleScope.launch {
            employeeViewModel.editEmployee(Employee(id, name, age, salary))
        }
    }

    private var mEmployee: Employee? = null

    private fun extractIdAndUpdateUi() {
        val bundle = arguments
        if (bundle != null && bundle.containsKey(KEY_EMPLOYEE_ID)) {
            val id = bundle.getInt(KEY_EMPLOYEE_ID)
            lifecycleScope.launch {
                mEmployee = employeeDao.getEmployee(id)
                updateUi()
            }
        }
    }
    private fun updateUi() {
        employee_name.text = mEmployee?.employee_name
        employee_salary.text = mEmployee?.employee_salary.toString()
        employee_age.text = mEmployee?.employee_age.toString()
        activity?.runOnUiThread {

        }
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
