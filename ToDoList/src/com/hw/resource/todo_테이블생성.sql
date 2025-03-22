-- DROP SEQUENCE IF EXISTS SEQ_MEMBER_CODE
BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE SEQ_MEMBER_CODE';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -2289 THEN
            RAISE;
        END IF;
END;

-- DROP SEQUENCE IF EXISTS SEQ_TODO_NO
BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE SEQ_TODO_NO';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -2289 THEN
            RAISE;
        END IF;
END;

-- DROP TABLE IF EXISTS TB_TODO
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE TB_TODO';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -942 THEN
            RAISE;
        END IF;
END;

-- DROP TABLE IF EXISTS TB_MEMBER
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE TB_MEMBER';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -942 THEN
            RAISE;
        END IF;
END;

-- TB_MEMBER.MEMBER_CODE를 위한 SEQUENCE CREATE
CREATE SEQUENCE SEQ_MEMBER_CODE
START WITH 1
INCREMENT BY 1
NOCYCLE
NOCACHE;

-- TB_TODO.TODO_NO을 위한 SEQUENCE CREATE
CREATE SEQUENCE SEQ_TODO_NO
START WITH 1
INCREMENT BY 1
NOCYCLE
NOCACHE;

-- 회원 정보 관리 테이블
CREATE TABLE TB_MEMBER (
	MEMBER_CODE NUMBER 				CONSTRAINT MEM_CODE_PK PRIMARY KEY,
	MEMBER_ID 	NVARCHAR2(20) CONSTRAINT MEM_ID_NN NOT NULL,
	MEMBER_PW 	NVARCHAR2(20) CONSTRAINT MEM_PW_NN NOT NULL,
	NICKNAME 		NVARCHAR2(10) CONSTRAINT NICK_UNQ UNIQUE,
	CONSTRAINT MEM_ID_UNQ UNIQUE(MEMBER_ID),
	CONSTRAINT MEM_PW_CK CHECK(LENGTH(MEMBER_PW) > 4)
);

COMMENT ON COLUMN TB_MEMBER.MEMBER_CODE IS '회원식별코드';
COMMENT ON COLUMN TB_MEMBER.MEMBER_ID 	IS '회원아이디';
COMMENT ON COLUMN TB_MEMBER.MEMBER_PW 	IS '회원비밀번호';
COMMENT ON COLUMN TB_MEMBER.NICKNAME 		IS '닉네임';

-- Todo 관리 테이블
CREATE TABLE TB_TODO (
	MEMBER_CODE		NUMBER CONSTRAINT MEM_CODE_FK REFERENCES TB_MEMBER ON DELETE CASCADE,
	TODO_NO 			NUMBER CONSTRAINT TODO_NO_PK PRIMARY KEY,
	TODO_TITLE 		NVARCHAR2(20),
	TODO_CONTENT	CLOB,
	COMPLETE_YN 	CHAR(1) DEFAULT 'N' CONSTRAINT COMP_YN_NN NOT NULL,
	CREATED_DATE 	DATE DEFAULT CURRENT_DATE CONSTRAINT C_DATE_NN NOT NULL,
	UPDATED_DATE 	DATE DEFAULT CURRENT_DATE CONSTRAINT U_DATE_NN NOT NULL,
	CONSTRAINT COMP_YN_CK CHECK(COMPLETE_YN = 'Y' OR COMPLETE_YN = 'N')
);

COMMENT ON COLUMN TB_TODO.MEMBER_CODE 	IS '회원식별코드';
COMMENT ON COLUMN TB_TODO.TODO_NO 			IS 'todo번호';
COMMENT ON COLUMN TB_TODO.TODO_TITLE 		IS 'todo제목';
COMMENT ON COLUMN TB_TODO.TODO_CONTENT 	IS 'todo내용';
COMMENT ON COLUMN TB_TODO.COMPLETE_YN 	IS '완료여부';
COMMENT ON COLUMN TB_TODO.CREATED_DATE 	IS '생성시간';
COMMENT ON COLUMN TB_TODO.UPDATED_DATE 	IS '수정시간';