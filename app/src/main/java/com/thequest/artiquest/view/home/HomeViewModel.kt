package com.thequest.artiquest.view.home

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thequest.artiquest.data.remote.api.response.ArtifactItem
import com.thequest.artiquest.data.repository.ArtifactRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ArtifactRepository) : ViewModel() {
    val error = MutableLiveData<String?>()
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _listArtifact = MutableLiveData<List<ArtifactItem>>()
    val listArtifact: LiveData<List<ArtifactItem>> = _listArtifact

    fun getArtifacts() {
        viewModelScope.launch {
            try {
                _loading.value = true
                val response = repository.getArtifacts()
                _listArtifact.value = response.data

            } catch (e: Exception) {
                Log.e(TAG, "Error load artifacts: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

    fun searchArtifacts(query: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val response = repository.searchArtifact(query)
                Handler(Looper.getMainLooper()).post {
                    _listArtifact.value = response.data
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error search artifacts: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }

}