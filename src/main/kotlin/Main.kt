import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.channels.Channel

fun main() = runBlocking {
    val inputArray = intArrayOf(1, 2, 3, 4, 5, 6)
    val numberOfProcessors = Runtime.getRuntime().availableProcessors()
    val tasksChannel = Channel<Int>(Channel.UNLIMITED)
    val doneChannel = Channel<Unit>(Channel.UNLIMITED)
    val mutex = Mutex()

    val activeElements = MutableList(inputArray.size) { i -> i }

    val workerJobs = List(numberOfProcessors) {
        launch {
            for (index in tasksChannel) {
                val oppositeIndex = activeElements.size - 1 - index
                mutex.withLock {
                    inputArray[activeElements[index]] += inputArray[activeElements[oppositeIndex]]
                    inputArray[activeElements[oppositeIndex]] = 0
                }
                doneChannel.send(Unit)
            }
        }
    }

    while (activeElements.size > 1) {
        val operations = activeElements.size / 2
        for (i in 0 until operations) {
            tasksChannel.send(i)
        }

        // Очікуємо завершення всіх завдань цієї хвилі
        repeat(operations) {
            doneChannel.receive()
        }

        activeElements.subList(operations, activeElements.size).clear()
    }

    tasksChannel.close()
    workerJobs.forEach { it.join() }
    println(inputArray.toList())
}
