package NetworkingLib;

/**
 * <p/>
 * Project: <b>CodeForIndia2015</b><br/>
 * Created by: Akhilesh Dhar Dubey on 27/9/15.<br/>
 * Email id: 2akhileshdubey@gmail.com<br/>
 * Skype id: akhileshdubey91
 * <p/>
 */

public interface HttpConstants {
    /* HTTP headers*/
    String HTTP_HEADER_KEY_CONTENT_TYPE = "Content-type";
    String HTTP_HEADER_VALUE_APPLICATION_JSON = "application/json";
    String HTTP_HEADER_VALUE_APPLICATION_FORM = "application/x-www-form-urlencoded;";
    String HTTP_HEADER_KEY_USER_AGENT = "User-Agent";
    String HTTP_HEADER_VALUE_HTTP_AGENT = "http.agent";
    String HTTP_REQUEST = "HTTPRequest";
    String HTTP_DEFAULT_ERROR_MESSAGE = "Oops something happened. Please try again";

    String APPLICATION_CONTEXT_NOT_SET_MESSAGE = "The Context is not set. Please call \"LoaderHandler.setContext(Context)\" from your Application subclass.";
    String ILLEGAL_ARGUMENT_EXCEPTION_CONTEXT_NULL = "The Context object is null. It should be a non null valid value.";
}