package com.signalsync

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class MultiEndpointManager(private val manager: DataManager) {

    fun subscribe(urls: List<String>): Flow<List<Any>> {
        val flows: List<Flow<Any>> = urls.map { manager.subscribe(it) }
        return combine(flows) { it.toList() }
    }
}
