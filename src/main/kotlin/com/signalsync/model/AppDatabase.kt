package com.signalsync.model

class AppDatabase {
    private val storage = mutableMapOf<String, String>()
    
    fun save(key: String, value: String) {
        storage[key] = value
    }

    fun load(key: String): String? = storage[key]

    fun clear() = storage.clear()
}
