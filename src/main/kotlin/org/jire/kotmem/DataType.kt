@file:JvmMultifileClass
@file:JvmName("Kotmem")

package org.jire.kotmem

import org.jire.kotmem.DataType.*
import java.util.*

sealed class DataType<T : Any>(val bytes: Int, val read: NativeBuffer.() -> T, val write: NativeBuffer.(T) -> Any) {

	object ByteDataType : DataType<Byte>(1, { byte() }, { byte(it) })

	object ShortDataType : DataType<Short>(2, { short() }, { short(it) })

	object IntDataType : DataType<Int>(4, { int() }, { int(it) })

	object LongDataType : DataType<Long>(8, { long() }, { long(it) })

	object FloatDataType : DataType<Float>(4, { float() }, { float(it) })

	object DoubleDataType : DataType<Double>(8, { double() }, { double(it) })

	object BooleanDataType : DataType<Boolean>(1, { byte() > 0 }, { byte((if (it) 1 else 0).toByte()) })

}

private val classToType by lazy {
	val map = HashMap<Class<*>, DataType<*>>()

	map[java.lang.Byte::class.java] = ByteDataType
	map[java.lang.Short::class.java] = ShortDataType
	map[java.lang.Integer::class.java] = IntDataType
	map[java.lang.Long::class.java] = LongDataType
	map[java.lang.Float::class.java] = FloatDataType
	map[java.lang.Double::class.java] = DoubleDataType
	map[java.lang.Boolean::class.java] = BooleanDataType

	Collections.unmodifiableMap(map)
}

fun <T : Any> dataTypeOf(`class`: Class<T>) = classToType[`class`] as DataType<T>