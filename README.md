**# backendprojectex
*** 백앤드 프로젝트
1. 시큐리티,OAuth2		*****
2. 회원,상품,게시글,,,, ->  페이징,검색, 댓글,파일, CRUD -> 시간상 CR
3. 관리자 페이지, 사용자 페이지 *****
4. 웹소켓, RibbitMQ  설정 	*****
   -> docker pull rabbitmq
   -> docker run -d -p 15672:15672 -p 5672:5672 --name rabbitmq rabbitmq
   -> docker stop rabbitmq
5. OpenApi 활용 -> 기본 3가지 	*****
6. 타임리프	*****

7. 장바구니 , 결제
8. swagger 설정	*****

-> React -> 프론트 앤드
-> Full



[Step 0]
네트워크 생성 (먼저)
docker network create spring-network
네트워크 삭제
docker network rm spring-network
네트워크 목록
docker network ls

[**Step 1] 스프링 부트 빌드(최초1회 또는 자바코드 수정시 실행)
./gradlew clean build -x test

# 1. 예전에 만들어진 구버전 jar 파일들을 완벽히 삭제
.\gradlew.bat clean

# 2. 수정한 최신 코드로 jar 파일을 다시 생성
.\gradlew.bat bootJar

# 3. 도커 이미지를 강제로 다시 빌드하고 띄우기 (캐시 무시)
docker-compose up -d --build --force-recreate


[Step 2] 컴포즈 실행 (기존의 rm,run 명령어 전체를 이 한줄이 대체함)
-d: 백그라운드 실행, --build:Dockerfile을 사용해 자바 이미지를 새로빌드
docker compose up -d --build

[Step 3] 실시간 로그 확인
전체 로그 보기
docker compose logs -f backendprojectex

[Step 4] 전체 서비스 종료 및 컨테이너 삭제
-> DB, RabbitMq 데이터는 볼륨 덕분에 보존됨

깔끔하게 컨테이너들을 내리고 삭제하고 싶을 때 사용 (DB데이터는 물론 덕분에 보존됨)
docker compose down



# 1. 켜져있는 컴포즈 서비스가 있다면 종료
docker compose down

# 2. 충돌을 일으키는 기존 수동 네트워크 강제 삭제
docker network rm my-network

# 3. 다시 도커 컴포즈 실행
docker compose up -d --build

docker compose ps

docker compose logs -f 



*** docker compose 주요 명령어

docker compose down -> 기존 이미지 강제 제거
docker compose up -d -> 다시삭제

*** docker rmi -f 이미지 -> 특정 이미지 삭제

-> docker login 로그인 후

*** docker push
docker tag 도커이미지:태그 도커로그인아이디/도커이미지:태그

*** docker pull 도커로그인아이디/도커이미지:태그

*** 실제 프로젝트 예시
1. docker hub에 이미지 push
2. 실행하려는 로컬에 docker-compose.yml생성
3. 그위치에서 docker compose up -d# backendproject_cicd







=============================================== [리눅스 명령어] =======================================================

pwd		#현재위치
cd 디렉토리	#이동
cd ~/		#기본위치로 이동
mkdir 디렉토리	#디렉토리 생성
ls		#현재파일조회
ls -l		#현재 디테일한 파일조회
cat 파일	#파일 보기
sudo명령어	#관리자 권한으로 명령어 실행

1. 아파치 설치

sudo yum update -y
sudo yum install -y httpd	// apache 설치
sudo systemctl start httpd	// apache 실행
sudo systemctl enable httpd	// 인스턴스가 시작할 때 자동으로 시작
sudo systemctl is-enabled httpd// 확인 enabled -> 정상



2. 자바 설치

sudo yum list java*jdk-devel
sudo dnf install -y java-17-amazon-corretto-devel
java -version
which java	-> 자바위치


------------------------- git설치 -----------------------------------------

sudo yum install git
git --version

--------------------------- 메모리 확장 ------------------------------------------

# 기존에 혹시 잡혀있을지 모르는 스왑파일 비활성화 및 삭제
sudo swapoff -a
sudo rm -f /swapfile

# 2GB 용량의 가상메모리 파일 생성
sudo dd if=/dev/zero of=/swapfile bs=1M count=2048

# 스왑파일 권한 및 타입 설정
sudo chmod 600 /swapfile
sudo mkswap /swapfile

# 스왑 공간 활성화
sudo swapon /swapfile

# 재부팅 시에도 자동 적용되도록 등록 (기존에 등록되어 있다면 중복 등록 방지용 확인 필요)
# 안전하게 파일 맨 끝에 등록 상태 확인후 free -m 확인
free -m

