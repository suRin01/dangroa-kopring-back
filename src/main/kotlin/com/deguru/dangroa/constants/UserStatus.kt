package com.deguru.dangroa.constants

enum class UserStatus(val status: Long) {

    ACTIVE(0), DELETED(1);

    companion object {
        private val VALUES = entries.toTypedArray()
        fun getByValue(status: Long) = VALUES.first { it.status == status }
    }

}