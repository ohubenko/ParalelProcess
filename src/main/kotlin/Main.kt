import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

fun main(inputList: MutableList<Int>): Int = runBlocking {
    val mutex = Mutex()
    suspend fun processPairSum(index: Int, oppositeIndex: Int) {
        val sum = inputList[index] + inputList[oppositeIndex]
        mutex.withLock {
            inputList[index] = sum
            inputList.removeAt(oppositeIndex)
        }
    }
    while (inputList.size > 1) {
        val jobs = mutableListOf<Job>()
        val halfSize = inputList.size / 2
        for (i in 0..<halfSize) {
            val oppositeIndex = inputList.lastIndex - i
            jobs.add(launch { processPairSum(i, oppositeIndex) })
        }
        jobs.forEach { it.join() }
        println(inputList)
    }

    if (inputList.isEmpty()) 0 else inputList[0]
}
