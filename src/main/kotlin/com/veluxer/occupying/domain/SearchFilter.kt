package com.veluxer.occupying.domain

interface SearchFilter {
    fun getToken(): String
    fun <T> getFilter(): T
}