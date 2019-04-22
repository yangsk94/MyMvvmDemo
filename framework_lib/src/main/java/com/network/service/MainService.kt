package com.medtap.network.library.api

import com.constans.ConstantUrl
import com.network.bean.AreaBean
import com.network.bean.LoginReqData
import com.network.bean.UserInfo
import com.network.entity.BaseEntity
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MainService {

    @GET(ConstantUrl.ALL_AREA_URL)
    fun getAreaList(): Observable<BaseEntity<AreaBean>>

    @POST(ConstantUrl.LOGIN_URL)
    fun login(@Body loginReqData: LoginReqData): Observable<BaseEntity<UserInfo>>

}
