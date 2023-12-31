package net.knowfx.yaodonghui.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import net.knowfx.yaodonghui.BuildConfig
import net.knowfx.yaodonghui.entities.ForgetPawd
import net.knowfx.yaodonghui.http.APIs
import net.knowfx.yaodonghui.http.HttpClientManager
import net.knowfx.yaodonghui.utils.SingleSourceLiveData
import net.knowfx.yaodonghui.entities.Result
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

class LoginRegisterViewModel(app: Application) : CommonViewModel(app) {

    private val server =  HttpClientManager.instance.client.createService(MyServer::class.java)

    val registerResult = SingleSourceLiveData<Any>()

    val modifyPwdResult = SingleSourceLiveData<Result<Boolean>>()

    val loginResult = SingleSourceLiveData<Any>()




    /**
     * 注册账号
     */
    fun registerAccount(phone: String, pwd: String, rePwd: String, code: String) {
        registerResult.setSource(server.registerAccount(phone, pwd, rePwd, code))
    }

    /**
     * 忘记密码
     */
    fun modifyPwd(phone: String, code: String, pwd: String) {
        modifyPwdResult.setSource(server.modifyPwd(ForgetPawd(phone, code, pwd)))
    }

    /**
     * 使用手机号密码登录
     */
    fun loginWithPwd(phone: String, pwd: String) {
        loginResult.setSource(server.loginWithPwd(phone, pwd))
    }

    /**
     * 使用手机号验证码登录
     */
    fun loginWithCode(phone: String, code: String) {
        loginResult.setSource(server.loginWithCode(phone, code))
    }

    /**
     * 修改密码
     */
    fun changePwd(oldPwd: String, newPwd: String) {
        modifyPwdResult.setSource(server.changePwd(oldPwd, newPwd))
    }
    private interface MyServer {

        @POST(APIs.URL_REGISTER)
        fun registerAccount(
            @Query("phone") phone: String,
            @Query("password") pwd: String,
            @Query("repassword") rePwd: String,
            @Query("code") code: String
        ): LiveData<Any>

        @POST(APIs.URL_FORGET_PWD)
        fun modifyPwd(
            @Body request: ForgetPawd
        ): LiveData<Result<Boolean>>

        @POST(APIs.URL_LOGIN_PWD)
        fun loginWithPwd(
            @Query("phone") phone: String,
            @Query("password") pwd: String
        ): LiveData<Any>

        @POST(APIs.URL_LOGIN_CODE)
        fun loginWithCode(
            @Query("phone") phone: String,
            @Query("code") code: String
        ): LiveData<Any>

        @POST(APIs.URL_CHANGE_PWD)
        fun changePwd(
            @Query("oldPassword") oldPwd: String,
            @Query("newPassword") newPwd: String
        ): LiveData<Result<Boolean>>
    }
}