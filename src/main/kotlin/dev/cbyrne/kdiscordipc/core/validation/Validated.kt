package dev.cbyrne.kdiscordipc.core.validation

/**
 * Used for validating an outgoing data structure.
 */
interface Validated {
    /**
     * This should return true or false, based on if your class' data is valid or not (i.e. it shouldn't cause any
     * issues when sent to Discord).
     *
     * If you have any child [Validated] objects, you should call their [validate] method.
     */
    fun validate(): Boolean
}
