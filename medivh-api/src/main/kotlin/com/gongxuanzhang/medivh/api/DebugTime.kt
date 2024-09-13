package com.gongxuanzhang.medivh.api


/**
 *
 * add it to the function you want to record time.
 * 
 * @author gongxuanzhangmelt@gmail.com
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class DebugTime
