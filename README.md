# 뚜밤뚜밤 서버
https://ddubam.site

📑 Architecture

![Frame 1321317857](https://github.com/seoul-night/ddubam-server/assets/72538151/6c528fb6-3f5a-4b2c-af99-5403e24491d6)
📋 Model Diagram

<img width="703" alt="스크린샷 2024-06-25 오전 3 08 01" src="https://github.com/seoul-night/ddubam-server/assets/72538151/4fb04231-58d7-48b9-b378-0792330abb2e">

# API 명세서

| 이름                                    | URL                                                                 | Method |
|---------------------------------------|-------------------------------------------------------------------|--------|
| 주변 산책로 리스트 조회 API           | /walks/near/{latitude}/{longitude}                                | GET    |
| 인기 산책로 리스트 조회 API           | /walks/popular                                                    | GET    |
| 관광지 리스트 조회 API                | /atractions                                                       | GET    |
| 관광지 주변 산책로 ID 조회 API        | /atractions/{latitude}/{longitude}                                | GET    |
| 산책로 상세정보 조회 API              | /walks/{trailId}/{userId}                                         | GET    |
| 유저 정보 조회 API                    | /members/{userId}                                                 | GET    |
| 완료한 산책 리스트 조회 API           | /members/walks/complete/{userId}                                  | GET    |
| 찜한 산책 리스트 조회 API             | /members/walks/select/{userId}                                    | GET    |
| 찜 목록 제거 API                      | /members/walks/select                                             | DELETE |
| 찜 목록 추가 API                      | /members/walks/select                                             | POST   |
| 산책 완료 추가 API                    | /members/walks/complete                                           | POST   |
| kakao로그인 API                       | /members/kakao/login                                              | POST   |
| 경로 검색 API                         | /walks/search/{startLatitude}/{startLongitude}/{endLatitude}/{endLongitude} | GET    |
| 현위치 기반 새로운 산책로 생성 API    | /walks/new/{latitude}/{longitude}                                 | GET    |
| 인기 산책로 이동 경로 및 산책로 조회 API | /walks/popular/route/{trailId}/{userId}/{latitude}/{longitude}   | GET    |
| 회원 탈퇴 API                         | /members/{userId}                                                 | DELETE |
| 도착지 후기 작성 API                  | /members/walks/search/complete                                    | POST   |
| 도착지 후기 리스트 조회 API           | /members/walks/search/complete/{userId}                           | GET    |
| 검색어 추가 API                       | /members/search/{userId}/{search}                                 | POST   |
| 최근 검색어 조회 API                  | /members/search/{userId}                                          | GET    |
| 검색어 삭제 API                       | /members/search/{userId}/{searchId}                               | DELETE |


# 화면설계도
<img width="761" alt="스크린샷 2024-06-25 오전 3 13 43" src="https://github.com/seoul-night/ddubam-server/assets/72538151/4ed27f56-51ed-4855-908b-126b26372493">
