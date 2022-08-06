package site.notion.dokuny.stock_dividend.exception.impl;

import org.springframework.http.HttpStatus;
import site.notion.dokuny.stock_dividend.exception.AbstractException;

public class NotMatchedPasswordException extends AbstractException {

	@Override
	public int getStatusCode() {
		return HttpStatus.BAD_REQUEST.value();
	}

	@Override
	public String getMessage() {
		return "비밀번호가 일치하지 않습니다.";
	}
}
