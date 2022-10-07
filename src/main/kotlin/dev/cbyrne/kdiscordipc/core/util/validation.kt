package dev.cbyrne.kdiscordipc.core.util

import dev.cbyrne.kdiscordipc.core.validation.Validated
import dev.cbyrne.kdiscordipc.core.validation.ValidationException

/**
 * Returns true if all items in the collection result in true when [validate] is called.
 * If any one of the items fail, false will be returned.
 */
fun <T : Validated> Iterable<T>.validate() = forEach { it.validate() }

/**
 * See [validate]
 */
fun <T : Validated> Iterable<T>?.validate(nullAllowed: Boolean = true) {
    check(nullAllowed, "is null")
    this?.forEach { it.validate() }
}

/**
 * Returns the result of [validate] if the value is not null.
 * Otherwise, false will be returned.
 */
fun <T : Validated> T?.validate() = this?.validate() ?: false

/**
 * Returns false if the string is less than 2 characters or empty, otherwise true
 */
fun String.validate() {
    check(isNotEmpty(), "is empty")
    check(length >= 2, "is less than 2")
}

/**
 * See [String.validate].
 */
fun String?.validate(nullAllowed: Boolean = true) = this?.validate() ?: nullAllowed

/**
 * Returns false if the number is less than 0
 */
fun Long.validate() = check(this >= 0, "less than 0")

/**
 * See [Long.validate]
 */
fun Long?.validate(nullAllowed: Boolean = true) {
    check(nullAllowed, "is null")
    this?.validate()
}

fun Any?.check(condition: Boolean, reason: String) =
    if (!condition) throw ValidationException("${this?.javaClass?.kotlin?.simpleName ?: ""}: $reason") else null