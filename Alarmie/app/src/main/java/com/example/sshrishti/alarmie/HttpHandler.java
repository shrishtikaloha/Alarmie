package com.example.sshrishti.alarmie;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by fssandhu on 16-11-27.
 */
public class HttpHandler {
    public HttpHandler() {
    }

    public String makeCall(String Url){
        String response = null;
        try{
            URL url = new URL(Url);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            InputStream ins = new BufferedInputStream(connection.getInputStream());
            response = convertStreamToString(ins);
        }
        catch (MalformedURLException e){
            Log.e("Http handler", "Url malfunction: " + e.getMessage());
        }catch (ProtocolException e) {
            Log.e("Http handler", "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e("Http handler", "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e("Http handler", "Exception: " + e.getMessage());
        }

        return response;
    }

    private String convertStreamToString(InputStream in) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
