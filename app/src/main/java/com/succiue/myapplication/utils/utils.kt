package com.succiue.myapplication.utils

import android.content.Intent
import android.os.Build
import java.io.Serializable


fun <T : Serializable?> Intent.getSerializable(key: String, m_class: Class<T>): T {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        this.getSerializableExtra(key, m_class)!!
    else
        this.getSerializableExtra(key) as T

}


inline fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, R : Any> multipleNonNull(
    p1: T1?,
    p2: T2?,
    p3: T3?,
    p4: T4?,
    block: (T1, T2, T3, T4) -> R?
): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null) block(
        p1,
        p2,
        p3,
        p4
    ) else null
}