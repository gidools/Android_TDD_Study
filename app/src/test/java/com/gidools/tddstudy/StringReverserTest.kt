package com.gidools.tddstudy

import org.hamcrest.core.Is.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class StringReverserTest {

    lateinit var SUT: StringReverser

    @Before
    fun setUp() {
        SUT = StringReverser()
    }

    @Test
    fun reverse_emptyString_emptyStringReturned() {
        val result = SUT.reverse("")
        assertThat(result, `is`(""))
    }

    @Test
    fun reverse_singleCharacter_sameStringReturned() {
        val inputString = "A"
        val result = SUT.reverse(inputString)
        assertThat(result, `is`(inputString))
    }

    @Test
    fun reverse_longString_reversedStringReturned() {
        val inputString = "Giseok Kwon"
        val result = SUT.reverse(inputString)
        assertThat(result, `is`("nowK koesiG"))
    }
}