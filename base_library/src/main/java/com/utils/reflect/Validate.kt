package com.utils.reflect


internal object Validate {

    fun isTrue(expression: Boolean, message: String, vararg values: Any) {
        if (expression == false) {
            throw IllegalArgumentException(String.format(message, *values))
        }
    }
}
