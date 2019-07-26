package com.github.xiaofan1519.aestools.aes.impl;

import com.github.xiaofan1519.aestools.aes.AES;
import javax.crypto.spec.IvParameterSpec;

public class ECB extends AES {

  public ECB(String pKCSModel, byte[] key) {
    super("ECB", pKCSModel, key, null);
  }
}
