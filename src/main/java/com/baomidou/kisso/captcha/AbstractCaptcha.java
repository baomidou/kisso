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

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;

import jakarta.servlet.http.HttpServletRequest;

import com.baomidou.kisso.common.util.RandomType;
import com.baomidou.kisso.common.util.RandomUtil;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 验证码抽象类
 * </p>
 *
 * @author hubin
 * @since 2018-08-11
 */
@Data
@Accessors(chain = true)
public abstract class AbstractCaptcha implements ICaptcha {

    /**
     * 是否为 GIF 验证码
     */
    protected boolean gif;
    /**
     * 字体
     */
    protected Font font;
    /**
     * RGB 颜色数组
     */
    protected int[][] rgbArr;
    /**
     * 干扰量
     */
    protected int interfere = 5;
    /**
     * 干扰色默认随机
     */
    protected Color interfereColor;
    /**
     * 验证码颜色默认随机
     */
    protected Color color;
    /**
     * 验证码随机字符长度
     */
    protected int length;
    /**
     * 验证码显示宽度
     */
    protected int width;
    /**
     * 验证码显示高度
     */
    protected int height;
    /**
     * 图片后缀
     */
    protected String suffix;
    /**
     * 验证码类型
     */
    protected RandomType randomType;
    /**
     * 常用汉字
     */
    protected String chineseUnicode;

    /**
     * 图片验证码票据存储接口
     */
    protected ICaptchaStore captchaStore;

    /**
     * 是否忽略验证内容大小写，默认 true
     */
    protected boolean ignoreCase = true;

    @Override
    public void generate(HttpServletRequest request, OutputStream out, String ticket) throws IOException {
        String captcha = randomCaptcha();
        if (getCaptchaStore(request).put(ticket, captcha)) {
            writeImage(captcha, out);
        }
    }

    @Override
    public boolean verification(HttpServletRequest request, String ticket, String captcha) {
        String tc = getCaptchaStore(request).get(ticket);
        if (null == tc) {
            return false;
        }
        return ignoreCase ? tc.equalsIgnoreCase(captcha) : tc.equals(captcha);
    }

    /**
     * <p>
     * 内置 HttpSession 存储验证
     * </p>
     *
     * @param request
     * @return
     */
    private ICaptchaStore getCaptchaStore(HttpServletRequest request) {
        if (null == captchaStore) {
            return new CaptchaStoreSession(request);
        }
        return captchaStore;
    }

    /**
     * <p>
     * 输出图片验证码
     * </p>
     *
     * @param captcha 验证码
     * @param out     输出流
     * @return 字符串验证码
     * @throws IOException
     */
    protected abstract String writeImage(String captcha, OutputStream out) throws IOException;

    /**
     * 产生两个数之间的随机数
     *
     * @param min 最小值
     * @param max 最大值
     * @return 随机数
     */
    protected int num(int min, int max) {
        return min + RandomUtil.RANDOM.nextInt(max - min);
    }

    /**
     * 产生0-num的随机数,不包括num
     *
     * @param num 最大值
     * @return 随机数
     */
    protected int num(int num) {
        return RandomUtil.RANDOM.nextInt(num);
    }

    /**
     * 随机验证码
     */
    protected String randomCaptcha() {
        // 默认设置
        if (null == randomType) {
            randomType = RandomType.MIX;
        }
        if (null == font) {
            if (RandomType.CHINESE == randomType) {
                font = new Font("楷体", Font.PLAIN, 28);
            } else {
                font = new Font("Arial", Font.PLAIN, 32);
            }
        }
        if (null == rgbArr) {
            rgbArr = ColorType.LIVELY;
        }
        if (null == suffix) {
            suffix = gif ? "gif" : "png";
        }
        if (width < 10) {
            width = 120;
        }
        if (height < 10) {
            height = 48;
        }
        if (length < 1) {
            length = 5;
        }
        // 生成随机码
        if (RandomType.CHINESE == randomType) {
            return RandomUtil.getChinese(chineseUnicode, length);
        }
        return RandomUtil.getText(randomType, length);
    }
}