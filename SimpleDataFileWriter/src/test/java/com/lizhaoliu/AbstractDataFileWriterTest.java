package com.lizhaoliu;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.lizhaoliu.CsvDataFileWriter;
import com.lizhaoliu.DtoOne;
import com.lizhaoliu.DtoTotal;
import com.lizhaoliu.DtoTwo;
import com.lizhaoliu.JsonDataFileWriter;
import com.lizhaoliu.XmlDataFileWriter;

public class AbstractDataFileWriterTest {

  @Test
  public void testWriteToFile() throws IOException {
    List<DtoTotal> data = Lists.newArrayList();
    int n = 10;
    int m = 5;
    for (int i = 0; i < n; ++i) {
      data.add(new DtoTotal(new DtoOne("val11-" + RandomStringUtils.randomAlphabetic(m), "val12-"
          + RandomStringUtils.randomAlphabetic(m)), new DtoTwo("val21-" + RandomStringUtils.randomAlphabetic(m),
          "val22-" + RandomStringUtils.randomAlphabetic(m))));
    }

    long time = 0;
    time = System.nanoTime();
    new CsvDataFileWriter().writeToFile(data.iterator(), new File("output.csv"));
    new JsonDataFileWriter().writeToFile(data.iterator(), new File("output.json"));
    new XmlDataFileWriter().writeToFile(data.iterator(), new File("output.xml"));
    time = System.nanoTime() - time;
    System.out.println("Time: " + time * 1e-9 + " s.");
  }
}
