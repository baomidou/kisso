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
   * <p>
   * 生成验证码
   * </p>
   *
   * @param out 输出流
   * @return 是否成功
   */
  @Override
  public String out(OutputStream out) throws IOException {
    String code = randomCode();
    if (gif) {
      GifEncoder gifEncoder = new GifEncoder();
      gifEncoder.start(out);
      gifEncoder.setQuality(180);
      gifEncoder.setDelay(100);
      gifEncoder.setRepeat(0);
      for (int i = 0; i < length; i++) {
        gifEncoder.addFrame(graphicsImage(code));
      }
      gifEncoder.finish();
    } else {
      ImageIO.write(graphicsImage(code), suffix, out);
    }
    out.flush();
    return code;
  }


  /**
   * <p>
   * 绘制图片验证码
   * </p>
   *
   * @param code 验证码
   * @return BufferedImage
   */
  private BufferedImage graphicsImage(String code) {
    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g = (Graphics2D) bi.getGraphics();
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, width, height);
    g.setFont(font);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    // 随机画干扰线
    if (interfere > 0) {
      for (int i = 0; i < interfere; i++) {
        g.setColor(null == interfereColor ? RandomUtil.getColor(150, 250) : interfereColor);
        g.setStroke(new BasicStroke(1.1f + RandomUtil.RANDOM.nextFloat() / 2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        int x1 = num(-10, width - 10);
        int y1 = num(5, height - 5);
        int x2 = num(10, width + 10);
        int y2 = num(2, height - 2);
        g.drawLine(x1, y1, x2, y2);
        // 画干扰圆圈
        g.setColor(null == interfereColor ? RandomUtil.getColor(100, 250) : interfereColor);
        g.drawOval(num(width), num(height), 3 + num(15), 3 + num(15));
      }
    }
    // 画字符串指定透明度
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
    int hp = (height - font.getSize()) >> 1;
    int h = height - hp;
    int w = width / length;
    //int sp = (w - font.getSize()) / 2;
    for (int i = 0; i < length; i++) {
      g.setColor(null == color ? new Color(20 + num(110), 20 + num(110), 20 + num(110)) : color);
      // 计算坐标
      int x = i * w + num(10);
      int y = h - num(9);
      if (x < 0) {
        x = 0;
      }
      if (x + font.getSize() > width) {
        x = width - font.getSize();
      }
      if (y > height) {
        y = height;
      }
      if (y - font.getSize() < 0) {
        y = font.getSize();
      }
      g.drawString(String.valueOf(code.charAt(i)), x, y);
    }
    return bi;
  }
}
