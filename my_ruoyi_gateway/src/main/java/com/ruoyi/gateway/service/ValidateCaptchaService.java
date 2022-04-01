package com.ruoyi.gateway.service;

import com.ruoyi.common.core.web.domain.AjaxResult;

public interface ValidateCaptchaService {
    AjaxResult createCaptcha();

    void checkCaptcha(String code, String uuid);
}
