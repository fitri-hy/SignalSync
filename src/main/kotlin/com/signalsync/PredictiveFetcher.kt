package com.signalsync

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.map

class PredictiveFetcher(private val manager: DataManager) {

    fun fetch(url: String): Flow<Any> {
        return manager.subscribe(url)
            .onEach { data ->
                val metrics = MLAnalyzer().analyze(data)
                println("Predictive metrics for $url: $metrics")
            }
            .map { data -> data }
    }
}
