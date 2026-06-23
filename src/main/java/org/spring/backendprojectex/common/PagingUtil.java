package org.spring.backendprojectex.common;

import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import java.util.Map;

public class PagingUtil {
    public static void setPaging(Model model, Page<?> pageData, int blockSize) {

        int currentPage = pageData.getNumber();     // 현재 페이지 (0부터 시작)
        int totalPage = pageData.getTotalPages();   // 전체 페이지 수
        long count = pageData.getTotalElements();   // 전체 데이터 개수

        // 데이터가 아예 없을 경우를 대비한 방어 로직 (뷰 에러 방지)
        if (totalPage == 0) {totalPage = 1;}

        int startPage = (currentPage / blockSize) * blockSize + 1;
        int endPage = Math.min(startPage + blockSize - 1, totalPage);
        if (startPage > totalPage) {startPage = totalPage;}

        model.addAttribute("startPage", startPage);     // 시작 페이지
        model.addAttribute("endPage", endPage);         // 마지막 페이지
        model.addAttribute("currentPage", currentPage); // 현재 페이지
        model.addAttribute("count", count);             // 총 데이터 개수
        model.addAttribute("totalPage", totalPage);     // 전체 페이지
    }


    // 2. ★ REST API(Map/JSON)용 페이징 메서드 추가 OpenAPI용
    public static void setPagingMap(Map<String, Object> map, Page<?> page, int blockNum) {
        int currentPage = page.getNumber(); // 현재 페이지
        int totalPages = page.getTotalPages(); // 전체 페이지 수

        int startPage = (currentPage / blockNum) * blockNum + 1;
        int endPage = Math.min(startPage + blockNum - 1, totalPages);

        // 데이터가 없을 때 페이지가 꼬이는 현상 방지
        if (endPage < startPage) {
            endPage = startPage;
        }

        // Map에 페이징 변수 일괄 세팅
        map.put("currentPage", currentPage);
        map.put("startPage", startPage);
        map.put("endPage", endPage);
        map.put("totalPages", totalPages);
    }


}
