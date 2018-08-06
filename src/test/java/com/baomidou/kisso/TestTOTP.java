package com.baomidou.kisso;

import java.io.IOException;
import java.util.Arrays;

import com.baomidou.kisso.common.encrypt.TOTP;

/**
 * TOTP 测试
 */
public class TestTOTP {

  /**
   * <p>
   * 在线生成 CODE<br>
   * http://gauth.apps.gbraad.nl/#main
   * </p>
   */
  public static void main(String[] args) throws Exception {

    String secretKey = TOTP.getSecretKey();

    System.out.print("Secret Key for Google Authenticator: ");
    System.out.println(TOTP.getFormatedKey(secretKey));
    System.out.println("Opt auth url: " + TOTP.getOtpAuthUrl("jobob@qq.com", "kisso", secretKey));
    System.out.println("Secret Key: " + secretKey + "\n");

    do {
      System.out.print("Ingrese codigo TOTP:  ");
      byte[] value = new byte[10];
      try {
        System.in.read(value);
      } catch (IOException e) {
        e.printStackTrace();
      }

      value = Arrays.copyOf(value, TOTP.PASS_CODE_LENGTH);
      String strValue = (new String(value)).trim();

      Long longValue = Long.parseLong(strValue);

      boolean result = TOTP.isValidCode(secretKey, longValue);
      System.out.println("Result-> " + result);
    } while (true);
  }
}
