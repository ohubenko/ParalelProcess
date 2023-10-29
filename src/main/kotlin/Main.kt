import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

fun main() = runBlocking {
    val inputList = mutableListOf(1, 2, 3, 4, 5, 6)
    val numberOfProcessors = Runtime.getRuntime().availableProcessors()
    val mutex = Mutex()

    // Основна логіка обчислення суми парних елементів
    suspend fun processPairSum(index: Int) {
        mutex.withLock {
            val oppositeIndex = inputList.lastIndex - index
            inputList[index] += inputList[oppositeIndex]
            inputList.removeAt(oppositeIndex)
        }
    }

    while (inputList.size > 1) {
        // Ініціалізація корутин для кожної пари
        val jobs = mutableListOf<Job>()
        val halfSize = inputList.size / 2
        for (i in 0 until halfSize) {
            jobs.add(launch { processPairSum(i) })
        }

        // Очікування завершення всіх корутин
        jobs.forEach { it.join() }
    }

    println(inputList[0]) // Вивід результату
}
