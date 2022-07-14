package com.tsp.new_tsp_admin.exception;

import lombok.Getter;

@Getter
public enum ApiExceptionType implements BaseExceptionType {
	// 로그인 관련 Type
	NO_LOGIN("NO_LOGIN", 401, "로그인 필요"),
	NO_ADMIN("NO_ADMIN", 403, "권한 없는 사용자"),

	// User 관련 Type
	ERROR_USER("ERROR_USER", 500, "유저 등록 에러"),
	ERROR_UPDATE_USER("ERROR_UPDATE_USER", 500, "유저 수정 에러"),
	ERROR_DELETE_USER("ERROR_DELETE_USER", 500, "유저 삭제 에러"),
	NOT_FOUND_USER("NOT_FOUND_USER", 200, "해당 유저 없음"),
	NOT_FOUND_USER_LIST("NOT_FOUND_USER_LIST", 200, "유저 리스트 없음"),

	// Model 관련 Type
	ERROR_MODEL("ERROR_MODEL", 500, "모델 등록 에러"),
	ERROR_UPDATE_MODEL("ERROR_UPDATE_MODEL", 500, "모델 수정 에러"),
	ERROR_DELETE_MODEL("ERROR_DELETE_MODEL", 500, "모델 삭제 에러"),
	NOT_FOUND_MODEL("NOT_FOUND_MODEL", 200, "해당 모델 없음"),
	NOT_FOUND_MODEL_LIST("NOT_FOUND_MODEL_LIST", 200, "모델 리스트 없음"),

	// Production 관련 Type
	ERROR_PRODUCTION("ERROR_PRODUCTION", 500, "프로덕션 등록 에러"),
	ERROR_UPDATE_PRODUCTION("ERROR_UPDATE_PRODUCTION", 500, "프로덕션 수정 에러"),
	ERROR_DELETE_PRODUCTION("ERROR_DELETE_PRODUCTION", 500, "프로덕션 삭제 에러"),
	NOT_FOUND_PRODUCTION("NOT_FOUND_PRODUCTION", 200, "해당 프로덕션 없음"),
	NOT_FOUND_PRODUCTION_LIST("NOT_FOUND_PRODUCTION_LIST", 200, "프로덕션 리스트 없음"),

	// Portfolio 관련 Type
	ERROR_PORTFOLIO("ERROR_PORTFOLIO", 500, "포트폴리오 등록 에러"),
	ERROR_UPDATE_PORTFOLIO("ERROR_UPDATE_PORTFOLIO", 500, "포트폴리오 수정 에러"),
	ERROR_DELETE_PORTFOLIO("ERROR_DELETE_PORTFOLIO", 500, "포트폴리오 삭제 에러"),
	NOT_FOUND_PORTFOLIO("NOT_FOUND_PORTFOLIO", 200, "해당 포트폴리오 없음"),
	NOT_FOUND_PORTFOLIO_LIST("NOT_FOUND_PORTFOLIO_LIST", 200, "포트폴리오 리스트 없음"),

	// Support 관련 Type
	ERROR_DELETE_SUPPORT("ERROR_DELETE_SUPPORT", 500,"지원모델 삭제 에러"),
	ERROR_SUPPORT("ERROR_SUPPORT", 500, "지원모델 등록 에러"),
	ERROR_UPDATE_SUPPORT("ERROR_UPDATE_SUPPORT", 500, "지원모델 수정 에러"),
	NOT_FOUND_SUPPORT("NOT_FOUND_SUPPORT", 200, "해당 지원서 없음"),
	NOT_FOUND_SUPPORT_LIST("NOT_FOUND_SUPPORT_LIST", 200, "지원서 리스트 없음"),

	// 서버 관련 TYPE
	RUNTIME_EXCEPTION("SERVER_ERROR", 500, "서버에러"),
	BAD_REQUEST("", 401, "권한에러"),
	NOT_NULL("NOT_NULL", 400, "필수값 누락"),
	ID_EXIST("ID_EXIST", 400, "같은 아이디 존재"),

	// 이미지 관련 TYPE
	ERROR_IMAGE("ERROR_IMAGE", 500, "이미지 등록 에러"),
	ERROR_UPDATE_IMAGE("ERROR_UPDATE_IMAGE", 500, "이미지 수정 에러"),
	ERROR_DELETE_IMAGE("ERROR_DELETE_IMAGE", 500, "이미지 삭제 에러"),

	// 공통 코드 관련 TYPE
	ERROR_COMMON("ERROR_COMMON", 500, "공통 코드 등록 에러"),
	NOT_FOUND_COMMON("NOT_FOUND_COMMON", 200, "해당 공통코드 없음"),
	NOT_FOUND_COMMON_LIST("NOT_FOUND_COMMON_LIST", 200, "공통 코드 리스트 없음");

	private final String errorCode;
	private final int httpStatus;
	private final String errorMessage;

	ApiExceptionType(String errorCode, int httpStatus, String errorMessage) {
		this.errorCode = errorCode;
		this.httpStatus = httpStatus;
		this.errorMessage = errorMessage;
	}

}
