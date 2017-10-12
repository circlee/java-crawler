package com.sample.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;

import com.sample.crawler.utils.HttpUtil;

/**
 * Created by circlee on 2017. 10. 11..
 */

/**
 *  채식 카페 본문글의 데이터의 경우 html파싱이 어려움
 *  본문글의 전화번호를 파싱하여 ,전화번호부사이트 검색후 정확한 매장명 파싱, 위치정보도 획득가능
 */
public class TextParseMain {

    public static void main(String[] args) {

    	int noCallNumCount = 0;
    	List<Map<String, String>> infoList = new ArrayList<>();
        Pattern callNumPattenr = Pattern.compile("(\\d+(-|.)\\d+(-|.)\\d+)");
        Pattern coodPattern = Pattern.compile("\\{ lat : ([\\d.]+) , lng :([\\d.]+) \\}");
        
        File f = new File(SampleMain.class.getClassLoader().getResource("seoul_ganada_store.txt").getFile());

        
        try(BufferedReader br = new BufferedReader( new FileReader(f))){


            StringBuilder sb = new StringBuilder();

            String line = null;
            int lineCnt = 0;

            while((line = br.readLine()) != null) {
            	
            	
            	
            	Matcher matcher = callNumPattenr.matcher(line);
            	
            	if(matcher.find()) {
            		
            		Map<String, String> parseMap = new HashMap<>();
        			
            		String callNumber = line.substring(matcher.start(), matcher.end());
            		
            		Map<String, String> paramMap = new HashMap<>();
            		paramMap.put("searchWord", callNumber);
            		
            		String body = HttpUtil.post("http://www.isuperpage.co.kr/search.asp", paramMap);
            		
            		Matcher coodMatcher = coodPattern.matcher(body);
            		
            		if(coodMatcher.find()) {
            			String lat = coodMatcher.group(1);
            			String lng = coodMatcher.group(2);
            			
            			parseMap.put("poi", lat + "," + lng);
            		}
            		
            		
            		Jsoup.parse(body)
            		.select("li.genarea1+li")
            		.stream()
            		.forEach(ele -> {
            			
            			String storeName = ele.select("div.tit_list > a.l_tit")
		            			.stream()
		            			.map(innerEle -> {
		            				return innerEle.html();
		            			})
		            			.findFirst()
		            			.orElse("")
		            			;
            			
            			parseMap.put("storeName", storeName);
            			
            			String category = ele.select("div.tit_list > span.l_ser")
                    			.stream()
                    			.map(innerEle -> {
                    				return innerEle.html();
                    			})
                    			.findFirst()
                    			.orElse("")
                    			;
            			
            			parseMap.put("category", category);
            			
            			
            			String phone = ele.select("div.l_cont > span.phone")
                    			.stream()
                    			.map(innerEle -> {
                    				return innerEle.html();
                    			})
                    			.findFirst()
                    			.orElse("")
                    			;
            			
            			parseMap.put("phone", phone);
            			
            			String oldAddr = ele.select("div.l_cont > span.phone+span")
                    			.stream()
                    			.map(innerEle -> {
                    				return innerEle.html();
                    			})
                    			.findFirst()
                    			.orElse("")
                    			;
            			
            			parseMap.put("oldAddr", oldAddr);
            			
            			String newAddr = ele.select("div.l_cont > span.loadv")
                    			.stream()
                    			.map(innerEle -> {
                    				return innerEle.html();
                    			})
                    			.findFirst()
                    			.orElse("")
                    			;
            			
            			parseMap.put("newAddr", newAddr);
            			
            		});
            		
            		infoList.add(parseMap);
            		
            	} else {
            		noCallNumCount ++;
            	}

                lineCnt++;
                System.out.println(lineCnt + "번 라인 처리...");
            	
            }
            System.out.println();
            
            infoList.stream()
        	.filter(infoMap -> !infoMap.isEmpty())
        	.forEach(System.out::println);
        	
        	long foundInfoCount = infoList.stream()
        	.filter(infoMap -> !infoMap.isEmpty())
        	.count();
        	
        	long notFoundInfoCount = infoList.stream()
        	.filter(infoMap -> infoMap.isEmpty())
        	.count();
        	
        	System.out.println("no call number count : "+noCallNumCount);
        	System.out.println("crawler found info count : "+foundInfoCount);
        	System.out.println("crawler not found info count : "+notFoundInfoCount);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
