package com.tibco.integration.net;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tibco.bean.Report;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.eclipse.jetty.http.HttpHeaders.USER_AGENT;

public class HttpSender {

    public static void main(String[] args) throws Exception{
        String url = "http://service4.99melove.cn/miai-hisproxy-service/service/rest/miaiTransfer.TsTransfer/collection/query?start=0&limit=25";
        String response = HttpSender.sendGet(url,null);
        System.out.println(response);
        JSONObject json = (JSONObject) JSON.parse(response);
        JSONObject result =  json.getJSONObject("result");

        Report report = new Report();

        if(result != null){
            JSONArray users = result.getJSONArray("users");
            if(users !=null && users.size() > 0){
                JSONObject user = users.getJSONObject(0);

                Report report1 = JSONObject.toJavaObject(user,Report.class);

                System.out.println(JSONObject.toJSON(report1));
            }
        }

    }

    /**
     * 向指定URL发送GET方法的请求
     */
    public static String sendGet(String url, String param) throws UnsupportedEncodingException, IOException {
        return sendGet(url, param, null);
    }

    public static String sendGet(String url, String param, Map<String, String> header) throws UnsupportedEncodingException, IOException {
        String result = "";
        BufferedReader in = null;
        String urlNameString = url  ;//+ "?" + param;
        URL realUrl = new URL(urlNameString);
        // 打开和URL之间的连接
        URLConnection connection = realUrl.openConnection();
        //设置超时时间
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(15000);
        // 设置通用的请求属性
        if (header != null) {
            Iterator<Entry<String, String>> it = header.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, String> entry = it.next();
//                System.out.println(entry.getKey() + ":::" + entry.getValue());
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        // 建立实际的连接
        connection.connect();
        // 获取所有响应头字段
        Map<String, List<String>> map = connection.getHeaderFields();
        // 遍历所有的响应头字段
        for (String key : map.keySet()) {
//            System.out.println(key + "--->" + map.get(key));
        }
        // 定义 BufferedReader输入流来读取URL的响应，设置utf8防止中文乱码
        in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        if (in != null) {
            in.close();
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     */
    public static String sendPost(String url, String param) throws UnsupportedEncodingException, IOException {
        return sendPost(url, param, null);
    }

    public static String sendPost(String urlStr, String param, Map<String, String> header) throws UnsupportedEncodingException, IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");

        OutputStream os = conn.getOutputStream();
        os.write(param.getBytes("UTF-8"));
        os.close();

        // read the response
//        InputStream in = new BufferedInputStream(conn.getInputStream());
        BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "utf-8"));
        String line = null;
        StringBuffer sb = new StringBuffer();
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();
        JSONObject jsonObject = JSONObject.parseObject(sb.toString());


//        in.close();
        conn.disconnect();

        return jsonObject.toJSONString();
    }
}
