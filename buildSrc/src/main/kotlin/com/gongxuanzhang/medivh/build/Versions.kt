package com.gongxuanzhang.medivh.build

import java.io.File
import java.util.*
import kotlin.reflect.KProperty

/**
 * @author gongxuanzhangmelt@gmail.com
 */
class Versions(versionFile: File) {

    private val properties = Properties()

    init {
        versionFile.inputStream().use {
            properties.load(it)
        }
    }

    operator fun getValue(thisRef: Any, property: KProperty<*>): String {
        return properties[property.name].toString()
    }

    override fun toString(): String {
        return properties.toString()
    }


}
