package com.ruoyi.system.controller;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.security.annotation.InnerAuth;
import com.ruoyi.system.api.domain.SysLogininfor;
import com.ruoyi.system.service.SysLogininforService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logininfor")
public class SysLogininforController extends BaseController {

    @Autowired
    private SysLogininforService logininforService;

    @InnerAuth
    @PostMapping
    public AjaxResult add(@RequestBody SysLogininfor logininfor) {
        int rows = logininforService.insertLoginInformation(logininfor);
        return AjaxResult.successIfAffectedRowsGreaterThan0(rows);
    }
}
