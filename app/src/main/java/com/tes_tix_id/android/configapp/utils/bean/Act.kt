package com.tes_tix_id.android.configapp.utils.bean

class Act : BaseBean() {
    var is_exipired = true //表示活动是否过期 默认活动过期不展示
    var act_id //活动id
            : String? = null
    var act_title //活动标题
            : String? = null
    var act_des //活动描述
            : String? = null
    var act_cover //活动封面图片地址
            : String? = null
    var act_link //活动链接地址
            : String? = null
}