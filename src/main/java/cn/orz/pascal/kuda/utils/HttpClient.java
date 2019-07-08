/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.orz.pascal.kuda.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author koduki
 */
public class HttpClient {

    private final String url;

    public HttpClient(String url) {
        this.url = url;
    }

    public HttpResponse get() {
        try {
            HttpURLConnection con = (HttpURLConnection) (new URL(this.url)).openConnection();
            InputStreamReader input = new InputStreamReader(con.getInputStream());
            int statusCode = con.getResponseCode();
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(input)) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            }
            return new HttpResponse(statusCode, sb.toString());
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
