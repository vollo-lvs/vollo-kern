package nl.vollo.kern.rest;

import org.springframework.http.HttpHeaders;

public class RestUtils {
    public static HttpHeaders errorHeader(String error) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Vollo-Error", error);
        return headers;
    }
}
