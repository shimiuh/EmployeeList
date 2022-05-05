package app.shimi.com.employeelist.view

import android.content.Context
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.appcompat.app.AlertDialog
import app.shimi.com.employeelist.R
import app.shimi.com.employeelist.model.dataModel.Employee
import app.shimi.com.employeelist.viewmodel.EmployeeListViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.employee_dialog.view.*
import kotlinx.android.synthetic.main.employee_fragment_list.*



class EmployeeListFragment : androidx.fragment.app.Fragment() {

    private val employeeListAdapter: EmployeeListAdapter by lazy { EmployeeListAdapter() }
    private lateinit var employeeViewModel: EmployeeListViewModel
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
                    employeeViewModel.createEmployee(name,salary,age)
                }
            }, null)
        }
        initObserver()
        initRecycler()
    }

    private fun initObserver() {
        employeeViewModel = activity?.let { ViewModelProviders.of(it).get(EmployeeListViewModel::class.java) }!!
        employeeViewModel.getEmployeeList().observe(viewLifecycleOwner, Observer {
            updateData(it)
        })
    }


    private fun updateData(list: List<Employee>) {
        mItemAnimation.animation.reset()
        employee_list.layoutAnimation = mItemAnimation
        employeeListAdapter.setEmployeeList(list)
        employee_list.scheduleLayoutAnimation()
        swipeRefreshList.isRefreshing = false
        view?.let { it1 -> Snackbar.make(it1, "Employee list updated", Snackbar.LENGTH_LONG).show() }
    }


    private fun initRecycler() {
        val resId = R.anim.layout_animation_fall_down
        mItemAnimation = AnimationUtils.loadLayoutAnimation(context, resId)
        employee_list.run {
            adapter = employeeListAdapter
            employeeListAdapter.setOnItemClickListener(object : EmployeeListAdapter.OnItemClickListener {
                override fun onClick(view: View, employee: Employee) {
                    (activity as MainActivity).showEmployee(employee)
                }
                override fun onRemoveItem(employee: Employee) {
                    swipeRefreshList.isRefreshing = true
                    employeeViewModel.deleteEmployee(employee)
                }
            })
        }
        swipeRefreshList.run {
            setOnRefreshListener { employeeViewModel.loadEmployees()}
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
