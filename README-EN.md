# SignalSync [ID](./README.md)

`SignalSync` is a Kotlin library for Android designed to simplify real-time data integration in mobile applications.

This library centralizes all data interactions through a single point, managing subscriptions to single, multi-endpoint, and graph-based endpoints, while providing simple data prefetching and analysis mechanisms.

---

## Philosophy

SignalSync was created due to the challenges often encountered when managing real-time data in Android apps. In many modern apps, data comes from multiple endpoints, changes frequently, and must be presented consistently in the UI. Without a good system, issues like duplicate data, slow updates, or extensive code boilerplate are common.

The idea for SignalSync arose from the desire to simplify this data flow: a single point of control for all data, one that is automated, secure, and easy to use. Using the Reactive First principle, every update is immediately channeled through Flow without manual polling. The Single Source of Truth concept keeps all data consistent, minimizes repeated fetches, and simplifies caching.

Predictive and observant features are added to intelligently process data, reduce latency, and maintain a responsive UI. All of this is built on the principles of simplicity and safety, making real-time data management more efficient and hassle-free.

---

## Diagram

<img src="./diagram.png" alt="diagram.png" >

---

## SDK and Platform Support

#### Android SDK 
   - Minimum SDK: 24
   - Compile SDK: 36
   - AndroidX

#### Compatibility 
   - jvmTarget = 17
   - sourceCompatibility = JavaVersion.VERSION_17
   
#### UI Toolkit
   - Compose
   - XML-based layouts
   - Native Views
   
#### Dependency Management / Build System 
   - Gradle Kotlin DSL
   - Groovy
   - Maven Artifact

---

## Integration (Kotlin DSL)

#### `settings.gradle.kts`

```gradle
include(":app", ":SignalSync")
```

#### `build.gradle.kts` (app)

```gradle
implementation(project(":SignalSync"))
```

---

## Use

#### Initialization

```kotlin
val signalSync = SignalSync.init(this)
```

---

#### Single Subscribe

```kotlin
lifecycleScope.launch {
    signalSync.subscribe("https://jsonplaceholder.typicode.com/todos/1")
    .collect { data ->
        val metrics = signalSync.analyze(data)
        tvRealtimeData.text = "Single:\n$data\nMetrics: $metrics"
    }
}
```

---

#### Multi Subscribe

```kotlin
lifecycleScope.launch {
    val urls = listOf(
        "https://jsonplaceholder.typicode.com/todos/1",
        "https://jsonplaceholder.typicode.com/todos/2"
    )
    signalSync.multiSubscribe(urls)
    .collect { dataList ->
        val metricsList = dataList.map { signalSync.analyze(it) }
        tvMultiData.text = "Multi:\n$dataList\nMetrics: $metricsList"
    }
}
```

---

#### Graph Subscribe

```kotlin
lifecycleScope.launch {
    val graph = mapOf(
        "todo1" to "https://jsonplaceholder.typicode.com/todos/1",
        "todo2" to "https://jsonplaceholder.typicode.com/todos/2"
    )
    signalSync.subscribeGraph(graph)
    .collect { graphData ->
        val metricsMap = graphData.mapValues { signalSync.analyze(it.value) }
        tvGraphData.text = "Graph:\n$graphData\nMetrics: $metricsMap"
    }
}
```

---

#### Predictive Fetch

```kotlin
lifecycleScope.launch {
    signalSync.predictiveFetch("https://jsonplaceholder.typicode.com/todos/1")
        .collect { data ->
        val metrics = signalSync.analyze(data)
        tvPredictiveData.text = "Predictive:\n$data\nMetrics: $metrics"
    }
}
```

---