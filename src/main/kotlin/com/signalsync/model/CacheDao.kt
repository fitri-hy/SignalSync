package com.signalsync.model

interface CacheDao {
    fun save(entity: CacheEntity)
    fun load(key: String): CacheEntity?
    fun clear()
}
