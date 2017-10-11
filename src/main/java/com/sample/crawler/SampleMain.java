package com.sample.crawler;

import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;

/**
 * Created by circlee on 2017. 10. 10..
 */
public class SampleMain {

    public static void main(String[] args) throws IOException {


        String url = "http://cafe.naver.com/ArticleRead.nhn?articleid=98016&clubid=10789848";

        File f = new File(SampleMain.class.getClassLoader().getResource("test.kml").getFile());

//        Jsoup.parse(f, "utf-8")
//                .select("Placemark")
//                .stream()
//                .forEach( el -> {
//                    //System.out.println(el.html());
//
//                    String name = el.getElementsByTag("name").html();
//                    String coordinates = el.getElementsByTag("coordinates").html();
//
//                    System.out.println(name);
//                    System.out.println(coordinates);
//                });

        Jsoup.connect(url)
                .get()
                .select("p.MsoNormal")
                .stream()
                .forEach( el -> {


                    if(el.html().contains("●")) {
                        System.out.println(el.html());
                    }
                });

    }
}
