package com.veluxer.occupying.domain

import org.springframework.util.MultiValueMap

interface SearchFilter {
    fun getFormData(): MultiValueMap<String, String>
}
