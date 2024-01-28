package com.ouday.memo.memo.domain.util

sealed class MemoOrder(val orderType: OrderType) {
    class Title(orderType: OrderType): MemoOrder(orderType)
    class Date(orderType: OrderType): MemoOrder(orderType)
    class Color(orderType: OrderType): MemoOrder(orderType)

    fun copy(orderType: OrderType): MemoOrder {
        return when(this) {
            is Title -> Title(orderType)
            is Date -> Date(orderType)
            is Color -> Color(orderType)
        }
    }
}
