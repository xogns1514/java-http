package org.apache.coyote.http11;

import java.util.Map;

public class HttpRequest {

    private static final String LINE_FEED = "\n";
    private static final int STARTLINE_INDEX = 0;
    private static final int HEADER_INDEX = 1;

    private final HttpStartLine startLine;
    private final HttpHeader headers;
    private final HttpBody body;

    public HttpRequest(String request) {
        startLine = new HttpStartLine(parseStartLine(request));
        headers = new HttpHeader(parseHeaders(request));
        body = new HttpBody(parseBody(request));
    }

    private String parseStartLine(String request) {
        String[] messages = parseMessage(request);
        return messages[STARTLINE_INDEX];
    }

    private String parseHeaders(String request) {
        String[] messages = parseMessage(request);
        StringBuilder builder = new StringBuilder();

        for (int i = HEADER_INDEX; i < messages.length; i++) {
            if (messages[i].isEmpty()) {
                break;
            }
            builder.append(messages[i]).append(LINE_FEED);
        }
        return builder.toString();
    }

    private String parseBody(String request) {
        String[] messages = parseMessage(request);
        StringBuilder builder = new StringBuilder();

        for (int i = HEADER_INDEX; i < messages.length; i++) {
            if (messages[i].isEmpty()) {
                for (int j = i; j < messages.length; j++) {
                    builder.append(messages[j]).append(LINE_FEED);
                }
                break;
            }
        }
        return builder.toString();
    }

    private String[] parseMessage(String request) {
        return request.split(LINE_FEED);
    }

    public Map<String, String> getHeaders() {
        return headers.getHeaders();
    }

    public String getBody() {
        return body.getBody();
    }

    public String getPath() {
        return startLine.getPath();
    }

    public HttpStartLine getStartLine() {
        return startLine;
    }
}
