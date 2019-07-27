package aestools.aes.impl;

import aestools.aes.AES;
import javax.crypto.spec.IvParameterSpec;

public class CBC extends AES {

  public CBC(String pKCSModel, byte[] key, byte[] iv) {
    super("CBC", pKCSModel, key, new IvParameterSpec(iv));
  }
}
