package io.mochadwi.mobilenews.network

import okhttp3.OkHttpClient
import java.security.cert.CertificateException
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Created by mochadwi on 3/14/18.
 */
class UnsafeOkhttpClient {
    companion object {
        // Create a trust manager that does not validate certificate chains
        // Install the all-trusting trust manager
        // Create an ssl socket factory with our all-trusting manager
        val unsafeOkHttpClient: OkHttpClient
            get() {
                try {
                    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                        @Throws(CertificateException::class)
                        override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                        }

                        @Throws(CertificateException::class)
                        override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                        }

                        override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                            return arrayOf()
                        }
                    })
                    val sslContext = SSLContext.getInstance("SSL")
                    sslContext.init(null, trustAllCerts, java.security.SecureRandom())
                    val sslSocketFactory = sslContext.socketFactory

                    val builder = OkHttpClient.Builder()
                    builder.sslSocketFactory(sslSocketFactory)
                    builder.hostnameVerifier { hostname, session -> true }

                    return builder.build()
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }

            }

    }
}
