const payResult = document.querySelector('#payResult')
const paymentType = document.querySelector('#paymentType')
const cartId = document.querySelector('#cartId')
const memberId = document.querySelector('#memberId')
const orderPost = document.querySelector('#orderPost')
const orderMethod = document.querySelector('#orderMethod')
const orderAddr = document.querySelector('#orderAddr')

const paymentFn2 = (event) => {
    event.preventDefault();
    const url = `/payment/insert`;
    const itemData = {
        cartId: cartId.textContent,
        orderPost: orderPost.value,
        orderMethod: orderMethod.value,
        payResult: payResult.textContent,
        orderAddr: orderAddr.value,
        memberId: memberId.textContent,
        paymentType: paymentType.value,
    }
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(itemData),
    })
        .then(res => res.json())
        .then(rs => {
            console.log(rs)
            // 정상 ->
            if (rs == 1) {
                location.href = `/payment/paymentList/${memberId.textContent.trim()}`;
            }
            // 예러 -> 그대로
        })
        .catch(err => console.log(err));
    alert("결제 실행 GO");
}

// 모달열기
function openModal(event) {
    // 기본동작 및 새로고침(폼 제출시) 차단
    event.preventDefault();
    // paymentId 값을 가져오기 위해 'tr'행을 찾는다.
    const paymentId = event.target.closest('tr').querySelector('.paymentId').innerText;
    // 비동기요청 주소 설정
    const url = `/api/admin/payment/detail/${paymentId}`;

    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("결제 정보를 가져오는데 실패했습니다.")
            }
            return response.json();
        })
        .then(data => {
            if (data) {
                // 모달 정보넣기
                document.querySelector('.modal-paymentId2').innerText = data.id;
                document.querySelector('.modal-memberId2').innerText = data.memberId;
                document.querySelector('.modal-paymentType2').innerText = data.paymentType;
                document.querySelector('.modal-orderPost2').innerText = data.orderPost;
                document.querySelector('.modal-orderAddr2').innerText = data.orderAddr;
                document.querySelector('.modal-payResult2').innerText = `${data.payResult}`;
                document.querySelector('.modal-orderMethod2').innerText = data.orderMethod;
                document.querySelector('.modal-createTime2').innerText = data.createTime;
                // 상품 리스트 처리
                const modalItemList = document.querySelector('.modal-item-list');
                modalItemList.innerHTML = '';

                const items = data.paymentItemEntities;

                if (items && items.length > 0) {
                    items.forEach(item => {
                        modalItemList.innerHTML += `
                        <div class="item-badge">
                            <span>상품: ${item.paymentItemTitle}</span>
                            <span>${item.paymentItemPrice}원</span>
                            <span>${item.paymentItemSize}개</span>
                        </div>`;
                    });
                } else {
                    modalItemList.innerHTML = `<span>구매 상품이 없습니다.</span>`;
                }

                // 모달 클래스추가 (모달창 띄우기)
                document.querySelector('.modal').classList.add('active');
            }
        })
        .catch(err => console.log(err));
}

// 모달 닫기
function closeModal(event) {
    const modal = document.querySelector('.modal');
    if (modal) {
        modal.classList.remove('active');
    }
}

// 모달 바깥 영역 클릭 시 닫히는 서브 이벤트 (사용자 편의성)
window.addEventListener('click', function(event) {
    const modal = document.querySelector('.modal');
    if (event.target === modal) {
        modal.classList.remove('active');
    }
});
