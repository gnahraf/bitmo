/*
 * Copyright 2018 Babak Farhang 
 */
package com.gnahraf.util.bitmo;

import java.io.IOException;
import java.io.InputStream;

public class Reader {
  private final TraverseState state;
  private final BitInputStream in;

  private int lastValue;

  public Reader(int maxDepth, InputStream in) {
    this.state = new TraverseState(maxDepth);
    this.in = new BitInputStream(in);
  }

  public int readNext() throws IOException {
    if (state.isDone())
      return -1;

    while (!state.pushBit(in.readBit()))
      ;

    // We could have stopped either because
    // 1. we got a new completed value, or
    // 2. there are no more values in the container, or
    // 3. both (1) and (2)
    final int completedValue = state.getCompletedValue();
    if (state.isDone() && lastValue == completedValue)
      return -1;
    lastValue = completedValue;
    return completedValue;
  }
}
