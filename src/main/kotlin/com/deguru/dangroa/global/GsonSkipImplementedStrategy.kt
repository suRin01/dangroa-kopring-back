package com.deguru.dangroa.global

import com.deguru.dangroa.model.CommonResponse
import com.nimbusds.jose.shaded.gson.ExclusionStrategy
import com.nimbusds.jose.shaded.gson.FieldAttributes


class GsonSkipImplementedStrategy : ExclusionStrategy {
    override fun shouldSkipField(field: FieldAttributes): Boolean {
        return field.declaringClass == CommonResponse.BaseResponse::class.java
    }

    override fun shouldSkipClass(aClass: Class<*>?): Boolean {
        return false
    }
}
