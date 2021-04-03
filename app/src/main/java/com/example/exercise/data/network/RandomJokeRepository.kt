package com.example.exercise.data.network

import com.example.exercise.data.api.RandomJokeWebService
import com.example.exercise.data.model.RandomJokeApi
import com.example.exercise.data.util.JokeResponse
import com.example.exercise.ui.util.CommonUtil
import retrofit2.HttpException
import retrofit2.Response


class RandomJokeRepository(remoteDataSource: RandomJokeWebService) {

    private val randomJokeWebService = remoteDataSource

    suspend fun fetchRandomJoke(firstName: String, lastName: String): JokeResponse? {
            try {
                val response: Response<RandomJokeApi> = randomJokeWebService.getRandomJoke(firstName, lastName)
                val joke = response.body()?.valueObj?.joke
                return JokeResponse(CommonUtil.RESULT_SUCCESS, false, null, joke, response.code())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        return JokeResponse(CommonUtil.RESULT_FAILURE, false, throwable.response()?.errorBody()?.string().toString(), null, throwable.code())
                    }
                    else -> {
                        return JokeResponse(CommonUtil.RESULT_FAILURE, true, "network error ", null, 0)
                    }
                }
            }
        }
}