window.addEventListener("DOMContentLoaded", () => {
    getItemReplyListFn();
});

// ======================================     상품 댓글 등록     ========================================== //
async function saveItemReplyFn() {
    const itemIdEl = document.getElementById('replyItemId');
    if (!itemIdEl) return;

    const itemId = itemIdEl.value;
    const writer = document.getElementById('itemReplyWriter').value;
    const content = document.getElementById('itemReplyContent').value;
    const password = document.getElementById('itemReplyPw').value;

    // 빈 댓글 및 비밀번호 누락 방지
    if (!password.trim()) {
        alert("비밀번호는 필수입니다.");
        document.getElementById('itemReplyPw').focus();
        return;
    }
    if (!content.trim()) {
        alert("댓글 내용을 입력하세요.");
        document.getElementById('itemReplyContent').focus();
        return;
    }

    // 전송할 페이로드 데이터 (ItemEntity 안에 id를 넣도록 맞춤)
    const payload = {
        itemReplyWriter: writer,
        itemReplyContent: content,
        itemReplyPw: password,
        itemEntity: {
            id: itemId
        }
    };

    try {
        const response = await fetch('/itemReply/insert', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            const msg = await response.text();
            alert(msg || "댓글이 등록되었습니다.");

            // 성공 시 입력창 초기화
            document.getElementById('itemReplyPw').value = '';
            document.getElementById('itemReplyContent').value = '';

            // 댓글 목록 다시 불러오기
            getItemReplyListFn();
        } else {
            const errorMsg = await response.text();
            alert("댓글 등록 실패: " + errorMsg);
        }
    } catch (error) {
        console.error("댓글 등록 중 오류:", error);
        alert("서버와 통신 중 오류가 발생했습니다.");
    }
}


// ======================================     상품 댓글 목록 조회     ========================================== //
async function getItemReplyListFn() {
    const itemIdEl = document.getElementById('replyItemId');
    if (!itemIdEl) return;

    const itemId = itemIdEl.value;

    try {
        const response = await fetch(`/itemReply/list/${itemId}`);
        if (!response.ok) {
            throw new Error("댓글 목록을 가져오는 데 실패했습니다.");
        }

        const data = await response.json();

        // 댓글 개수 표시 업데이트
        document.querySelector('.itemReply-count').textContent = `(${data.length})`;

        const listContainer = document.getElementById('itemReplyList');
        let html = "";

        if (data.length === 0) {
            html = "<p style='padding: 20px 0; color: #666;'>등록된 댓글이 없습니다.</p>";
        } else {
            data.forEach(reply => {

                // Spring Boot의 LocalDateTime이 JSON 배열 형태로 넘어올 경우를 방어하는 날짜 포맷팅 로직
                let createTime = reply.createTime || reply.updateTime || "";
                if (Array.isArray(createTime)) {
                    const month = String(createTime[1]).padStart(2, '0');
                    const day = String(createTime[2]).padStart(2, '0');
                    const hour = String(createTime[3]).padStart(2, '0');
                    const minute = String(createTime[4]).padStart(2, '0');
                    createTime = `${createTime[0]}.${month}.${day} ${hour}:${minute}`;
                }

                html += `
                    <div class="reply-item" style="padding: 15px 0; border-bottom: 1px solid #eee;">
                        <div class="reply-top" style="display: flex; justify-content: space-between; margin-bottom: 10px;">
                            <p style="font-weight: bold; margin: 0;">${reply.itemReplyWriter}</p>
                            <span style="font-size: 13px; color: #888;">${createTime}</span>
                        </div>
                        <div class="reply-content">
                            <p style="margin: 0; line-height: 1.5; color: #333;">${reply.itemReplyContent}</p>
                        </div>
                    </div>
                `;
            });
        }

        listContainer.innerHTML = html;

    } catch (error) {
        console.error("댓글 조회 중 오류:", error);
        document.getElementById('itemReplyList').innerHTML = "<p>댓글을 불러오는 중 오류가 발생했습니다.</p>";
    }
}