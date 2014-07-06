package com.lizhaoliu;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import com.lizhaoliu.annotation.DataField;

/**
 * A sample CSV file writer, it writes header and rows with comma as separator
 */
public class CsvDataFileWriter extends AbstractDataFileWriter {

  private static final String FIELD_SPARATOR = ", ";

  @Override
  protected void writeToWriter(final Iterator<?> dtoIterator, final Writer writer) throws IOException {
    boolean headerWritten = false;
    while (dtoIterator.hasNext()) {
      Object dto = dtoIterator.next();
      if (!headerWritten) {
        Collection<String> headerFields = collectFileds(dto, new FieldFormatter() {
          @Override
          public String formatField(Class<?> fieldType, Object fieldValue, DataField fieldAnnotation) {
            return fieldAnnotation.name();
          }
        });
        String headers = StringUtils.join(headerFields, FIELD_SPARATOR) + System.lineSeparator();
        writer.write(headers);
        headerWritten = true;
      }
      Collection<String> rowFields = collectFileds(dto, new FieldFormatter() {
        @Override
        public String formatField(Class<?> fieldType, Object fieldValue, DataField fieldAnnotation) {
          return fieldValue.toString();
        }
      });
      String row = StringUtils.join(rowFields, FIELD_SPARATOR) + System.lineSeparator();
      writer.write(row);
    }
  }
}
