package com.ouday.memo.memo.domain.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}
