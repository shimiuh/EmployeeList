package com.kotlin.mvvm.boilerplate.ui.component.binding

import android.text.Html
import android.view.View
import android.widget.TextView
import android.widget.Toolbar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.shimi.com.employeelist.data.model.Employee
import app.shimi.com.employeelist.ui.view.EmployeeListAdapter

object CustomViewBinding {

    @BindingAdapter("app:isRefreshing")
    @JvmStatic
    fun SwipeRefreshLayout.isRefreshing(isRefreshing: Boolean) {
        this.isRefreshing = isRefreshing
    }

    @BindingAdapter("app:onRefreshListener")
    @JvmStatic
    fun SwipeRefreshLayout.setOnRefreshListener(func: () -> Unit) {
        setOnRefreshListener { func() }
    }


    @BindingAdapter("app:addVerticalItemDecoration")
    @JvmStatic
    fun RecyclerView.addVerticalItemDecoration(isVertical: Boolean) {
        addItemDecoration(DividerItemDecoration(context, if (isVertical) RecyclerView.VERTICAL else RecyclerView.HORIZONTAL))
    }

    @BindingAdapter("app:items")
    @JvmStatic
    fun setListEmployees(recyclerView: RecyclerView, items: List<Employee>) {
        with(recyclerView.adapter as EmployeeListAdapter?) {
            this?.setEmployeeList(items)
        }
    }

    @BindingAdapter("app:textHtml")
    @JvmStatic
    fun TextView.textHtml(text: String?) {
        text?.let { setText(Html.fromHtml(it)) }
    }

    @BindingAdapter("app:navigationOnClickListener")
    @JvmStatic
    fun Toolbar.setNavigationOnClickListener(onClickListener: View.OnClickListener) {
        setNavigationOnClickListener(onClickListener)
    }
}