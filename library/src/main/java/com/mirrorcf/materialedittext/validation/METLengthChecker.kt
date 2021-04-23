package com.mirrorcf.materialedittext.validation

/**
 * Created by mariotaku on 15/4/12.
 */
abstract class METLengthChecker {
    abstract fun getLength(text: CharSequence?): Int
}