import java.util.concurrent.*

//val data = intArrayOf(1, 2, 3, 4, 5, 6)

fun main(data: IntArray): Int {
    return data.takeIf { it.isNotEmpty() }?.let {
        val res = calculateSum(data)
        println("Sum: $res")
        res
    } ?: 0
}

fun calculateSum(data: IntArray): Int {
    val queue = ConcurrentLinkedDeque<Int>()
    queue.addAll(listOf(*data.toTypedArray()))

    val executor = Executors.newFixedThreadPool(data.size / 2)
    var countWave = 1
    while (queue.size > 1) {
        println(queue)
        val tasks = mutableListOf<Callable<Int>>()
        println("Wave: $countWave")
        countWave++
        for (i in 0 until queue.size / 2) {
            tasks.add(CallableTask(queue.pollFirst(), queue.pollLast()))
        }
        val futures: List<Future<Int>> = executor.invokeAll(tasks)

        futures.forEach {
            queue.add(it.get())
        }
    }

    executor.shutdown()
    executor.awaitTermination(1, TimeUnit.MINUTES)

    return queue.poll()
}

class CallableTask(private val num1: Int, private val num2: Int) : Callable<Int> {
    override fun call(): Int {
        println("$num1 + $num2")
        return num1 + num2
    }
}