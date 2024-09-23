package tech.medivh.core


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
sealed interface ParamResolver<out T> {

    fun resolve(value: String): T
}

data object StringResolver : ParamResolver<String> {

    override fun resolve(value: String): String {
        return value
    }
}

data object IntResolver : ParamResolver<Int> {

    override fun resolve(value: String): Int {
        return value.toInt()
    }
}

data object ListResolver : ParamResolver<List<String>> {

    override fun resolve(value: String): List<String> {
        return value.split(",")
    }
}


