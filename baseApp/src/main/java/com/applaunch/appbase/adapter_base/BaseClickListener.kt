package com.applaunch.appbase.adapter_base

interface BaseClickListener<T> {
    fun onClickItem(data:T,position:Int)
}