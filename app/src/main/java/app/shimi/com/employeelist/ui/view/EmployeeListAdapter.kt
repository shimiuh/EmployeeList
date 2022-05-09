package app.shimi.com.employeelist.ui.view

import android.util.Log
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.shimi.com.employeelist.R
import app.shimi.com.employeelist.data.model.Employee
import app.shimi.com.employeelist.databinding.ItemEmployeeBinding
import app.shimi.com.employeelist.ui.viewmodel.EmployeeListViewModel

class EmployeeListAdapter(
    private var employeeList: List<Employee>,
    private val employeeViewModel: EmployeeListViewModel?
) : RecyclerView.Adapter<EmployeeListAdapter.EmployeeHolder>() {

//    lateinit var listener: OnItemClickListener
//    private var employeeList: List<Employee> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeHolder {
        return EmployeeHolder(DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_employee, parent, false
        ) )
    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeHolder =
//        EmployeeHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.item_employee, parent, false),listener,employeeList)





    override fun getItemCount() = employeeList.size

    //override fun onBindViewHolder(holder: EmployeeHolder, position: Int) = holder.bind(employeeList[position])


    override fun onBindViewHolder(holder: EmployeeHolder, position: Int) =
        holder.bind(employeeList[position], object : OnItemClickListener {
            override fun onClick(employee: Employee) {
               Log.d("TAG","in EmployeeHolder OnClickListener size = ${employeeList.size} employee = $employee")
                employeeViewModel?.openEmployeeDetails(employee)

            }
            override fun onRemoveItem(employee: Employee) {
                employeeViewModel?.deleteEmployee(employee)
            }
        })

    class EmployeeHolder(private val binding: ItemEmployeeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(employeeEntity: Employee, employeeListener: OnItemClickListener) {
            with(binding)
            {
                employee  = employeeEntity
                listener = employeeListener
                executePendingBindings()
            }
        }
    }








//    override fun onBindViewHolder(holder: EmployeeHolder, position: Int) {
//        val employee = comments[position]
//        holder.bind(employee)
//    }





//    fun setOnItemClickListener(listener: OnItemClickListener) {
//        this.listener = listener
//    }

//    class EmployeeHolder (
//        private val viewDataBinding: ViewDataBinding, listener: EmployeeListAdapter.OnItemClickListener, employeeList: List<Employee>)
//        : RecyclerView.ViewHolder(viewDataBinding.root) {
//
//        init {
//            viewDataBinding.root.setOnClickListener {
//                listener.onClick(it, employeeList[bindingAdapterPosition])
//                Log.d("TAG","in EmployeeHolder OnClickListener size = ${employeeList.size} bindingAdapterPosition = $bindingAdapterPosition")
//            }
//            this.viewDataBinding.root.findViewById<View>(R.id.deleteItem).setOnClickListener {
//                listener.onRemoveItem( employeeList[bindingAdapterPosition] )
//            }
//        }
//
//        fun bind(employee: Employee) {
//            viewDataBinding.setVariable(BR.employee, employee)
//            viewDataBinding.executePendingBindings()
//        }
//
//    }

    fun setEmployeeList(employeeList: List<Employee>?) {
        if(employeeList != null) {
            this.employeeList = employeeList
            notifyDataSetChanged()
        }
        Log.d("TAG","in setEmployeeList ${itemCount}")
    }

    interface OnItemClickListener {
        fun onClick(employee: Employee)
        fun onRemoveItem(employee: Employee)
    }

}



