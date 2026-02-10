package com.wmt.module.credit.report.framework.jmreport.adapter;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.jmreport.desreport.render.handler.convert.ApiDataConvertAdapter;
import org.springframework.stereotype.Component;

/**
 * 积木报表 HTTP 接口数据适配器：
 * 将后端 CommonResult<JmReportServiceByIndustryTotalRespVO> 结构
 * 转换为积木报表可直接识别的单行数据。
 *
 * 用法：
 * 1. 在积木报表的数据集配置中选择 HTTP/接口数据源。
 * 2. 填写 URL：/admin-api/credit/jmreport/data/service-by-industry/total
 * 3. 在「适配器/转换器 Bean 名称」填写：serviceByIndustryTotalParser
 * 4. 数据集直接绑定 data 下的字段即可。
 *
 * @author Auto
 */
@Component("serviceByIndustryTotalParser")
public class ServiceByIndustryTotalApiDataConvertAdapter implements ApiDataConvertAdapter {

    @Override
    public String getData(JSONObject jsonObject) {
        if (jsonObject == null) {
            return "{}";
        }
        JSONObject data = jsonObject.getJSONObject("data");
        if (data == null) {
            return "{}";
        }
        // 返回单行数据（包装成数组，积木可能需要）
        return "[" + data.toJSONString() + "]";
    }

    @Override
    public String getLinks(JSONObject jsonObject) {
        return "";
    }

    @Override
    public String getTotal(JSONObject jsonObject) {
        return "1";
    }

    @Override
    public String getCount(JSONObject jsonObject) {
        return "1";
    }
}
