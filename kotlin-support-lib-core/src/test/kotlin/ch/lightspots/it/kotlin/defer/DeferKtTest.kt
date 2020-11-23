package ch.lightspots.it.kotlin.defer

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import strikt.api.expectThat
import strikt.assertions.isEqualTo

internal class DeferKtTest {

    @Test
    fun testOneDeferExecuted() {
        val results = mutableListOf<Int>()
        withDefer {
            defer { results.add(10) }

            results.add(0)
        }

        expectThat(results) isEqualTo mutableListOf(0, 10)
    }

    @Test
    fun testOrderOfDeferIsLastInFirstOut() {
        val results = mutableListOf<Int>()
        withDefer {
            defer { results.add(10) }

            defer { results.add(20) }

            results.add(0)

            defer { results.add(30) }
        }
        expectThat(results) isEqualTo mutableListOf(0, 30, 20, 10)
    }

    @Test
    fun testDeferExecutedWhenExceptionIsThrown() {
        val results = mutableListOf<Int>()
        assertThrows<IllegalArgumentException> {
            withDefer {
                defer { results.add(10) }

                throw IllegalArgumentException("Test")
            }
        }

        expectThat(results) isEqualTo mutableListOf(10)
    }
}