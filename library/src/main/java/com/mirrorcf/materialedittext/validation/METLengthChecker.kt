package com.mirrorcf.materialedittext.validation


abstract class METLengthChecker {
    abstract fun getLength(text: CharSequence?): Int
}