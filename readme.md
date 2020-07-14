# 몰입캠프 1주차 과제

### 팀원

* [박종우](https://github.com/jjwow73)
* [이혜민](https://github.com/IamHyemin)

## 1주차 과제

### 안드로이드 어플 만들기

- [x] 첫번째 탭: 나의 연락처 구축
- [x] 두번째 탭: 나만의 이미지 갤러리 구축.
- [x] 세번째 탭: 다이어리

### 기술 버전

* Android Gradle Plugin Version: 4.0.0
* Gradle Version : 6.1.1
* min SDK Version: 24(Nougat 7.0)
* target SDK Version: 30

### 요약

<img src=".\docs\summary.jpg" alt="git-flow_overall_graph" style="zoom:80%;" />

<img src=".\docs\git_summary.jpg" alt="git-flow_overall_graph" style="zoom: 80%;" />

#### 개발 Flow

**Git Flow**를 활용했습니다. 

<img src=".\docs\git-flow_overall_graph.png" alt="git-flow_overall_graph" style="zoom: 50%;" />

**Hyemin**이란 `develop` branch를 두고, **Hyemin**을 기준으로 PR(Pull Request) 작업을 수행했으며 PR시 Code Review 과정을 거쳐 Approve시 Merge작업을 했습니다. 기능 구현이 완전할 때 `master` branch로 Merge했습니다.

#### 스토리보드
세번째 탭을 시작하기 앞서, 기본적인 스토리보드를 디자인을 먼저 했습니다. [프로토타입참고](https://www.figma.com/proto/dhskBeSnSTPV0YASGrzDDQ/FlowCamp-ThirdTab?node-id=0%3A1&scaling=scale-down)

## 설명

### 1. 연락처

- JSON 파일에서 사진, 이름, 연락처 연동
- 핸드폰 연락청에서 사진, 이름, 연락처 연동
- 연락처 클릭하면 전화, 문자가 가능한 화면 나타남
- 연락처를 오래 누르면 삭제
- 오른쪽 아래 FAB 버튼으로 이름, 연락처 추가
- 검색 기능
- DB와 연결하여 앱 나갔다가 들어와도 갱신된 데이터 유지

### 2. 갤러리

- 오른쪽 아래 버튼 클릭하여 카메라로 찍은 사진 저장

- 사진 클릭하면 세부화면
- ViewPager 이용 세부화면에서 좌우 스와이프로 사진 이동
- GestureDector 이용 세부화면에서 위아래 플링감지 후 세부정보 확인 동작
- GridView -> RecyclerView+Glide로 성능 개선.

### 3. 다이어리

- 예쁜 달력
- 프라이버시를 존중한 비밀번호 잠금 해제
- 각 날짜 클릭 시 날짜에 맞는 To-Do, 일기 열람 가능
- 날짜마다 To-Do, 일기 추가 가능
- To-Do 있는 날짜에 보라색 점 추가
- 앱 사용 중 재미를 위한 효과음
- DB와 연결하여 앱 나갔다가 들어와도 갱신된 데이터 유지



## 아쉬운 점

* 커밋의 단위: 너무 자주 커밋한 느낌. 기능 단위로, 그리고 해당 기능에 대한 테스트 검증이 끝난 단위를 커밋의 단위로  둘 것.
* 아키텍쳐: 안드로이드를 처음하다보니 폴더 구조나 아키텍쳐가 미흡함. 구글의 추천 아키텍쳐인 *MVVM* 구조를 사용해볼 것.

