package com.lizhaoliu;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Service that provides utility to write POJOs to an output file
 */
public interface DataFileWriter {

  /**
   * Write all POJOs to {@code outputFile}
   * 
   * @param pojoIterator
   *          an {@link Iterator} sliding over all POJOs
   * @param outputFile
   *          output file to write to
   * @throws IOException
   *           if an I/O error occurs while writing to feed file
   */
  void writeToFile(Iterator<?> pojoIterator, File outputFile) throws IOException;
}
