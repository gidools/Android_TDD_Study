package com.gidools.tddstudy

class StringReverser {

    fun reverse(inputString: String): String {
        val stringBuilder = StringBuilder()
        for (index in inputString.length - 1 downTo 0) {
            stringBuilder.append(inputString[index])
        }
        return stringBuilder.toString()
    }
}