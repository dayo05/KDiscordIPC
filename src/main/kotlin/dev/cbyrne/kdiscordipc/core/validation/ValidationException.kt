package dev.cbyrne.kdiscordipc.core.validation

import java.lang.Exception

data class ValidationException(val reason: String) : Exception(reason)
