package com.elcom.com.quizupapp.ui.network

/**
 * Created by admin on 3/8/2018.
 */
class ConstantAPI {
    class BASE_URL {
        val bar ="http://192.168.28.27/e-quiz/public/api/"
    }

}

enum class Team(val type: String) {
    ADONG("ADONG"),
    CONTRACTOR("CONTRACTOR"),
    STOCK("STOCK"),
}

enum class PROJECTTYPE(val type: String) {
    NEW("NEW"),
    PRROCESSING("PRROCESSING"),
    PAUSED("PAUSED"),
    DONE("DONE")
}