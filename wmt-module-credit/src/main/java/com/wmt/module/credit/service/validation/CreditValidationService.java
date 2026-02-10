package com.wmt.module.credit.service.validation;

import com.wmt.module.credit.dal.dataobject.form.CreditFormDataDO;

/**
 * 征信校验 Service 接口
 *
 * @author AHC源码
 */
public interface CreditValidationService {

    /**
     * 校验表单数据
     *
     * @param formData 表单数据
     */
    void validateFormData(CreditFormDataDO formData);

    /**
     * 校验单个字段
     *
     * @param fieldCode 字段编码
     * @param value     字段值
     * @param formData  表单数据（用于自定义校验时访问其他字段）
     */
    void validateField(String fieldCode, Object value, java.util.Map<String, Object> formData);

}
