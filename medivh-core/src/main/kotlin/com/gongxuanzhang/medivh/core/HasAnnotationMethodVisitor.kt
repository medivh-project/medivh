package com.gongxuanzhang.medivh.core

import java.io.File


fun main() {
    val dir = File("/Users/gongxuanzhang/workSpace/java/github/kotlin-play/build/classes/kotlin/main")
    dir.listFiles { file -> file.extension == "class" }?.forEach {
        Medivh(it).execute()
    }
}
