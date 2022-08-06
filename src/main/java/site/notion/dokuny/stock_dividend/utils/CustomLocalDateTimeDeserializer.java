package site.notion.dokuny.stock_dividend.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

	@Override
	public Object deserializeWithType(JsonParser p, DeserializationContext ctxt,
		TypeDeserializer typeDeserializer) throws IOException, JacksonException {

		return LocalDateTime.parse(p.getText(),FORMATTER);
	}


	@Override
	public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt)
		throws IOException, JacksonException {

		return LocalDateTime.parse(p.getText(),FORMATTER);
	}
}
