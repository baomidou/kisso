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

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.baomidou.kisso.common.util.RandomUtil;

/**
 * <p>
 * 图片验证码
 * </p>
 *
 * @author hubin
 * @since 2018-08-11
 */
public class ImageCaptcha extends AbstractCaptcha {

    /**
     * 图片验证码
     */
    private static ImageCaptcha IMAGE_CAPTCHA;

    /**
     * 验证码实例
     */
    public static synchronized ImageCaptcha getInstance() {
        if (null == IMAGE_CAPTCHA) {
            IMAGE_CAPTCHA = new ImageCaptcha();
        }
        return IMAGE_CAPTCHA;
    }

    private ImageCaptcha() {
        // to do nothing
        // use ImageCaptcha.getInstance()....generate(request, response.getOutputStream(), ticket);
    }

    /**
     * <p>
     * 生成图片验证码
     * </p>
     *
     * @param out 输出流
     * @return 是否成功
     */
    @Override
    protected String writeImage(String captcha, OutputStream out) throws IOException {
        if (gif) {
            GifEncoder gifEncoder = new GifEncoder();
            gifEncoder.start(out);
            gifEncoder.setQuality(180);
            gifEncoder.setDelay(100);
            gifEncoder.setRepeat(0);
            for (int i = 0; i < length; i++) {
                gifEncoder.addFrame(graphicsImage(captcha, i));
            }
            gifEncoder.finish();
        } else {
            ImageIO.write(graphicsImage(captcha, 1), suffix, out);
        }
        out.flush();
        return captcha;
    }


    /**
     * <p>
     * 绘制图片验证码
     * </p>
     *
     * @param code 验证码
     * @param flag 透明度
     * @return BufferedImage
     */
    private BufferedImage graphicsImage(String code, int flag) {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) bi.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setFont(font);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 随机画干扰线
        if (interfere > 0) {
            for (int i = 0; i < interfere; i++) {
                g.setColor(null == interfereColor ? RandomUtil.getColor(rgbArr) : interfereColor);
                g.setStroke(new BasicStroke(1.1f + RandomUtil.RANDOM.nextFloat() / 2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
                int x1 = num(-10, width - 10);
                int y1 = num(5, height - 5);
                int x2 = num(10, width + 10);
                int y2 = num(2, height - 2);
                g.drawLine(x1, y1, x2, y2);
                // 画干扰圆圈
                g.setColor(null == interfereColor ? RandomUtil.getColor(rgbArr) : interfereColor);
                g.drawOval(num(width), num(height), 3 + num(15), 3 + num(15));
            }
        }
        // 画字符串
        int h = height - ((height - font.getSize()) >> 1);
        int w = width / length;
        for (int i = 0; i < length; i++) {
            g.setColor(null == color ? RandomUtil.getColor(rgbArr) : color);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, gif ? getAlpha(i, flag) : 0.75f));
            // 计算坐标
            g.drawString(String.valueOf(code.charAt(i)), (width - (length - i) * w) + (w - font.getSize()) + 1, h - 3);
        }
        return bi;
    }


    /**
     * 获取透明度,从0到1,自动计算步长
     */
    private float getAlpha(int i, int j) {
        int num = i + j;
        float r = (float) 1 / (length - 1);
        float s = length * r;
        return num >= length ? (num * r - s) : num * r;
    }
}
