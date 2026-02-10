/**
 * credit 包下，征信报送模块（Credit Reporting Module）。
 * 例如说：字段配置、表单数据、计算规则、汇总报表等等
 *
 * 1. Controller URL：以 /credit/ 开头，避免和其它 Module 冲突
 * 2. DataObject 表名：以 credit_ 开头，方便在数据库中区分
 *
 * 注意，由于 Credit 模块下，容易和其它模块重名，所以类名都加载 Credit 的前缀~
 */
package com.wmt.module.credit;
