package com.signalsync

import android.content.Context
import kotlinx.coroutines.flow.Flow

class SignalSync private constructor(private val context: Context) {

    private val analyzer = MLAnalyzer()
    private val manager = DataManager(context)
    private val multiManager = MultiEndpointManager(manager)
    private val graphManager = GraphSubscriptionManager(manager)

    companion object {
        @Volatile private var INSTANCE: SignalSync? = null

        fun init(context: Context): SignalSync {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SignalSync(context).also { INSTANCE = it }
            }
        }

        fun instance(): SignalSync {
            return INSTANCE ?: throw IllegalStateException("SignalSync not initialized")
        }
    }

    fun analyze(data: Any): Map<String, Any> = analyzer.analyze(data)
    fun subscribe(url: String): Flow<Any> = manager.subscribe(url)
    fun multiSubscribe(urls: List<String>): Flow<List<Any>> = multiManager.subscribe(urls)
    fun subscribeGraph(graph: Map<String, String>): Flow<Map<String, Any>> = graphManager.subscribeGraph(graph)
    fun predictiveFetch(url: String): Flow<Any> = PredictiveFetcher(manager).fetch(url)
}
