/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.orz.pascal.kuda.utils;

/**
 *
 * @author koduki
 */
public class HttpResponse {

    private final int statusCode;
    private final String body;

    public HttpResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "HttpResponse{" + "statusCode=" + statusCode + ", body=" + body + '}';
    }

}
