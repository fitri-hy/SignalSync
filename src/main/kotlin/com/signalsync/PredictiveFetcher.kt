package com.signalsync

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class PredictiveFetcher(private val manager: DataManager) {

    fun fetch(url: String): Flow<Any> {
        return manager.subscribe(url)
            .onEach { data ->
                val analyzer = MLAnalyzer()
                val metrics = analyzer.analyze(data)
                println("PredictiveFetcher metrics for $url: $metrics")
            }
            .map { data ->
                data
            }
    }
}
