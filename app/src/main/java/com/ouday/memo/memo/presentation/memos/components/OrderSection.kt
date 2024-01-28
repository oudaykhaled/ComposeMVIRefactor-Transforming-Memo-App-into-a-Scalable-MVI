package com.ouday.memo.memo.presentation.memos.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ouday.memo.memo.domain.util.MemoOrder
import com.ouday.memo.memo.domain.util.OrderType

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    memoOrder: MemoOrder = MemoOrder.Date(OrderType.Descending),
    onOrderChange: (MemoOrder) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Title",
                selected = memoOrder is MemoOrder.Title,
                onSelect = { onOrderChange(MemoOrder.Title(memoOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Date",
                selected = memoOrder is MemoOrder.Date,
                onSelect = { onOrderChange(MemoOrder.Date(memoOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Color",
                selected = memoOrder is MemoOrder.Color,
                onSelect = { onOrderChange(MemoOrder.Color(memoOrder.orderType)) }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Ascending",
                selected = memoOrder.orderType is OrderType.Ascending,
                onSelect = {
                    onOrderChange(memoOrder.copy(OrderType.Ascending))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Descending",
                selected = memoOrder.orderType is OrderType.Descending,
                onSelect = {
                    onOrderChange(memoOrder.copy(OrderType.Descending))
                }
            )
        }
    }
}