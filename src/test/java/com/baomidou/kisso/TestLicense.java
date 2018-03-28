package com.baomidou.kisso;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.util.Properties;

import com.baomidou.kisso.common.util.IoUtil;
import com.baomidou.kisso.common.util.OrderedProperties;
import com.baomidou.kisso.exception.LicenseException;
import com.baomidou.kisso.security.license.License;
import com.baomidou.kisso.security.license.LicenseGenerator;
import com.baomidou.kisso.security.license.LicenseManager;

/**
 * <p>
 * License 测试
 * </p>
 *
 * @author hubin
 * @since 2018-03-28
 */
public class TestLicense {

  public static final String TEMPLATE_FILE = "/Users/hubin/IdeaProjects/kisso/src/main/resources/template.dat";
  public static final String PRIVATE_KEY_FILE = "license.key";

  public static void main(String[] args) {
    try {
      new TestLicense().createLicenseInfo();
      TestLicense.licenseVerifier();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public static void licenseVerifier() {
    LicenseManager licenseManager = LicenseManager.getInstance();
    try {
      License license = licenseManager.getLicense();
      System.out.println("license = " + license);
      boolean valid = licenseManager.isValidLicense(license);
      System.out.println("valid = " + valid);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void generateLicense(Properties features, OutputStream output, String privateKeyFile) throws LicenseException {
    LicenseGenerator.generateLicense(features, output, privateKeyFile);
  }

  public void createLicenseInfo() throws Exception {
    if (!existsKeyFiles()) {
      generateKeys();
    }

    Properties properties = new OrderedProperties();
    properties.setProperty("a", "1");
    properties.setProperty("c", "2");
    properties.setProperty("expirationDate", "9/12/2020");

    LicenseGenerator.generateLicense(properties, "license.key");

    System.out.println("License generated in '" + LicenseManager.LICENSE_FILE + "' file.");
  }

  public void createLicense() throws Exception {
    if (!existsKeyFiles()) {
      generateKeys();
    }

    Properties properties = new OrderedProperties();
    properties.load(new FileInputStream(TEMPLATE_FILE));
    LicenseGenerator.generateLicense(properties, "license.key");

    System.out.println("License generated in '" + LicenseManager.LICENSE_FILE + "' file.");
  }

  private boolean existsKeyFiles() {
    return existsPrivateKeyFile() && existsPublicKeyFile();
  }

  private boolean existsPrivateKeyFile() {
    File privateKeyFile = new File(PRIVATE_KEY_FILE);
    boolean exists = privateKeyFile.exists() && privateKeyFile.isFile();

    return exists;
  }

  private boolean existsPublicKeyFile() {
    File publicKeyFile = new File(LicenseManager.PUBLIC_KEY_FILE);
    boolean exists = publicKeyFile.exists() && publicKeyFile.isFile();

    return exists;
  }

  private void generateKeys() throws LicenseException {
    createKeys(LicenseManager.PUBLIC_KEY_FILE, PRIVATE_KEY_FILE);
  }

  public static void createKeys(String publicUri, String privateUri) throws LicenseException {
    try {
      KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
      keyGen.initialize(1024, new SecureRandom());
      KeyPair keyPair = keyGen.generateKeyPair();

      IoUtil.writeFile(publicUri, keyPair.getPublic().getEncoded());
      IoUtil.writeFile(privateUri, keyPair.getPrivate().getEncoded());
    } catch (Exception e) {
      throw new LicenseException(e);
    }
  }
}
