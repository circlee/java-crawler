package com.sample.crawler;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by circlee on 2017. 10. 10..
 */
public class DiningCodeParseMain {

    public static void main(String[] args) throws IOException {

    	int pageNum = 1;
        String url = "http://www.diningcode.com/list.php?chunk=10&query=채식&page=";


        while(true) {
        
        	Document doc = Jsoup.connect(url+pageNum)
            .get();
        	
            doc.getElementsByTag("dc-restaurant")
            .stream()
            .forEach( el -> {
                
            	
            	String name = el.select("div.dc-restaurant-name").get(0).html().replaceAll("<.*?>", "").replaceAll("\n", "").trim();
            	String category = el.select("div.dc-restaurant-category").get(0).html().replaceAll("<.*?>", "").replaceAll("\n", "").replaceAll(",", "/").trim();
            	String etc = el.select("div.dc-restaurant-info").get(0).html().replaceAll("<.*?>", "").replaceAll("\n", "").trim();
            	String addr = el.select("div.dc-restaurant-info").get(1).html().replaceAll("<.*?>", "").replaceAll("\n", "").trim();
            	String phone = el.select("div.dc-restaurant-info").get(2).html().replaceAll("<.*?>", "").replaceAll("\n", "").trim();
            	
//            	System.out.println(name);
//            	System.out.println(category);
//            	System.out.println(etc);
//            	System.out.println(addr);
//            	System.out.println(phone);
//            	
//            	System.out.println("-------------------");
            	
            	
            	System.out.println(name +","+phone +","+category +","+addr +","+etc);
            });

            
            boolean hasNext = false;
            if(doc.select("td.right_arrow_cell").size() > 0) {
            	Element nextEl = doc.select("td.right_arrow_cell").get(0);
            	
            	if(nextEl.html().matches(".*href=\"/list.php.*")) {
            		hasNext = true;
            	}
            	
            }
            
            
            if(hasNext){
            	pageNum ++;
            } else {
            	break;	
            }
        }
        
    }
}
