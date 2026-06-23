window.addEventListener("DOMContentLoaded", () => {
    replyListFn();
});

// ======================================     게시글 삭제     ========================================== //
async function deleteCommunityFn(communityId) {
    if (!confirm("게시글을 삭제하시겠습니까?")) {
        return;
    }

    const loginMemberIdEl = document.getElementById("loginMemberId");
    const memberId = loginMemberIdEl ? loginMemberIdEl.value : null;

    if (!memberId) {
        alert("로그인 정보가 없습니다.");
        return;
    }

    try {
        // 백엔드 컨트롤러 API 경로에 맞게 URL 호출 (Controller 구현에 따라 수정될 수 있습니다)
        const response = await fetch(`/api/community/delete/${communityId}?memberId=${memberId}`, {
            method: "DELETE"
        });

        if (response.ok) {
            alert("게시글 삭제가 완료되었습니다.");
            location.href = "/community/communityList"; // 삭제 후 목록 페이지로 이동
        } else {
            const msg = await response.text();
            alert(msg || "게시글 삭제에 실패했습니다.");
        }
    } catch (error) {
        console.error(error);
        alert("서버와 통신 중 오류가 발생했습니다.");
    }
}


// ======================================     댓글 목록 조회     ========================================== //
async function replyListFn() {
    const communityId = document.getElementById("id").value;
    const loginMemberIdEl = document.getElementById("loginMemberId");
    // sec:authorize="isAuthenticated()" 조건으로 인해 태그가 렌더링되지 않았을 경우를 대비한 안전한 처리
    const loginMemberId = loginMemberIdEl ? Number(loginMemberIdEl.value) : null;

    try {
        const response = await fetch(`/api/communityReply/list/${communityId}`);
        if (!response.ok) {
            throw new Error("댓글 조회 실패");
        }
        const result = await response.json();

        const replyList = document.getElementById("replyList");
        let html = "";

        result.forEach(reply => {
            // 본인이 작성한 댓글인지 확인하여 삭제 버튼 노출 여부 결정
            const isOwner = loginMemberId !== null && loginMemberId === Number(reply.memberId);

            // Spring Boot에서 LocalDateTime이 배열 형태로 넘어올 경우를 대비한 날짜 포맷팅 방어 코드
            let createTime = reply.createTime;
            if (Array.isArray(createTime)) {
                const month = String(createTime[1]).padStart(2, '0');
                const day = String(createTime[2]).padStart(2, '0');
                const hour = String(createTime[3]).padStart(2, '0');
                const minute = String(createTime[4]).padStart(2, '0');
                createTime = `${createTime[0]}.${month}.${day} ${hour}:${minute}`;
            }

            html += `
                <div class="reply-item">
                    <div class="reply-header">
                        <span class="writer">${reply.replyWriter}</span>
                        <span class="date">${createTime}</span>
                    </div>
                    <div class="reply-body">
                        <p>${reply.replyContent}</p>
                    </div>
                    ${isOwner ? `
                        <div class="reply-btns">
                            <button type="button" onclick="deleteReplyFn(${reply.id})">삭제</button>
                        </div>
                    ` : ''}
                </div>
            `;
        });

        replyList.innerHTML = html;
        document.querySelector(".reply-count").innerText = result.length;

    } catch (error) {
        console.error(error);
    }
}


// ======================================     댓글 등록     ========================================== //
async function saveReplyFn() {
    const communityId = document.getElementById("id").value;
    const loginMemberIdEl = document.getElementById("loginMemberId");

    // 비정상적인 접근(비로그인) 방어
    if (!loginMemberIdEl) {
        alert("로그인이 필요한 서비스입니다.");
        return;
    }

    const memberId = loginMemberIdEl.value;
    const replyWriter = document.getElementById("replyWriter").value;
    const replyPw = document.getElementById("replyPw").value;
    const replyContent = document.getElementById("replyContent").value;

    if (!replyPw.trim()) {
        alert("비밀번호를 입력하세요.");
        document.getElementById("replyPw").focus();
        return;
    }

    if (!replyContent.trim()) {
        alert("댓글 내용을 입력하세요.");
        document.getElementById("replyContent").focus();
        return;
    }

    const dto = {
        communityId,
        memberId,
        replyWriter,
        replyPw,
        replyContent
    };

    try {
        const response = await fetch("/api/communityReply/save", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(dto)
        });

        if (response.ok) {
            alert("댓글 등록 완료");
            // 작성 폼 초기화
            document.getElementById("replyPw").value = "";
            document.getElementById("replyContent").value = "";
            // 작성 후 댓글 목록 갱신
            replyListFn();
        } else {
            const msg = await response.text();
            alert(msg || "댓글 등록 실패");
        }
    } catch (error) {
        console.error(error);
    }
}


// ======================================     댓글 삭제     ========================================== //
async function deleteReplyFn(replyId) {
    if (!confirm("댓글을 삭제하시겠습니까?")) {
        return;
    }

    try {
        const response = await fetch(`/api/communityReply/delete/${replyId}`, {
            method: "DELETE"
        });

        if (response.ok) {
            alert("삭제 완료");
            replyListFn(); // 삭제 후 댓글 목록 갱신
        } else {
            const msg = await response.text();
            alert(msg || "삭제 실패");
        }
    } catch (error) {
        console.error(error);
    }
}