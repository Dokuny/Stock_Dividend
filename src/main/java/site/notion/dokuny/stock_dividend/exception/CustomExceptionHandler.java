package site.notion.dokuny.stock_dividend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import site.notion.dokuny.stock_dividend.exception.AbstractException;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {


	@ExceptionHandler(AbstractException.class)
	protected ResponseEntity<ErrorResponse> handleCustomException(AbstractException e) {

		ErrorResponse response = ErrorResponse.builder()
			.code(e.getStatusCode())
			.message(e.getMessage())
			.build();

		return ResponseEntity.status(response.getCode()).body(response);
	}
}
