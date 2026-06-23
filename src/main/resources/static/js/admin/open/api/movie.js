const sceneList= document.querySelector('.scene-list');
const sceneContent= document.querySelector('.scene-content');
const tTable= document.querySelector('.t-table');

// 통합된 영화 목록 및 검색 조회 함수
const sceneListFn = (page = 0) => {
    // 검색어 및 조건 가져오기
    const searchVal = document.querySelector("#search") ? document.querySelector("#search").value : "";
    const subjectVal = document.querySelector("#subject") ? document.querySelector("#subject").value : "";

    const apiURL = `/open/api/movie/movieList?page=${page}&search=${searchVal}&subject=${subjectVal}`;

    fetch(apiURL)
        .then(res => res.json())
        .then(rs => {
            console.log("서버가 보내준 데이터 원본:", rs);
            let movieList = [];
            movieList = rs.movieList.content; // 일반적인 Page 객체 형태

            // 테이블 내용 그리기
            const sceneContent = document.querySelector('.scene-content');
            let html1 = ``;

            if (movieList.length === 0) {
                html1 = `<tr><td colspan="9" style="text-align:center;">검색 결과가 없습니다.</td></tr>`;
            } else {
                movieList.forEach(el => {
                    html1 += `
                        <tr>
                            <td>${el.id}</td>
                            <td>${el.movieNm}</td>
                            <td>${el.openDt}</td>
                            <td>${el.genreAlt || '-'}</td>
                            <td>${el.typeNm || '-'}</td>
                            <td>${el.repNationNm || '-'}</td>
                            <td>${el.prdtStatNm || '-'}</td>
                            <td>${el.movieCd}</td>
                            <td><button onclick="sceneDetailFn('${el.movieCd}')">보기</button></td>
                        </tr>
                    `;
                });
            }
            sceneContent.innerHTML = html1;

            // 페이징 함수 호출 (서버 원본 데이터를 그대로 넘겨줌)
            moviePagingFn(rs);
        })
        .catch(err => console.error("데이터 통신 에러:", err));
};


function moviePagingFn(rs) {
    const pagingUl = document.querySelector('#moviePaging');
    if (!pagingUl) return;

    // 서버가 보내준 데이터에서 안전하게 페이징 변수들 추출
    const currentPage = rs.currentPage !== undefined ? rs.currentPage : 0;
    const startPage = rs.startPage !== undefined ? rs.startPage : 1;
    const endPage = rs.endPage !== undefined ? rs.endPage : 1;

    let totalPages = 1;
    if (rs.totalPages !== undefined) {
        totalPages = rs.totalPages;
    } else if (rs.movieList && rs.movieList.totalPages !== undefined) {
        totalPages = rs.movieList.totalPages;
    }

    // 데이터가 0건일 때 페이징 빈칸 처리
    if (totalPages === 0) {
        pagingUl.innerHTML = `<li><p>검색결과가 없습니다.</p></li>`;
        return;
    }

    let html = `<li><a href="javascript:void(0);" onclick="sceneListFn(0)" class="span1">처음</a></li>`;

    // 이전 버튼
    if (currentPage > 0) {
        html += `<li><a href="javascript:void(0);" onclick="sceneListFn(${currentPage - 1})" class="span1">이전</a></li>`;
    } else {
        html += `<li><span class="span0">이전</span></li>`;
    }

    // 번호 블록
    for (let i = startPage; i <= endPage; i++) {
        if (i === currentPage + 1) {
            html += `<li class="block"><span class="now">${i}</span></li>`;
        } else {
            html += `<li class="block"><a href="javascript:void(0);" onclick="sceneListFn(${i - 1})">${i}</a></li>`;
        }
    }

    // 다음 버튼
    if (currentPage < totalPages - 1) {
        html += `<li><a href="javascript:void(0);" onclick="sceneListFn(${currentPage + 1})" class="span1">다음</a></li>`;
    } else {
        html += `<li><span class="span0">다음</span></li>`;
    }

    // 마지막 버튼
    html += `<li><a href="javascript:void(0);" onclick="sceneListFn(${totalPages - 1})" class="span1">마지막</a></li>`;

    pagingUl.innerHTML = html;
}

// 검색 버튼 이벤트
const searchFn = (event) => {
    // 새로고침없이
    if (event) event.preventDefault();
    // 검색조건을 가지고 0페이지부터
    sceneListFn(0);
};





