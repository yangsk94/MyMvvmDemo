package com.utils.reflect

import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.HashMap

import com.utils.reflect.ReflectUtils.print


/**
 * @author yangsk
 *
 * 反射强制清除第三方内部持有APP的引用数据工具类
 */
object FieldUtils {

    private val sFieldCache = HashMap<String, Field>()

    private fun getKey(cls: Class<*>, fieldName: String): String {
        val sb = StringBuilder()
        sb.append(cls.toString()).append("#").append(fieldName)
        return sb.toString()
    }

    private fun getField(cls: Class<*>?, fieldName: String, forceAccess: Boolean): Field? {
        Validate.isTrue(cls != null, "The class must not be null")
        Validate.isTrue(!ReflectUtils.isEmpty(fieldName), "The field name must not be blank/empty")

        val key = cls?.let { getKey(it, fieldName) }
        val cachedField: Field?
        synchronized(sFieldCache) {
            cachedField = sFieldCache[key]
        }
        if (cachedField != null) {
            if (forceAccess && !cachedField.isAccessible) {
                cachedField.isAccessible = true
            }
            return cachedField
        }

        // check up the superclass hierarchy
        var acls = cls
        while (acls != null) {
            try {
                val field = acls.getDeclaredField(fieldName)
                // getDeclaredField checks for non-public scopes as well
                // and it returns accurate results
                if (!Modifier.isPublic(field.modifiers)) {
                    if (forceAccess) {
                        field.isAccessible = true
                    } else {
                        acls = acls.superclass
                        continue
                    }
                }
                synchronized(sFieldCache) {
                    key?.let { sFieldCache.put(it, field) }
                }
                return field
            } catch (ex: NoSuchFieldException) { // NOPMD
                // ignore
            }

            acls = acls?.superclass
        }
        // check the public interface case. This must be manually searched for
        // incase there is a public supersuperclass field hidden by a private/package
        // superclass field.
        var match: Field? = null
        for (class1 in ReflectUtils.getAllInterfaces(cls)) {
            try {
                val test = class1.getField(fieldName)
                cls?.let {
                    Validate.isTrue(
                        match == null,
                        "Reference to field %s is ambiguous relative to %s" + "; a matching field exists on two or more implemented interfaces.",
                        fieldName,
                        it
                    )
                }
                match = test
            } catch (ex: NoSuchFieldException) { // NOPMD
                // ignore
            }

        }
        synchronized(sFieldCache) {
            if (match != null && key != null) {
                sFieldCache.put(key, match)
            }
        }
        return match
    }

    @Throws(IllegalAccessException::class)
    fun readField(field: Field?, target: Any?, forceAccess: Boolean = true): Any? {
        Validate.isTrue(field != null, "The field must not be null")
        if (field != null) {
            if (forceAccess && !field.isAccessible) {
                field.isAccessible = true
            } else {
                MemberUtils.setAccessibleWorkaround(field)
            }
        }
        return field?.get(target)
    }


    @Throws(IllegalAccessException::class)
    fun writeField(field: Field?, target: Any?, value: Any, forceAccess: Boolean = true) {
        Validate.isTrue(field != null, "The field must not be null")
        if (field != null) {
            if (forceAccess && !field.isAccessible) {
                field.isAccessible = true
            } else {
                MemberUtils.setAccessibleWorkaround(field)
            }
        }
        if (field != null) {
            field.set(target, value)
        }
    }

    fun getField(cls: String, fieldName: String): Field? {
        try {
            return getField(Class.forName(cls), fieldName, true)
        } catch (ignore: Throwable) {
            print(ignore)
        }

        return null
    }

    fun getField(cls: Class<*>, fieldName: String): Field? {
        return getField(cls, fieldName, true)
    }

    @Throws(IllegalAccessException::class)
    fun readField(target: Any, fieldName: String): Any? {
        Validate.isTrue(target != null, "target object must not be null")
        val cls = target.javaClass
        val field = getField(cls, fieldName, true)
        Validate.isTrue(field != null, "Cannot locate field %s on %s", fieldName, cls)
        // already forced access above, don't repeat it here:
        return readField(field, target, false)
    }

