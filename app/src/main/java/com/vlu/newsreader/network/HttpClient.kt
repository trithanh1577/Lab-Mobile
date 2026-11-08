package com.vlu.newsreader.network

import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class HttpClient {

    @Throws(IOException::class)
    fun get(urlString: String): String {
        val connection = URL(urlString).openConnection() as HttpURLConnection

        return try {
            connection.requestMethod = "GET"
            connection.connectTimeout = CONNECT_TIMEOUT_MILLIS
            connection.readTimeout = READ_TIMEOUT_MILLIS
            connection.instanceFollowRedirects = true
            connection.setRequestProperty("Accept", "application/json")
            connection.connect()

            val responseCode = connection.responseCode
            if (responseCode !in 200..299) {
                throw IOException("HTTP request failed with status $responseCode")
            }

            connection.inputStream.bufferedReader(Charsets.UTF_8).use { reader ->
                reader.readText()
            }
        } finally {
            connection.disconnect()
        }
    }

    private companion object {
        const val CONNECT_TIMEOUT_MILLIS = 10_000
        const val READ_TIMEOUT_MILLIS = 10_000
    }
}
