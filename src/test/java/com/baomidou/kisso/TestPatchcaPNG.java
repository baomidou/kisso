/**
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

import java.awt.Color;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.baomidou.kisso.common.captcha.background.OvalNoiseBackgroundFactory;
import com.baomidou.kisso.common.captcha.color.SingleColorFactory;
import com.baomidou.kisso.common.captcha.filter.ConfigurableFilterFactory;
import com.baomidou.kisso.common.captcha.filter.library.AbstractImageOp;
import com.baomidou.kisso.common.captcha.filter.library.WobbleImageOp;
import com.baomidou.kisso.common.captcha.service.ConfigurableCaptchaService;
import com.baomidou.kisso.common.captcha.utils.encoder.EncoderHelper;

/**
 * <p>
 * 验证码测试
 * </p>
 *
 * @author hubin
 * @since 2015-12-01
 */
public class TestPatchcaPNG {

    /**
     * 测试
     * <p>
     * 执行后生成图片 test_patchca.png 当前项目目录下
     * </p>
     */
    public static void main(String[] args) throws IOException {
        File dir = new File(System.getProperty("user.dir"));
        if (!dir.exists()) {
            dir.mkdir();
        }

        ConfigurableCaptchaService cs = new ConfigurableCaptchaService();
        //验证码宽高
//		cs.setWidth(119);
//		cs.setHeight(42);

        //设置 6 位自适应验证码
//		AdaptiveRandomWordFactory arw = new AdaptiveRandomWordFactory();
//		arw.setMinLength(6);
//		arw.setMaxLength(6);
//		cs.setWordFactory(arw);

        //字符大小设置
//		RandomFontFactory rf = new RandomFontFactory();
//		rf.setMinSize(28);
//		rf.setMaxSize(32);
//		cs.setFontFactory(rf);

        //文本渲染
//		cs.setTextRenderer(new RandomYBestFitTextRenderer());

        //设置一个单一颜色字体
        cs.setColorFactory(new SingleColorFactory(new Color(59, 162, 9)));
//		cs.setFilterFactory(new CurvesRippleFilterFactory(cs.getColorFactory()));

        //图片滤镜设置
        ConfigurableFilterFactory filterFactory = new ConfigurableFilterFactory();
        List<BufferedImageOp> filters = new ArrayList<BufferedImageOp>();

        //摆动干扰
        WobbleImageOp wio = new WobbleImageOp();
        wio.setEdgeMode(AbstractImageOp.EDGE_CLAMP);
        wio.setxAmplitude(2.0);
        wio.setyAmplitude(1.0);
        filters.add(wio);

        //曲线干扰
//		CurvesImageOp cio = new CurvesImageOp();
//		cio.setColorFactory(new SingleColorFactory(new Color(59, 162, 9)));
//		cio.setEdgeMode(AbstractImageOp.EDGE_ZERO);
//		cio.setStrokeMax(0.3f);
//		cio.setStrokeMin(0.1f);
//		filters.add(cio);

        filterFactory.setFilters(filters);
        cs.setFilterFactory(filterFactory);

        //椭圆形干扰背景
        cs.setBackgroundFactory(new OvalNoiseBackgroundFactory());

        //线形干扰背景
//		cs.setBackgroundFactory(new LineNoiseBackgroundFactory());
        FileOutputStream fos = new FileOutputStream(new File(dir, "test_patchca.png"));
        EncoderHelper.getChallangeAndWriteImage(cs, "png", fos);
        fos.close();
    }
}
