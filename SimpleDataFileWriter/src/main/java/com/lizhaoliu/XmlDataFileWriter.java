package com.lizhaoliu;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import com.lizhaoliu.annotation.DataField;

/**
 * A sample implementation of a XML file writer, i.e. no templates, no formatting...
 */
public class XmlDataFileWriter extends AbstractDataFileWriter {

  private static final String ELEMENT_FORMAT = "<%s>%s</%s>";

  @Override
  public void write(Iterator<?> dtoIterator, Writer writer) throws IOException {
    while (dtoIterator.hasNext()) {
      Object obj = dtoIterator.next();
      Collection<String> fields = collectFileds(obj, new FieldComposer() {
        @Override
        public String composeField(Class<?> fieldType, Object fieldValue, DataField fieldAnnotation) {
          return String.format(ELEMENT_FORMAT, fieldAnnotation.name(), fieldValue, fieldAnnotation.name());
        }
      });
      String record = StringUtils.join(fields, StringUtils.EMPTY);
      String objName = obj.getClass().getSimpleName();
      String output = String.format(ELEMENT_FORMAT, objName, record, objName);
      writer.write(output);
    }
  }

}
