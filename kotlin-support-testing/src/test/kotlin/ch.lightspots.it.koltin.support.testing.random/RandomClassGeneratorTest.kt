package ch.lightspots.it.koltin.support.testing.random

import ch.lightspots.it.kotlin.support.testing.random.randomClassInstance
import org.junit.jupiter.api.Test
import strikt.api.expectCatching
import strikt.api.expectThat
import strikt.assertions.*
import java.math.BigDecimal
import java.time.Instant

class RandomClassGeneratorTest {

    @Test
    fun testClassWithEmptyConstructor() {
        class TestClass

        val testClass: TestClass = randomClassInstance()
        expectThat(testClass)
            .isA<TestClass>()
    }

    @Test
    fun testClassWithNonEmptyConstructor() {
        class Foo
        class TestClass(val foo: Foo)

        val testClass: TestClass = randomClassInstance()


        expectThat(testClass)
            .isA<TestClass>()
    }

    @Test
    fun testClassWithPrimitiveConstructorParameters() {
        class TestClass(
            val double: Double,
            val float: Float,
            val long: Long,
            val int: Int,
            val short: Short,
            val byte: Byte,
            val string: String,
            val char: Char,
            val boolean: Boolean,
            val array: Array<String>
        )

        val testClass: TestClass = randomClassInstance()

        expectThat(testClass)
            .isA<TestClass>()
    }

    @Test
    fun testClassWithPrimitiveAndNormalConstructorParameters() {
        class Foo(val string: String)

        class TestClass(
            val foo: Foo,
            val double: Double,
            val float: Float,
            val long: Long,
            val int: Int,
            val short: Short,
            val byte: Byte,
            val string: String,
            val char: Char,
            val boolean: Boolean
        )

        val testClass: TestClass = randomClassInstance()

        expectThat(testClass)
            .isA<TestClass>()
    }

    @Test
    fun testClassWithNullableConstructorParameters() {
        class Foo(val string: String?)
        class TestClass(val foo: Foo?)

        val testClass: TestClass = randomClassInstance()

        expectThat(testClass)
            .isA<TestClass>()
    }

    @Test
    fun testClassWithPrivateConstructor() {
        class TestClass private constructor()

        expectCatching { randomClassInstance<TestClass>() }
            .isFailure()
            .isA<NoSuchElementException>()
            .message isEqualTo "No suitable constructor found for ${TestClass::class}"
    }

    @Test
    fun testClassWithEnumConstructorParameter() {
        class TestClass(val enum: TestEnum)

        val testClass: TestClass = randomClassInstance()

        expectThat(testClass)
            .isA<TestClass>()
            .and {
                get { enum }
                    .isA<TestEnum>()
                    .isOneOf(TestEnum.GO, TestEnum.JAVA, TestEnum.KOTLIN)
            }
    }

    @Test
    fun testClassWithArrayConstructorParameters() {
        class Foo(val string: String?)
        class TestClass(
            val double: DoubleArray,
            val float: FloatArray,
            val long: LongArray,
            val int: IntArray,
            val short: ShortArray,
            val byte: ByteArray,
            val string: Array<String>,
            val char: CharArray,
            val boolean: BooleanArray,
            val foo: Array<Foo>,
            val fooNullable: Array<Foo?>
        )

        val testClass: TestClass = randomClassInstance()

        expectThat(testClass)
            .isA<TestClass>()
    }

    @Test
    fun testClassWithListConstructorParameters() {
        class Foo(val string: String?)
        class TestClass(
            val double: List<Double>,
            val float: List<Float>,
            val long: List<Long>,
            val int: List<Int>,
            val short: List<Short>,
            val byte: List<Byte>,
            val string: List<String>,
            val boolean: List<Boolean>,
            val foo: List<Foo>,
            val fooNullable: List<Foo?>
        )

        val testClass: TestClass = randomClassInstance()

        expectThat(testClass)
            .isA<TestClass>()
    }

    @Test
    fun testClassWithMutableListConstructorParameters() {
        class Foo(val string: String?)
        class TestClass(
            val double: MutableList<Double>,
            val float: MutableList<Float>,
            val long: MutableList<Long>,
            val int: MutableList<Int>,
            val short: MutableList<Short>,
            val byte: MutableList<Byte>,
            val string: MutableList<String>,
            val boolean: MutableList<Boolean>,
            val foo: MutableList<Foo>,
            val fooNullable: MutableList<Foo?>
        )

        val testClass: TestClass = randomClassInstance()

        expectThat(testClass)
            .isA<TestClass>()
    }

    @Test
    fun testClassWithBigDecimal() {
        class TestClass(val decimal: BigDecimal)

        val testClass: TestClass = randomClassInstance()

        expectThat(testClass)
            .isA<TestClass>()
    }

    @Test
    fun testRandomInstantDirect() {
        val now: Instant = randomClassInstance()

        expectThat(now)
            .isA<Instant>()
    }

    @Test
    fun testRandomBigDecimalDirect() {
        val decimal: BigDecimal = randomClassInstance()

        expectThat(decimal)
            .isA<BigDecimal>()
    }

    @Test
    fun testRandomEnumDirect() {
        val enum: TestEnum = randomClassInstance()

        expectThat(enum)
            .isA<TestEnum>()
    }
}

enum class TestEnum {
    KOTLIN,
    JAVA,
    GO
}