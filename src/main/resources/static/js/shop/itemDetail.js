const id = document.querySelector('#id')
const itemTitle = document.querySelector('#itemTitle')
const itemDetail = document.querySelector('#itemDetail')
const itemPrice = document.querySelector('#itemPrice')
const memberId = document.querySelector('#memberId')
const itemSize = document.querySelector('#itemSize')
const total = document.querySelector('span#total')

const totalFn = () => {
    const totalVal = itemSize.value * itemPrice.value
    console.log(totalVal)
    total.innerText = totalVal.toLocaleString()+'원';
};

itemSize.addEventListener('change', totalFn);


const cartFn = (event) => {
    event.preventDefault();

    const itemData = {
        id: id.value,
        itemTitle: itemTitle.value,
        itemDetail: itemDetail.value,
        itemPrice: itemPrice.value,
        memberId: memberId.value,
        itemSize: itemSize.value
    };

    fetch('/shop/insert/addCart2', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(itemData)
    })
        .then(res => res.json())
        .then(rs => {
            if (rs == 1) {
                alert('장바구니 등록 완료');
                location.href = `/shop/cartList/${memberId.value}`;
            } else {
                alert('장바구니 등록 실패');
            }
        })
        .catch(err => {
            console.error(err);
            alert('서버 오류');
        });
};


(() => totalFn())();