//즉시 실행함수
(() => {
    getItemReplyListFn();
})();

// 상품댓글 등록
const saveItemReplyFn = () => {
    const itemId = document.getElementById('replyItemId').value;
    const writer = document.getElementById('itemReplyWriter').value;
    const content = document.getElementById('itemReplyContent').value;
    const password = document.getElementById('itemReplyPw').value;

    // 빈 댓글 방지
    if (content.trim() === "") {
        alert("댓글을 입력하세요.");
        return;
    }
    if (password.trim() === "") {
        alert("비밀번호는 필수입니다.");
        return;
    }

    fetch('/itemReply/insert', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            itemReplyWriter: writer,
            itemReplyContent: content,
            itemReplyPw: password,
            // ItemReplyDto의 필드 타입이 ItemEntity 객체
            // JSON 타입에 맞게 {id:값} 변환
            itemEntity: {
                id: itemId
            }
        })
    })
        .then(res => res.text())
        .then(msg => {
            alert(msg); // 성공 알림창
            document.getElementById('itemReplyContent').value = ''; // 입력창 비우기
            getItemReplyListFn();   // 댓글작성후 목록보이기
        })
        .catch(error => {
            alert(error.message);
        });

}

// 상품 댓글 목록
function getItemReplyListFn() {
    const itemId = document.getElementById('replyItemId').value;

    fetch(`/itemReply/list/${itemId}`)
        .then(res => res.json())
        .then(data => {
            document.querySelector('.itemReply-count').textContent = `(${data.length})`;
            let html = "";

            // 댓글이 없을 경우
            if (data.length === 0) {
                html = "<p>등록된 댓글이 없습니다.</p>";
                return;
            } else {
                data.forEach(reply => {
                    html += `
                <div class="reply-item">
                    <div class="reply-top">
                        <p>${reply.itemReplyWriter}</p>
                    </div>
                    <div class="reply-content">
                        <p>${reply.itemReplyContent}</p>
                    </div>
                </div>
            `;
                });
            }
            document.getElementById('itemReplyList').innerHTML = html;
        });
}