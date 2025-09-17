package com.signalsync

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.signalsync.util.JsonParser
import java.net.URL

class DataManager(private val context: Context) {

    private val cache = JsonParser(context, "signalsync_cache.json")
    private val flowMap = mutableMapOf<String, MutableStateFlow<Any>>()

    fun subscribe(url: String): Flow<Any> {
        val flow = flowMap.getOrPut(url) {
            MutableStateFlow(fetchCached(url) ?: "empty")
        }
        fetchRemote(url, flow)
        return flow
    }

    private fun fetchCached(url: String): Any? = cache.load(url, Any::class.java)

    private fun fetchRemote(url: String, flow: MutableStateFlow<Any>) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val json = URL(url).readText()
                val patched = DeltaEngine.applyPatch(flow.value, json)
                flow.value = patched
                cache.save(url, patched)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
