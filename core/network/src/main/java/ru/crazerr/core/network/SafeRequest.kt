package ru.crazerr.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import io.ktor.utils.io.CancellationException

suspend inline fun <reified T> HttpClient.safeRequest(
    block: HttpRequestBuilder.() -> Unit
): Result<T> {
    return try {
        val response = request { block() }
        if (response.status.value in 200..299) {
            Result.success(response.body())
        } else {
            Result.failure(Exception("Error: ${response.status.value}, ${response.status.description}"))
        }
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}

suspend inline fun <reified T> HttpClient.delete(
    url: String,
    block: HttpRequestBuilder.() -> Unit = {}
): Result<T> {
    return safeRequest {
        this.url(url)
        this.method = HttpMethod.Delete
        block()
    }
}

suspend inline fun <reified T> HttpClient.post(
    url: String,
    block: HttpRequestBuilder.() -> Unit = {}
): Result<T> {
    return safeRequest {
        this.url(url)
        this.method = HttpMethod.Post
        block()
    }
}

suspend inline fun <reified T> HttpClient.get(
    url: String,
    block: HttpRequestBuilder.() -> Unit = {}
): Result<T> {
    return safeRequest {
        this.url(url)
        this.method = HttpMethod.Get
        block()
    }
}

suspend inline fun <reified T> HttpClient.put(
    url: String,
    block: HttpRequestBuilder.() -> Unit = {}
): Result<T> {
    return safeRequest {
        this.url(url)
        this.method = HttpMethod.Put
        block()
    }
}