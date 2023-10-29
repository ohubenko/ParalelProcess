import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.channels.Channel

fun main() = runBlocking {
    val inputArray = intArrayOf(1, 2, 3, 4, 5, 6)
    val numberOfProcessors = Runtime.getRuntime().availableProcessors()
    val tasksChannel = Channel<Int?>(Channel.UNLIMITED) // Null використовується як позначка завершення
    val mutex = Mutex()
    val doneChannel = Channel<Unit>(Channel.UNLIMITED)
    var activeLength = inputArray.size

    val workerJobs = List(numberOfProcessors) {
        launch {
            for (index in tasksChannel) {
                if (index == null) continue

                val oppositeIndex = activeLength - 1 - index
                mutex.withLock {
                    inputArray[index] += inputArray[oppositeIndex]
                    inputArray[oppositeIndex] = 0
                }
                doneChannel.send(Unit)
            }
        }
    }

    while (activeLength > 1) {
        val halfLength = activeLength / 2
        for (i in 0 until halfLength) {
            tasksChannel.send(i)
        }

        // Очікуємо завершення всіх завдань цієї хвилі
        repeat(halfLength) {
            doneChannel.receive()
        }

        activeLength = halfLength
    }

    // Сигнал до робочих потоків про завершення роботи
    repeat(numberOfProcessors) {
        tasksChannel.send(null)
    }

    tasksChannel.close()
    workerJobs.forEach { it.join() }
    println(inputArray[0])
}
