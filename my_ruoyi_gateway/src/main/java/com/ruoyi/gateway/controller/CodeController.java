package com.ruoyi.gateway.controller;

import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.gateway.service.ValidateCaptchaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class CodeController {

    @Resource
    private ValidateCaptchaService validateCaptchaService;

    @GetMapping("/code")
    public AjaxResult getCode() {
        return validateCaptchaService.createCaptcha();
    }
}