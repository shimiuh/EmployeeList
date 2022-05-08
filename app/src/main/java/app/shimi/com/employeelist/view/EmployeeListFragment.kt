package app.shimi.com.employeelist.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.shimi.com.employeelist.R
import app.shimi.com.employeelist.data.model.Employee
import app.shimi.com.employeelist.view.viewmodel.EmployeeListViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.employee_dialog.view.*
import kotlinx.android.synthetic.main.employee_fragment_list.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class EmployeeListFragment : androidx.fragment.app.Fragment() {

    private val employeeListAdapter: EmployeeListAdapter by lazy { EmployeeListAdapter() }
    private val employeeViewModel: EmployeeListViewModel by activityViewModels()

    private lateinit var mItemAnimation: LayoutAnimationController
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.employee_fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            showEmployee(view.context, object: OnDone {
                override fun onDone(name: String, salary: String, age: String) {
                    swipeRefreshList.isRefreshing = true
                    lifecycleScope.launch{
                        employeeViewModel.createEmployee(name,salary,age)
                    }
                }
            }, null)
        }
        swipeRefreshList.setOnRefreshListener {
            loadEmployees()
        }
        initRecycler()
        initDataObserver()

    }


    private fun initDataObserver() {
        lifecycleScope.launchWhenResumed {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            //repeatOnLifecycle(Lifecycle.State.STARTED) {}
            employeeViewModel.employeeUiState.collect {
                // New value received
                when (it) {
                    is EmployeeListViewModel.LatestEmployeeUiState.Success -> updateData(it.employees)
                    is EmployeeListViewModel.LatestEmployeeUiState.Error -> showError(it.exception)
                    is EmployeeListViewModel.LatestEmployeeUiState.ActionSuccess -> {}
                }
            }
        }
    }

    private fun showError(exception: Throwable) {
        Log.d("TAG","in On Error ${exception.message}")

        swipeRefreshList.isRefreshing = false
        view?.let { Snackbar.make(it, "On Error ${exception.message}", Snackbar.LENGTH_LONG).show() }
    }


    private fun updateData(list: List<Employee>) {
        Log.d("TAG","in updateData list = ${list.size}")
        mItemAnimation.animation.reset()
        employeeList.layoutAnimation = mItemAnimation
        employeeListAdapter.setEmployeeList(list)
        employeeList.scheduleLayoutAnimation()
        swipeRefreshList.isRefreshing = false
        //view?.let {Snackbar.make(it, "Employee list updated", Snackbar.LENGTH_LONG).show() }
    }


    private fun initRecycler() {
        val resId = R.anim.layout_animation_fall_down
        mItemAnimation = AnimationUtils.loadLayoutAnimation(context, resId)
        employeeList.run {
            adapter = employeeListAdapter
            employeeListAdapter.setOnItemClickListener(object : EmployeeListAdapter.OnItemClickListener {
                override fun onClick(view: View, employee: Employee) {
                    (activity as MainActivity).showEmployee(employee)
                }
                override fun onRemoveItem(employee: Employee) {
                    swipeRefreshList.isRefreshing = true
                    deleteEmployee(employee)
                }
            })
        }
    }

    private fun deleteEmployee(employee: Employee) {
        lifecycleScope.launch {
            employeeViewModel.deleteEmployee(employee)
        }
    }

    private fun loadEmployees() {
        lifecycleScope.launch {
            employeeViewModel.loadEmployees()
        }
    }

    companion object {
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
                mDialogView.dialogName.setText(employee.employee_name.toString())
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

}
