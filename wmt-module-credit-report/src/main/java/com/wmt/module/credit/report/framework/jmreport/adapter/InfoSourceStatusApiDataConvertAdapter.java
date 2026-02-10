package com.wmt.module.credit.report.framework.jmreport.adapter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.jmreport.desreport.render.handler.convert.ApiDataConvertAdapter;
import org.springframework.stereotype.Component;

/**
 * 积木报表 HTTP 接口数据适配器：
 * 将后端 CommonResult<JmReportInfoSourceStatusRespVO> 结构
 * 转换为积木报表可直接识别的「扁平列表」数据集（数组 JSON）。
 *
 * 用法：
 * 1. 在积木报表的数据集配置中选择 HTTP/接口数据源。
 * 2. 填写 URL：/admin-api/credit/jmreport/data/info-source/status
 * 3. 在「适配器/转换器 Bean 名称」填写：infoSourceStatusParser
 * 4. 数据集无需再配置 data/items 路径，直接按字段名绑定即可。
 *
 * @author Auto
 */
@Component("infoSourceStatusParser")
public class InfoSourceStatusApiDataConvertAdapter implements ApiDataConvertAdapter {

    /**
     * 返回 list 数据集，转换成积木报表需要的扁平结构（JSONArray，不再包含外层 data / items 嵌套）。
     *
     * 期望输入 JSON 结构：
     * {
     *   "code": 0,
     *   "msg": "success",
     *   "data": {
     *     "periodId": "...",
     *     "reportId": "...",
     *     "recordId": "...",
     *     "totalProviderOrgTotal": 3,
     *     "totalProviderOrgCurrent": 26,
     *     "items": [
     *       {
     *         "industryCode": "bank",
     *         "sourceTypeName": "银行",
     *         "providerOrgTotal": 1,
     *         "providerOrgCurrent": 3
     *       },
     *       ...
     *     ]
     *   }
     * }
     *
     * 适配后返回：
     * [
     *   {
     *     "industryCode": "bank",
     *     "sourceTypeName": "银行",
     *     "providerOrgTotal": 1,
     *     "providerOrgCurrent": 3
     *   },
     *   ...
     * ]
     */
    @Override
    public String getData(JSONObject jsonObject) {
        if (jsonObject == null) {
            return "[]";
        }
        // CommonResult.data
        JSONObject data = jsonObject.getJSONObject("data");
        if (data == null) {
            return "[]";
        }
        // data.items 为我们真正的列表数据
        JSONArray items = data.getJSONArray("items");
        if (items == null) {
            return "[]";
        }
        // 直接返回 items 数组，积木会把数组中的每个对象当成一行
        return items.toJSONString();
    }

    /**
     * 图表 links（当前场景不用，可返回空）
     */
    @Override
    public String getLinks(JSONObject jsonObject) {
        return "";
    }

    /**
     * 总页数（当前场景无分页，固定返回 "1" 即可）
     */
    @Override
    public String getTotal(JSONObject jsonObject) {
        return "1";
    }

    /**
     * 总条数（当前场景无分页，返回实际条数或 "0" 皆可，这里简单返回 items.size）
     */
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

