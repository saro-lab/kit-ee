package me.saro.kit.db

class SequencePool<T> private constructor(
    private val name: String,
    private val minPoolSize: Int,
    private val maxPoolSize: Int,
    private val increaseTimeMillis: Long,
    private val decreaseTimeMillis: Long,
    private val multiple: Int
) {
    private val store: ArrayDeque<T> = ArrayDeque(minPoolSize)
    private var lastIssuedSize: Int = minPoolSize
    private var lastIssuedTime: Long = 0L

    @Synchronized
    fun get(requestNewSequenceList: (needSequenceSize: Int) -> List<T>): T {
        if (store.isEmpty()) {
            store.addAll(requestNewSequenceList(needSequenceSize))
        }
        return store.removeFirst()
    }

    fun resetLastIssuedTime() {
        lastIssuedTime = 0L
    }

    val nowPoolSize: Int get() = lastIssuedSize
    val nowStoreSize: Int get() = store.size

    private val needSequenceSize: Int get() {
        val now = System.currentTimeMillis()
        if ((now - lastIssuedTime) < increaseTimeMillis) {
            lastIssuedSize = (lastIssuedSize * multiple).coerceAtMost(maxPoolSize)
        } else if ((now - lastIssuedTime) > decreaseTimeMillis) {
            lastIssuedSize = (lastIssuedSize / multiple).coerceAtLeast(minPoolSize)
        }
        lastIssuedTime = now
        return lastIssuedSize
    }

    override fun toString(): String {
        return "name: $name / maxPoolSize: $maxPoolSize / nowPool: $nowPoolSize / nowStore: $nowStoreSize"
    }

    class Builder() {
        private var name: String = ""
        private var minPoolSize: Int = 1
        private var maxPoolSize: Int = 100
        private var increaseMinutes: Int = 1
        private var decreaseMinutes: Int = 5
        private var multiple: Int = 2

        fun minPoolSize(minPoolSize: Int): Builder = this.apply { this.minPoolSize= minPoolSize
        }
        fun maxPoolSize(maxPoolSize: Int): Builder = this.apply { this.maxPoolSize = maxPoolSize }
        fun increaseMinutes(increaseMinutes: Int): Builder = this.apply { this.increaseMinutes = increaseMinutes }
        fun decreaseMinutes(decreaseMinutes: Int): Builder = this.apply { this.decreaseMinutes = decreaseMinutes }
        fun multiple(multiple: Int): Builder = this.apply { this.multiple = multiple }

        fun <T> build(): SequencePool<T> {
            if (minPoolSize < 1 || maxPoolSize < minPoolSize) {
                throw IllegalArgumentException("minPoolSize < 1 || maxPoolSize < minPoolSize")
            }
            if (increaseMinutes < 1 || decreaseMinutes < 1) {
                throw IllegalArgumentException("increaseMinutes < 1 || decreaseMinutes < 1")
            }
            if (decreaseMinutes <= increaseMinutes) {
                throw IllegalArgumentException("decreaseMinutes <= increaseMinutes")
            }
            if (multiple < 2) {
                throw IllegalArgumentException("multiple < 2")
            }

            return SequencePool(name, minPoolSize, maxPoolSize, increaseMinutes * 60L, decreaseMinutes * 60L, multiple)
        }
    }
}