package me.saro.kit.webs;

import javax.net.ssl.*;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * Web Ignore Certificate
 * @author PARK Yong Seo
 * @since 1.0.0
 */
public class WebIgnoreCertificate {
    private WebIgnoreCertificate() {
    }

    final static HostnameVerifier IGNORE_HOSTNAME_VERIFIER = (hostname, session) -> true;

    final static TrustManager[] ALL_TRUST_MANAGER = new TrustManager[] {
            new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
    };

    /**
     * casing ignore certificate to HttpURLConnection
     * @param connection
     * @throws KeyManagementException 
     * @throws NoSuchAlgorithmException 
     */
    public static void ignoreCertificate(HttpURLConnection connection) throws KeyManagementException, NoSuchAlgorithmException {
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, ALL_TRUST_MANAGER, new java.security.SecureRandom());
        ((HttpsURLConnection)connection).setSSLSocketFactory(sslContext.getSocketFactory());
        ((HttpsURLConnection)connection).setHostnameVerifier(IGNORE_HOSTNAME_VERIFIER);

    }
}
