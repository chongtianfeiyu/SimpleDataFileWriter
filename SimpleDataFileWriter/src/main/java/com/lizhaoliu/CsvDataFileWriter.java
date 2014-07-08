package com.lizhaoliu;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.lizhaoliu.annotation.DataField;

/**
 * A sample CSV file writer, it writes header and rows with comma as separator
 */
public class CsvDataFileWriter extends AbstractDataFileWriter {

  private static final String FIELD_SPARATOR = ",";

  @Override
  public void write(final Iterator<?> dtoIterator, final Writer writer) throws IOException {
    boolean headerWritten = false;
    while (dtoIterator.hasNext()) {
      Object dto = dtoIterator.next();
      if (!headerWritten) {
        Collection<String> headerFields = collectFileds(dto, new FieldComposer() {
          @Override
          public String composeField(Class<?> fieldType, Object fieldValue, DataField fieldAnnotation) {
            return fieldAnnotation.name();
          }
        });
        String headers = StringUtils.join(headerFields, FIELD_SPARATOR) + System.lineSeparator();
        writer.write(headers);
        headerWritten = true;
      }
      Collection<String> rowFields = collectFileds(dto, new FieldComposer() {
        @Override
        public String composeField(Class<?> fieldType, Object fieldValue, DataField fieldAnnotation) {
          return ObjectUtils.toString(fieldValue);
        }
      });
      String row = StringUtils.join(rowFields, FIELD_SPARATOR) + System.lineSeparator();
      writer.write(row);
    }
  }
}
