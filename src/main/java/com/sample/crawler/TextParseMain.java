package com.sample.crawler;

import java.io.*;
import java.util.regex.Pattern;

/**
 * Created by circlee on 2017. 10. 11..
 */

/**
 *  채식 카페 본문글의 데이터의 경우 html파싱이 어려움
 *  본문글을 타입벼로 파일로 분리하여 , 기호가 달린 첫번째 라인의 유효 데이터 (매장명 , 전화번호 , (옵션) 채식등급) 정보를 파싱
 */
public class TextParseMain {

    public static void main(String[] args) {


        Pattern p = Pattern.compile("^(.*?●)(.*+)(\\d+(-|\\.)\\d+(-|\\.)\\d+)?(\\S+)?$");

        System.out.println(p);


        File f = new File(SampleMain.class.getClassLoader().getResource("seoul_ganada.txt").getFile());

        try(BufferedReader br = new BufferedReader( new FileReader(f))){


            StringBuilder sb = new StringBuilder();

            String line = null;

            while((line = br.readLine()) != null) {
                if(line.contains("●")) {
                    System.out.println(line);
//
//                    Matcher matcher = p.matcher(line);
//
//                    if(matcher.find()) {
//                        System.out.println("find");
//                        System.out.println(matcher.groupCount());
//                        System.out.println(matcher.group(0));
//
//                    }


                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
