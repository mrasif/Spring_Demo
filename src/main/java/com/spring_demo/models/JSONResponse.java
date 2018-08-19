package com.spring_demo.models;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

public class JSONResponse {
    Map<String, Object> body;
    MultiValueMap<String, String> headers;
    HttpStatus httpStatus;

    public JSONResponse() {
        this.body=new HashMap<>();
        this.headers=new LinkedMultiValueMap<>();
        this.httpStatus=HttpStatus.OK;
    }

    public JSONResponse(Map<String, Object> body, MultiValueMap<String, String> headers, HttpStatus httpStatus) {
        this.body = body;
        this.headers = headers;
        this.httpStatus = httpStatus;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public void  addBody(String key, Object value){
        this.body.put(key,value);
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }

    public MultiValueMap<String, String> getHeaders() {
        return headers;
    }

    public void addHeader(String key, String value){
        this.headers.add(key,value);
    }

    public void setHeaders(MultiValueMap<String, String> headers) {
        this.headers = headers;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public ResponseEntity toResponseEntity(){
        return new ResponseEntity<>(this.body, this.headers, this.httpStatus);
    }

    @Override
    public String toString() {
        return "JSONResponse{" +
                "body=" + body +
                ", headers=" + headers +
                ", httpStatus=" + httpStatus +
                '}';
    }
}
