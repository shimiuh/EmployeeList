package app.shimi.com.employeelist.view

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import app.shimi.com.employeelist.BR
import app.shimi.com.employeelist.R
import app.shimi.com.employeelist.model.dataModel.Employee


class EmployeeListAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<EmployeeHolder>() {

    interface OnItemClickListener {
        fun onClick(view: View, employee: Employee)
        fun onRemoveItem(employee: Employee)
    }
    lateinit var listener: OnItemClickListener
    private var employeeList: List<Employee> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeHolder =
        EmployeeHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.item_employee, parent, false),listener,employeeList)

    override fun onBindViewHolder(holder: EmployeeHolder, position: Int) {
        val employee = employeeList[position]
        holder.bind(employee)
    }

    override fun getItemCount() = employeeList.size

    fun setEmployeeList(employeeList: List<Employee>?) {
        if(employeeList != null) {
            this.employeeList = employeeList
            notifyDataSetChanged()
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

}


class EmployeeHolder (
    private val viewDataBinding: ViewDataBinding, listener: EmployeeListAdapter.OnItemClickListener, employeeList: List<Employee>)
    : RecyclerView.ViewHolder(viewDataBinding.root) {

    init {
        viewDataBinding.root.setOnClickListener(View.OnClickListener {
            listener.onClick(it, employeeList[adapterPosition] )
        })
        this.viewDataBinding.root.findViewById<View>(R.id.deleteItem).setOnClickListener {
            listener.onRemoveItem( employeeList[adapterPosition] )
        }
    }

    fun bind(employee: Employee) {
        viewDataBinding.setVariable(BR.employee, employee)
        viewDataBinding.executePendingBindings()
    }
}