const sceneDetailFn = (movieCd) => {
    // const apiURL = `/api/open/movie/movieDetailJava/${movieCd}`;
    const apiURL = `/open/api/movie/movieDetail/${movieCd}`;
//
    fetch(apiURL)
        .then(res => res.json())
        .then(rs => {
            console.log(rs);  console.log(rs.movie);

            document.querySelector('.movie-detail-modal').style.display = 'flex';
            // API 응답에서 문자열로 받은 JSON을 파싱
            const movieStr = rs.movie;
            const movie = movieStr;
            const movieInfoResult = movie;
            const movieInfo = JSON.parse(rs.movie).movieInfoResult.movieInfo;

            // 영화 정보 추출
            const movieId = movieInfo.id;  //삭제시
            const movieNm = movieInfo.movieNm;
            const movieCdVal = movieInfo.movieCd;
            const openDt = movieInfo.openDt;
            const prdtStatNm = movieInfo.prdtStatNm;
            const nations = movieInfo.nations.map(n => n.nationNm).join(', ');
            const genres = movieInfo.genres.map(g => g.genreNm).join(', ');
            const typeNm = movieInfo.typeNm;
            const movieNmEn = movieInfo.movieNmEn;
            const source = movieInfo.source
            const html1 = `
                <span class="modal-close-btn" onclick="closeModal()">X</span>
                <h1>${movieNm} (${movieCdVal})</h1>
                <ul>
                    <li>개봉일: ${openDt}</li>
                    <li>상태: ${prdtStatNm}</li>
                    <li>국적: ${nations}</li>
                    <li>등급: ${genres}</li>
                    <li>장르: ${typeNm}</li>
                    <li>영문: ${movieNmEn}</li>
                    <li><button>삭제</button></li>
                </ul>
            `;

            document.querySelector('.movie-detail-modal-con').innerHTML = html1;
        })
        .catch(err => alert("영화 상세 정보 로딩 중 오류 발생: " + err.message));
};


// 이벤트위임 -> 리스트 부모객체에 이벤트를 한번만
const ul = document.querySelector('.menu-left > ul');

ul.addEventListener('click', function (e) {
    // closest() 함수
    // 주어진 CSS 선택자와 일치하는 가장 가까운(closest) 상위 조상 요소를 찾아서 반환
    // 이때, 해당 요소 자신도 검사 대상에 포함
    // 일치하는 요소가 없으면 null을 반환
    // 실제 클릭된 요소가 li가 아닐 경우 li를 찾아야 함
    const clickedLi = e.target.closest('li');

    // li가 아닌 영역을 클릭했을 경우 무시
    if (!clickedLi || !ul.contains(clickedLi)) return;

    // 모든 li에서 active 제거
    Array.from(ul.children).forEach((el) => el.classList.remove('active'));

    // 클릭된 li에 active 추가
    clickedLi.classList.add('active');
});

