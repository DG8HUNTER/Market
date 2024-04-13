package com.example.test.Functions

fun searchElement(list: MutableList<HashMap<String, Any?>>, key: String, value: Any): Boolean {
    for (map in list) {
        if (map[key] == value) {
            return true
        }
    }
    return false
}

