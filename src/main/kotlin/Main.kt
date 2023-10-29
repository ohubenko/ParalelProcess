import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    val inputList = mutableListOf(1, 2, 3, 4, 5, 6)
    val sum: Int
    measureTimeMillis {
//        val inputList = (1..1000000).toMutableList()
        val mutex = Mutex()

        // Основна логіка обчислення суми парних елементів
        suspend fun processPairSum(index: Int, oppositeIndex: Int) {
            val sum = inputList[index] + inputList[oppositeIndex]
            mutex.withLock {
                inputList[index] = sum
                inputList.removeAt(oppositeIndex)
            }
        }
        while (inputList.size > 1) {
            // Ініціалізація корутин для кожної пари
            val jobs = mutableListOf<Job>()
            val halfSize = inputList.size / 2
            for (i in 0..<halfSize) {
                val oppositeIndex = inputList.lastIndex - i
                jobs.add(launch { processPairSum(i, oppositeIndex) })
            }
            // Очікування завершення всіх корутин
            jobs.forEach { it.join() }
            println(inputList) // Вивід результату
        }
        sum = inputList[0]
    }.also { println("Time execution: $it ms for [1, 2, 3, 4, 5, 6] ") }

    println("Sum: $sum") // Вивід результату
}
