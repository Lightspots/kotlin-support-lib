package ch.lightspots.it.kotlin.defer

class Deferrer {
    private val deferredActions = mutableListOf<() -> Unit>()

    /**
     * defer the execution of the given function until leaving this block.
     */
    fun defer(f: () -> Unit) {
        deferredActions.add(f)
    }

    fun runActions() {
        for (i in deferredActions.size - 1 downTo 0) {
            deferredActions[i]()
        }
    }
}

/**
 * Enables Go like defer [(Tour of Go)](https://tour.golang.org/flowcontrol/12) in the given body.
 */
inline fun <T> withDefer(body: Deferrer.() -> T): T {
    val deferrer = Deferrer()
    return try {
        deferrer.body()
    } finally {
        deferrer.runActions()
    }
}