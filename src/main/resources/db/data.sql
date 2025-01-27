use mentoring_db;

insert into users values(0,202155532,1,now(),now(),'장전캠퍼스','정보의생명공학대학 정보컴퓨터공학부','yesul0718@pusan.ac.kr','김예슬','01041930473','0','ADMIN');

#SystemUser_DB
insert into system_users value (1,now(), now(),'admin','$2a$10$eeka6zXwBZ3QspJvtxHwW.tJXOvEnaHYAub5tOsSP6kDuW9Ympaey','ROOT');

insert into home_edits value (1,'mainpage_1','2025년 멘토링 프로그램'),
(2,'mainpage_Calendar','                <li><span class="list-date">2024-09-02 (월) ~ 2024-09-08 (일)</span> <span class="subject">1차 커리큘럼 계획서 제출</span></li>
                <li><span class="list-date">2024-09-03 (화) ~ 2024-09-08 (일)</span> <span class="subject">1주차</span></li>
                <li><span class="list-date">2024-09-09 (월) ~ 2024-09-15 (일)</span> <span class="subject">2주차</span></li>
                <li><span class="list-date">2024-09-16 (월) ~ 2024-09-22 (일)</span> <span class="subject">3주차</span></li>
                <li><span class="list-date">2024-09-23 (월) ~ 2024-09-29 (일)</span> <span class="subject">4주차</span></li>
                <li><span class="list-date">2024-09-30 (월) ~ 2024-10-06 (일)</span> <span class="subject">5주차</span></li>
                <li><span class="list-date">2024-10-07 (월) ~ 2024-10-11 (금)</span> <span class="subject">6주차</span></li>
                <li><span class="list-date">2024-10-12 (토) ~ 2024-10-15 (화)</span> <span class="subject">중간보고서 제출</span></li>
                <li><span class="list-date">2024-10-20 (일) ~ 2024-10-27 (일)</span> <span class="subject">중간 점검 날짜</span></li>
                <li><span class="list-date">2024-10-29 (화) ~ 2024-10-29 (화)</span> <span class="subject">산업체 멘토링 강사와 대면 점검</span></li>
                <li><span class="list-date">2024-10-30 (수) ~ 2024-11-02 (토)</span> <span class="subject">2차 커리큘럼 계획서 제출</span></li>
                <li><span class="list-date">2024-11-04 (월) ~ 2024-11-10 (일)</span> <span class="subject">2주차</span></li>
                <li><span class="list-date">2024-11-11 (월) ~ 2024-11-17 (일)</span> <span class="subject">3주차</span></li>
                <li><span class="list-date">2024-11-18 (월) ~ 2024-11-24 (일)</span> <span class="subject">4주차</span></li>
                <li><span class="list-date">2024-11-25 (월) ~ 2024-12-01 (일)</span> <span class="subject">5주차</span></li>
                <li><span class="list-date">2024-12-02 (월) ~ 2024-12-06 (금)</span> <span class="subject">6주차, 최종 보고서 제출</span></li>
                <li><span class="list-date">2024-12-07 (토) ~ 2024-12-09 (월)</span> <span class="subject">최종 프로젝트 결과물 제출</span></li>
                <li><span class="list-date">2024-12-13 (금)</span> <span class="subject">결과물 평가 및 성적 발표</span></li>'),
(3,'mainpage_footer_1','현 사업은 정보의생명공학대학 학생회 정의에서 하반기 사업으로 진행하는 사업안입니다.
부산대학교 소프트웨어 융합 교육원과 정보의생명공학대학이 함께합니다.'),
(4,'mainpage_footer_address','경상남도 양산시 물금읍 부산대학로 49 경암공학관,
부산광역시 금정구 부산대학로63번길 2(장전동) 제6공학관(컴퓨터공학관)'),
(5,'mainpage_footer_email','pnuibe.justice1@gmail.com'),
(6, 'mainpage_footer_instagram','https://www.instagram.com/pnu_ibe/');

