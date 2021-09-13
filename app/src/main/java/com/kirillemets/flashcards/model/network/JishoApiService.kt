package com.kirillemets.flashcards.model.network

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://jisho.org/api/v1/search/words/"

interface JishoApiService {
    @GET(BASE_URL)
    fun getDataObjectAsync(@Query("keyword") word: String): Deferred<QueryData>
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
