package com.zhair.stitchwell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AddOrderViewModel:ViewModel() {
    val OrdersRepository = OrderRepository()
    val isSaving = MutableStateFlow<Boolean>(false)
    val isSaved= MutableStateFlow<Boolean?>(null)
    val failureMessage = MutableStateFlow<String?>(null)


    fun saveOrder(order: Order) {
        viewModelScope.launch {
            val result = OrdersRepository.saveOrder(order)
            isSaving.value=false
            if (result.isSuccess) {

                isSaved.value=true
//                isSuccessfullySaved.value = true
            } else {
                failureMessage.value = result.exceptionOrNull()?.message
            }
        }
    }
}
