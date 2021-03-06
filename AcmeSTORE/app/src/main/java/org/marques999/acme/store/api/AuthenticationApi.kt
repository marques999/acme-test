package org.marques999.acme.store.api

import retrofit2.http.Body
import retrofit2.http.POST

import io.reactivex.Observable

import org.marques999.acme.store.model.Authentication
import org.marques999.acme.store.model.Customer
import org.marques999.acme.store.model.CustomerPOST
import org.marques999.acme.store.model.SessionJwt

interface AuthenticationApi {

    @POST("login")
    fun login(@Body authentication: Authentication): Observable<SessionJwt>

    @POST("customers/")
    fun register(@Body customerPOST: CustomerPOST): Observable<Customer>
}