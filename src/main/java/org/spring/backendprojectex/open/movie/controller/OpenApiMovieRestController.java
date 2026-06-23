package org.spring.backendprojectex.open.movie.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.backendprojectex.common.PagingUtil;
import org.spring.backendprojectex.open.movie.dto.MovieDto;
import org.spring.backendprojectex.open.movie.dto.boxOffice.BoxOfficeDto;
import org.spring.backendprojectex.open.movie.service.BoxOfficeService;
import org.spring.backendprojectex.open.movie.service.MovieDetailService;
import org.spring.backendprojectex.open.movie.service.MovieService;
import org.spring.backendprojectex.open.util.OpenApiUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/open/api/movie")
@RequiredArgsConstructor
@Tag(name = "movie API", description = "kobis openAPI 영화 데이터 조회 및 연동 API")
public class OpenApiMovieRestController {

    private final BoxOfficeService boxOfficeService;
    private final MovieService movieService;
    private final MovieDetailService movieDetailService;

    @Value("${open.kobis.serviceKey}")
    private String key;

    // ============================      영화 목록 (API 동기화 + 페이징 검색)        ================================//
    // 외부 API를 가져와 DB에 저장
    // 브라우저(AJAX)에서 /api/open/movie/movieList로 GET요청
    // 컨트롤러에서 KOBIS OPEN API 서버로보낼 URL주소 생성
    // OpenApiUtil.get()사용하여 영화정보를 JSON문자열(ResponseBody)을 받아온다
    // JSON문자열을 movieService.insertResponseBody(responseBody)로 넘겨주고, 서비스에서 JSON파싱, 로컬DB저장, 자바객체(List<MovieDto>로 반환
    // 로컬 DB에 저장된 데이터를 가져와 화면에 보여준다
    @Operation(
            summary = "영화 목록 조회 및 페이징 검색 (API 동기화 포함)",
            description = "영진위 Open API로부터 2026년 개봉 영화 데이터를 받아와 DB에 실시간 동기화한 후, 로컬 DB에서 페이징 및 조건 검색을 수행하여 반환.")
    @Parameters({
            @Parameter(name = "page", in = ParameterIn.QUERY, description = "페이지 번호 (0부터 시작)", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "size", in = ParameterIn.QUERY, description = "한 페이지당 노출할 개수", schema = @Schema(type = "integer", defaultValue = "10")),
            @Parameter(name = "subject", in = ParameterIn.QUERY, description = "검색 조건 분류(movieNm, genreAlt 등)", example = "movieNm"),
            @Parameter(name = "search", in = ParameterIn.QUERY, description = "검색 키워드", example = "아바타")
    })
    @GetMapping("/movieList")
    public ResponseEntity<Map<String, Object>> movieList(
            // ★ 중요: size=0은 에러가 나므로 실무 규격에 맞게 10개(또는 8개)로 기본값 지정
            @Parameter(hidden = true) @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "subject", required = false) String subject,
            @RequestParam(name = "search", required = false) String search) {

        // [1단계] 외부 API 호출 및 로컬 DB 실시간 동기화 (기존 movieList 로직)
        try {
            String movieSearch = "searchMovieList.json";  // 가져올 내용
            String itemPerPage = "100";                  // 가져올 갯수
            String openStartDt = "2026";                 // 개봉년도
            String apiURL = "http://kobis.or.kr/kobisopenapi/webservice/rest/movie/" + movieSearch + "?key=" + key
                    + "&itemPerPage=" + itemPerPage + "&openStartDt=" + openStartDt;

            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("Content-type", "application/json"); // JSON 데이터

            // 영진위 원본 JSON 데이터 수신
            String responseBody = OpenApiUtil.get(apiURL, requestHeaders);
            System.out.println(responseBody + " <<< 실시간 영진위 API responseBody");

            // 수신 데이터를 파싱하여 DB 저장 및 Null-safe 정제 처리
            movieService.insertResponseBody(responseBody);

        } catch (Exception e) {
            // 외부 네트워크 장애 등으로 API 호출이 실패하더라도
            // 시스템이 다운되지 않고 기존 로컬 DB 데이터로 유연하게 대체하기 위한 예외 방어 영역
            System.out.println("영진위 API 동기화 실패(로컬 DB 데이터로 대체): " + e.getMessage());
        }

        // [2단계] 로컬 DB에서 페이징 및 조건 검색 데이터 획득
        Page<MovieDto> movieDtos = movieService.getMovieList(pageable, subject, search);

        // [3단계] 블록 페이징(startPage, endPage) 인덱스 계산 로직
        Map<String ,Object> map = new HashMap<>();

        // common폴더 PagingUtil 클래스
        PagingUtil.setPagingMap(map, movieDtos, 3);

        map.put("movieList", movieDtos);

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }


    //별도의 테이블에 저장
    @Operation(summary = "KOBIS 영화 상세 정보 조회 및 저장", description = "영화 고유 코드(movieCd)를 이용해 KOBIS에서 상세 정보를 조회한 뒤 DB에 저장.")
    @GetMapping("/movieDetail/{movieCd}")
    public ResponseEntity<Map<String, String>> movieDetail(
            @Parameter(description = "KOBIS 영화 고유 코드",example = "20242153") @PathVariable("movieCd") String movieCd) {
        String apiURL = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key=" + key
                + "&movieCd=" + movieCd;
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-type", "application/json"); //JSON데이터
        String responseBody = OpenApiUtil.get(apiURL, requestHeaders);

        movieDetailService.movieInfoResultFn(responseBody);
        Map<String, String> movie = new HashMap<>();
        movie.put("movie", responseBody);

        System.out.println(movie+" <<< movie");

        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    // DB데이터 get -> 기존에  movieList 테이블리스트 중에서 찾기
    @Operation(summary = "DB에 저장된 영화 상세 정보 조회", description = "로컬 DB에서 영화 고유 코드(movieCd)에 매칭되는 영화 정보를 찾아 조회합니다.")
    @GetMapping("/movieDetailJava/{movieCd}")
    public ResponseEntity<?> movieDetailJava(
            @Parameter(description = "KOBIS 영화 고유코드", example = "20242153") @PathVariable("movieCd") String movieCd) {
//        MovieDetailDto movieDetailDto= movieDetailService.movieInfoJava(movieCd);
        MovieDto movieDto= movieService.movieInfoJava(movieCd);
        System.out.println(movieDto+" <<< movieDto");
        Map<String, MovieDto> movie = new HashMap<>();
        movie.put("movie", movieDto);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    @Deprecated
    @Operation(summary = "주간 박스오피스 테스트 (고정값 방식)", description = "특정 날짜가 하드코딩된 주간 박스오피스 API 임시 테스트용 메서드", deprecated = true)
    @GetMapping("/boxOffice/{showDate}")
    public ResponseEntity<?> boxOfficeJava(@PathVariable("showDate") String showDate) {
        String movieSearch = "searchWeeklyBoxOfficeList.json";  // 가져올내용
        String apiURL = "https://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/" + movieSearch + "?key=" + key
                + "&targetDt="+ 20250922 + "&weekGb=" + 0;
        Map<String ,String > requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");  // JSON 데이터

        String responseBody = OpenApiUtil.get(apiURL, requestHeaders);
        System.out.println(responseBody);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @Deprecated
    @Operation(summary = "주간 박스오피스 테스트 (고정값 방식)", description = "특정 날짜가 하드코딩된 주간 박스오피스 API 임시 테스트용 메서드", deprecated = true)
    @GetMapping("/boxOffice/{targetDt}/{weekGb}")
    public ResponseEntity<?> boxOfficeJava2(
            @Parameter(description = "조회 기준일 (YYYYMMDD 형식, 특수문자 자동 제거됨)", example = "20260601") @PathVariable("targetDt") String targetDt,
            @Parameter(description = "주간/주말/주중 선택 (0:주간, 1:주말, 2:주중)", example = "0") @PathVariable("weekGb") String weekGb) {

        if (targetDt != null) {
            targetDt = targetDt.replaceAll("[^0-9]", "");
        }
        log.info("============ 정제된 targetDt: " + targetDt + " ===================");
        log.info("============ weekGb: " + weekGb + " ===================");

        if (targetDt == null || targetDt.length() != 8) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", "올바르지 않은 targetDt 형식입니다. YYYYMMDD 형태여야 합니다. 입력값: " + targetDt);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
        }
        String movieSearch = "searchWeeklyBoxOfficeList.json";
        String apiURL = "https://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/" + movieSearch + "?key=" + key
                + "&targetDt=" + targetDt + "&weekGb=" + weekGb;
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-type", "application/json");
        String responseBody = OpenApiUtil.get(apiURL, requestHeaders);

        try {
            List<BoxOfficeDto> boxOfficeResult = boxOfficeService.insertBoxOfficeBody(responseBody);
            Map<String, List<BoxOfficeDto>> map = new HashMap<>();
            map.put("boxOfficeResult", boxOfficeResult);
            return ResponseEntity.status(HttpStatus.OK).body(map);
        } catch (Exception e) {
            log.error("KOBIS API 응답 파싱 중 오류 발생: ", e);
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", "API 데이터를 가져오는데 실패했습니다. (KOBIS 에러 혹은 DTO 매핑 실패)");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }



    // DB데이터 get
    @Operation(summary = "DB 저장된 박스오피스 영화 상세 조회", description = "로컬 DB 박스오피스 목록 테이블에서 특정 영화 코드에 해당하는 엔티티 데이터를 가져옵니다.")
    @GetMapping("/boxOfficeDetailJava/{movieCd}")
    public ResponseEntity<Map<String, BoxOfficeDto>> boxOfficeDetailJava(
            @Parameter(description = "KOBIS 영화 고유 코드", example = "20242153") @PathVariable("movieCd")String movieCd) {
        BoxOfficeDto boxOfficeDto = boxOfficeService.boxOfficeMovieInfoJava(movieCd);
        Map<String ,BoxOfficeDto> movie = new HashMap<>();
        movie.put("movie", boxOfficeDto);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

}
