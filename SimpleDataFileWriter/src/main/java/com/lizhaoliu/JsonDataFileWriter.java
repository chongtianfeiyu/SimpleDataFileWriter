package com.lizhaoliu;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import com.lizhaoliu.annotation.DataField;

/**
 * A sample implementation of JSON data file writer, it cannot handle array type
 */
public class JsonDataFileWriter extends AbstractDataFileWriter {

  @Override
  protected void writeToWriter(Iterator<?> dtoIterator, Writer writer) throws IOException {
    while (dtoIterator.hasNext()) {
      Object obj = dtoIterator.next();
      Collection<String> fields = collectFileds(obj, new FieldFormatter() {
        @Override
        public String formatField(Class<?> fieldType, Object fieldValue, DataField fieldAnnotation) {
          return String.format("{\"%s\" : \"%s\"}", fieldAnnotation.name(), fieldValue);
        }
      });
      writer.write(StringUtils.join(fields, ", "));
    }
  }
}
