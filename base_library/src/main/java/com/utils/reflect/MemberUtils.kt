package com.utils.reflect

import java.lang.reflect.AccessibleObject
import java.lang.reflect.Member
import java.lang.reflect.Modifier
import java.util.HashMap

internal object MemberUtils {

    private val ACCESS_TEST = Modifier.PUBLIC or Modifier.PROTECTED or Modifier.PRIVATE
    private val ORDERED_PRIMITIVE_TYPES = arrayOf(
        java.lang.Byte.TYPE,
        java.lang.Short.TYPE,
        Character.TYPE,
        Integer.TYPE,
        java.lang.Long.TYPE,
        java.lang.Float.TYPE,
        java.lang.Double.TYPE
    )

    private val primitiveWrapperMap = HashMap<Class<*>, Class<*>>()

    private val wrapperPrimitiveMap = HashMap<Class<*>, Class<*>>()

    private fun isPackageAccess(modifiers: Int): Boolean {
        return modifiers and ACCESS_TEST == 0
    }

    fun isAccessible(m: Member?): Boolean {
        return m != null && Modifier.isPublic(m.modifiers) && !m.isSynthetic
    }

    fun setAccessibleWorkaround(o: AccessibleObject?): Boolean {
        if (o == null || o.isAccessible) {
            return false
        }
        val m = o as Member?
        if (m != null) {
            if (!o.isAccessible && Modifier.isPublic(m.modifiers) && isPackageAccess(m.declaringClass.modifiers)) {
                try {
                    o.isAccessible = true
                    return true
                } catch (e: SecurityException) { // NOPMD
                    // ignore in favor of subsequent IllegalAccessException
                }

            }
        }
        return false
    }

    fun isAssignable(classArray: Array<Class<*>>?, toClassArray: Array<Class<*>>?, autoboxing: Boolean): Boolean {
        var classArray = classArray
        var toClassArray = toClassArray
        if (ReflectUtils.isSameLength(classArray, toClassArray) == false) {
            return false
        }
        if (classArray == null) {
            classArray = ReflectUtils.EMPTY_CLASS_ARRAY
        }
        if (toClassArray == null) {
            toClassArray = ReflectUtils.EMPTY_CLASS_ARRAY
        }
        if (classArray != null) {
            for (i in classArray.indices) {
                if (isAssignable(classArray[i], toClassArray?.get(i), autoboxing) == false) {
                    return false
                }
            }
        }
        return true
    }

    @JvmOverloads
    fun isAssignable(cls: Class<*>?, toClass: Class<*>?, autoboxing: Boolean = true): Boolean {
        var cls = cls
        if (toClass == null) {
            return false
        }
        // have to check for null, as isAssignableFrom doesn't
        if (cls == null) {
            return !toClass.isPrimitive
        }
        //autoboxing:
        if (autoboxing) {
            if (cls.isPrimitive && !toClass.isPrimitive) {
                cls = primitiveToWrapper(cls)
                if (cls == null) {
                    return false
                }
            }
            if (toClass.isPrimitive && !cls.isPrimitive) {
                cls = wrapperToPrimitive(cls)
                if (cls == null) {
                    return false
                }
            }
        }
        if (cls == toClass) {
            return true
        }
        if (cls.isPrimitive) {
            if (toClass.isPrimitive == false) {
                return false
            }
            if (Integer.TYPE == cls) {
                return (java.lang.Long.TYPE == toClass
                        || java.lang.Float.TYPE == toClass
                        || java.lang.Double.TYPE == toClass)
            }
            if (java.lang.Long.TYPE == cls) {
                return java.lang.Float.TYPE == toClass || java.lang.Double.TYPE == toClass
            }
            if (java.lang.Boolean.TYPE == cls) {
                return false
            }
            if (java.lang.Double.TYPE == cls) {
                return false
            }
            if (java.lang.Float.TYPE == cls) {
                return java.lang.Double.TYPE == toClass
            }
            if (Character.TYPE == cls) {
                return (Integer.TYPE == toClass
                        || java.lang.Long.TYPE == toClass
                        || java.lang.Float.TYPE == toClass
                        || java.lang.Double.TYPE == toClass)
            }
            if (java.lang.Short.TYPE == cls) {
                return (Integer.TYPE == toClass
                        || java.lang.Long.TYPE == toClass
                        || java.lang.Float.TYPE == toClass
                        || java.lang.Double.TYPE == toClass)
            }
            return if (java.lang.Byte.TYPE == cls) {
                (java.lang.Short.TYPE == toClass
                        || Integer.TYPE == toClass
                        || java.lang.Long.TYPE == toClass
                        || java.lang.Float.TYPE == toClass
                        || java.lang.Double.TYPE == toClass)
            } else false
            // should never get here
        }
        return toClass.isAssignableFrom(cls)
    }

