package com.example.myproducts.entity

class StateData<T>(var status : Status, var data: T?, var error_code: Int?, var message: String?) {

    enum class Status {
        SUCCESS, ERROR, LOADING
    }
}