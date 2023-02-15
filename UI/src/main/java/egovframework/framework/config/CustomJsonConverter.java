package egovframework.framework.config;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public class CustomJsonConverter extends MappingJackson2HttpMessageConverter {
	public CustomJsonConverter() {
//		ObjectMapper objectMapper = new ObjectMapper();
//		objectMapper.getFactory().setCharacterEscapes(new HTMLCharacterUnEscapes());
////		objectMapper.registerModule(new JavaTimeModule());
//		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//		super.setObjectMapper(objectMapper);
	}

	@Override
	public void setSupportedMediaTypes(List<MediaType> supportedMediaTypes) {
		// TODO Auto-generated method stub
		super.setSupportedMediaTypes(supportedMediaTypes);
	}

	@Override
	protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		// 추후에 http api 개발시에 원문 그대로 보내주기 위해서는 escape된 문자를 unescape 하여 보내줄 필요성은 있어보인다.
		// 해당 메소드에서 object를 unescape 시켜서 json 통신을 하면 잘 될것으로 예상함.
//		System.out.println(object.toString());

		super.writeInternal(object, type, outputMessage);
	}
}
