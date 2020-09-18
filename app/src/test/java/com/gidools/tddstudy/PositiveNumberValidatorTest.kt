package com.gidools.tddstudy

import org.hamcrest.core.Is
import org.hamcrest.core.Is.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PositiveNumberValidatorTest {

    private lateinit var SUT: PositiveNumberValidator;

    @Before
    fun setup() {
        SUT = PositiveNumberValidator()
    }

    @Test
    fun test1() {
        var result = SUT.isPositive(-1)
        assertThat(result, `is`(false))

        result = SUT.isPositive(0)
        assertThat(result, `is`(false))

        result = SUT.isPositive(1)
        assertThat(result, `is`(true))
    }
}