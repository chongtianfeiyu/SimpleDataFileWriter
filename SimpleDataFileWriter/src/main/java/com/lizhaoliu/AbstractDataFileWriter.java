package com.lizhaoliu;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

import org.apache.log4j.Logger;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.lizhaoliu.annotation.DataField;

/**
 * An abstract feed file writer service that provides utility to traverse and locate all fields that are annotated with
 * {@link DataField}
 */
public abstract class AbstractDataFileWriter implements DataFileWriter {

  private static final Logger logger = Logger.getLogger(AbstractDataFileWriter.class);

  @Override
  public void write(Iterable<?> pojos, Writer writer) throws IOException {
    Preconditions.checkNotNull(pojos);
    Preconditions.checkNotNull(writer);

    write(pojos.iterator(), writer);
  }

  /**
   * A helper method that creates a {@link List} which contains all fields annotated with {@link DataField} from
   * {@code obj} in proper {@link String} representations.
   * 
   * @param obj
   *          a DTO that carries data fields to be written out
   * @param fieldFormatter
   *          a {@link FieldComposer} to create {@link String} representation of a field
   * @return a {@link List} containing formatted fields in {@link String} representation
   */
  protected List<String> collectFileds(final Object obj, final FieldComposer fieldFormatter) {
    if (obj == null) {
      return Collections.emptyList();
    }
    Queue<FieldEntry> fieldsCollector = Queues.newPriorityQueue();
    for (Field field : obj.getClass().getDeclaredFields()) {
      processField(obj, field, fieldFormatter, fieldsCollector);
    }
    List<String> resultList = Lists.newArrayList();
    while (!fieldsCollector.isEmpty()) {
      resultList.add(fieldsCollector.poll().fieldValue);
    }
    return resultList;
  }

  /**
   * Process and traverse all fields of {@code field}. Note that this method will recursively traverse all fields of
   * {@code field} and its sub-fields until it sees a field annotated with {@link DataField} or there are no more
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
   * {@code field1}, {@code field2}, ... and all fields of {@code field1}, {@code field2}... will be traversed.
   * 
   * @param obj
   *          the object which contains {@code field}
   * @param field
   *          a {@link Field} object representing a field inside {@code obj}
   * @param fieldComposer
   *          a {@link FieldComposer} instance
   * @param fieldsCollector
   *          a {@link Collection} which contains fields
   */
  private void processField(final Object obj, final Field field, final FieldComposer fieldComposer,
      final Collection<FieldEntry> fieldsCollector) {
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
    DataField fieldAnnotation = field.getAnnotation(DataField.class);
    // if this field is annotated with DataField
    if (fieldAnnotation != null) {
      int fieldIndex = fieldAnnotation.position();
      // use empty string for null fields
      String formattedField = fieldComposer.composeField(field.getType(), fieldValue, fieldAnnotation);
      fieldsCollector.add(new FieldEntry(fieldIndex, formattedField));
      return;
    }
    // otherwise traverse all sub-fields of current field
    for (Field subField : field.getType().getDeclaredFields()) {
      processField(fieldValue, subField, fieldComposer, fieldsCollector);
    }
  }

  /**
   * A helper index-value pair class that can be sorted according to index
   */
  private static class FieldEntry implements Comparable<FieldEntry> {

    public final Integer fieldIndex;

    public final String fieldValue;

    public FieldEntry(Integer fieldIndex, String fieldValue) {
      this.fieldIndex = fieldIndex;
      this.fieldValue = fieldValue;
    }

    @Override
    public int compareTo(FieldEntry o) {
      // no need to do null check
      return fieldIndex - o.fieldIndex;
    }
  }

  /**
   * This utility interface provides a method {@link #composeField(Class, Object, DataField)} that takes a value of a
   * field in an object and returns a properly formatted {@link String} representation of that field for different file
   * format.
   */
  protected static interface FieldComposer {

    /**
     * Create a proper {@link String} representation of the field, {@link String} escaping and other issues should be
     * taken into consideration
     * 
     * @param fieldType
     *          specific type of the field
     * @param fieldValue
     *          value of the field
     * @param fieldAnnotation
     *          annotation of type {@link DataField}
     * @return A properly formatted field element, e.g. for JSON, this may be "{"name" : value}"; for CSV, this could be
     *         "value"
     */
    String composeField(Class<?> fieldType, Object fieldValue, DataField fieldAnnotation);
  }
}
