package com.zhair.stitchwell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class OrderDetailViewModel:ViewModel() {
    val isSaving = MutableStateFlow<Boolean?>(false)
    val isSaved = MutableStateFlow<Boolean?>(null)
    val isFailure = MutableStateFlow<String?>(null)
    val orderRepository = OrderRepository()

    fun updateOrder(order: Order) {
        viewModelScope.launch {
            isSaving.value=true
            val result = orderRepository.updateRequest(order)
            if (result.isSuccess) {
                isSaved.value = true
                isSaving.value= false
            } else {
                isSaving.value= false
                isFailure.value = result.exceptionOrNull()?.message
            }
        }
    }


}