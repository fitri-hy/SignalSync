package com.signalsync.util

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode

object JsonPatch {

    private val mapper = ObjectMapper()

    fun patch(oldJson: String, newJson: String): String {
        val oldNode = mapper.readTree(oldJson)
        val newNode = mapper.readTree(newJson)
        return merge(oldNode, newNode).toString()
    }

    private fun merge(oldNode: JsonNode, newNode: JsonNode): JsonNode {
        if (oldNode is ObjectNode && newNode is ObjectNode) {
            val result = oldNode.deepCopy<ObjectNode>()
            newNode.fieldNames().forEach { field ->
                val newValue = newNode.get(field)
                val oldValue = result.get(field)
                result.set<JsonNode>(
                    field,
                    if (oldValue != null && oldValue.isObject && newValue.isObject) {
                        merge(oldValue, newValue)
                    } else {
                        newValue
                    }
                )
            }
            return result
        }
        return newNode
    }
}
