INSERT INTO USER (id, user_id, password, name, email) VALUES (1, 'movingline', '123456', '동선', 'movinglinecheck@gmail.com');
INSERT INTO USER (id, user_id, password, name, email) VALUES (2, 'zingoworks', '123456', '진산', 'zingworks@gmail.com');

INSERT INTO QUESTION (id, writer_id, title, contents, create_date, deleted) VALUES (1, '2', '징고 첫 질문(첫글)', '징고 첫 질문 내용', CURRENT_TIMESTAMP(), 0);
INSERT INTO QUESTION (id, writer_id, title, contents, create_date, deleted) VALUES (2, '2', '징고 두번째 질문', '징고 두 질문 내용', CURRENT_TIMESTAMP(), 0);
INSERT INTO QUESTION (id, writer_id, title, contents, create_date, deleted) VALUES (3, '1', '무빙이 첫 질문', '무빙이 첫 질문 내용', CURRENT_TIMESTAMP(), 0);
INSERT INTO QUESTION (id, writer_id, title, contents, create_date, deleted) VALUES (4, '1', '무빙이 두번째 질문(최신 글)', '무빙이 두번째 질문 내용', CURRENT_TIMESTAMP(), 0);

INSERT INTO ANSWER (id, writer_id, question_id, contents, create_date, deleted) VALUES (1, 1, 1, '징고 첫 질문의 무빙이 답변', current_timestamp, 0);
INSERT INTO ANSWER (id, writer_id, question_id, contents, create_date, deleted) VALUES (2, 2, 1, '징고 첫 질문의 징고 스스로 답변', current_timestamp, 0);
INSERT INTO ANSWER (id, writer_id, question_id, contents, create_date, deleted) VALUES (3, 1, 2, '징고 두번째 질문의 무빙이 답변', current_timestamp, 0);
INSERT INTO ANSWER (id, writer_id, question_id, contents, create_date, deleted) VALUES (4, 2, 3, '무빙이 첫 질문의 징고 답변', current_timestamp, 0);
