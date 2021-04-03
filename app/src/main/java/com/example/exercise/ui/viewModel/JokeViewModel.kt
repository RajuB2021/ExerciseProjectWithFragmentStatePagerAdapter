package com.example.exercise.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exercise.data.network.RandomJokeRepository
import com.example.exercise.data.util.JokeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JokeViewModel(
    repository: RandomJokeRepository, firstName: String, lastName: String
) : ViewModel() {

    private val repository: RandomJokeRepository = repository
    private val firstName = firstName
    private val lastName = lastName
    
    var jokeResponse: MutableLiveData<JokeResponse>? = MutableLiveData()
    init {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                repository.fetchRandomJoke(firstName, lastName)
            }
            if (response != null) {
                jokeResponse?.value = response
            }
        }
    }

    fun getRandomJoke() {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                repository.fetchRandomJoke(firstName, lastName)
            }
            if (response != null) {
                jokeResponse?.value = response
            }
        }
    }

}