package com.example.petfinder.extensions

inline fun <A, B, R> ifNotNull(a: A?, b: B?, code: (A, B) -> R): R? =
    if (a != null && b != null) {
        code(a, b)
    } else null