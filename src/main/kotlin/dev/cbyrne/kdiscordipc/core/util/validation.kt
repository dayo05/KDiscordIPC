package dev.cbyrne.kdiscordipc.core.util

import dev.cbyrne.kdiscordipc.core.validation.Validated

/**
 * Returns true if all items in the collection result in true when [validate] is called.
 * If any one of the items fail, false will be returned.
 */
fun <T: Validated> Iterable<T>.validate() = all { it.validate() }

/**
 * See [validate]
 */
fun <T: Validated> Iterable<T>?.validate(nullAllowed: Boolean = true) = this?.all { it.validate() } ?: nullAllowed

/**
 * Returns the result of [validate] if the value is not null.
 * Otherwise, false will be returned.
 */
fun <T: Validated> T?.validate() = this?.validate() ?: false

/**
 * Returns false if the string is less than 2 characters or empty, otherwise true
 */
fun String.validate() = length >= 2 && isNotEmpty()

/**
 * See [String.validate].
 */
fun String?.validate(nullAllowed: Boolean = true) = this?.validate() ?: nullAllowed

/**
 * Returns false if the number is less than 0
 */
fun Long.validate() = this >= 0

/**
 * See [Long.validate]
 */
fun Long?.validate(nullAllowed: Boolean = true) = this?.validate() ?: nullAllowed
