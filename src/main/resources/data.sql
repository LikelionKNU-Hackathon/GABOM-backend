INSERT INTO stamps (category, image_url, name) VALUES
-- 분식
('분식', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/bunsik_1.png', '분식 스탬프1'),
('분식', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/bunsik_2.png', '분식 스탬프2'),
('분식', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/bunsik_3.png', '분식 스탬프3'),

-- 한식
('한식', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/korea_1.png', '한식 스탬프1'),
('한식', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/korea_2.png', '한식 스탬프2'),
('한식', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/korea_3.png', '한식 스탬프3'),

-- 요리주점
('요리주점', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/pub_1.png', '요리주점 스탬프1'),
('요리주점', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/pub_2.png', '요리주점 스탬프2'),
('요리주점', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/pub_3.png', '요리주점 스탬프3'),

-- 디저트
('디저트', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/cafe_1.png', '디저트 스탬프1'),
('디저트', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/cafe_2.png', '디저트 스탬프2'),
('디저트', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/cafe_3.png', '디저트 스탬프3'),

-- 오락
('오락', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/game_1.png', '오락 스탬프1'),
('오락', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/game_2.png', '오락 스탬프2'),
('오락', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/game_3.png', '오락 스탬프3'),

-- 아시안
('아시안', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/asia_1.png', '아시안 스탬프1'),
('아시안', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/asia_2.png', '아시안 스탬프2'),
('아시안', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/asia_3.png', '아시안 스탬프3'),

-- 양식
('양식', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/western_1.png', '양식 스탬프1'),
('양식', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/western_2.png', '양식 스탬프2'),
('양식', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/western_3.png', '양식 스탬프3'),

-- 중식
('중식', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/china_1.png', '중식 스탬프1'),
('중식', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/china_2.png', '중식 스탬프2'),
('중식', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/china_3.png', '중식 스탬프3'),

-- 일식
('일식', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/japan_1.png', '일식 스탬프1'),
('일식', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/japan_2.png', '일식 스탬프2'),
('일식', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/japan_3.png', '일식 스탬프3'),

-- 서비스
('서비스', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/service_1.png', '서비스 스탬프1'),
('서비스', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/service_2.png', '서비스 스탬프2'),
('서비스', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/stamps/service_3.png', '서비스 스탬프3');

INSERT INTO titles (category, name, description, goal_count) VALUES
                                                                 ('분식', '분식 요정', '분식 10회 방문하기', 10),
                                                                 ('일식', '일식愛 빠진 자', '일식 10회 방문하기', 10),
                                                                 ('중식', '중국집 단골왕', '중식 10회 방문하기', 10),
                                                                 ('한식', '한식의 품격', '한식 10회 방문하기', 10),
                                                                 ('양식', '이탈리아 맛피아', '양식 10회 방문하기', 10),
                                                                 ('요리주점', '오늘도 한잔러', '요리주점 10회 방문하기', 10),
                                                                 ('디저트', '당충전 전문가', '디저트 10회 방문하기', 10),
                                                                 ('오락', '오락실 지박령', '오락 10회 방문하기', 10),
                                                                 ('서비스', '생활밀착왕', '서비스 10회 방문하기', 10),
                                                                 ('아시안', '향신료 정복자', '아시안 10회 방문하기', 10);

-- 저널 케이스 데이터
INSERT INTO journal_cases (name, category, image_url) VALUES
                                                          ('Animal 1', '동물', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/case/animal_1.png'),
                                                          ('Animal 2', '동물', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/case/animal_2.png'),
                                                          ('Animal 3', '동물', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/case/animal_3.png'),

                                                          ('Spring 1', '벚꽃', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/case/spring_1.png'),
                                                          ('Spring 2', '벚꽃', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/case/spring_2.png'),

                                                          ('Pattern 1', '패턴', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/case/pattern_1.png'),
                                                          ('Pattern 2', '패턴', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/case/pattern_2.png'),
                                                          ('Pattern 3', '패턴', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/case/pattern_3.png'),

                                                          ('Summer 1', '여름', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/case/summer_1.png'),
                                                          ('Summer 2', '여름', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/case/summer_2.png'),
                                                          ('Summer 3', '여름', 'https://gabom-stamps.s3.ap-northeast-2.amazonaws.com/case/summer_3.png');

INSERT INTO stores (name, address, category, opening_hours, latitude, longitude) VALUES
-- 분식
('The 진분식', '경기 용인시 기흥구 동백죽전대로527번길 100-3 (중동 1110-10)', '분식', '10:00 ~ 18:00', 37.280786, 127.143401),
('금화로 분식', '경기 용인시 기흥구 금화로108번길 34 1층 (상갈동 101-23)', '분식', '월~토 06:00 ~ 15:00', 37.269460, 127.106706),
('황보네 분식', '경기 수원시 권선구 권광로 89', '분식', '10:30 - 19:00', 37.255867, 127.029424),

-- 한식
('가오리와 방패연', '경기 용인시 기흥구 하갈로 98 (하갈동 233)', '한식', '매일 15:00 ~ 17:00', 37.256227, 127.098087),
('공세로 분식', '경기 용인시 기흥구 공세로 243 (공세동 394-9)', '한식', '화~토 11:00 ~ 21:00 (휴게 15:30~17:00)', 37.242103, 127.103425),
('죽전로 분식', '경기 용인시 기흥구 죽전로43번길 11-10 1층 (보정동 1203-10)', '한식', '화~일 11:30 ~ 21:30 (휴게 15:30~17:00)', 37.321275, 127.112491),

-- 요리주점
('라파즈', '경기 용인시 기흥구 강남로 12 스카이프라자 201·202호', '요리주점', '매일 17:00 ~ 03:00', 37.271107, 127.127487),
('다복상회', '경기 용인시 기흥구 강남로 3 105호', '요리주점', '17:00 ~ 01:00 (라스트오더 00:30)', 37.271279, 127.126246),
('친구포차', '경기 용인시 기흥구 강남동로 12 103호', '요리주점', '13:00 ~ 02:00 (라스트오더 00:30)', 37.270934, 127.127865),

-- 디저트
('지니엄', '경기 용인시 기흥구 강남서로75번길 2-8 파크빌 103호', '디저트', '10:00 ~ 17:50 (일·월 휴무)', 37.278384, 127.130253),
('또우화', '경기 용인시 기흥구 갈곡로7번길 3 1층', '디저트', '10:00 ~ 20:00 (일 휴무)', 37.272224, 127.128679),
('어바웃유어유스', '경기 용인시 기흥구 갈곡로7번길 7 1층 104호', '디저트', '11:00 ~ 18:00 (일·월 휴무)', 37.272420, 127.128324),

-- 오락
('셜록홈즈 용인동백점', '경기 용인시 기흥구 동백중앙로 283 골드프라자 D동 303~305호', '오락', '매일 10:00 ~ 22:40', 37.277682, 127.153145),
('토리PC 강남대점', '경기 용인시 기흥구 강남로 6 삼성타운 2층', '오락', '24시간 영업', 37.270451, 127.126958),
('히트코인노래연습장', '경기 용인시 기흥구 강남로 7 2층', '오락', '매일 10:00 ~ 24:00', 37.271423, 127.126489),

-- 아시안
('포카라 인도 요리전문점', '경기 용인시 기흥구 구갈동 594-1 그랜드프라자 204호', '아시안', '매일 11:00 ~ 22:00', 37.262677, 127.148081),
('화화돈', '경기 용인시 기흥구 기흥역로 63 AK& 기흥 3층', '아시안', '매일 11:00 ~ 21:00 (라스트오더 20:30)', 37.274670, 127.116764),
('하노이별', '경기 용인시 기흥구 기흥역로 63 AK& 기흥 1층', '아시안', '매일 10:30 ~ 21:00 (브레이크 15:30~16:30)', 37.274364, 127.117072),

-- 양식
('쏘어', '경기도 용인시 기흥구 구갈동 582 3번지 111호', '양식', '월~토 11:00 ~ 21:00 (브레이크 15:00~16:00)', 37.241200, 127.078700),
('레볼루션', '경기도 용인시 기흥구 구갈동 592-3번지 102호', '양식', '매일 11:00 ~ 22:00', 37.271877, 127.128617),
('인앤피자', '경기도 용인시 기흥구 기흥로14번길 4', '양식', '15:00 ~ 02:00', 37.277352, 127.116452),

-- 중식
('청궁', '경기 용인시 기흥구 갈곡로7번길 4', '중식', '10:05 ~ 20:00', 37.272237, 127.128876),
('남경', '경기 용인시 기흥구 갈곡로8번길 4-3 사랑하는교회', '중식', '09:30 ~ 21:30', 37.271584, 127.129443),
('한성양꼬치', '경기 용인시 기흥구 강남로 13 한성양꼬치 116호', '중식', '14:00 ~ 24:00', 37.271845, 127.127048),

-- 서비스
('카이정헤어 용인 강남대점', '경기 용인시 기흥구 강남동로 6 그랜드프라자 2층', '서비스', '매일 10:00 ~ 20:00', 37.271579, 127.127885),
('위시엔조이 셀프빨래방 용인 강남대점', '경기 용인시 기흥구 갈곡로8번길 4-17', '서비스', '24시간 영업', 37.270963, 127.130490),
('다이소 용인강남대점', '경기 용인시 기흥구 강남로 7 203, 204호', '서비스', '매일 09:30 ~ 22:00', 37.271479, 127.126637),

-- 일식
('연정', '경기 용인시 기흥구 강남동로 14 강남대 팰리스뷰오피스텔 1층 102호', '일식', '09:30 ~ 22:00 (브레이크 14:30~15:30, 일 휴무)', 37.270597, 127.127678),
('겐코', '경기 용인시 기흥구 강남동로 7 1층', '일식', '11:00 ~ 21:00 (일 휴무)', 37.271668, 127.128387),
('감성초밥 한판', '경기 용인시 기흥구 갈곡로 3 덕천빌딩 101호', '일식', '11:00 ~ 22:00 (브레이크 15:00~17:00, 월 휴무)', 37.271712, 127.128780);
