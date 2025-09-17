package com.signalsync

import android.content.Context
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import com.signalsync.util.JsonParser
import java.net.URL
import java.security.MessageDigest

class DataManager(private val context: Context) {

    private val cache = JsonParser(context, "signalsync_cache.json")
    private val flowMap = mutableMapOf<String, MutableStateFlow<Any>>()
    private val hashMap = mutableMapOf<String, String>()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun subscribe(url: String): Flow<Any> {
        val flow = flowMap.getOrPut(url) {
            MutableStateFlow(fetchCached(url) ?: "empty")
        }

        startBackgroundFetch(url, flow)
        return flow
    }

    private fun fetchCached(url: String): Any? = cache.load(url, Any::class.java)

    private fun startBackgroundFetch(url: String, flow: MutableStateFlow<Any>) {
        scope.launch {
            while (isActive) {
                try {
                    val json = URL(url).readText()
                    val newHash = md5(json)
                    val oldHash = hashMap[url]

                    if (newHash != oldHash) {
                        val patched = DeltaEngine.applyPatch(flow.value, json)
                        flow.value = patched
                        cache.save(url, patched)
                        hashMap[url] = newHash
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                delay(2000)
            }
        }
    }

    private fun md5(input: String): String {
        val bytes = MessageDigest.getInstance("MD5").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
