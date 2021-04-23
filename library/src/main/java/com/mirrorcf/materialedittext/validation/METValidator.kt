package com.mirrorcf.materialedittext.validation

/**
 * Base Validator class to either implement or inherit from for custom validation
 */
abstract class METValidator(
        /**
         * Error message that the view will display if validation fails.
         *
         *
         * This is protected, so you can change this dynamically in your [.isValid]
         * implementation. If necessary, you can also interact with this via its getter and setter.
         */
        var errorMessage: String) {

    /**
     * Abstract method to implement your own validation checking.
     *
     * @param text    The CharSequence representation of the text in the EditText field. Cannot be null, but may be empty.
     * @param isEmpty Boolean indicating whether or not the text param is empty
     * @return True if valid, false if not
     */
    abstract fun isValid(text: CharSequence, isEmpty: Boolean): Boolean
}