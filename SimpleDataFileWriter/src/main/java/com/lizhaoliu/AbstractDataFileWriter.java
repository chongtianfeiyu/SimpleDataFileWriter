package com.lizhaoliu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import com.lizhaoliu.annotation.DataField;

/**
 * An abstract feed file writer service that provides utility to traverse and
 * locate all fields that are annotated with {@link DataField}
 */
public abstract class AbstractDataFileWriter implements DataFileWriter {

  private static final Logger logger = Logger.getLogger(AbstractDataFileWriter.class);

  @Override
  public void writeToFile(Iterator<?> dtoIterator, File outputFile) throws IOException {
    try (Writer writer = new BufferedWriter(new FileWriter(outputFile))) {
      writeToWriter(dtoIterator, writer); // delegate the writing implementation
      writer.flush();
    }
  }

  /**
   * A helper method that creates a {@link Collection} which contains all fields
   * annotated with {@link DataField} from {@code dto} in proper {@link String}
   * representations.
   * 
   * @param obj
   *          a DTO that carries data fields to be written out
   * @param fieldFormatter
   *          a {@link FieldFormatter} to create {@link String} representation
   *          of a field
   * @return a {@link Collection} containing formatted fields in {@link String}
   *         representation
   */
  protected Collection<String> collectFileds(final Object obj, final FieldFormatter fieldFormatter) {
    if (obj == null) {
      return Collections.emptySet();
    }
    // Whether the fields are ordered or not, a TreeMultimap will do the right
    // thing
    Multimap<Integer, String> fieldsContainer = TreeMultimap.create();
    for (Field field : obj.getClass().getDeclaredFields()) {
      processField(obj, field, fieldFormatter, fieldsContainer);
    }
    return fieldsContainer.values();
  }

  /**
   * Process and traverse all fields of {@code field}. Note that this method
   * will recursively traverse all fields of {@code field} and its sub-fields
   * until it sees a field annotated with {@link DataField} or there are no more
   * sub-fields. E.g.
   * 
   * <pre>
   * {@code
   * class Dto {
   *  field1;
   *  field2;
   *  ...
   * }
   * </pre>
   * 
   * {@code field1}, {@code field2}, ... and all fields of {@code field1},
   * {@code field2}... will be traversed.
   * 
   * @param obj
   *          the object which contains {@code field}
   * @param field
   *          a {@link Field} object representing a field inside {@code obj}
   * @param fieldFormatter
   *          a {@link FieldFormatter} instance
   * @param fieldsContainer
   *          a {@link Multimap} instance to collect formatted fields
   */
  private void processField(final Object obj, final Field field, final FieldFormatter fieldFormatter,
      final Multimap<Integer, String> fieldsContainer) {
    // skip if the field value is null
    if (obj == null) {
      return;
    }
    field.setAccessible(true); // so that private fields can be accessed
    Object fieldValue = null;
    try {
      fieldValue = field.get(obj);
    } catch (IllegalArgumentException | IllegalAccessException e) {
      logger.warn("An error occured while processing field: " + field.getName(), e);
    }
    DataField feedFieldAnnotation = field.getAnnotation(DataField.class);
    // if this field is annotated with FeedField
    if (feedFieldAnnotation != null) {
      int fieldIndex = feedFieldAnnotation.position();
      // use empty string for null fields
      String formatted = fieldValue == null ? StringUtils.EMPTY : fieldFormatter.formatField(field.getType(),
          fieldValue, feedFieldAnnotation);
      fieldsContainer.put(fieldIndex, formatted);
      return;
    }
    // otherwise traverse all sub-fields of current field
    for (Field subField : field.getType().getDeclaredFields()) {
      processField(fieldValue, subField, fieldFormatter, fieldsContainer);
    }
  }

  /**
   * Iterate every DTO using {@code dtoIterator} and writes them to
   * {@code writer}
   * 
   * @param dtoIterator
   *          an {@link Iterator} of generic type objects
   * @param writer
   *          an {@link Writer} to write to
   * @throws IOException
   *           if an I/O error occurs while writing to {@link Writer}
   */
  protected abstract void writeToWriter(Iterator<?> dtoIterator, Writer writer) throws IOException;

  /**
   * This utility interface provides a method
   * {@link #formatField(Class, Object, DataField)} that takes a value of a
   * field in an object and returns a properly formatted {@link String}
   * representation of that field for different file format.
   */
  protected static interface FieldFormatter {

    /**
     * Create a properly formatted {@link String} representation of the field
     * 
     * @param fieldType
     *          specific type of the field
     * @param fieldValue
     *          value of the field
     * @param fieldAnnotation
     *          annotation of type {@link DataField}
     * @return A properly formatted field element, e.g. for JSON, this may be
     *         "{"name" : value}"; for CSV, this could be "value"
     */
    String formatField(Class<?> fieldType, Object fieldValue, DataField fieldAnnotation);
  }
}
