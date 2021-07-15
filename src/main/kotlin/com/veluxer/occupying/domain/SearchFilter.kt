package com.veluxer.occupying.domain

interface SearchFilter {
    fun <T> getFilter(): T
}
