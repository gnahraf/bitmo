/*
 * Copyright 2018 Babak Farhang 
 */
package com.gnahraf.util.bitmo;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BitOutputStream extends FilterOutputStream {
  private byte b;
  private int bi = 8;

  public BitOutputStream(OutputStream out) {
    super(out);
  }

  public void writeBit(boolean value) throws IOException {
    b = BitUtils.setBit(b, --bi, value);
    if (bi == 0) {
      write(b);
      b = 0;
      bi = 8;
    }
  }

  public void flush() throws IOException {
    flushBits();
    super.flush();
  }

  public void flushBits() throws IOException {
    if (bi != 8)
      write(b);
  }

}
