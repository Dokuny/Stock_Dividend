package site.notion.dokuny.stock_dividend.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractException extends RuntimeException{

	abstract public int getStatusCode();

	abstract public String getMessage();
}
