package net.exkazuu.mimicdance.interpreter;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;

public class BearHandlingTask extends AsyncTask<Void, String, Boolean> {

    private String arg;

    BearHandlingTask(String arg) {
        this.arg = arg;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        boolean result = false;

        // All your code goes in here
        try {
            // URL指定
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://192.168.91.98:3000/form");
            // BODYに登録、設定
            ArrayList<NameValuePair> value = new ArrayList<NameValuePair>();
            value.add(new BasicNameValuePair("input1", arg));
            System.out.println("Send: " + arg);

            try {
                post.setEntity(new UrlEncodedFormEntity(value, "UTF-8"));
                // リクエスト送信
                HttpResponse response = client.execute(post);
                // 取得
                HttpEntity entity = response.getEntity();
                EntityUtils.toString(entity, "UTF-8");
            } catch (IOException e) {
                System.out.println(e);
            }
            client.getConnectionManager().shutdown();
        } catch (Exception e2) {
            System.out.println(e2);
        }
        // If you want to do something on the UI use progress update

        publishProgress("progress");
        return result;
    }

    protected void onProgressUpdate(String... progress) {
    }
}
