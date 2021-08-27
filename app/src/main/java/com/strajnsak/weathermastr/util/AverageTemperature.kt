package com.strajnsak.weathermastr.util

class AverageTemperature {
    companion object {
        fun calculate(intList: List<Int>): Int{
            var total = 0
            for(i in intList){
                total += i
            }
            return total/intList.size
        }
    }
}