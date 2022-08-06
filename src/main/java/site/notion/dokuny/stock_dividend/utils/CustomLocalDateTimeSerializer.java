package site.notion.dokuny.stock_dividend.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomLocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

	private static final DateTimeFormatter FORMATTER =
		DateTimeFormatter.ISO_LOCAL_DATE_TIME;


	@Override
	public void serializeWithType(LocalDateTime value, JsonGenerator g, SerializerProvider provider,
		TypeSerializer typeSer) throws IOException {
		g.writeString(value.format(FORMATTER));
	}


	@Override
	public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
		throws IOException {
		gen.writeString(value.format(FORMATTER));
	}
}
