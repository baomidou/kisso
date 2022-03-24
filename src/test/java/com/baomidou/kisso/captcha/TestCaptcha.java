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
package com.baomidou.kisso.captcha;

import com.baomidou.kisso.common.util.RandomType;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.FileOutputStream;

/**
 * 测试图片验证码
 *
 * @author hubin
 * @since 2018-08-11
 */
public class TestCaptcha {
    private static String text = "66N188";

    private ImageCaptcha getImageCaptcha() {
        ImageCaptcha imageCaptcha = ImageCaptcha.getInstance();
        imageCaptcha.setWidth(150);
        imageCaptcha.setHeight(50);
        imageCaptcha.setRgbArr(ColorType.LIVELY);
        return imageCaptcha;
    }

    @Test
    public void test() throws Exception {
        String code = getImageCaptcha().writeImage(text, new FileOutputStream("abc.png"));
        System.out.println("验证码：" + code);
    }

    @Test
    public void testGIf() throws Exception {
        String code = getImageCaptcha().setGif(true)
                .setLength(6)
                .setWidth(120)
                .setHeight(60)
                .setRandomType(RandomType.MIX)
                .setFont(new Font("Arial", Font.PLAIN, 32))
                .writeImage(text, new FileOutputStream("abc.gif"));
        System.out.println("验证码：" + code);
    }

    @Test
    public void testHan() throws Exception {
        String code = getImageCaptcha().setRandomType(RandomType.CHINESE)
                .setFont(new Font("微软雅黑", Font.PLAIN, 30))
                .writeImage(text, new FileOutputStream("han.png"));
        System.out.println("验证码：" + code);
    }

    @Test
    public void testGifHan() throws Exception {
        String code = ImageCaptcha.getInstance().setGif(false)
                .setGif(true)
                .setLength(4)
                .setInterfere(3)
                .setRandomType(RandomType.CHINESE)
                .setFont(new Font("微软雅黑", Font.PLAIN, 30))
                .setColor(Color.BLACK)
                .setInterfereColor(Color.orange)
                .writeImage(text, new FileOutputStream("han.gif"));
        System.out.println("验证码：" + code);
    }

}
