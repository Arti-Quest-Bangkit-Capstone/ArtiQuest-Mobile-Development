package com.thequest.artiquest.view.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequest.artiquest.data.remote.api.response.Artifact
import com.thequest.artiquest.data.repository.ArtifactRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: ArtifactRepository) : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    val detailArtifact = MutableLiveData<Artifact>()

    fun getDetailArtifact(id: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val detailResponse = repository.getDetailArtifact(id)

                detailArtifact.value = detailResponse.data!!

            } catch (e: Exception) {
                Log.e(TAG, "Error load artifact: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }

}