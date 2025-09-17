package com.signalsync

import com.signalsync.util.JsonPatch
import com.google.gson.Gson

object DeltaEngine {
    private val gson = Gson()
    
    fun applyPatch(oldData: Any, newJson: String): Any {
        return try {
            val oldJson = gson.toJson(oldData)
            JsonPatch.patch(oldJson, newJson)
        } catch (e: Exception) {
            newJson
        }
    }
}
