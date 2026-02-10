package com.wmt.module.credit.report.framework.jmreport.adapter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.jmreport.desreport.render.handler.convert.ApiDataConvertAdapter;
import org.springframework.stereotype.Component;

/**
 * 积木报表 HTTP 接口数据适配器：
 * 将后端 CommonResult<JmReportProductServiceRespVO> 结构
 * 转换为积木报表可直接识别的「扁平列表」数据集（数组 JSON）。
 *
 * 用法：
 * 1. 在积木报表的数据集配置中选择 HTTP/接口数据源。
 * 2. 填写 URL：/admin-api/credit/jmreport/data/product-service
 * 3. 在「适配器/转换器 Bean 名称」填写：productServiceParser
 * 4. 数据集无需再配置 data/items 路径，直接按字段名绑定即可。
 *
 * @author Auto
 */
@Component("productServiceParser")
public class ProductServiceApiDataConvertAdapter implements ApiDataConvertAdapter {

    @Override
    public String getData(JSONObject jsonObject) {
        if (jsonObject == null) {
            return "[]";
        }
        JSONObject data = jsonObject.getJSONObject("data");
        if (data == null) {
            return "[]";
        }
        JSONArray items = data.getJSONArray("items");
        if (items == null) {
            return "[]";
        }
        return items.toJSONString();
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
        if (jsonObject == null) {
            return "0";
        }
        JSONObject data = jsonObject.getJSONObject("data");
        if (data == null) {
            return "0";
        }
        JSONArray items = data.getJSONArray("items");
        return items == null ? "0" : String.valueOf(items.size());
    }
}