const boxOfficeFn = () => {
    let apiURL = "";
    const dateInputEl = document.getElementById("targetDt");
    const weekGbEl = document.querySelector("input[name='weekGb']:checked");

    // 오늘 날짜 구하기
    const getToday = () => {
        const today = new Date();
        today.setDate(today.getDate()-7);
        const yyyy = today.getFullYear(); // 년도 2025
        const mm = String(today.getMonth() + 1).padStart(2, '0'); // 9 -> 09
        const dd = String(today.getDate()).padStart(2,'0');      // 9 -> 09
        return `${yyyy}${mm}${dd}`;
    };
    // 요소가 없을 경우 기본값으로 요청
    if (!dateInputEl || !weekGbEl) {
//        apiURL = `/rest/boxOffice/20200920/0`;
        apiURL = `/open/api/movie/boxOffice/${getToday()}/0`; // 오늘 날짜, 주간(0) 기본
    } else {
        const dateInputVal = dateInputEl.value;
        const weekGb = weekGbEl.value;
        if (!dateInputVal) {
            alert("📅 날짜를 선택해주세요.");
            return;
        }
        const targetDt = dateInputVal.replaceAll("-", "");
        apiURL = `/open/api/movie/boxOffice/${targetDt}/${weekGb}`;
    }

    // 나머지 fetch 코드는 동일하게 유지
    fetch(apiURL)
        .then(res => res.json())
        .then(rs => {
            const boxOfficeResult = rs.boxOfficeResult;

            if (!boxOfficeResult || boxOfficeResult.length === 0) {
                alert("📭 조회된 박스오피스 데이터가 없습니다.");
                return;
            }

            let html2 = "";
            boxOfficeResult.forEach(el => {
                html2 += `
                    <tr>
                        <td>${el.rank}</td>
                        <td >${el.movieNm}</td>
                        <td>${el.openDt}</td>
                        <td>${Number(el.audiAcc).toLocaleString()}</td>
                        <td>${Number(el.salesAcc).toLocaleString()}</td>
                        <td>${el.movieCd}</td>
                        <td><button onclick="boxOfficeDetailFn('${el.movieCd}')">보기</button></td>
                    </tr>
                `;
            });

            const html1 = `
                <thead>
                    <tr>
                     <td colspan="7" style="text-align:right;padding: 1vmin; box-sizing:border-box">
                        <div class="box-office-form" style="margin-bottom: 20px;">
                            <label for="targetDt">📅 날짜 선택 (YYYY-MM-DD):</label>
                            <input type="date" id="targetDt">
                            <label style="margin-left: 20px;">📊 주간 구분:</label>
                            <label><input type="radio" name="weekGb" value="0" checked> 주간</label>
                            <label><input type="radio" name="weekGb" value="1"> 주말</label>
                            <label><input type="radio" name="weekGb" value="2"> 평일</label>
                            <button onclick="boxOfficeFn()" style="margin-left: 20px;">조회</button>
                        </div>
                     </td>
                    </tr>
                    <tr>
                        <td colspan="7" style="text-align:right;padding: 1vmin; box-sizing:border-box">
                            <span>${boxOfficeResult[0].boxofficeType}</span> ·
                            <span>${boxOfficeResult[0].showRange}</span>
                        </td>
                    </tr>
                    <tr>
                        <th>순위</th>
                        <th>제목</th>
                        <th>개봉일</th>
                        <th>누적관객수</th>
                        <th>누적매출액</th>
                        <th>영화코드</th>
                        <th>보기</th>
                    </tr>
                </thead>
                <tbody class="scene-content">
                    ${html2}
                </tbody>
            `;

            const tTable = document.querySelector(".t-table");
            if (tTable) {
                tTable.innerHTML = html1;
            } else {
                console.error("테이블 요소(.t-table)를 찾을 수 없습니다.");
            }
            // 박스오피스화면에서는 페이징 사라지기
            const pagingUl = document.querySelector('#moviePaging');
            if (pagingUl) {
                pagingUl.innerHTML = ''; // 페이징 영역 비우기
            }
        })
        .catch(err => {
            console.error("🚨 API 호출 중 에러:", err);
            alert("박스오피스 데이터를 불러오지 못했습니다.");
        });
};

const boxOfficeDetailFn = (movieCd) => {
//    const apiURL = `/rest/movieDetail/${movieCd}`;
    const apiURL = `/open/api/movie/boxOfficeDetailJava/${movieCd}`;
    fetch(apiURL)
        .then(res => res.json())
        .then(rs => {
            console.log(rs);  console.log(rs.movie);

            document.querySelector('.movie-detail-modal').style.display = 'flex';
            const movieStr = rs.movie;
            const movie = movieStr;

            const movieInfoResult = movie;
            const movieInfo = movieInfoResult;

            // 영화 정보 추출
            const movieId = movieInfo.id;  //삭제시
            const movieNm = movieInfo.movieNm;
            const movieCdVal = movieInfo.movieCd;
            const openDt = movieInfo.openDt;
            const rank = movieInfo.rank;
            const audiAcc = movieInfo.audiAcc;
            const salesAcc = movieInfo.salesAcc;

            const html1 = `
                <span class="modal-close-btn" onclick="closeModal()">X</span>
                <h1>${movieNm} (${movieCdVal})</h1>
                <ul>
                    <li>개봉일: ${openDt}</li>
                    <li>영화명: ${movieNm}</li>
                    <li>랭킹: ${rank}</li>
                    <li>누적관객수: ${audiAcc}</li>
                    <li>누적매출액: ${salesAcc}</li>
                    <li><button>삭제</button></li>
                </ul>
            `;

            document.querySelector('.movie-detail-modal-con').innerHTML = html1;
        })
        .catch(err => alert("영화 상세 정보 로딩 중 오류 발생: " + err.message));
};

const closeModal=(e)=>{
    document.querySelector('.movie-detail-modal').style.display = 'none';}
(() => {
    sceneListFn();
})();




















