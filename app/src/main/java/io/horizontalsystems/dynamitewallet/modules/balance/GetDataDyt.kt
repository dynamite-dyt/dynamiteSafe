package io.horizontalsystems.dynamitewallet.modules.balance

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface GetData {

//Describe the request type and the relative URL//

    @GET
    fun getDytStats(@Url url:String): Call<GetDYT>

}