package net.knowfx.yaodonghui.utils

import android.content.Context
import android.util.Log
import java.io.InputStream
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object SSLSocketFactoryUtils {
    const val KEY_STORE_CLIENT_PATH = "kjsl.cer" //cer文件
    /*
     * 默认信任所有的证书
     * */
    fun createSSLSocketFactory(context: Context?): SSLSocketFactory? {
        var sslSocketFactory: SSLSocketFactory? = null
        try {
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, arrayOf<TrustManager?>(createTrustAllManager()), SecureRandom())
            sslSocketFactory = sslContext.socketFactory
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return sslSocketFactory
    }

    /**
     * 获得指定流中的服务器端证书库
     */
    fun getTurstManager(vararg certificates: InputStream?): Array<TrustManager> {
        try {
            val certificateFactory = CertificateFactory.getInstance("X.509")
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            keyStore.load(null, null)
            var index = 0
            for (certificate in certificates) {
                if (certificate == null) {
                    continue
                }
                var certificate1: Certificate?
                certificate1 = try {
                    certificateFactory.generateCertificate(certificate)
                } finally {
                    certificate.close()
                }
                val certificateAlias = Integer.toString(index++)
                keyStore.setCertificateEntry(certificateAlias, certificate1)
            }
            val trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory
                    .getDefaultAlgorithm()
            )
            trustManagerFactory.init(keyStore)
            return trustManagerFactory.trustManagers
        } catch (e: Exception) {
            Log.e("httpDebug", "SSLSocketFactoryUtils", e)
        }
        return trustManagers
    }

    /**
     * 获得信任所有服务器端证书库
     */
    lateinit var trustManagers: Array<TrustManager>

    fun getTrustAllManager(): Array<X509TrustManager>{
        return arrayOf<X509TrustManager>(MyX509TrustManager())
    }

    fun createTrustAllManager(): X509TrustManager? {
        var tm: X509TrustManager? = null
        try {
            tm = object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                    //do nothing，接受任意客户端证书
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                    //do nothing，接受任意服务端证书
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
        } catch (e: Exception) {
        }
        return tm
    }

    class MyX509TrustManager : X509TrustManager {
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            println("cert: " + chain[0].toString() + ", authType: " + authType)
        }

        override fun getAcceptedIssuers(): Array<X509Certificate>? {
            return null
        }
    }

    class TrustAllHostnameVerifier : HostnameVerifier {
        override fun verify(hostname: String, session: SSLSession): Boolean {
            return true
        }
    }
}