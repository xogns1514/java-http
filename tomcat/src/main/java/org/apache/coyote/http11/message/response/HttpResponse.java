package org.apache.coyote.http11.message.response;

import java.io.IOException;
import org.apache.coyote.http11.message.common.ContentType;
import org.apache.coyote.http11.message.common.HttpHeaderField;
import org.apache.coyote.http11.message.common.HttpHeaders;
import org.apache.util.ResourceReader;

public class HttpResponse {

    private static final String CHARSET = ";charset=utf-8";
    private static final String CRLF = "\r\n";
    private static final String DEFAULT_BODY = "";

    private final HttpStatusLine statusLine;
    private final HttpHeaders headers;
    private final HttpResponseBody body;

    public HttpResponse() {
        this(new HttpStatusLine(HttpStatus.OK), new HttpHeaders(), new HttpResponseBody(DEFAULT_BODY));
    }

    public HttpResponse(HttpStatusLine statusLine, HttpHeaders headers, HttpResponseBody body) {
        this.statusLine = statusLine;
        this.headers = headers;
        this.body = body;
    }

    public void setStatusLine(HttpStatus httpStatus) {
        this.statusLine.setHttpStatus(httpStatus);
    }

    public void setContentType(ContentType contentType) {
        this.headers.setHeaders(HttpHeaderField.CONTENT_TYPE.getName(), contentType.getType());
    }

    public void setStaticBody(String source) {
        try {
            String content = ResourceReader.readContent(source);
            String contentType = ResourceReader.readeContentType(source);

            setContentType(contentType);
            setBody(content);

        } catch (IOException exception) {
            throw new IllegalArgumentException("해당 파일을 찾을 수 없습니다: " + source);
        }
    }

    public void setContentType(String contentType) {
        this.headers.setHeaders(HttpHeaderField.CONTENT_TYPE.getName(), contentType + CHARSET);
    }

    public void setBody(String body) {
        this.body.setBody(body);
        this.setHeader(HttpHeaderField.CONTENT_LENGTH.getName(), String.valueOf(body.getBytes().length));
    }

    public void setHeader(String key, String value) {
        this.headers.setHeaders(key, value);
    }

    @Override
    public String toString() {
        return String.join(CRLF, statusLine.toString(), headers.toString(), body.toString());
    }
}
