/*
 * Copyright (c) 2017-2022, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.kisso.common;

import static com.baomidou.kisso.common.OperatingSystem.OS.MAC;
import static com.baomidou.kisso.common.OperatingSystem.OS.UNIX;
import static com.baomidou.kisso.common.OperatingSystem.OS.WINDOWS;

/**
 * <p>
 * 操作系统判断
 * </p>
 *
 * @author hubin
 * @since 2018-09-15
 */
public class OperatingSystem {

  public enum OS {
    WINDOWS,
    MAC,
    UNIX
  }

  private static final OS OS = get(System.getProperty("os.name", "").toLowerCase());

  public static OS get() {
    return OS;
  }

  private static OS get(String osName) {
    if (isWindows(osName)) {
      return WINDOWS;
    } else if (isMac(osName)) {
      return MAC;
    } else if (isUnix(osName)) {
      return UNIX;
    }
    return null;
  }

  public static boolean isWindows(String osName) {
    return (osName.contains("win"));
  }

  public static boolean isMac(String osName) {
    return (osName.contains("mac"));
  }

  public static boolean isUnix(String osName) {
    return (osName.contains("nix") || osName.contains("nux") || osName.contains("aix"));
  }
}
