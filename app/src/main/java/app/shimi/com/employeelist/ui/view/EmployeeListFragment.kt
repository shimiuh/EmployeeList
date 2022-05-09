package app.shimi.com.employeelist.ui.view

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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import app.shimi.com.employeelist.R
import app.shimi.com.employeelist.data.model.Employee
import app.shimi.com.employeelist.databinding.EmployeeFragmentListBinding
import app.shimi.com.employeelist.ui.viewmodel.EmployeeListViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.employee_dialog.view.*
import kotlinx.android.synthetic.main.employee_fragment_list.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class EmployeeListFragment : androidx.fragment.app.Fragment() {


    private val employeeViewModel: EmployeeListViewModel by activityViewModels()
    private val employeeListAdapter: EmployeeListAdapter by lazy { EmployeeListAdapter(listOf(),employeeViewModel) }

    private lateinit var mItemAnimation: LayoutAnimationController
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val dataBinding = EmployeeFragmentListBinding.inflate(inflater, container, false).apply {
            this.viewModel = employeeViewModel
            this.adapter = employeeListAdapter
        }
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        initRecycler()
        initDataObserver()

    }


    private fun initDataObserver() {

        lifecycleScope.launch {
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                employeeViewModel.employeeUiState.collect {
                    when (it) {
                        is EmployeeListViewModel.EmployeeUiState.Success -> updateData(it.employees)
                        is EmployeeListViewModel.EmployeeUiState.Error -> showError(it.exception)
                    }
                }
            }
        }

        employeeViewModel.openEmployeeDetailsEvent.observe(viewLifecycleOwner) { employee ->
            employee?.let {
                //DODO: use NavController
                (activity as MainActivity).showEmployee(it)
                //findNavController().navigate(R.id.fragment_container)
            }
        }
    }

    private fun showError(exception: Throwable) {
        Log.d("TAG","in On Error ${exception.message}")
        view?.let { Snackbar.make(it, "On Error ${exception.message}", Snackbar.LENGTH_LONG).show() }
    }


    private fun updateData(list: List<Employee>) {
        Log.d("TAG","in updateData list = ${list.size}")
        mItemAnimation.animation.reset()
        employeeList.layoutAnimation = mItemAnimation
        employeeListAdapter.setEmployeeList(list)
        employeeList.scheduleLayoutAnimation()
    }


    private fun initRecycler() {
        val resId = R.anim.layout_animation_fall_down
        mItemAnimation = AnimationUtils.loadLayoutAnimation(context, resId)
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
        val TAG = "EmployeeListFragment"
    }
}
