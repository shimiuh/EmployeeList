package app.shimi.com.employeelist.repository

import app.shimi.com.employeelist.model.dataModel.Employee

data class CachedData(val employees: List<Employee>, val message: String, val error: Throwable? = null)
