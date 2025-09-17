package com.signalsync.util

import android.content.Context
import com.google.gson.Gson
import java.io.File

class JsonParser(private val context: Context, private val filename: String) {
    
    private val gson = Gson()
    private val file = File(context.filesDir, filename)

    fun <T> save(key: String, value: T) {
        val map = loadAll().toMutableMap()
        map[key] = value as Any
        file.writeText(gson.toJson(map))
    }

    fun <T> load(key: String, clazz: Class<T>): T? {
        val map = loadAll()
        val obj = map[key] ?: return null
        return gson.fromJson(gson.toJson(obj), clazz)
    }

    private fun loadAll(): Map<String, Any> {
        return if (file.exists()) gson.fromJson(file.readText(), Map::class.java) as Map<String, Any>
        else emptyMap()
    }

    fun clear() {
        if (file.exists()) file.delete()
    }
}
