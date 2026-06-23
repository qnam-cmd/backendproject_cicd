const memberId = document.querySelector('#memberId')
const orderPost = document.querySelector('#orderPost')
const orderMethod = document.querySelector('#orderMethod')
const orderAddr = document.querySelector('#orderAddr')


const paymentFn=(event) => {
    event.preventDefault();
    console.log("버튼클릭됨");

    const url = `/api/payment/insert`;
    const itemData = {
        cartId: cartId.innerText,
        orderPost: orderPost.value,
        orderMethod: orderMethod.value,
        payResult: payResult.innerText,
        orderAddr: orderAddr.value,
        memberId : memberId.innerText,
        paymentType : paymentType.value,
    }
    fetch(url, {
        method:'POST',
        headers:{
            'Content-Type' : 'application/json'
        },
        body: JSON.stringify(itemData)
    })
        .then(res=> res.json())
        .then(rs=>{
            console.log(rs)
            //정상
            if(rs==1){
                location.href=`/payment/paymentList`;
            }
        })
        .catch(err=>console.log(err));
    alert("결제실행 GO");
}