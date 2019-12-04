package me.saro.kit.webs;

import me.saro.kit.Files;
import me.saro.kit.Texts;
import me.saro.kit.functions.ThrowableConsumer;
import me.saro.kit.functions.ThrowableFunction;

import java.io.File;
import java.io.InputStream;

/**
 * Web Client
 * @author PARK Yong Seo
 * @since 1.0.0
 */
public interface Web {
    /**
     * create get method Web
     * @param url
     * @return
     */
    static Web get(String url) {
        return new WebImpl(url, "GET");
    }

    /**
     * create post method Web
     * @param url
     * @return
     */
    static Web post(String url) {
        return new WebImpl(url, "POST");
    }

    /**
     * create put method Web
     * @param url
     * @return
     */
    static Web put(String url) {
        return new WebImpl(url, "PUT");
    }

    /**
     * create patch method Web
     * @param url
     * @return
     */
    static Web patch(String url) {
        return new WebImpl(url, "PATCH");
    }

    /**
     * create delete method Web
     * @param url
     * @return
     */
    static Web delete(String url) {
        return new WebImpl(url, "DELETE");
    }
    
    /**
     * request charset
     * @return
     */
    String getRequestCharset();
    
    /**
     * response charset
     * @return
     */
    String getResponseCharset();

    /**
     * create custom method Web
     * @param url
     * @return
     */
    static Web custom(String url, String method) {
        return new WebImpl(url, method);
    }
    
    /**
     * Connect Timeout
     * @param connectTimeout
     * @return
     */
    Web setConnectTimeout(int connectTimeout);
    
    /**
     * Read Timeout
     * @param readTimeout
     * @return
     */
    Web setReadTimeout(int readTimeout);
    
    /**
     * set request Charset
     * @param charset
     * @return
     */
    Web setRequestCharset(String charset);
    
    /**
     * set response charset
     * @param charset
     * @return
     */
    Web setResponseCharset(String charset);
    
    /**
     * ignore https certificate
     * <br>
     * this method not recommend
     * <br>
     * ignore certificate is defenseless the MITM(man-in-the-middle attack)
     * @param ignoreCertificate
     * @return
     */
    Web setIgnoreCertificate(boolean ignoreCertificate);
    
    /**
     * add url parameter
     * <br>
     * always append url parameter even post method
     * <br>
     * is not body write
     * @param name
     * @param value
     * @return
     */
    Web addUrlParameter(String name, String value);
    
    /**
     * set header
     * @param name
     * @param value
     * @return
     */
    Web setHeader(String name, String value);
    
    /**
     * write body binary
     * @param bytes
     * @return
     */
    Web writeBody(byte[] bytes);
    
    /**
     * writeBodyParameter
     * <br>
     * <b>WARNING : </b> is not json type
     * <br>
     * <br>
     * web
     * <br>
     *  .writeBodyParameter("aa", "11")
     * <br>
     * .writeBodyParameter("bb", "22");
     * <br>
     * <b>equals</b>
     * <br>
     * aa=11&amp;bb=22
     * @param name
     * @param value
     * @return
     */
    Web writeBodyParameter(String name, String value);
    
    /**
     * to Custom result
     * @param result
     * @param function
     * @return
     */
    <R> WebResult<R> toCustom(WebResult<R> result, ThrowableFunction<InputStream, R> function);
    
    /**
     * to Custom result
     * @param function
     * @return
     */
    default <R> WebResult<R> toCustom(ThrowableFunction<InputStream, R> function) {
        return toCustom(new WebResult<R>(), function);
    }

    /**
     * save file and return WebResult
     * @return WebResult[WebResult]
     */
    default WebResult<File> saveFile(File file, boolean overwrite) {
        return toCustom(is -> Files.createFile(file, overwrite, is));
    }

    /**
     * readRawResultStream
     * @param reader
     * @return it has Body
     */
    default WebResult<String> readRawResultStream(ThrowableConsumer<InputStream> reader) {
        return toCustom(is -> {
            reader.accept(is);
            return "OK";
        });
    }
    
    /**
     * set header ContentType
     * @param value
     * @return
     */
    default Web setContentType(String value) {
        return setHeader("Content-Type", value);
    }
    
    /**
     * ContentType application/json
     * @return
     */
    default Web setContentTypeApplicationJson() {
        return setHeader("Content-Type", "application/json");
    }
    
    /**
     * write Body text
     * @param text
     * @return
     */
    default Web writeBody(String text) {
        return writeBody(Texts.getBytes(text, getRequestCharset()));
    }
}
