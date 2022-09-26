package com.example.myproducts.entity

open class StateData<T>(
    open var status: Status,
    var data: T?,
    open var error_code: Int?,
    open var message: String?
) {

    enum class Status {
        SUCCESS, ERROR, LOADING
    }
}

class StateInfo(
    override var status: Status,
    override var error_code: Int?,
    override var message: String?
) : StateData<Any>(status, null, error_code, message)