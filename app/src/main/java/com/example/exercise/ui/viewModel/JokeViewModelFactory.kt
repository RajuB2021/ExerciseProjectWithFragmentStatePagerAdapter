package com.example.exercise.ui.viewModel

import androidx.lifecycle.ViewModel

import androidx.lifecycle.ViewModelProvider
import com.example.exercise.data.network.RandomJokeRepository


class JokeViewModelFactory(repository: RandomJokeRepository, firstName:String,lastName:String) :
        ViewModelProvider.Factory {
    private val repository: RandomJokeRepository = repository
    val firstName : String = firstName
    val lastName : String = lastName

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return JokeViewModel(repository,firstName,lastName) as T
    }
}