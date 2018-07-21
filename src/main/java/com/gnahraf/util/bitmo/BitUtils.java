/*
 * Copyright 2018 Babak Farhang 
 */
package com.gnahraf.util.bitmo;

class BitUtils {
  public static boolean getBit(int target, int pos) {

    return (target & (1 << pos)) != 0;
  }

  public static int setBit(int target, int pos, boolean value) {
    final int mask = 1 << pos;
    if (value)
      target |= mask;
    else
      target &= ~mask;
    return target;
  }

  public static boolean getBit(byte target, int pos) {

    return (target & (1 << pos)) != 0;
  }

  public static byte setBit(byte target, int pos, boolean value) {
    final int mask = 1 << pos;
    if (value)
      target |= mask;
    else
      target &= ~mask;
    return target;
  }

}
