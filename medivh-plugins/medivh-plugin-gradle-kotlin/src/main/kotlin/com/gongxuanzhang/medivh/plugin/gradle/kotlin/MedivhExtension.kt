package com.gongxuanzhang.medivh.plugin.gradle.kotlin

import com.gongxuanzhang.medivh.core.MedivhParam


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
open class MedivhExtension {

    private val includePackage = mutableSetOf<String>()

    fun include(packages: String) {
        includePackage.add(packages)
    }


    internal fun toParams(): String {
        val sb = StringBuilder()
        if (includePackage.isNotEmpty()) {
            sb.append("${MedivhParam.INCLUDE.key}=${includePackage.joinToString(",")}")
        }
        return sb.toString()
    }
}
