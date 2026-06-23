package org.spring.backendprojectex.open.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class OpenApiBusUtil {
    private static final String BUS_ROUTE_LIST_URL = "http://ws.bus.go.kr/api/rest/busRouteInfo/getBusRouteList";
    private static final String STATION_BY_ROUTE_URL = "http://ws.bus.go.kr/api/rest/busRouteInfo/getStaionByRoute";

    public static String getBusList(String serviceKey, String searchVal) throws IOException {
        StringBuilder urlBuilder = new StringBuilder(BUS_ROUTE_LIST_URL); /*URL*/
        // 서비스키
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + serviceKey); /*Service Key*/
        // 노선번호
        urlBuilder.append("&" + URLEncoder.encode("strSrch","UTF-8") + "=" + URLEncoder.encode(searchVal, "UTF-8")); /*노선ID*/
        // 반환 타입
        urlBuilder.append("&resultType=json"); /*시작 정류소 순번*/

        return getBusResultData(urlBuilder.toString());
    }


    public static String getStationList(String serviceKey, String busRouteId) throws IOException {
        // 1. URL 직접 입력
        String url = "http://ws.bus.go.kr/api/rest/busRouteInfo/getStaionByRoute";

        // 2. 파라미터 구성 (URLEncoder 없이 직접 연결 - 서비스키에 특수문자가 있다면 예외)
        StringBuilder urlBuilder = new StringBuilder(url);
        urlBuilder.append("?ServiceKey=" + serviceKey);
        urlBuilder.append("&busRouteId=" + busRouteId);
        urlBuilder.append("&resultType=json");

        System.out.println("최종 호출 URL 확인: " + urlBuilder.toString());
        return getBusResultData(urlBuilder.toString());
    }

    public static String getBusResultData(String requestURL) throws IOException{

        URL url = new URL(requestURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());

        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());
        return sb.toString();
    }
}
