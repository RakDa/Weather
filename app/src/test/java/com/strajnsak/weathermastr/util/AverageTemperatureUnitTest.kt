package com.strajnsak.weathermastr.util

import org.junit.Assert
import org.junit.Test

class AverageTemperatureUnitTest {
    @Test
    fun calculateTest() {
        val inputList = listOf(1,2,3)
        val expectedOutput = 2
        val output = AverageTemperature.calculate(inputList)
        Assert.assertEquals(expectedOutput, output)
    }
}