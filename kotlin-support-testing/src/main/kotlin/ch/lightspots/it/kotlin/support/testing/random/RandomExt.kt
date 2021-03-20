package ch.lightspots.it.kotlin.support.testing.random

import java.lang.reflect.Array.newInstance
import java.math.BigDecimal
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KTypeProjection
import kotlin.reflect.KVisibility

private const val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789äöüàéè겨ലɬؔ誁߮"

fun Random.nextChar() = source.random(this)

fun Random.nextString(length: Int = 100): String = (1..length)
    .map { source.random(this) }
    .joinToString("")

fun Random.nextString(minLength: Int, maxLength: Int): String = (1..nextInt(minLength, maxLength))
    .map { source.random(this) }
    .joinToString("")

val randomClassGeneratorDefault = RandomClassGenerator(Random.Default)

/**
 * Creates an instance of [T]. If [T] has a parameterless public constructor then it will be used to create an instance of this class,
 * otherwise a constructor with minimal number of parameters will be used with randomly-generated values.
 *
 * @throws NoSuchElementException if [T] has no public constructor.
 */
inline fun <reified T : Any> randomClassInstance() = randomClassGeneratorDefault.randomClassInstance<T>()

data class Configuration(
    val arraySize: IterableSize = IterableSize(0, 25),
    val listSize: IterableSize = IterableSize(0, 25),
    val stringLength: IterableSize = IterableSize(10, 100)
)

data class IterableSize(val minSize: Int, val maxSize: Int)

class RandomClassGenerator(private val random: Random, private val config: Configuration = Configuration()) {

    /**
     * Creates an instance of [T]. If [T] has a parameterless public constructor then it will be used to create an instance of this class,
     * otherwise a constructor with minimal number of parameters will be used with randomly-generated values.
     *
     * @throws NoSuchElementException if [T] has no public constructor.
     */
    inline fun <reified T : Any> randomClassInstance() = T::class.randomClassInstance()

    @JvmSynthetic
    @PublishedApi
    internal fun <T : Any> KClass<T>.randomClassInstance(): T {
        val instance = this.constructors.find { it.parameters.isEmpty() && it.visibility == KVisibility.PUBLIC }?.call()

        return if (instance != null) instance else {
            val constructor = this.constructors
                .filter { it.visibility == KVisibility.PUBLIC }
                .minByOrNull { it.parameters.size }
                ?: throw NoSuchElementException("No suitable constructor found for $this")

            val params = constructor.parameters
                .map { param ->
                    val klass = param.type.classifier as KClass<*>
                    when {
                        klass.isPrimitive() -> klass.randomPrimitive()
                        klass.java.isEnum -> klass.randomEnum()
                        klass.java.isArray -> klass.randomArray()
                        klass == List::class -> randomList(param.type.arguments)
                        klass == MutableList::class -> randomList(param.type.arguments).toMutableList()
                        klass == Set::class -> randomList(param.type.arguments).toSet()
                        klass == MutableSet::class -> randomList(param.type.arguments).toMutableSet()
                        klass == Instant::class -> randomInstant()
                        klass == BigDecimal::class -> randomBigDecimal()
                        else -> klass.randomClassInstance()
                    }
                }
                .toTypedArray()

            constructor.call(*params)
        }
    }

    /**
     * @return true if this class represents a primitive type which can be handled in [randomPrimitive].
     */
    private fun KClass<*>.isPrimitive(): Boolean = when (this) {
        Double::class,
        Float::class,
        Long::class,
        Int::class,
        Short::class,
        Byte::class,
        String::class,
        Char::class,
        Boolean::class -> true
        else -> false
    }

    /**
     * Handles generation of primitive types since they do not have a public constructor.
     */
    private fun KClass<*>.randomPrimitive(): Any? {
        return when (this) {
            Double::class -> random.nextDouble()
            Float::class -> random.nextFloat()
            Long::class -> random.nextLong()
            Int::class -> random.nextInt()
            Short::class -> random.nextInt().toShort()
            Byte::class -> random.nextInt().toByte()
            String::class -> random.nextString(config.stringLength.minSize, config.stringLength.maxSize)
            Char::class -> random.nextChar()
            Boolean::class -> random.nextBoolean()
            else -> null
        }
    }

    /**
     * Handles generation of enums types since they do not have a public constructor.
     */
    private fun KClass<*>.randomEnum(): Any? {
        return this.java.enumConstants.random(random)
    }

    @Suppress("UNCHECKED_CAST")
    private fun KClass<*>.randomArray(): Any? {
        val componentType = java.componentType.kotlin
        if (componentType.java.isArray) {
            TODO("Support arrays with more than one dimension")
        }
        val size = random.nextInt(config.arraySize.minSize, config.arraySize.maxSize)
        if (componentType != String::class && componentType.isPrimitive()) {
            return when (componentType) {
                Double::class -> DoubleArray(size) { random.nextDouble() }
                Float::class -> FloatArray(size) { random.nextFloat() }
                Long::class -> LongArray(size) { random.nextLong() }
                Int::class -> IntArray(size) { random.nextInt() }
                Short::class -> ShortArray(size) { random.nextInt().toShort() }
                Byte::class -> ByteArray(size) { random.nextInt().toByte() }
                Char::class -> CharArray(size) { random.nextChar() }
                Boolean::class -> BooleanArray(size) { random.nextBoolean() }
                else -> throw IllegalStateException("Should not happen")
            }
        }
        val array = newInstance(componentType.java, size) as Array<Any?>
        for (i in array.indices) {
            array[i] = componentType.randomClassInstance()
        }
        return array
    }

    @Suppress("UNCHECKED_CAST")
    private fun randomList(arguments: List<KTypeProjection>): List<Any?> {
        // TODO Generate null members for nullable list elements
        val componentType = arguments.first().type!!.classifier as KClass<*>
        val list = (0 until random.nextInt(config.listSize.minSize, config.listSize.maxSize))

        if (componentType.isPrimitive()) {
            return list.map { componentType.randomPrimitive() }
        }

        return list
            .map { componentType.randomClassInstance() }
    }

    private fun randomInstant(): Instant {
        val until = Instant.now().plus(5 * 365, ChronoUnit.DAYS).toEpochMilli()
        val millis = random.nextLong(until)
        return Instant.ofEpochMilli(millis)
    }

    private fun randomBigDecimal(): BigDecimal {
        return random.nextDouble().toBigDecimal()
    }
}