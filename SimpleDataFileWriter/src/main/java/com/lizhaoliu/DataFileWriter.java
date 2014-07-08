package com.lizhaoliu;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

/**
 * Service that provides utility to write POJOs to an output file
 */
public interface DataFileWriter {

  /**
   * Write all POJOs to {@code writer}
   * 
   * @param pojoIterator
   *          an {@link Iterator} sliding over all POJOs
   * @param writer
   *          a {@link Writer} to write to
   * @throws IOException
   *           if an I/O error occurs while writing
   */
  void write(Iterator<?> pojoIterator, Writer writer) throws IOException;

  /**
   * Write all POJOs to {@code writer}
   * 
   * @param pojos
   *          a collection of POJOs
   * @param writer
   *          a {@link Writer} to write to
   * @throws IOException
   *           if an I/O error occurs while writing
   */
  void write(Iterable<?> pojos, Writer writer) throws IOException;
}
