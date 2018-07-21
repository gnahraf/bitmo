/*
 * Copyright 2018 Babak Farhang 
 */
package com.gnahraf.util.bitmo;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.TreeSet;

import org.junit.Test;

public class ReadWriteTest {

  @Test
  public void testElement0Depth1() throws IOException {
    doRoundtripTest(1, new int[] { 0 });
  }

  @Test
  public void testElement1Depth1() throws IOException {
    doRoundtripTest(1, new int[] { 1 });
  }

  @Test
  public void testFullDepth1() throws IOException {
    doRoundtripTest(1, new int[] { 0, 1 });
  }

  @Test
  public void testElement0Depth2() throws IOException {
    doRoundtripTest(2, new int[] { 0 });
  }

  @Test
  public void testElement1Depth2() throws IOException {
    doRoundtripTest(2, new int[] { 1 });
  }

  @Test
  public void testElement2Depth2() throws IOException {
    doRoundtripTest(2, new int[] { 2 });
  }

  @Test
  public void testElement3Depth2() throws IOException {
    doRoundtripTest(2, new int[] { 3 });
  }

  @Test
  public void testElements01Depth2() throws IOException {
    doRoundtripTest(2, new int[] { 0, 1 });
  }

  @Test
  public void testElements02Depth2() throws IOException {
    doRoundtripTest(2, new int[] { 0, 2 });
  }

  @Test
  public void testElements03Depth2() throws IOException {
    doRoundtripTest(2, new int[] { 0, 3 });
  }

  @Test
  public void testElements12Depth2() throws IOException {
    doRoundtripTest(2, new int[] { 1, 2 });
  }

  @Test
  public void testElements13Depth2() throws IOException {
    doRoundtripTest(2, new int[] { 1, 3 });
  }

  @Test
  public void testElements23Depth2() throws IOException {
    doRoundtripTest(2, new int[] { 2, 3 });
  }

  @Test
  public void testElements012Depth2() throws IOException {
    doRoundtripTest(2, new int[] { 0, 1, 2 });
  }

  @Test
  public void testElements013Depth2() throws IOException {
    doRoundtripTest(2, new int[] { 0, 1, 3 });
  }

  @Test
  public void testElements023Depth2() throws IOException {
    doRoundtripTest(2, new int[] { 0, 2, 3 });
  }

  @Test
  public void testElements123Depth2() throws IOException {
    doRoundtripTest(2, new int[] { 1, 2, 3 });
  }

  @Test
  public void testFullDepth2() throws IOException {
    doRoundtripTest(2, new int[] { 0, 1, 2, 3 });
  }

  @Test
  public void test01Depth8() throws IOException {
    doRoundtripTest(8, new int[] { 0, 1, 17, 49, 64, 127, 255 });
  }

  @Test
  public void test02Depth8() throws IOException {
    doRoundtripTest(8, new int[] { 1, 17, 49, 64, 127, 128, 129, 244, 255 });
  }

  @Test
  public void testDepth9() throws IOException {
    doRoundtripTest(9, new int[] { 1, 17, 49, 64, 127, 128, 129, 244, 255 });
  }

  @Test
  public void test02Depth9() throws IOException {
    doRoundtripTest(9, new int[] { 0, 1, 4, 5, 9, 10, 12, 17, 49, 64, 127, 128, 129, 244, 255, 256, 260, 261 });
  }
  
  @Test
  public void testDepth31With40() throws IOException {
    doRoundtripTest(31, generateRandom(40), Integer.MAX_VALUE);
  }
  
  @Test
  public void testDepth31With80() throws IOException {
    doRoundtripTest(31, generateRandom(80), Integer.MAX_VALUE);
  }
  
  @Test
  public void testDepth31With1024() throws IOException {
    doRoundtripTest(31, generateRandom(1024), Integer.MAX_VALUE);
  }
  
  @Test
  public void testDepth31With1024Sub1M() throws IOException {
    int maxValue = 1024 * 1024;
    doRoundtripTest(31, generateRandom(1024, maxValue), maxValue);
  }
  
  @Test
  public void testDepth20With2048Sub1M() throws IOException {
    int maxValue = 1024 * 1024;
    doRoundtripTest(20, generateRandom(2048, maxValue), maxValue);
  }
  
  @Test
  public void testDepth23With2048Sub8M() throws IOException {
    int maxValue = 8 * 1024 * 1024;
    int depth = 23;
    int sampleSize = 2 * 1024;
    doRoundtripTest(depth, generateRandom(sampleSize, maxValue), maxValue);
  }
  
  @Test
  public void testDepth23With4kSub8M() throws IOException {
    int maxValue = 8 * 1024 * 1024;
    int depth = 23;
    int sampleSize = 4 * 1024;
    doRoundtripTest(depth, generateRandom(sampleSize, maxValue), maxValue);
  }
  
  @Test
  public void testDepth23With16kSub8M() throws IOException {
    int maxValue = 8 * 1024 * 1024;
    int depth = 23;
    int sampleSize = 16 * 1024;
    doRoundtripTest(depth, generateRandom(sampleSize, maxValue), maxValue);
  }
  
  @Test
  public void testDepth23With64kSub8M() throws IOException {
    int maxValue = 8 * 1024 * 1024;
    int depth = 23;
    int sampleSize = 64 * 1024;
    doRoundtripTest(depth, generateRandom(sampleSize, maxValue), maxValue);
  }
  
  @Test
  public void testDepth31With64kSub8M() throws IOException {
    int maxValue = 8 * 1024 * 1024;
    int depth = 31;
    int sampleSize = 64 * 1024;
    doRoundtripTest(depth, generateRandom(sampleSize, maxValue), maxValue);
  }
  
  
  private TreeSet<Integer> generateRandom(int count) {
    return generateRandom(count, Integer.MAX_VALUE);
  }
  
  private TreeSet<Integer> generateRandom(int count, int maxValue) {
    TreeSet<Integer> numbers = new TreeSet<>();
    Random random = new Random(42);
    while (count > numbers.size()) {
      numbers.add(random.nextInt(maxValue));
    }
    return numbers;
  }
  

  private void doRoundtripTest(int depth, int[] values) throws IOException {
    doRoundtripTest(depth, new IntList(values), values[values.length - 1]);
  }
  
  private void doRoundtripTest(int depth, Collection<Integer> values, int maxValue) throws IOException {
    DecimalFormat numFormatter = new DecimalFormat("#,###.###");
    System.out.println();
    int count = values.size();
    System.out.println("D " + depth + ":  " + count + " #s / " + numFormatter.format(maxValue) + " .." + (count > 42 ? "" : " " + values));
    System.out.println("sample density: " + numFormatter.format(((double) count ) / maxValue));
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Writer writer = new Writer(depth, out);
    for (Integer i : values)
      writer.write(i);
    writer.finish();
    InputStream in = new ByteArrayInputStream(out.toByteArray());
    Reader reader = new Reader(depth, in);
    for (Integer i : values)
      assertEquals(i.intValue(), reader.readNext());
    assertEquals(-1, reader.readNext());
    System.out.println("Compressed byte length: " + out.size());
    System.out.println("Bytes per value: " + numFormatter.format(((double) out.size()) / count));
  }
}




class IntList extends AbstractList<Integer> {
  private final int[] array;
  IntList(int[] array) {
    this.array = array;
  }
  @Override
  public Integer get(int index) {
    return array[index];
  }
  @Override
  public int size() {
    return array.length;
  }
}