-------------------- ec2 -> <- github 연동 -----------------------------------

cd ~/.ssh
ssh-keygen -t rsa -C {github이메일}
cat id_rsa.pub

이후 엔터 3번정도 누르면 key값 나온다

ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABgQDF1Gq1ElbLH4zM8+oYInjW2nZ/r/EfW2w9yEsXywtxJkLp8uEXTuy40JqHpSjnajvkhlsX58YdKJiqidrIZWqbrJLDZugZnu6jXgQEc4FFbBjuYOqpFEuJYMC9Tj4DDaaW8w7FGxFp3nF8F0DfbXv9GppxXgJH9xrEn1dzMM04MYGoi+gKltVmiVfokwpYqn9fSXpN7xemnFcNPEkTqzkhhZbsbSFMm5CXC6P8cm6ipHkJAYERfqIb1u/Evwwb0Sn9SwDIIp9OvpIgWNEjncl1II6roDC30ZMnCz2WRFjE3HVZ7k6cpyQawi7LZvhYQKAhwQq0+ddYQxAWrCmoocK7xpiqP866XxZRdzYXEdLEsAo49sR8AH8SHGv8ZqrEZQJ67ufEDZbHWaA5/3w67oKmWqo6AtXoDgpsXtW2S9IEKfATkluIBfVEEfN5OHvjBphhBX7sJcMPsXBBrowS/gS7SSc93c8z+vYBM9A6DYb00/ds0nwnxGzktoDpL6PRHB0= qnamed@gmail.com




--------------------------------- docker 설치 ---------------------------------------

# 패키지 리스트 업데이트
sudo dnf update -y

# 도커 엔진 설치
sudo dnf install -y docker

# 도커 서비스 시작
sudo systemctl start docker

# 부팅 시 자동실행 설정
sudo systemctl enable docker

# 현재 사용자(ec2-user)를 docker 그룹에 추가
sudo usermod -aG docker $USER

# 변경된 그룹 설정을 현재 터미널 세셔넹 즉시반영
newgrp docker

docker version


----------------------------------- docker-compose 설치 ---------------------------------

# 도커 플러그인 디렉토리 생성
mkdir -p ~/.docker/cli-plugins/

# GitHub 공식 저장소에서 최신 도커 컴포즈 바이너리 다운
curl -SL https://github.com/docker/compose/releases/download/v2.26.0/docker-compose-linux-x86_64 -o ~/.docker/cli-plugins/docker-compose

# 파일에 실행 권한 부여
chmod +x ~/.docker/cli-plugins/docker-compose

# 도커 서비스 재시작
sudo systemctl restart docker

# 설치 정상 여부 확인
docker compose version


********* AI 답변 ****************
# 1. 최신 버전의 Docker Compose 다운로드 및 /usr/local/bin 디렉토리에 저장
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

-L				# 다운로드 주소가 리다이렉트될 경우 자동으로 추적하는옵션
$(uname -s) , $(uname -m)	# 각각 시스템의 OS(Linux)와 아키텍쳐(x86_64또는 aarch64)를 자동 감지, 알맞은 바이너리파일 타게팅
-o				# 출력할 파일 위치

# 2. 다운로드한 파일에 실행(Execute) 권한 부여
sudo chmod +x /usr/local/bin/docker-compose

# 3. 설치가 잘 되었는지 버전 확인
docker-compose version




************
-p 옵션은 상위폴더가 없으면 상위 폴더까지 자동으로 한번에 만들어주는 옵션

1. app 폴더 생성 및 이동
   mkdir -p /home/ec2-user/app

cd /home/ec2-user/app

2. docker-compose.yml 생성 -> 관리자 권한으로 생성(sudo)
   sudo vi docker-compose.yml
## 주요명령어
:q 	-> 저장 안하고 종료
:wq	-> 저장 하고 종료


3. 파일 업로드 폴더에 권한설정 -> 777(모든권한허용)
   mkdir -p /home/ec2-user/app/uploads/item
   mkdir -p /home/ec2-user/app/uploads/board

# upload 폴더와 그 하위 폴더 전체의 읽기/쓰기 권한을 도커가 접근허용
sudo chmod -R 777 /home/ec2-user/app/uploads




배포 주소: http://43.201.217.118:8094

서버 정보: AWS EC2 (Amazon Linux 2023)

배포 방식: GitHub Actions를 통한 Docker 자동 배포

환경 변수 관리: GitHub Settings -> Secrets에 등록 (KAKAO_MAP_APPKEY, DB_PASSWORD 등)

서버 접속 방법: 관리자 권한 소지자만 SSH 접속 가능