    init {
        primitiveWrapperMap[java.lang.Boolean.TYPE] = Boolean::class.java
        primitiveWrapperMap[java.lang.Byte.TYPE] = Byte::class.java
        primitiveWrapperMap[Character.TYPE] = Char::class.java
        primitiveWrapperMap[java.lang.Short.TYPE] = Short::class.java
        primitiveWrapperMap[Integer.TYPE] = Int::class.java
        primitiveWrapperMap[java.lang.Long.TYPE] = Long::class.java
        primitiveWrapperMap[java.lang.Double.TYPE] = Double::class.java
        primitiveWrapperMap[java.lang.Float.TYPE] = Float::class.java
        primitiveWrapperMap[Void.TYPE] = Void.TYPE
    }

    init {
        for (primitiveClass in primitiveWrapperMap.keys) {
            val wrapperClass = primitiveWrapperMap.get(primitiveClass)
            if (primitiveClass != wrapperClass) {
                if (wrapperClass != null) {
                    wrapperPrimitiveMap.put(wrapperClass, primitiveClass)
                }
            }
        }
    }

    fun primitiveToWrapper(cls: Class<*>?): Class<*>? {
        var convertedClass = cls
        if (cls != null && cls.isPrimitive) {
            convertedClass = primitiveWrapperMap[cls]
        }
        return convertedClass
    }

    fun wrapperToPrimitive(cls: Class<*>): Class<*>? {
        return wrapperPrimitiveMap.get(cls)
    }

    fun compareParameterTypes(left: Array<Class<*>>, right: Array<Class<*>>, actual: Array<Class<*>>): Int {
        val leftCost = getTotalTransformationCost(actual, left)
        val rightCost = getTotalTransformationCost(actual, right)
        return if (leftCost < rightCost) -1 else if (rightCost < leftCost) 1 else 0
    }

    private fun getTotalTransformationCost(srcArgs: Array<Class<*>>, destArgs: Array<Class<*>>): Float {
        var totalCost = 0.0f
        for (i in srcArgs.indices) {
            val srcClass: Class<*>
            val destClass: Class<*>
            srcClass = srcArgs[i]
            destClass = destArgs[i]
            totalCost += getObjectTransformationCost(srcClass, destClass)
        }
        return totalCost
    }

    private fun getObjectTransformationCost(srcClass: Class<*>?, destClass: Class<*>): Float {
        var srcCla = srcClass
        if (destClass.isPrimitive) {
            return getPrimitivePromotionCost(srcCla, destClass)
        }
        var cost = 0.0f
        while (srcCla != null && destClass != srcCla) {
            if (destClass.isInterface && isAssignable(srcCla, destClass)) {
                // slight penalty for interface match.
                // we still want an exact match to override an interface match,
                // but
                // an interface match should override anything where we have to
                // get a superclass.
                cost += 0.25f
                break
            }
            srcCla = srcCla.superclass
        }
        /*
         * If the destination class is null, we've travelled all the way up to
         * an Object match. We'll penalize this by adding 1.5 to the cost.
         */
        if (srcClass == null) {
            cost += 1.5f
        }
        return cost
    }

    private fun getPrimitivePromotionCost(srcClass: Class<*>?, destClass: Class<*>): Float {
        var cost = 0.0f
        var cls = srcClass
        if (cls != null) {
            if (!cls.isPrimitive) {
                // slight unwrapping penalty
                cost += 0.1f
                cls = wrapperToPrimitive(cls)
            }
        }
        var i = 0
        while (cls != destClass && i < ORDERED_PRIMITIVE_TYPES.size) {
            if (cls == ORDERED_PRIMITIVE_TYPES[i]) {
                cost += 0.1f
                if (i < ORDERED_PRIMITIVE_TYPES.size - 1) {
                    cls = ORDERED_PRIMITIVE_TYPES[i + 1]
                }
            }
            i++
        }
        return cost
    }
}
