package com.food.ordering.zinger.seller.utils


object AppConstants {
    const val PREFS_AUTH_TOKEN = "auth_token"
    const val PREFS_LOGIN_PREFS = "loginPrefs"
    const val PREFS_CUSTOMER = "customer"
    const val PREFS_CART = "cart"
    const val PREFS_CART_SHOP = "cart_shop"
    const val PREFS_ORDER_DETAIL = "cart_shop"
    const val PREFS_SELLER_ID = "id"
    const val PREFS_SELLER_NAME = "name"
    const val PREFS_SELLER_EMAIL = "email"
    const val PREFS_SELLER_PLACE = "place"
    const val PREFS_SELLER_MOBILE = "mobile"
    const val PREFS_SELLER_ROLE = "role"

    const val ORDER_STATUS_PENDING = "PENDING"
    const val ORDER_STATUS_TXN_FAILURE = "TXN_FAILURE"
    const val ORDER_STATUS_PLACED = "PLACED"
    const val ORDER_STATUS_CANCELLED_BY_USER = "CANCELLED_BY_USER"
    const val ORDER_STATUS_ACCEPTED = "ACCEPTED"
    const val ORDER_STATUS_CANCELLED_BY_SELLER = "CANCELLED_BY_SELLER"
    const val ORDER_STATUS_READY = "READY"
    const val ORDER_STATUS_OUT_FOR_DELIVERY = "OUT_FOR_DELIVERY"
    const val ORDER_STATUS_COMPLETED = "COMPLETED"
    const val ORDER_STATUS_DELIVERED = "DELIVERED"
    const val ORDER_STATUS_REFUND_INITIATED = "REFUND_INITIATED"
    const val ORDER_STATUS_REFUND_COMPLETED= "REFUND_COMPLETED"

    enum class STATUS{
        PENDING, TXN_FAILURE, PLACED, CANCELLED_BY_USER, ACCEPTED, CANCELLED_BY_SELLER, READY, OUT_FOR_DELIVERY, COMPLETED, DELIVERED, REFUND_INITIATED, REFUND_COMPLETED
    }

    enum class ROLE{
        SHOP_OWNER,SELLER,DELIVERY
    }

}
