package com.example.exercise.dagger2

import com.example.exercise.data.api.RandomJokeWebService
import com.example.exercise.data.network.RandomJokeRepository
import com.example.exercise.data.network.RetrofitRandomJoke
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class RandomRepositoryModule {

    @Provides
    @Singleton
    fun provideRandomJokeWebService(): RandomJokeWebService {
        return RetrofitRandomJoke().getApi(RandomJokeWebService::class.java)
    }

    @Provides
    @Singleton
    fun provideRandomJokeRepository(randomJokeWebService: RandomJokeWebService): RandomJokeRepository {
        return RandomJokeRepository(randomJokeWebService)
    }
}


  
