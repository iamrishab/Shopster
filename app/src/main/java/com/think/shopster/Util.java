package com.think.shopster;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by anand on 17/04/17.
 */

public class Util {

    public static ArrayList<ProductData> getDataFromServer(String urlString) throws IOException {
        URL url= new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        InputStream in=null;
        try {
            urlConnection.connect();
            in = new BufferedInputStream(urlConnection.getInputStream());
        }
        catch(Exception e){
            System.out.println("Error");
            System.out.println(e);
        }
        finally {
            urlConnection.disconnect();
        }
        return readStream(in);
    }

    private static ArrayList<ProductData> readStream(InputStream in) throws IOException {
        BufferedReader br=new BufferedReader(new InputStreamReader(in));
        String jsonData=br.readLine();
//        System.out.println(jsonData);
        return generateProductSets(jsonData);
    }

    private static ArrayList<ProductData> generateProductSets(String data){
        ArrayList<ProductData> results=new ArrayList<>();
        String[] dataSets = data.split(",");
        for(String s:dataSets){
            ProductData p=new ProductData();
            String[] attr=s.split("\\s+");
            for (String s1:attr){
                System.out.println(s1);
            }
            p.product_name=attr[0].toUpperCase()+" "+attr[1].toUpperCase();
            System.out.println(attr[2]);
            p.image_url=attr[2];
            System.out.println(attr[3]);
            p.f_url=attr[3];
            System.out.println(attr[4]);
            p.f_price=Float.parseFloat(attr[4]);
            p.s_url=attr[5];
            p.s_price=Float.parseFloat(attr[6]);
            p.a_url=attr[7];
            p.a_price=Float.parseFloat(attr[8]);
            p.min_price= p.a_price<p.s_price?p.a_price<p.f_price?p.a_price:p.f_price:
                    p.s_price<p.f_price?p.s_price:p.f_price;
//            System.out.println(p.product_name);
            results.add(p);
            System.out.println("--------------------");
        }
        return results;
    }

}
