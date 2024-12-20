package tech.medivh.api


/**
 *
 * add it to the function you want to record time.
 *
 * @author gongxuanzhangmelt@gmail.com
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class DebugTime(val expectTime: Long = 0L)
