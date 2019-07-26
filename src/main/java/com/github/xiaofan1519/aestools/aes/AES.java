package com.github.xiaofan1519.aestools.aes;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public abstract class AES {

  private static final String ALGORITHM = "AES";

  /**
   * 算法模式
   */
  private String algorithmModel;

  /**
   * 补码模式
   */
  private String pKCSModel;

  /**
   * 密钥
   */
  private byte[] key;

  /**
   * 偏移量, 不使用则为null
   */
  private IvParameterSpec iv;

  public AES(String algorithmModel, String pKCSModel, byte[] key, IvParameterSpec iv) {
    this.algorithmModel = algorithmModel;
    this.pKCSModel = pKCSModel;
    this.key = key;
    this.iv = iv;
  }

  /**
   * 初始化 Cipher
   *
   * @param opMode 模式 {@link Cipher#ENCRYPT_MODE}
   * @return Cipher
   * @throws GeneralSecurityException 初始化失败
   */
  private Cipher init(int opMode) throws GeneralSecurityException {
    SecretKeySpec key = new SecretKeySpec(this.key, ALGORITHM);
    Cipher cipher =
      Cipher.getInstance(
        ALGORITHM + "/" + algorithmModel + "/" + pKCSModel);

    if (null == this.iv) {
      cipher.init(opMode, key);
    } else {
      cipher.init(opMode, key, this.iv);
    }
    return cipher;
  }

  /**
   * 加密一段字节数组，失败返回null
   *
   * @param plainBytes 明文字节数组
   * @return 加密后的字节数组
   */
  private byte[] encrypt(byte[] plainBytes) throws GeneralSecurityException {
    Cipher cipher = init(Cipher.ENCRYPT_MODE);
    return cipher.doFinal(plainBytes);
  }

  /**
   * 解密一段字节数组，失败返回null
   *
   * @param cipherBytes 密文字节数组
   * @return 解密后的字节数组
   */
  private byte[] decrypt(byte[] cipherBytes) throws GeneralSecurityException {
    Cipher cipher = init(Cipher.DECRYPT_MODE);
    return cipher.doFinal(cipherBytes);
  }

  /**
   * 加密一段字符串，失败返回null
   *
   * @param plainText 明文字符串
   * @return 加密后的字符串
   */
  public String encrypt(String plainText) throws GeneralSecurityException {
    byte[] plainBytes = plainText.getBytes(StandardCharsets.UTF_8);
    byte[] cipherBytes = encrypt(plainBytes);
    if (null == cipherBytes) {
      return null;
    }
    return Base64.getEncoder().encodeToString(cipherBytes);
  }

  /**
   * 解密一段字符串，失败返回null
   *
   * @param cipherText 密文字符串
   * @return 解密后的字符串
   */
  public String decrypt(String cipherText) throws GeneralSecurityException {
    byte[] cipherBytes = Base64.getDecoder().decode(cipherText);
    byte[] plainBytes = decrypt(cipherBytes);
    if (null == plainBytes) {
      return null;
    }
    return new String(plainBytes, StandardCharsets.UTF_8);
  }
}
