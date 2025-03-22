package me.saro.kit.cache

import java.util.concurrent.ConcurrentHashMap
import java.util.function.BiConsumer

class CacheStore<ID, T>(
    private val cacheTimeMillis: Long
) {
    private val store: MutableMap<ID, CacheWrapper<T>> = ConcurrentHashMap()

    fun find(id: ID, orElseUpdateAndGet: (ID) -> T): T = store[id]?.data ?: update(id, orElseUpdateAndGet(id))

    fun findOrNull(id: ID): T? = store[id]?.data

    fun update(id: ID, data: T): T = data.apply { store[id] = CacheWrapper(System.currentTimeMillis() + cacheTimeMillis, this) }

    fun remove(id: ID): T? = store.remove(id)?.data

    fun clear(): Unit = store.clear()

    fun forEach(action: BiConsumer<ID, T>) = store.forEach { (k, v) -> v.data?.also { action.accept(k, it) } }

    fun all(): Map<ID, T> = HashMap<ID, T>().also { forEach { k, v -> it[k] = v } }

    fun keys() = ArrayList<ID>(store.size).also { forEach { k, _ -> it.add(k) } }

    private class CacheWrapper<T> (
        private val expireTimeMillis: Long,
        private val _data: T
    ) {
        val data get(): T? = if (expireTimeMillis >= System.currentTimeMillis()) _data else null
    }
}

