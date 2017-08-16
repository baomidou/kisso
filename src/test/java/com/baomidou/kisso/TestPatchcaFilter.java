/**
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baomidou.kisso;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import com.baomidou.kisso.common.captcha.color.SingleColorFactory;
import com.baomidou.kisso.common.captcha.filter.predefined.CurvesRippleFilterFactory;
import com.baomidou.kisso.common.captcha.filter.predefined.DiffuseRippleFilterFactory;
import com.baomidou.kisso.common.captcha.filter.predefined.DoubleRippleFilterFactory;
import com.baomidou.kisso.common.captcha.filter.predefined.MarbleRippleFilterFactory;
import com.baomidou.kisso.common.captcha.filter.predefined.WobbleRippleFilterFactory;
import com.baomidou.kisso.common.captcha.service.ConfigurableCaptchaService;

/**
 * <p>
 * 验证码过滤器测试
 * </p>
 *
 * @author hubin
 * @since 2015-12-01
 */
public class TestPatchcaFilter extends Frame implements ActionListener {

    private static final long serialVersionUID = 6698906953413370733L;
    private BufferedImage img;
    private Button reloadButton;
    private int counter;

    /**
     * 测试
     */
    public static void main(String[] args) {
        new TestPatchcaFilter().setVisible(true);
    }

    public TestPatchcaFilter() {
        super("Kisso Patchca Demo");
        this.setSize(300, 200);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width - this.getSize().width) / 2;
        int y = (dim.height - this.getSize().height) / 2;
        this.setLocation(x, y);
        Panel bottom = new Panel();
        reloadButton = new Button("Next filter");
        reloadButton.addActionListener(this);
        bottom.add(reloadButton);
        this.add(BorderLayout.SOUTH, bottom);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        if (img == null) {
            createImage();
        }
        if (img != null) {
            g.drawImage(img, 70, 60, this);
        }
    }

    /**
     * 创建验证码
     */
    public void createImage() {
        ConfigurableCaptchaService cs = new ConfigurableCaptchaService();
        cs.setColorFactory(new SingleColorFactory(new Color(25, 60, 170)));
        switch (counter % 5) {
            case 0:
                cs.setFilterFactory(new CurvesRippleFilterFactory(cs.getColorFactory()));
                break;
            case 1:
                cs.setFilterFactory(new MarbleRippleFilterFactory());
                break;
            case 2:
                cs.setFilterFactory(new DoubleRippleFilterFactory());
                break;
            case 3:
                cs.setFilterFactory(new WobbleRippleFilterFactory());
                break;
            case 4:
                cs.setFilterFactory(new DiffuseRippleFilterFactory());
                break;
        }
        img = cs.getCaptcha().getImage();
        counter++;
    }

    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == reloadButton) {
            createImage();
            repaint();
        }
    }

}
