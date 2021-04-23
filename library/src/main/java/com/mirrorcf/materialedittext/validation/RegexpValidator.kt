package com.mirrorcf.materialedittext.validation

import java.util.regex.Pattern

/**
 * Custom validator for Regexes
 */
class RegexpValidator : METValidator {
    private var pattern: Pattern

    constructor(errorMessage: String, regex: String) : super(errorMessage) {
        pattern = Pattern.compile(regex)
    }

    constructor(errorMessage: String, pattern: Pattern) : super(errorMessage) {
        this.pattern = pattern
    }

    override fun isValid(text: CharSequence, isEmpty: Boolean): Boolean {
        return pattern.matcher(text).matches()
    }
}