package com.signalsync

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class GraphSubscriptionManager(private val manager: DataManager) {

    fun subscribeGraph(graph: Map<String, String>): Flow<Map<String, Any>> {
        val flows = graph.map { (node, url) ->
            manager.subscribe(url).map { data -> node to data }
        }
        return combine(flows) { pairs -> pairs.toMap() }
    }
}
