// openChat()           채팅 팝업 열기 + 웹소켓 연결 시도
// onconnect()          SockJS + STOMP로 서버에 연결 및 구독 설정
// msgSendClickFn()     질문을 전송하고 응답을 기다림
// showMessageFn()      받은 메시지를 화면에 출력
// disconnect()         채팅 종료 및 연결 해제
// questionString()     사용자 질문 HTML 반환 함수

// DOM 요소
const chatDisp = document.querySelector('#chat-disp');
const chatContent = document.querySelector('#chat-content');
const question = document.querySelector('#question');
let stompClient = null;

// 채팅창 열기
const openChat =()=> {
    if(typeof Stomp === 'undefined') {
        alert("채팅 라이브러리를 불러오는 중입니다. 잠시만 기다려주세요.")
    }
    console.log("openChat");
    chatDisp.classList.add('show');
    onconnect();
}
// STOMP 접속
const onconnect =()=> {
    alert('접속!');
    // 1. 웹소켓 연결 (SockJS)
    const socket = new SockJS('/chatEndpoint');
    // 2. STOMP 클라이언트 생성
    stompClient = Stomp.over(socket);
    // 3. 서버 연결
    stompClient.connect({},(frame) => {
        console.log("Connected", frame);
        // 최초 연결 시 서버에 인사 메시지 전송
        stompClient.send("/app/hellow",{}, JSON.stringify({content:'GUEST111'}));
        // 서버 응답 구독
        stompClient.subscribe("/topic/greetings",(message)=>{
            const body = JSON.parse(message.body);
            showMessageFn(body.message);
        });
        stompClient.subscribe("/topic/message",(message)=> {
            const body = JSON.parse(message.body);
            showMessageFn(body.message);
        })
        // rabbitmq 응답 구독
        stompClient.subscribe("/topic/question",(message) => {
            const body = JSON.parse(message.body);
            showMessageFn(body.message);
        })
        stompClient.subscribe("/topic/notification", (message) => {
            const body = JSON.parse(message.body);
            showMessageFn(body.message); // 화면출력
        })
    },(error)=>{
        console.error('STOMP연결 실패',error);
        alert('서버 연결실패');
    })
}

// 질문 전송
const msgSendClickFn =()=> {
    const inputVal = question.value.trim();
    if(inputVal.length===0) {
        alert('내용을 입력해주세요.');
        question.focus();
        return;
    }
    // 1. 사용자의 질문 메시지 바로 표시
    const questionHTML = questionString(inputVal);
    showMessageFn(questionHTML);
    // 2. 서버에 메시지 전송
    stompClient.send("/app/message",{},JSON.stringify({content:inputVal}));
    // 입력창 초기화 및 포커스
    question.value='';
    question.focus();
    // 채팅창 맨아래로 스크롤
    chatContent.scrollTop = chatContent.scrollHeight;
}

// rabbitMQ 이용한 질문 전송
const rabbitMsgSendClickFn =()=> {
    const inputVal = question.value.trim();
    if(inputVal.length===0) {
        alert('내용을 입력해주세요.');
        question.focus();
        return;
    }

    // 1. 사용자 질문을 화면에 먼저 표시
    const questionHTML = questionString(inputVal);
    showMessageFn(questionHTML);

    // 2. 서버의 @MessageMapping("/rabbit")으로 메시지 전송
    // 서버는 이 메시지를 받아 RabbitMQ 큐에 넣고,
    // 나중에 Receiver가 처리를 완료하면 /topic/question으로 응답을 보냅니다.
    stompClient.send("/app/bot", {}, JSON.stringify({
        content: inputVal
    }));

    // 3. 입력창 초기화
    question.value = '';
    question.focus();
}


// 사용자 질문 메시지 HTML생성
const questionString =(data)=> {
    const time = new Date().toLocaleString();
    return `
        <div class="questionString">
            <div class="question-data">${data}</div>
            <div class="data-time">${time}</div>
        </div>
    `;
}

// 채팅 메시지 출력
const showMessageFn =(message)=> {
    const divTag = document.createElement('div');
    divTag.classList.add('data-con');
    divTag.innerHTML = message;
    chatContent.appendChild(divTag);
    // 스크롤 맨 아래로 이동
    chatContent.scrollTop = chatContent.scrollHeight;
}

// 연결 해제
const disconnect =()=> {
    if(stompClient) {
        stompClient.disconnect(()=>{
            console.log("Disconnected");
        })
        stompClient = null;
    }
    chatDisp.classList.remove('show');
    chatContent.innerHTML = '';
}
document.querySelector('#close').addEventListener('click', () => {
    disconnect();
});
// Enter키로 메시지 전송
question.addEventListener('keydown',(e)=>{
    if(e.key === 'Enter') {
        msgSendClickFn();
    }
})


