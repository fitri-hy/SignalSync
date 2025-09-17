package com.signalsync

class MLAnalyzer {
    fun analyze(data: Any): Map<String, Any> {
        val result = mutableMapOf<String, Any>()
        val str = data.toString()

        result["length"] = str.length
        result["type"] = data::class.simpleName ?: "Unknown"

        if (data is Number) {
            result["isPositive"] = data.toDouble() > 0
            result["isInteger"] = data.toDouble() % 1 == 0.0
        }

        return result
    }
}