    @Throws(IllegalAccessException::class)
    fun readField(target: Any, fieldName: String, forceAccess: Boolean): Any? {
        Validate.isTrue(target != null, "target object must not be null")
        val cls = target.javaClass
        val field = getField(cls, fieldName, forceAccess)
        Validate.isTrue(field != null, "Cannot locate field %s on %s", fieldName, cls)
        // already forced access above, don't repeat it here:
        return readField(field, target, forceAccess)
    }

    @Throws(IllegalAccessException::class)
    @JvmOverloads
    fun writeField(target: Any, fieldName: String, value: Any, forceAccess: Boolean = true) {
        Validate.isTrue(target != null, "target object must not be null")
        val cls = target.javaClass
        val field = getField(cls, fieldName, true)
        Validate.isTrue(field != null, "Cannot locate declared field %s.%s", cls.name, fieldName)
        // already forced access above, don't repeat it here:
        writeField(field, target, value, forceAccess)
    }

    @Throws(IllegalAccessException::class)
    fun readStaticField(field: Field, forceAccess: Boolean): Any? {
        Validate.isTrue(field != null, "The field must not be null")
        Validate.isTrue(Modifier.isStatic(field.modifiers), "The field '%s' is not static", field.name)
        return readField(field, (null as Any?), forceAccess)
    }

    @Throws(IllegalAccessException::class)
    fun readStaticField(cls: String, fieldName: String): Any? {
        try {
            return readStaticField(Class.forName(cls), fieldName)
        } catch (ignored: Throwable) {
            print(ignored)
        }

        return null
    }

    @Throws(IllegalAccessException::class)
    fun readStaticField(cls: Class<*>, fieldName: String): Any? {
        val field = getField(cls, fieldName, true)
        Validate.isTrue(field != null, "Cannot locate field '%s' on %s", fieldName, cls)
        // already forced access above, don't repeat it here:
        return field?.let { readStaticField(it, true) }
    }

    @Throws(IllegalAccessException::class)
    fun writeStaticField(field: Field, value: Any, forceAccess: Boolean) {
        Validate.isTrue(field != null, "The field must not be null")
        Validate.isTrue(
            Modifier.isStatic(field.modifiers), "The field %s.%s is not static", field.declaringClass.name,
            field.name
        )
        writeField(field, (null as Any?), value, forceAccess)
    }

    @Throws(IllegalAccessException::class)
    fun writeStaticField(cls: String, fieldName: String, value: Any) {
        try {
            writeStaticField(Class.forName(cls), fieldName, value)
        } catch (ignore: Throwable) {
            print(ignore)
        }

    }

    @Throws(IllegalAccessException::class)
    fun writeStaticField(cls: Class<*>, fieldName: String, value: Any) {
        val field = getField(cls, fieldName, true)
        Validate.isTrue(field != null, "Cannot locate field %s on %s", fieldName, cls)
        // already forced access above, don't repeat it here:
        if (field != null) {
            writeStaticField(field, value, true)
        }
    }

    fun getDeclaredField(cls: String, fieldName: String, forceAccess: Boolean): Field? {
        try {
            return getDeclaredField(Class.forName(cls), fieldName, forceAccess)
        } catch (ignore: Throwable) {
            print(ignore)
        }

        return null
    }

    fun getDeclaredField(cls: Class<*>, fieldName: String, forceAccess: Boolean): Field? {
        Validate.isTrue(cls != null, "The class must not be null")
        Validate.isTrue(!ReflectUtils.isEmpty(fieldName), "The field name must not be blank/empty")
        try {
            // only consider the specified class by using getDeclaredField()
            val field = cls.getDeclaredField(fieldName)
            if (!MemberUtils.isAccessible(field)) {
                if (forceAccess) {
                    field.isAccessible = true
                } else {
                    return null
                }
            }
            return field
        } catch (e: NoSuchFieldException) { // NOPMD
            // ignore
        }

        return null
    }

    @Throws(IllegalAccessException::class)
    fun writeDeclaredField(target: Any, fieldName: String, value: Any) {
        Validate.isTrue(target != null, "target object must not be null")
        val cls = target.javaClass
        val field = getDeclaredField(cls, fieldName, true)
        Validate.isTrue(field != null, "Cannot locate declared field %s.%s", cls.name, fieldName)
        // already forced access above, don't repeat it here:
        writeField(field, target, value, false)
    }


}
