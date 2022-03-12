package com.xx.gateway.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import com.google.code.kaptcha.Producer;
import com.xx.common.core.constant.Constants;
import com.xx.common.core.web.domain.AjaxResult;
import com.xx.common.redis.service.RedisService;
import com.xx.gateway.config.properties.CaptchaProperties;
import com.xx.gateway.service.ValidateCaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class ValidateCaptchaServiceImpl implements ValidateCaptchaService {
    @Resource(name = "captchaProducerChar")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CaptchaProperties captchaProperties;

    /**
     * 生成验证码
     *
     * @return
     */
    @Override
    public AjaxResult createCaptcha() {
        AjaxResult ajaxResult = AjaxResult.success();
        Boolean enabled = captchaProperties.getEnabled();
        ajaxResult.put(Constants.CAPTCHA_ON_OFF, enabled);
        if (!enabled) {
            return ajaxResult;
        }

        // capStr是显示的字符 code是结果
        String capStr = null, code = null;
        BufferedImage image = null;

        // choose math or text based on the yaml file in Nacos.
        String type = captchaProperties.getType();
        if (Constants.CAPTCHA_TYPE_MATH.equals(type)) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        }

        if (Constants.CAPTCHA_TYPE_CHAR.equals(type)) {
            capStr = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        //The captcha is valid 2 minutes
        String simpleUUID = IdUtil.simpleUUID();
        String redisVerifyKey = Constants.CAPTCHA_CODE_KEY + simpleUUID;
        redisService.setCacheObject(redisVerifyKey, code, 2L, TimeUnit.MINUTES);

        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, Constants.CAPTCHA_FILE_TYPE, os);
        } catch (IOException e) {
            return AjaxResult.error(e.getMessage());
        }

        ajaxResult.put(Constants.UUID, simpleUUID);
        ajaxResult.put(Constants.IMG, Base64.encode(os.toByteArray()));
        return ajaxResult;
    }
}
