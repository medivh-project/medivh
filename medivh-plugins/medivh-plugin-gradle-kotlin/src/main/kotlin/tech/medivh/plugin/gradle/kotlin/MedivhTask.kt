package tech.medivh.plugin.gradle.kotlin

import org.gradle.api.DefaultTask


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
abstract class MedivhTask : DefaultTask() {

    init {
        this.group = "medivh"
    }

}
