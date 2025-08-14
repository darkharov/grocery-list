package app.grocery.list.commons.format

fun String.ellipsize(maxLength: Int): String =
    if (length <= maxLength) {
        this
    } else {
        take(maxLength) + Typography.ellipsis
    }
