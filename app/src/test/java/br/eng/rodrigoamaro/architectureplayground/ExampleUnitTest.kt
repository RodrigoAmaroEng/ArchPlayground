package br.eng.rodrigoamaro.architectureplayground

import io.reactivex.Observable
import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class Fixtures {
    @Test
    fun addition_isCorrect() {
        val d = listOf(RandomManipulator(), OddEvenManipulator())
        val m = listOf(SumManipulator(), MultiplyManipulator())
        Observable.fromIterable(d)
                .reduce(1) { v, f -> f.doStuff(v) }
                .flatMap { v ->
                    Observable.fromIterable(m)
                            .reduce(v) { value, acc -> acc.doStuff(value) }
                }
                .doOnSuccess { println(it) }
                .subscribe()
    }

    interface Manipulator {
        fun doStuff(value: Int): Int
    }

    class SumManipulator : Manipulator {
        override fun doStuff(value: Int): Int {
            return value + 1
        }
    }

    class MultiplyManipulator : Manipulator {
        override fun doStuff(value: Int): Int {
            return value * 2
        }
    }

    class RandomManipulator : Manipulator {
        override fun doStuff(value: Int): Int {
            val nextInt = Random().nextInt(10)
            println("Random $nextInt")
            return value + nextInt
        }
    }

    class OddEvenManipulator : Manipulator {
        override fun doStuff(value: Int): Int {
            return value % 2
        }
    }


}

