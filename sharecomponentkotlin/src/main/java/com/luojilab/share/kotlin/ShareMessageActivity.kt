package com.luojilab.share.kotlin

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import com.luojilab.component.componentlib.service.AutowiredService
import com.luojilab.componentservice.share.bean.Author
import com.luojilab.router.facade.annotation.Autowired
import com.luojilab.router.facade.annotation.RouteNode

/**
 * Created by mrzhang on 2017/12/29.
 */
@RouteNode(path = "/shareMagazine", desc = "分享杂志页面")
class ShareMessageActivity : Activity() {

    @Autowired(name = "bookName")
    @JvmField
    var magazineName: String? = null

    @Autowired
    @JvmField
    var author: Author? = null

    var tvShareTitle: TextView? = null
    var tvShareBook: TextView? = null
    var tvAuthor: TextView? = null
    var tvCounty: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AutowiredService.Factory.getInstance().create().autowire(this)
        setContentView(R.layout.share_activity_share)

        tvShareTitle = findViewById(R.id.share_title) as TextView?
        tvShareBook = findViewById(R.id.share_tv_tag) as TextView?
        tvAuthor = findViewById(R.id.share_tv_author) as TextView?
        tvCounty = findViewById(R.id.share_tv_county) as TextView?


        tvShareTitle?.text = "Magazine"
        tvShareBook?.setText(magazineName)
        tvAuthor?.setText(author?.name ?: "zmq")
        tvCounty!!.setText(author?.county ?: "China")

    }

}