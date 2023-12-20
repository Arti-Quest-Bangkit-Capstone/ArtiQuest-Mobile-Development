package com.thequest.artiquest.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thequest.artiquest.data.repository.ArtifactRepository
import com.thequest.artiquest.view.detail.DetailViewModel
import com.thequest.artiquest.view.home.HomeViewModel

class ArtifactViewModelFactory(private val repository: ArtifactRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }

            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }


            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ArtifactViewModelFactory? = null

        @JvmStatic
        fun getInstance(repository: ArtifactRepository): ArtifactViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ArtifactViewModelFactory::class.java) {
                    INSTANCE = ArtifactViewModelFactory(repository)
                }
            }
            return INSTANCE as ArtifactViewModelFactory
        }
    }
}