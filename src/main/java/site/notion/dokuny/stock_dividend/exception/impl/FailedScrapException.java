package site.notion.dokuny.stock_dividend.exception.impl;

import org.springframework.http.HttpStatus;
import site.notion.dokuny.stock_dividend.exception.AbstractException;

public class FailedScrapException extends AbstractException {

	@Override
	public int getStatusCode() {
		return HttpStatus.INTERNAL_SERVER_ERROR.value();
	}

	@Override
	public String getMessage() {
		return "스크랩에 실패하였습니다.";
	}
}
