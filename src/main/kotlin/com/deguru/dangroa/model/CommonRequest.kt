package com.deguru.dangroa.model

class CommonRequest {
    data class Paging(
        val pageIndex: Int = 0,
        val pageSize: Int = 10,
    )
}