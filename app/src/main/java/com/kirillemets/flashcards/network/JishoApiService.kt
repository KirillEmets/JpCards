package com.kirillemets.flashcards.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://jisho.org/api/v1/search/words/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface JishoApiService {
    @GET(BASE_URL)
    fun getDataObjectAsync(@Query("keyword") word: String): Deferred<QueryData>
}

object JishoApi {
    val retrofitService: JishoApiService by lazy {
        retrofit.create(JishoApiService::class.java)
    }
}

class QueryData (
    val data: List<QueryWord>
) {
    class QueryWord(
        val japanese: List<JapaneseWord>?,
        val senses: List<Sense>?
    ) {
        class JapaneseWord (
            val reading: String?,
            val word: String?
        )
        class Sense (
            val english_definitions: List<String>
        )
    }
}
