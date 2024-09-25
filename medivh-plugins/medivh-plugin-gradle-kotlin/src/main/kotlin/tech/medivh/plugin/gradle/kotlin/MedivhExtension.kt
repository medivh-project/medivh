package tech.medivh.plugin.gradle.kotlin

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.SetProperty
import tech.medivh.core.MedivhParam
import javax.inject.Inject


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
open class MedivhExtension @Inject constructor(objects: ObjectFactory) {

    val includePackage: SetProperty<String> = objects.setProperty(String::class.java)

    fun include(packageName: String) {
        val currentPackages = includePackage.getOrElse(emptySet())
        includePackage.set(currentPackages + packageName)
    }

    fun skip(): Boolean {
        return includePackage.getOrElse(emptySet()).isEmpty()
    }

    fun toParams(): String {
        val sb = StringBuilder()
        val packageNames = includePackage.getOrElse(emptySet())
        if (packageNames.isNotEmpty()) {
            sb.append("${MedivhParam.INCLUDE.key}=${packageNames.joinToString(",")}")
        }
        return sb.toString()
    }
}
