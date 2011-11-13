package com.android.Android_Maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class ServerConnection {
  private String server_url;

  public ServerConnection(String url) {
     server_url = url;
  }
  
  public RequestStatus postStringData(String data) {
    try {
      return postEntity(new StringEntity(data));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return RequestStatus.UEE;
    }
  }

  public RequestStatus postNameValuePairs(List<NameValuePair> nvps) {
    try {
      return postEntity(new UrlEncodedFormEntity(nvps));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return RequestStatus.UEE;
    }
  }

  private RequestStatus postEntity(HttpEntity entity) {
    DefaultHttpClient client = new DefaultHttpClient();
    HttpPost postRequest = new HttpPost(server_url);
    HttpResponse response = null;

    try {
      postRequest.setEntity(entity);
      response = client.execute(postRequest);

      BufferedReader r = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
      String first_line = r.readLine();
      if (first_line.equals("OK"))
        return RequestStatus.OK;
      else
        return RequestStatus.FAILED;
    } catch (ClientProtocolException e) {
      e.printStackTrace();
      return RequestStatus.CPE;
    } catch (IOException e) {
      e.printStackTrace();
      return RequestStatus.IOE;
    }
  }

  public enum RequestStatus {
    OK, FAILED, UEE, CPE, IOE;
  }
}
