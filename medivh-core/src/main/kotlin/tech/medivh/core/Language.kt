package tech.medivh.core

import java.util.*


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
enum class Language(val bundle: ResourceBundle) {

    ZH(ResourceBundle.getBundle("i18n/message", Locale.CHINA)),
    EN(ResourceBundle.getBundle("i18n/message", Locale.ENGLISH));


}
