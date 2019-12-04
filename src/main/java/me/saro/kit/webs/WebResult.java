package me.saro.kit.webs;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * web result
 * @author PARK Yong Seo
 * @since 1.0.0
 */
public class WebResult<T> {

    WebResult() {
    }

    // http status
    private int status = -1;

    // exception
    private Exception exception;

    // headers
    private Map<String, List<String>> headers = Collections.emptyMap();

    // response body data
    private T body;
    
    private String errorBody;

    /**
     * is status 2xx + have not exception
     * @return
     */
    public boolean isSuccess() {
        return isStatus2xx() && exception == null;
    }
    
    /**
     * is status 2xx
     * @return
     */
    public boolean isStatus2xx() {
        return status >= 200 && status < 300;
    }

    /**
     * is status 3xx
     * @return
     */
    public boolean isStatus3xx() {
        return status >= 300 && status < 400;
    }

    /**
     * is status 4xx
     * @return
     */
    public boolean isStatus4xx() {
        return status >= 400 && status < 500;
    }

    /**
     * is status 5xx
     * @return
     */
    public boolean isStatus5xx() {
        return status >= 500 && status < 600;
    }
    
    /**
     * has body
     * @return
     */
    public boolean hasBody() {
        return body != null;
    }

    /**
     * get response body data
     * @return Optional response body data
     */
    public T getBody() {
        return body;
    }
    
    /**
     * get response body data
     * @param orElse
     * @return
     */
    public T getBody(T orElse) {
        return body != null ? body : orElse;
    }
    
    /**
     * get response body data
     * throw null body
     * @return Optional response body data
     * @throws X
     */
    public <X extends Throwable> T getBodyWithThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (body == null) {
            throw exceptionSupplier.get();
        }
        return body;
    }
    
    /**
     * get response body data
     * throw null body
     * @return Optional response body data
     * @throws NullPointerException
     */
    public T getBodyWithThrow() throws NullPointerException {
        return getBodyWithThrow(NullPointerException::new);
    }
    
    /**
     * get response error body data
     * @return Optional response body data
     */
    public String getErrorBody() {
        return errorBody;
    }
    
    /**
     * get response error body data
     * @param orElse
     * @return
     */
    public String getErrorBody(String orElse) {
        return errorBody != null ? errorBody : orElse;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public void setErrorBody(String errorBody) {
        this.errorBody = errorBody;
    }

    @Override
    public String toString() {
        return "WebResult{" +
                "status=" + status +
                ", exception=" + exception +
                ", headers=" + headers +
                ", body=" + body +
                ", errorBody='" + errorBody + '\'' +
                '}';
    }
}