window.addEventListener("DOMContentLoaded", () => {
    replyListFn();
});

// 댓글 목록 조회
async function replyListFn() {

    const communityId = document.getElementById("id").value;

    const loginMemberIdEl = document.getElementById("loginMemberId");

    const loginMemberId = loginMemberIdEl ? Number(loginMemberIdEl.value) : null;

    try {
        const response =
            await fetch(`/api/communityReply/list/${communityId}`);
        if (!response.ok) {
            throw new Error("댓글 조회 실패");
        }
        const result = await response.json();

        const replyList =
            document.getElementById("replyList");

        let html = "";

        result.forEach(reply => {

            const isOwner =
                loginMemberId !== null &&
                loginMemberId === Number(reply.memberId);

            html += `
                <div class="reply-item">

                    <div class="reply-header">
                        <span class="writer">
                            ${reply.replyWriter}
                        </span>

                        <span class="date">
                            ${reply.createTime}
                        </span>
                    </div>

                    <div class="reply-body">
                        <p>
                            ${reply.replyContent}
                        </p>
                    </div>

                    ${
                isOwner
                    ? `
                            <div class="reply-btns">
                                <button onclick="deleteReplyFn(${reply.id})">
                                    삭제
                                </button>
                            </div>
                          `
                    : ''
            }

                </div>
            `;
        });

        replyList.innerHTML = html;

        document.querySelector(".reply-count").innerText =
            result.length;

    } catch (error) {
        console.error(error);
    }
}


// 댓글 등록
async function saveReplyFn() {

    const communityId = document.getElementById("id").value;
    const memberId = document.getElementById("loginMemberId").value;
    const replyWriter = document.getElementById("replyWriter").value;
    const replyPw = document.getElementById("replyPw").value;
    const replyContent = document.getElementById("replyContent").value;

    if (!replyPw.trim()) {
        alert("비밀번호를 입력하세요.");
        return;
    }

    if (!replyContent.trim()) {
        alert("댓글 내용을 입력하세요.");
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
        const response = await fetch(
            "/api/communityReply/save",
            {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(dto)
            }
        );

        if (response.ok) {
            alert("댓글 등록 완료");
            document.getElementById("replyPw").value = "";
            document.getElementById("replyContent").value = "";
            replyListFn();
        } else {
            const msg = await response.text();
            alert(msg || "댓글 등록 실패");
        }
    } catch (error) {
        console.error(error);
    }
}


// 댓글삭제
async function deleteReplyFn(replyId) {

    if (!confirm("댓글을 삭제하시겠습니까?")) {
        return;
    }
    try {
        const response = await fetch(
            `/api/communityReply/delete/${replyId}`,
            {
                method: "DELETE"
            }
        );

        if (response.ok) {
            alert("삭제 완료");
            replyListFn();
        } else {
            const msg = await response.text();
            alert(msg || "삭제 실패");
        }
    } catch (error) {
        console.error(error);
    }
}