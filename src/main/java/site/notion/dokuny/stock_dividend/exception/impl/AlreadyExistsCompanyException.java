package site.notion.dokuny.stock_dividend.exception.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import site.notion.dokuny.stock_dividend.exception.AbstractException;

@Slf4j
public class AlreadyExistsCompanyException extends AbstractException {
	@Override
	public int getStatusCode() {
		return HttpStatus.BAD_REQUEST.value();
	}

	@Override
	public String getMessage() {
		return "이미 저장된 회사 입니다.";
	}
}
