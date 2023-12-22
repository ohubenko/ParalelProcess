import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class MainKtTest {

    @Test
    fun testSimpleArray() {
        val input = mutableListOf(1, 2, 3, 4, 5, 6).toIntArray()
        val result = main(input)
        assertEquals(21, result)
    }

    @Test
    fun testHundredArray() {
        val input = (1..100).toMutableList().toIntArray()
        val result = main(input)
        assertEquals(5050, result)
    }

    @Test
    fun testThousandArray() {
        val input = (1..1000).toMutableList().toIntArray()
        val result = main(input)
        assertEquals(500500, result)
    }

    @Test
    fun testTenThousandArray() {
        val input = (1..10_000).toMutableList().toIntArray()
        val result = main(input)
        assertEquals(50005000, result)
    }

    @Test
    fun testArray() {
        val input = (1..100_000).toMutableList().toIntArray()
        val result = main(input)
        assertEquals(50005000, result)
    }

    @Test
    fun testArrayWithZeros() {
        val input = mutableListOf(0, 0, 0, 0, 0, 0).toIntArray()
        val result = main(input)
        assertEquals(0, result)
    }

    @Test
    fun testArrayWithNegativeNumbers() {
        val input = mutableListOf(-1, -2, -3, 3, 2, 1).toIntArray()
        val result = main(input)
        assertEquals(0, result)
    }

    @Test
    fun testArrayWithSingleElement() {
        val input = mutableListOf(5).toIntArray()
        val result = main(input)
        assertEquals(5, result)
    }

    @Test
    fun testEmptyArray() {
        val input = mutableListOf<Int>().toIntArray()
        val result = main(input)
        assertEquals(0, result)  // Assuming 0 for an empty array
    }
}