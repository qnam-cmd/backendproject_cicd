const closeFn=()=> {
    document.querySelector(".admin_header_modal").classList.remove("show")
}

const adminMemberDetailFn = (event, id) => {
    event.preventDefault();
    console.log(id)
    const url = `/api/admin/member/detail/${id}`;
    fetch(url)
        .then((res) => res.json())
        .then((rs) => {
            console.log(rs)
            const member = rs.member;

            document.querySelector('.userId').innerText =member.id ;
            document.querySelector('.userEmail').innerText =member.userEmail ;
            document.querySelector('.userPw').innerText =member.userPw ;
            document.querySelector('.userName').innerText =member.userName ;
            document.querySelector('.role').innerText =member.role ;
            // 가입일 날짜 포맷 변환 (YYYY년 MM월 DD일 HH시 MM분)
            if (member.createTime) {
                const date = new Date(member.createTime);
                const year = date.getFullYear();
                const month = String(date.getMonth() + 1).padStart(2, '0');
                const day = String(date.getDate()).padStart(2, '0');
                const hours = String(date.getHours()).padStart(2, '0');
                const minutes = String(date.getMinutes()).padStart(2, '0');

                document.querySelector('.createTime').innerText = `${year}년 ${month}월 ${day}일 ${hours}시 ${minutes}분`;
            } else {
                document.querySelector('.createTime').innerText = '';
            }

        })
        .catch((err) => console.log(err));
    document.querySelector(".admin_header_modal").classList.add("show");
};