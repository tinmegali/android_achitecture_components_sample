package com.tinmegali.myweather.web

import android.arch.lifecycle.LiveData
import com.tinmegali.myweather.models.ApiResponse

import com.tinmegali.myweather.models.WeatherResponse

import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response


class LiveDataCallAdapter<R>(
        private val responseType: Type
    )
    : CallAdapter<R, LiveData<ApiResponse<R>>>
{

    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(call: Call<R>): LiveData<ApiResponse<R>> {
        return object : LiveData<ApiResponse<R>>() {
            internal var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    call.enqueue(object : Callback<R> {
                        override fun onResponse(call1: Call<R>, response: Response<R>) {
                            postValue(ApiResponse(
                                    data = response.body()
                            ))
                        }

                        override fun onFailure(call2: Call<R>, t: Throwable) {
                            postValue(ApiResponse(
                                    error = ApiError(
                                            message = t.message,
                                            statusCode = 500
                                    )
                            ))
                        }
                    })
                }
            }
        }
    }
}
