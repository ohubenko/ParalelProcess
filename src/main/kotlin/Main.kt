import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicInteger

fun main() = runBlocking {
    var inputList = mutableListOf(1, 2, 3, 4, 5, 6)
    val numberOfProcessors = Runtime.getRuntime().availableProcessors()
    val tasksChannel = Channel<Int?>(Channel.UNLIMITED)
    val doneCounter = AtomicInteger(0)
    val mutex = Mutex()

    val workerJobs = List(numberOfProcessors) {
        launch {
            for (index in tasksChannel) {
                index?.let {
                    mutex.withLock {
                        val oppositeIndex = inputList.size - 1 - it
                        inputList[it] += inputList[oppositeIndex]
                        inputList.removeAt(oppositeIndex)
                    }
                    doneCounter.incrementAndGet()
                }
            }
        }
    }

    while (inputList.size > 1) {
        for (i in 0 until inputList.size / 2) {
            tasksChannel.send(i)
        }

        while (doneCounter.get() < inputList.size / 2) {
            delay(10)
        }
        doneCounter.set(0)
    }

    tasksChannel.close()
    workerJobs.forEach { it.join() }

    println(inputList)
}
