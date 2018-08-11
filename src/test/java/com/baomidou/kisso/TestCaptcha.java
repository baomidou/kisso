/*
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
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
package com.baomidou.kisso;

import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;

import org.junit.Test;

import com.baomidou.kisso.captcha.ImageCaptcha;
import com.baomidou.kisso.common.util.RandomType;

/**
 * <p>
 * 测试图片验证码
 * </p>
 *
 * @author hubin
 * @since 2018-08-11
 */
public class TestCaptcha {

    @Test
    public void test() throws Exception {
        String code = new ImageCaptcha()
                .out(new FileOutputStream(new File("abc.png")));
        System.out.println("验证码：" + code);
    }

    @Test
    public void testGIf() throws Exception {
        String code = new ImageCaptcha()
                .setGif(true)
                .setRandomType(RandomType.MIX)
                .out(new FileOutputStream(new File("abc.gif")));
        System.out.println("验证码：" + code);
    }

    @Test
    public void testHan() throws Exception {
        String code = new ImageCaptcha()
                .setRandomType(RandomType.CHINESE)
                .setFont(new Font("微软雅黑", Font.PLAIN, 30))
                .out(new FileOutputStream(new File("han.png")));
        System.out.println("验证码：" + code);
    }

    @Test
    public void testGifHan() throws Exception {
        String code = new ImageCaptcha().setGif(false)
                .setGif(true)
                .setRandomType(RandomType.CHINESE)
                .setFont(new Font("微软雅黑", Font.PLAIN, 30))
                .out(new FileOutputStream(new File("han.gif")));
        System.out.println("验证码：" + code);
    }

}
