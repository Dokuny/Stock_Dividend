package site.notion.dokuny.stock_dividend.exception.impl;

import org.springframework.http.HttpStatus;
import site.notion.dokuny.stock_dividend.exception.AbstractException;

public class NoCompanyException extends AbstractException {

	@Override
	public int getStatusCode() {
		return HttpStatus.BAD_REQUEST.value();
	}

	@Override
	public String getMessage() {
		return "존재하지 않는 회사명입니다.";
	}
}
