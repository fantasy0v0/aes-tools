package aestools.aes.impl;

import aestools.aes.AES;

public class ECB extends AES {

  public ECB(String pKCSModel, byte[] key) {
    super("ECB", pKCSModel, key, null);
  }
}
