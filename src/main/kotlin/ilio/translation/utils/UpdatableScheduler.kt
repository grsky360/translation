package ilio.translation.utils

import java.util.concurrent.*

@Suppress("UNCHECKED_CAST")
class UpdatableScheduler(private val pollSize: Int = 1) {
    private val queue = DelayQueue<UpdatableTask>()
    private val executor = ThreadPoolExecutor(0, 1, 0, TimeUnit.MILLISECONDS,
        queue as BlockingQueue<Runnable>)

    fun schedule(delay: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS, runnable: Runnable): UpdatableTask {
        return schedule(UpdatableTask(delay, timeUnit, runnable))
    }

    fun schedule(task: UpdatableTask): UpdatableTask {
        if (!queue.contains(task)) {
            executor.execute(task)
        }
        return task
    }
}

data class UpdatableTask(var delay: Long, val timeUnit: TimeUnit = TimeUnit.MILLISECONDS, val runnable: Runnable) : Delayed, Runnable {
    @Volatile
    private var init = System.currentTimeMillis()

    fun reset() {
        init = System.currentTimeMillis()
    }

    override fun compareTo(other: Delayed): Int {
        return delay.compareTo(other.getDelay(timeUnit))
    }

    override fun getDelay(unit: TimeUnit): Long {
        val remain = init + TimeUnit.MILLISECONDS.convert(delay, timeUnit) - System.currentTimeMillis()
        return unit.convert(remain, TimeUnit.MILLISECONDS)
    }

    override fun run() {
        runnable.run()
    }
}
