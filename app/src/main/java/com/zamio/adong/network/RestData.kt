package com.elcom.com.quizupapp.ui.network

import com.zamio.adong.network.Pagination

/**
 * Created by Hailpt on 3/20/2018.
 */
class RestData<T> {
    var status: Int = 0
    var message: String? = null
    var pagination: Pagination? = null
    var data: T? = null
}