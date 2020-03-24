package com.elcom.com.quizupapp.utils

import android.content.Context

/**
 * Created by Hailpt on 3/21/2018.
 */
class PreferUtils {

    private val PREFER_NAME = "quizup"
    private val PREFER_GCM_TOKEN = "gcm_token"
    private val PREFER_AVATAR_MYSELF = "PREFER_AVATAR_MYSELF"
    private val USER_ID = "user_id"
    private val CHANLLENGE_TIME_TO_INTIVE = "CHANLLENGE_TIME_TO_INTIVE"
    private val KEY_ENCRYPTION = "kenc"

//    private val applicationContext  = QuizUpApp().getAppContext()!!

    fun setToken(context: Context, token: String) {
        val editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit()
        editor.putString(PREFER_GCM_TOKEN, token)
        editor.commit()
    }

    fun getToken(context: Context): String? {
        val preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE)
        return preferences.getString(PREFER_GCM_TOKEN, "")
    }

    fun setAvatar(context: Context, token: String) {
        val editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit()
        editor.putString(PREFER_AVATAR_MYSELF, token)
        editor.commit()
    }

    public  fun getAvatar(context: Context): String? {
        val preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE)
        return preferences.getString(PREFER_AVATAR_MYSELF, "")
    }

    fun setUserId(context: Context, token: String) {
        val editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit()
        editor.putString(USER_ID, token)
        editor.commit()
    }

    fun getUserId(context: Context): String? {
        val preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE)
        return preferences.getString(USER_ID, "")
    }


    fun setChallengeTimeToInviteFriend(context: Context, time: String) {
        val editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit()
        editor.putString(CHANLLENGE_TIME_TO_INTIVE, time)
        editor.commit()
    }

    fun getChallengeTimeToInviteFriend(context: Context): String? {
        val preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE)
        return preferences.getString(CHANLLENGE_TIME_TO_INTIVE, "")
    }


}