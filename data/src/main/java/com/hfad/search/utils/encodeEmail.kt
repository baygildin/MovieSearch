package com.hfad.search.utils

fun encodeEmail(str: String) = str.replace(".", "*").lowercase()