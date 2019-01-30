package ch.lightspots.it.kotlin.defer

import org.amshove.kluent.`should equal`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class DeferKtTest {

  @Test
  fun testOneDeferExecuted() {
    val results = mutableListOf<Int>()
    withDefer {
      defer { results.add(10) }

      results.add(0)
    }

    listOf(0, 10) `should equal` results
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
    listOf(0, 30, 20, 10) `should equal` results
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
    listOf(10) `should equal` results
  }
}