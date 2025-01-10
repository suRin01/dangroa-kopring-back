package com.deguru.dangroa.model

class CommonResponse {
    data class PaginationResponse<T>(
        val pageIndex: Int,
        val pageSize: Int = 10,
        val totalCount: Long,
        val data: T
    ){
        constructor(paging: CommonRequest.Paging, totalCount:Long, data: T) : this(
            paging.pageIndex,
            paging.pageSize,
            totalCount,
            data)
    }
}