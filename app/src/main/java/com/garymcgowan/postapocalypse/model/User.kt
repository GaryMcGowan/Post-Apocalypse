package com.garymcgowan.postapocalypse.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @Json(name = "id") val id: Int,
    @Json(name = "website") val website: String? = null,
    @Json(name = "address") val address: Address? = null,
    @Json(name = "phone") val phone: String? = null,
    @Json(name = "name") val name: String? = null,
    @Json(name = "company") val company: Company? = null,
    @Json(name = "email") val email: String? = null,
    @Json(name = "username") val username: String? = null
) : Parcelable

@Parcelize
data class Address(
    @Json(name = "zipcode") val zipCode: String? = null,
    @Json(name = "geo") val geo: Geo? = null,
    @Json(name = "suite") val suite: String? = null,
    @Json(name = "city") val city: String? = null,
    @Json(name = "street") val street: String? = null
) : Parcelable

@Parcelize
data class Company(
    @Json(name = "bs") val bs: String? = null,
    @Json(name = "catchPhrase") val catchPhrase: String? = null,
    @Json(name = "name") val name: String? = null
) : Parcelable

@Parcelize
data class Geo(
    @Json(name = "lng") val lng: String? = null,
    @Json(name = "lat") val lat: String? = null
) : Parcelable
