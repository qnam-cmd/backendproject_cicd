const search = document.querySelector("#search")
const city = document.querySelector(".city .con")
const icon = document.querySelector(".city .icon")
const description = document.querySelector(".city .description")
const temp_min = document.querySelector(".temp_min .con")
const temp_max = document.querySelector(".temp_max .con")
const sunrise = document.querySelector(".sunrise .con")
const sunset = document.querySelector(".sunset .con")

// OpenWeather API Key 보안문제때문에 자바스크립트에서는 테스트로만쓰자
const key = `420a12d0bb4330371d33cb26b0b89588`;


// JSON 객체그대로받기(WeatherDto)
const weatherSearchFn = () => {
    const appURL = `/open/api/weather/search/${search.value}`

    fetch(appURL)
        .then(res => res.json())
        .then(rs => {
            console.log("서버 응답 데이터:", rs);

            // weatherDto
            const weather = rs.weather;

            city.innerText = weather.name;

            // ★ 주의: 현재 WeatherDto.java 파일에 description 변수가 없어서 글자가 안 나올 수 있습니다!
            if(description) description.innerText = weather.description || "상태 정보 없음";

            icon.innerHTML = `
                <img alt="icon" style="width:30px;height: 30px" src="https://openweathermap.org/img/wn/${weather.icon}@2x.png">
            `;

            if(temp_max) temp_max.innerText = Math.round(Number(weather.temp_max) - 272.15).toFixed(2);
            if(temp_min) temp_min.innerText = Math.round(Number(weather.temp_min) - 272.15).toFixed(2);
            if(sunrise) sunrise.innerText = new Date(Number(weather.sunrise) * 1000).toLocaleString();
            if(sunset) sunset.innerText = new Date(Number(weather.sunset) * 1000).toLocaleString();

            const lat = weather.lat;
            const lon = weather.lon;
            weatherMapFn(lat, lon);
        })
        .catch(err => console.error("날씨 API 에러:", err));
}

function weatherMapFn(lat,lon) {

    var mapContainer = document.getElementById('map'), // 지도를 표시할 div
        mapOption = {
            center: new kakao.maps.LatLng(lat, lon), // 지도의 중심좌표
            level: 3 // 지도의 확대 레벨
        };

    // 지도를 표시할 div와  지도 옵션으로  지도를 생성합니다
    var map = new kakao.maps.Map(mapContainer, mapOption);

}


// 즉시 실행함수
(()=>{
    weatherSearchFn();
})();