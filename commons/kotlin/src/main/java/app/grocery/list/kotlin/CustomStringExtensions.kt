package app.grocery.list.kotlin

fun String.ellipsize(maxLength: Int): String =
    if (length <= maxLength) {
        this
    } else {
        take(maxLength) + Typography.ellipsis
    }
