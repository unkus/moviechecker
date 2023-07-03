package moviechecker.database.converters;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class UriPersistanceConverter implements AttributeConverter<URI, String> {

	private Logger logger = LoggerFactory.getLogger(UriPersistanceConverter.class);

	@Override
	public String convertToDatabaseColumn(URI attribute) {
		if (logger.isDebugEnabled()) {
			logger.debug("Converting URI (" + attribute + ") to String.");
		}
		return attribute.toString();
	}

	@Override
	public URI convertToEntityAttribute(String dbData) {
		if (logger.isDebugEnabled()) {
			logger.debug("Converting String (" + dbData + ") to URI.");
		}
		return URI.create(dbData);
	}

}
