package com.seanghay.outstagram.fragment.choose

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seanghay.outstagram.model.ChooseImageItem
import com.seanghay.outstagram.repository.GalleryRepository
import kotlinx.coroutines.launch


class ChooseViewModel : ViewModel() {

    val images: MutableLiveData<List<ChooseImageItem>> = MutableLiveData()
    val isLoading = MutableLiveData(true)

    fun loadPhotos() {
        viewModelScope.launch {
            images.value = GalleryRepository.getPhotos()
        }
    }
}