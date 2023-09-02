package api.controller;

import api.controller.response.ApiResponse;
import api.entity.Company;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class PostRequest {
    public static int responseCode ;
    private static URL obj;
    private final static String HTTP_METHOD = "POST";
    private static   HttpURLConnection con;


    public static HttpURLConnection getCon() {
        return con;
    }

    public static void setCon(HttpURLConnection con) {
        PostRequest.con = con;
    }

    public static URL getObj() {
        return obj;
    }

    public static void setObj(URL obj) {
        PostRequest.obj = obj;
    }

    public PostRequest() {
    }

    public static void createCompany(Company company,String url) throws IOException {
            writeJson(company,url,true);
            readJson(false,true);
    }

    public static void getCommands(Company company,String url) throws IOException {
         writeJson(company,url,false);
         readJson(false,false);
    }
    public static void getSingIn(Company company,String url) throws IOException {
        writeJson(company,url,false);
        readJson(true,false);
    }
    public static void getJson(String response,boolean isSingIn){
        if (isSingIn) {
            JSONObject jsonResponseObject = new JSONObject(response);
            ApiResponse.isRunning = jsonResponseObject.getBoolean("isHacking");
            ApiResponse.isWorkOnce=jsonResponseObject.getBoolean("isWorkOnce");
        }
        else {
                ApiResponse.setJsonArray(new JSONArray(response));
            }

    }
    public  static void writeJson(Company company ,String url, boolean isCreate) throws IOException {
        setObj(new URL(url));
        HttpURLConnection httpURLConnection = (HttpURLConnection) getObj().openConnection();
        setCon(httpURLConnection);
        getCon().setRequestMethod(HTTP_METHOD);
        getCon().setRequestProperty("Content-Type","application/json");
        getCon().setRequestProperty("Accept","application/json");
        getCon().setDoOutput(true);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",company.getName());
        jsonObject.put("password",company.getPassword());
        jsonObject.put("baseBoardNumber",company.getBaseBoardNumber());
        jsonObject.put("responsibleCodeId", Company.getResponsibleCodeId());
        jsonObject.put("responsibleCode",company.getResponsibleCode());

        if (isCreate){
            jsonObject.put("createdDate",company.getFirstDate());
        }
        OutputStreamWriter writer = new OutputStreamWriter(getCon().getOutputStream());
        writer.write(jsonObject.toString());
        writer.flush();

    }
    public static void fileUploadDer(String url,String apiUrl,Company company){
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            File file = new File(url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addPart("file", new FileBody(file));
            HttpUriRequest request = RequestBuilder.post()
                    .setUri(apiUrl)
                    .setHeader("name", company.getName())
                    .setHeader("id", String.valueOf(Company.getResponsibleCodeId()))
                    .setEntity(builder.build()).build();
            CloseableHttpResponse response =  httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("Status code: " + statusCode);
        } catch (Exception e) {
            System.out.println("Uploading file failed...");
        }

    }


    public static  void readJson(boolean isSingIn,boolean isCreate) throws IOException {
        StringBuilder response = new StringBuilder();
        int HttpResult = con.getResponseCode();
        if (HttpResult == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(getCon().getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line).append("\n");
            }
            if (!isCreate){
                getJson(String.valueOf(response),isSingIn);
            }

            br.close();

        } else {
            System.out.println(con.getResponseMessage()+"\n"+getCon().getResponseCode());
        }
        responseCode =getCon().getResponseCode();
    }

    }

