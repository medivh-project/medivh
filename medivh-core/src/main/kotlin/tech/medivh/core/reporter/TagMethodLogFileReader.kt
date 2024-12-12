package tech.medivh.core.reporter

import java.io.File
import java.nio.ByteBuffer


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class TagMethodLogFileReader(private val file: File) {


    fun read(action: (TagMethod) -> Unit) {
        ByteBuffer.wrap(file.readBytes()).apply {
            while (this.hasRemaining()) {
                val classNameBox = ByteArray(getInt())
                get(classNameBox)
                val methodNameBox = ByteArray(getInt())
                get(methodNameBox)
                val expectTime = getLong()
                action(TagMethod(String(methodNameBox), String(classNameBox), expectTime))
            }
        }
    }

}
