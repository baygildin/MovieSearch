package com.sbaygildin.search.utils

fun encodeEmail(str: String) = str.replace(".", "*").lowercase()