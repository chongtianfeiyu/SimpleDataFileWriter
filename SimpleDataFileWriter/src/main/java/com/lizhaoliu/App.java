package com.lizhaoliu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * Entry point
 */
public class App {

  private static final Logger logger = Logger.getLogger(App.class);

  /**
   * THIS METHOD REMAINS UNMODIFIED UNLESS NECESSARY
   * 
   * @param args
   */
  public static void main(String[] args) {
    logger.info("--APPLICATION STARTED--");
    new App().runApp(args);
  }

  /**
   * Start running the application
   * 
   * @param args
   */
  public void runApp(String[] args) {
    List<DtoTotal> data = Lists.newArrayList();
    int numRecords = 100000;
    int strLen = 5;
    for (int i = 0; i < numRecords; ++i) {
      data.add(new DtoTotal(new DtoOne("val11-" + RandomStringUtils.randomAlphabetic(strLen), "val12-"
          + RandomStringUtils.randomAlphabetic(strLen)), new DtoTwo("val21-"
          + RandomStringUtils.randomAlphabetic(strLen), "val22-" + RandomStringUtils.randomAlphabetic(strLen))));
    }
    data = ImmutableList.copyOf(data);

    ExecutorService exec = Executors.newCachedThreadPool();
    exec.submit(new BenchmarkTask(data.iterator(), new CsvDataFileWriter(), new File("output.csv")));
    exec.submit(new BenchmarkTask(data.iterator(), new JsonDataFileWriter(), new File("output.json")));
    exec.submit(new BenchmarkTask(data.iterator(), new XmlDataFileWriter(), new File("output.xml")));
    exec.shutdown();
  }

  static class BenchmarkTask implements Runnable {

    private final Iterator<?> dataItr;
    private final DataFileWriter writer;
    private final File outputFile;

    public BenchmarkTask(Iterator<?> dataItr, DataFileWriter writer, File outputFile) {
      this.dataItr = dataItr;
      this.writer = writer;
      this.outputFile = outputFile;
    }

    @Override
    public void run() {
      long time = 0;
      time = System.nanoTime();
      try (Writer fileWriter = new BufferedWriter(new FileWriter(outputFile))){
        writer.write(dataItr, fileWriter);
        fileWriter.flush();
      } catch (Exception e) {
        logger.error("An error occured: ", e);
      }
      time = System.nanoTime() - time;
      System.out.println(String.format("\"%s\" size = %.2f mb, time lapsed = %.2f s.", outputFile,
          outputFile.length() * 1e-6, time * 1e-9));
    }
  }
}
