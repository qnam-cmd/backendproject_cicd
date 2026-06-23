const memberDetailFn=(event, id)=>{
    event.preventDefault();
    console.log(id)
    const url = `/api/admin/member/detail/${id}`;
    fetch(url)
        .then((res)=>res.json())
        .then((rs)=> {
            console.log(rs)
            const member = rs.member;
            document.querySelector('.userId2').innerText = member.id;
            document.querySelector('.userEmail2').innerText = member.userEmail;
            document.querySelector('.userPw2').innerText = member.userPw;
            document.querySelector('.userName2').innerText = member.userName;
            document.querySelector('.role2').innerText = member.role;
            // 가입일 날짜 포맷 변환 (YYYY년 MM월 DD일 HH시 MM분)
            if (member.createTime) {
                const date = new Date(member.createTime);
                const year = date.getFullYear();
                const month = String(date.getMonth() + 1).padStart(2, '0');
                const day = String(date.getDate()).padStart(2, '0');
                const hours = String(date.getHours()).padStart(2, '0');
                const minutes = String(date.getMinutes()).padStart(2, '0');

                document.querySelector('.createTime2').innerText = `${year}년 ${month}월 ${day}일 ${hours}시 ${minutes}분`;
            } else {
                document.querySelector('.createTime2').innerText = '';
            }
        })
        .catch((err)=>console.log(err));
    document.querySelector(".member_modal").classList.add("show");
}

const closeFn0=()=> {
    document.querySelector(".member_modal").classList.remove("show")
}