package com.baygildins.search.utils

fun encodeEmail(str: String) = str.replace(".", "*").lowercase()