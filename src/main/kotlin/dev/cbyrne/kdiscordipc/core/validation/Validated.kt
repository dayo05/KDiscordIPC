package dev.cbyrne.kdiscordipc.core.validation

/**
 * Used for validating an outgoing data structure.
 */
interface Validated {
    /**
     * This should throw an exception if your class' data is not valid (i.e. it wouldn't be accepted by Discord).
     * If you have any child [Validated] objects, you should call their [validate] method.
     */
    fun validate()
}
