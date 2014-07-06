package com.lizhaoliu;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Service that provides utility to write a DTO data to an output file
 */
public interface DataFileWriter {

  /**
   * Write all DTO objects to {@code feedFile}
   * 
   * @param dtoIterator
   *          an {@link Iterator} of DTO objects
   * @param outputFile
   *          output file to write to
   * @throws IOException
   *           if an I/O error occurs while writing to feed file
   */
  void writeToFile(Iterator<?> dtoIterator, File outputFile) throws IOException;
}
