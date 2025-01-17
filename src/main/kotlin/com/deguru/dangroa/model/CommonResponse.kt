package com.deguru.dangroa.model

import com.deguru.dangroa.global.CommonException

class CommonResponse {
    abstract class BaseResponse(
        open val resultCode: String,
        open val message: String?,
        open val detailMessages: Map<String, String>?,
    )
    data class PaginationResponse<T>(
        override val resultCode: String,
        override val message: String?,
        override val detailMessages: Map<String, String>?,
        val pageIndex: Int,
        val pageSize: Int = 10,
        val totalCount: Long,
        val data: T
    ): BaseResponse(resultCode, message, detailMessages) {
        constructor(paging: CommonRequest.Paging, totalCount:Long, data: T) : this(
            "OK",
            null,
            null,
            paging.pageIndex,
            paging.pageSize,
            totalCount,
            data)
    }

    data class CommonErrorResponse(
        override val resultCode: String,
        override val message: String?,
        override val detailMessages: Map<String, String>?,
    ):BaseResponse(resultCode, message, detailMessages){
        constructor(exception: CommonException, detailMessages: Map<String, String>?) : this(
            exception.exceptionCode.code,
            exception.exceptionCode.message,
            detailMessages)

    }
}