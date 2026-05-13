# AHC-tdc-wmt开发平台


**简介**:AHC-tdc-wmt开发平台


**HOST**:http://172.20.10.4:48080


**联系人**:


**Version**:1.0.0


**接口路径**:/v3/api-docs/system


[TOC]






# 管理后台 - 用户


## 修改用户


**接口地址**:`/admin-api/system/user/update`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "username": "wmt",
  "nickname": "wmt",
  "remark": "我是一个用户",
  "deptId": "我是一个用户",
  "postIds": 1,
  "email": "wmt@cn",
  "mobile": 15601691300,
  "sex": 1,
  "avatar": "https://www.wmt.cn/xxx.png",
  "password": 123456
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userSaveReqVO|管理后台 - 用户创建/修改 Request VO|body|true|UserSaveReqVO|UserSaveReqVO|
|&emsp;&emsp;id|用户编号||false|integer(int64)||
|&emsp;&emsp;username|用户账号||true|string||
|&emsp;&emsp;nickname|用户昵称||true|string||
|&emsp;&emsp;remark|备注||false|string||
|&emsp;&emsp;deptId|部门编号||false|integer(int64)||
|&emsp;&emsp;postIds|岗位编号数组||false|array|integer(int64)|
|&emsp;&emsp;email|用户邮箱||false|string(email)||
|&emsp;&emsp;mobile|手机号码||false|string||
|&emsp;&emsp;sex|用户性别，参见 SexEnum 枚举类||false|integer(int32)||
|&emsp;&emsp;avatar|用户头像||false|string||
|&emsp;&emsp;password|密码||true|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 修改用户状态


**接口地址**:`/admin-api/system/user/update-status`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "status": 1
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userUpdateStatusReqVO|管理后台 - 用户更新状态 Request VO|body|true|UserUpdateStatusReqVO|UserUpdateStatusReqVO|
|&emsp;&emsp;id|用户编号||true|integer(int64)||
|&emsp;&emsp;status|状态，见 CommonStatusEnum 枚举||true|integer(int32)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 重置用户密码


**接口地址**:`/admin-api/system/user/update-password`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "password": 123456
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userUpdatePasswordReqVO|管理后台 - 用户更新密码 Request VO|body|true|UserUpdatePasswordReqVO|UserUpdatePasswordReqVO|
|&emsp;&emsp;id|用户编号||true|integer(int64)||
|&emsp;&emsp;password|密码||true|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 导入用户


**接口地址**:`/admin-api/system/user/import`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|file|Excel 文件|query|true|||
|updateSupport|是否支持更新，默认为 false|query|false|boolean||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultUserImportRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||UserImportRespVO|UserImportRespVO|
|&emsp;&emsp;createUsernames|创建成功的用户名数组|array|string|
|&emsp;&emsp;updateUsernames|更新成功的用户名数组|array|string|
|&emsp;&emsp;failureUsernames|导入失败的用户集合，key 为用户名，value 为失败原因|object||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"createUsernames": [],
		"updateUsernames": [],
		"failureUsernames": {}
	}
}
```


## 删除用户


**接口地址**:`/admin-api/system/user/delete`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 批量删除用户


**接口地址**:`/admin-api/system/user/delete-list`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|ids|编号列表|query|true|array|integer|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 新增用户


**接口地址**:`/admin-api/system/user/create`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "username": "wmt",
  "nickname": "wmt",
  "remark": "我是一个用户",
  "deptId": "我是一个用户",
  "postIds": 1,
  "email": "wmt@cn",
  "mobile": 15601691300,
  "sex": 1,
  "avatar": "https://www.wmt.cn/xxx.png",
  "password": 123456
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userSaveReqVO|管理后台 - 用户创建/修改 Request VO|body|true|UserSaveReqVO|UserSaveReqVO|
|&emsp;&emsp;id|用户编号||false|integer(int64)||
|&emsp;&emsp;username|用户账号||true|string||
|&emsp;&emsp;nickname|用户昵称||true|string||
|&emsp;&emsp;remark|备注||false|string||
|&emsp;&emsp;deptId|部门编号||false|integer(int64)||
|&emsp;&emsp;postIds|岗位编号数组||false|array|integer(int64)|
|&emsp;&emsp;email|用户邮箱||false|string(email)||
|&emsp;&emsp;mobile|手机号码||false|string||
|&emsp;&emsp;sex|用户性别，参见 SexEnum 枚举类||false|integer(int32)||
|&emsp;&emsp;avatar|用户头像||false|string||
|&emsp;&emsp;password|密码||true|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultLong|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": 0
}
```


## 获得用户分页列表


**接口地址**:`/admin-api/system/user/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|username|用户账号，模糊匹配|query|false|string||
|mobile|手机号码，模糊匹配|query|false|string||
|status|展示状态，参见 CommonStatusEnum 枚举类|query|false|integer(int32)||
|createTime|创建时间|query|false|array|string|
|deptId|部门编号，同时筛选子部门|query|false|integer(int64)||
|roleId|角色编号|query|false|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultPageResultUserRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||PageResultUserRespVO|PageResultUserRespVO|
|&emsp;&emsp;total|总量|integer(int64)||
|&emsp;&emsp;list|数据|array|UserRespVO|
|&emsp;&emsp;&emsp;&emsp;id|用户编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;username|用户账号|string||
|&emsp;&emsp;&emsp;&emsp;nickname|用户昵称|string||
|&emsp;&emsp;&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;&emsp;&emsp;deptId|部门ID|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;deptName|部门名称|string||
|&emsp;&emsp;&emsp;&emsp;postIds|岗位编号数组|array|integer|
|&emsp;&emsp;&emsp;&emsp;email|用户邮箱|string||
|&emsp;&emsp;&emsp;&emsp;mobile|手机号码|string||
|&emsp;&emsp;&emsp;&emsp;sex|用户性别，参见 SexEnum 枚举类|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;avatar|用户头像|string||
|&emsp;&emsp;&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举类|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;loginIp|最后登录 IP|string||
|&emsp;&emsp;&emsp;&emsp;loginDate|最后登录时间|string(date-time)||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 1,
				"username": "wmt",
				"nickname": "wmt",
				"remark": "我是一个用户",
				"deptId": "我是一个用户",
				"deptName": "IT 部",
				"postIds": 1,
				"email": "wmt@cn",
				"mobile": 15601691300,
				"sex": 1,
				"avatar": "https://www.wmt.cn/xxx.png",
				"status": 1,
				"loginIp": "192.168.1.1",
				"loginDate": "时间戳格式",
				"createTime": "时间戳格式"
			}
		]
	}
}
```


## 获取用户精简信息列表


**接口地址**:`/admin-api/system/user/list-all-simple`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>只包含被开启的用户，主要用于前端的下拉选项</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListUserSimpleRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|UserSimpleRespVO|
|&emsp;&emsp;id|用户编号|integer(int64)||
|&emsp;&emsp;nickname|用户昵称|string||
|&emsp;&emsp;deptId|部门ID|integer(int64)||
|&emsp;&emsp;deptName|部门名称|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 1024,
			"nickname": "AHC",
			"deptId": "我是一个用户",
			"deptName": "IT 部"
		}
	]
}
```


## 获取用户精简信息列表


**接口地址**:`/admin-api/system/user/simple-list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>只包含被开启的用户，主要用于前端的下拉选项</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListUserSimpleRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|UserSimpleRespVO|
|&emsp;&emsp;id|用户编号|integer(int64)||
|&emsp;&emsp;nickname|用户昵称|string||
|&emsp;&emsp;deptId|部门ID|integer(int64)||
|&emsp;&emsp;deptName|部门名称|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 1024,
			"nickname": "AHC",
			"deptId": "我是一个用户",
			"deptName": "IT 部"
		}
	]
}
```


## 获得用户详情


**接口地址**:`/admin-api/system/user/get`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultUserRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||UserRespVO|UserRespVO|
|&emsp;&emsp;id|用户编号|integer(int64)||
|&emsp;&emsp;username|用户账号|string||
|&emsp;&emsp;nickname|用户昵称|string||
|&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;deptId|部门ID|integer(int64)||
|&emsp;&emsp;deptName|部门名称|string||
|&emsp;&emsp;postIds|岗位编号数组|array|integer(int64)|
|&emsp;&emsp;email|用户邮箱|string||
|&emsp;&emsp;mobile|手机号码|string||
|&emsp;&emsp;sex|用户性别，参见 SexEnum 枚举类|integer(int32)||
|&emsp;&emsp;avatar|用户头像|string||
|&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举类|integer(int32)||
|&emsp;&emsp;loginIp|最后登录 IP|string||
|&emsp;&emsp;loginDate|最后登录时间|string(date-time)||
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 1,
		"username": "wmt",
		"nickname": "wmt",
		"remark": "我是一个用户",
		"deptId": "我是一个用户",
		"deptName": "IT 部",
		"postIds": 1,
		"email": "wmt@cn",
		"mobile": 15601691300,
		"sex": 1,
		"avatar": "https://www.wmt.cn/xxx.png",
		"status": 1,
		"loginIp": "192.168.1.1",
		"loginDate": "时间戳格式",
		"createTime": "时间戳格式"
	}
}
```


## 获得导入用户模板


**接口地址**:`/admin-api/system/user/get-import-template`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||


**响应参数**:


暂无


**响应示例**:
```javascript

```


## 导出用户


**接口地址**:`/admin-api/system/user/export-excel`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|username|用户账号，模糊匹配|query|false|string||
|mobile|手机号码，模糊匹配|query|false|string||
|status|展示状态，参见 CommonStatusEnum 枚举类|query|false|integer(int32)||
|createTime|创建时间|query|false|array|string|
|deptId|部门编号，同时筛选子部门|query|false|integer(int64)||
|roleId|角色编号|query|false|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||


**响应参数**:


暂无


**响应示例**:
```javascript

```


# 管理后台 - 用户个人中心


## 修改用户个人信息


**接口地址**:`/admin-api/system/user/profile/update`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "nickname": "wmt",
  "email": "wmt@cn",
  "mobile": 15601691300,
  "sex": 1,
  "avatar": "https://www.wmt.cn/1.png"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userProfileUpdateReqVO|管理后台 - 用户个人信息更新 Request VO|body|true|UserProfileUpdateReqVO|UserProfileUpdateReqVO|
|&emsp;&emsp;nickname|用户昵称||false|string||
|&emsp;&emsp;email|用户邮箱||false|string(email)||
|&emsp;&emsp;mobile|手机号码||false|string||
|&emsp;&emsp;sex|用户性别，参见 SexEnum 枚举类||false|integer(int32)||
|&emsp;&emsp;avatar|角色头像||false|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 修改用户个人密码


**接口地址**:`/admin-api/system/user/profile/update-password`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "oldPassword": 123456,
  "newPassword": 654321
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userProfileUpdatePasswordReqVO|管理后台 - 用户个人中心更新密码 Request VO|body|true|UserProfileUpdatePasswordReqVO|UserProfileUpdatePasswordReqVO|
|&emsp;&emsp;oldPassword|旧密码||true|string||
|&emsp;&emsp;newPassword|新密码||true|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 获得登录用户信息


**接口地址**:`/admin-api/system/user/profile/get`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultUserProfileRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||UserProfileRespVO|UserProfileRespVO|
|&emsp;&emsp;id|用户编号|integer(int64)||
|&emsp;&emsp;username|用户账号|string||
|&emsp;&emsp;nickname|用户昵称|string||
|&emsp;&emsp;email|用户邮箱|string||
|&emsp;&emsp;mobile|手机号码|string||
|&emsp;&emsp;sex|用户性别，参见 SexEnum 枚举类|integer(int32)||
|&emsp;&emsp;avatar|用户头像|string||
|&emsp;&emsp;loginIp|最后登录 IP|string||
|&emsp;&emsp;loginDate|最后登录时间|string(date-time)||
|&emsp;&emsp;createTime|创建时间|string(date-time)||
|&emsp;&emsp;roles||array|RoleSimpleRespVO|
|&emsp;&emsp;&emsp;&emsp;id|角色编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;name|角色名称|string||
|&emsp;&emsp;dept||DeptSimpleRespVO|DeptSimpleRespVO|
|&emsp;&emsp;&emsp;&emsp;id|部门编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;name|部门名称|string||
|&emsp;&emsp;&emsp;&emsp;parentId|父部门 ID|integer(int64)||
|&emsp;&emsp;posts||array|PostSimpleRespVO|
|&emsp;&emsp;&emsp;&emsp;id|岗位序号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;name|岗位名称|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 1,
		"username": "wmt",
		"nickname": "wmt",
		"email": "wmt@cn",
		"mobile": 15601691300,
		"sex": 1,
		"avatar": "https://www.wmt.cn/xxx.png",
		"loginIp": "192.168.1.1",
		"loginDate": "时间戳格式",
		"createTime": "时间戳格式",
		"roles": [
			{
				"id": 1024,
				"name": "AHC"
			}
		],
		"dept": {
			"id": 1024,
			"name": "AHC",
			"parentId": 1024
		},
		"posts": [
			{
				"id": 1024,
				"name": "小土豆"
			}
		]
	}
}
```


# 管理后台 - 租户


## 更新租户


**接口地址**:`/admin-api/system/tenant/update`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "name": "AHC",
  "contactName": "wmt",
  "contactMobile": 15601691300,
  "status": 1,
  "websites": "https://www.wmt.cn",
  "packageId": 1024,
  "expireTime": "",
  "accountCount": 1024,
  "username": "wmt",
  "password": 123456
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenantSaveReqVO|管理后台 - 租户创建/修改 Request VO|body|true|TenantSaveReqVO|TenantSaveReqVO|
|&emsp;&emsp;id|租户编号||false|integer(int64)||
|&emsp;&emsp;name|租户名||true|string||
|&emsp;&emsp;contactName|联系人||true|string||
|&emsp;&emsp;contactMobile|联系手机||false|string||
|&emsp;&emsp;status|租户状态||true|integer(int32)||
|&emsp;&emsp;websites|绑定域名数组||false|array|string|
|&emsp;&emsp;packageId|租户套餐编号||true|integer(int64)||
|&emsp;&emsp;expireTime|过期时间||true|string(date-time)||
|&emsp;&emsp;accountCount|账号数量||true|integer(int32)||
|&emsp;&emsp;username|用户账号||true|string||
|&emsp;&emsp;password|密码||true|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 删除租户


**接口地址**:`/admin-api/system/tenant/delete`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 批量删除租户


**接口地址**:`/admin-api/system/tenant/delete-list`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|ids|编号列表|query|true|array|integer|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 创建租户


**接口地址**:`/admin-api/system/tenant/create`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "name": "AHC",
  "contactName": "wmt",
  "contactMobile": 15601691300,
  "status": 1,
  "websites": "https://www.wmt.cn",
  "packageId": 1024,
  "expireTime": "",
  "accountCount": 1024,
  "username": "wmt",
  "password": 123456
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenantSaveReqVO|管理后台 - 租户创建/修改 Request VO|body|true|TenantSaveReqVO|TenantSaveReqVO|
|&emsp;&emsp;id|租户编号||false|integer(int64)||
|&emsp;&emsp;name|租户名||true|string||
|&emsp;&emsp;contactName|联系人||true|string||
|&emsp;&emsp;contactMobile|联系手机||false|string||
|&emsp;&emsp;status|租户状态||true|integer(int32)||
|&emsp;&emsp;websites|绑定域名数组||false|array|string|
|&emsp;&emsp;packageId|租户套餐编号||true|integer(int64)||
|&emsp;&emsp;expireTime|过期时间||true|string(date-time)||
|&emsp;&emsp;accountCount|账号数量||true|integer(int32)||
|&emsp;&emsp;username|用户账号||true|string||
|&emsp;&emsp;password|密码||true|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultLong|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": 0
}
```


## 获取租户精简信息列表


**接口地址**:`/admin-api/system/tenant/simple-list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>只包含被开启的租户，用于【首页】功能的选择租户选项</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListTenantRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|TenantRespVO|
|&emsp;&emsp;id|租户编号|integer(int64)||
|&emsp;&emsp;name|租户名|string||
|&emsp;&emsp;contactName|联系人|string||
|&emsp;&emsp;contactMobile|联系手机|string||
|&emsp;&emsp;status|租户状态|integer(int32)||
|&emsp;&emsp;websites|绑定域名数组|array|string|
|&emsp;&emsp;packageId|租户套餐编号|integer(int64)||
|&emsp;&emsp;expireTime|过期时间|string(date-time)||
|&emsp;&emsp;accountCount|账号数量|integer(int32)||
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 1024,
			"name": "AHC",
			"contactName": "wmt",
			"contactMobile": 15601691300,
			"status": 1,
			"websites": "https://www.wmt.cn",
			"packageId": 1024,
			"expireTime": "",
			"accountCount": 1024,
			"createTime": ""
		}
	]
}
```


## 获得租户分页


**接口地址**:`/admin-api/system/tenant/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|name|租户名|query|false|string||
|contactName|联系人|query|false|string||
|contactMobile|联系手机|query|false|string||
|status|租户状态（0正常 1停用）|query|false|integer(int32)||
|createTime|创建时间|query|false|array|string|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultPageResultTenantRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||PageResultTenantRespVO|PageResultTenantRespVO|
|&emsp;&emsp;total|总量|integer(int64)||
|&emsp;&emsp;list|数据|array|TenantRespVO|
|&emsp;&emsp;&emsp;&emsp;id|租户编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;name|租户名|string||
|&emsp;&emsp;&emsp;&emsp;contactName|联系人|string||
|&emsp;&emsp;&emsp;&emsp;contactMobile|联系手机|string||
|&emsp;&emsp;&emsp;&emsp;status|租户状态|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;websites|绑定域名数组|array|string|
|&emsp;&emsp;&emsp;&emsp;packageId|租户套餐编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;expireTime|过期时间|string(date-time)||
|&emsp;&emsp;&emsp;&emsp;accountCount|账号数量|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 1024,
				"name": "AHC",
				"contactName": "wmt",
				"contactMobile": 15601691300,
				"status": 1,
				"websites": "https://www.wmt.cn",
				"packageId": 1024,
				"expireTime": "",
				"accountCount": 1024,
				"createTime": ""
			}
		]
	}
}
```


## 获得租户


**接口地址**:`/admin-api/system/tenant/get`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultTenantRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||TenantRespVO|TenantRespVO|
|&emsp;&emsp;id|租户编号|integer(int64)||
|&emsp;&emsp;name|租户名|string||
|&emsp;&emsp;contactName|联系人|string||
|&emsp;&emsp;contactMobile|联系手机|string||
|&emsp;&emsp;status|租户状态|integer(int32)||
|&emsp;&emsp;websites|绑定域名数组|array|string|
|&emsp;&emsp;packageId|租户套餐编号|integer(int64)||
|&emsp;&emsp;expireTime|过期时间|string(date-time)||
|&emsp;&emsp;accountCount|账号数量|integer(int32)||
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 1024,
		"name": "AHC",
		"contactName": "wmt",
		"contactMobile": 15601691300,
		"status": 1,
		"websites": "https://www.wmt.cn",
		"packageId": 1024,
		"expireTime": "",
		"accountCount": 1024,
		"createTime": ""
	}
}
```


## 使用租户名，获得租户编号


**接口地址**:`/admin-api/system/tenant/get-id-by-name`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>登录界面，根据用户的租户名，获得租户编号</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|name|租户名|query|true|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultLong|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": 0
}
```


## 使用域名，获得租户信息


**接口地址**:`/admin-api/system/tenant/get-by-website`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>登录界面，根据用户的域名，获得租户信息</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|website|域名|query|true|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultTenantRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||TenantRespVO|TenantRespVO|
|&emsp;&emsp;id|租户编号|integer(int64)||
|&emsp;&emsp;name|租户名|string||
|&emsp;&emsp;contactName|联系人|string||
|&emsp;&emsp;contactMobile|联系手机|string||
|&emsp;&emsp;status|租户状态|integer(int32)||
|&emsp;&emsp;websites|绑定域名数组|array|string|
|&emsp;&emsp;packageId|租户套餐编号|integer(int64)||
|&emsp;&emsp;expireTime|过期时间|string(date-time)||
|&emsp;&emsp;accountCount|账号数量|integer(int32)||
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 1024,
		"name": "AHC",
		"contactName": "wmt",
		"contactMobile": 15601691300,
		"status": 1,
		"websites": "https://www.wmt.cn",
		"packageId": 1024,
		"expireTime": "",
		"accountCount": 1024,
		"createTime": ""
	}
}
```


## 导出租户 Excel


**接口地址**:`/admin-api/system/tenant/export-excel`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|name|租户名|query|false|string||
|contactName|联系人|query|false|string||
|contactMobile|联系手机|query|false|string||
|status|租户状态（0正常 1停用）|query|false|integer(int32)||
|createTime|创建时间|query|false|array|string|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||


**响应参数**:


暂无


**响应示例**:
```javascript

```


# 管理后台 - 租户套餐


## 更新租户套餐


**接口地址**:`/admin-api/system/tenant-package/update`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "name": "VIP",
  "status": 1,
  "remark": "好",
  "menuIds": []
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenantPackageSaveReqVO|管理后台 - 租户套餐创建/修改 Request VO|body|true|TenantPackageSaveReqVO|TenantPackageSaveReqVO|
|&emsp;&emsp;id|套餐编号||false|integer(int64)||
|&emsp;&emsp;name|套餐名||true|string||
|&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举||true|integer(int32)||
|&emsp;&emsp;remark|备注||false|string||
|&emsp;&emsp;menuIds|关联的菜单编号||true|array|integer(int64)|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 删除租户套餐


**接口地址**:`/admin-api/system/tenant-package/delete`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 批量删除租户套餐


**接口地址**:`/admin-api/system/tenant-package/delete-list`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|ids|编号列表|query|true|array|integer|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 创建租户套餐


**接口地址**:`/admin-api/system/tenant-package/create`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "name": "VIP",
  "status": 1,
  "remark": "好",
  "menuIds": []
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenantPackageSaveReqVO|管理后台 - 租户套餐创建/修改 Request VO|body|true|TenantPackageSaveReqVO|TenantPackageSaveReqVO|
|&emsp;&emsp;id|套餐编号||false|integer(int64)||
|&emsp;&emsp;name|套餐名||true|string||
|&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举||true|integer(int32)||
|&emsp;&emsp;remark|备注||false|string||
|&emsp;&emsp;menuIds|关联的菜单编号||true|array|integer(int64)|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultLong|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": 0
}
```


## 获得租户套餐分页


**接口地址**:`/admin-api/system/tenant-package/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|name|套餐名|query|false|string||
|status|状态|query|false|integer(int32)||
|remark|备注|query|false|string||
|createTime|创建时间|query|false|array|string|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultPageResultTenantPackageRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||PageResultTenantPackageRespVO|PageResultTenantPackageRespVO|
|&emsp;&emsp;total|总量|integer(int64)||
|&emsp;&emsp;list|数据|array|TenantPackageRespVO|
|&emsp;&emsp;&emsp;&emsp;id|套餐编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;name|套餐名|string||
|&emsp;&emsp;&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;&emsp;&emsp;menuIds|关联的菜单编号|array|integer|
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 1024,
				"name": "VIP",
				"status": 1,
				"remark": "好",
				"menuIds": [],
				"createTime": ""
			}
		]
	}
}
```


## 获得租户套餐


**接口地址**:`/admin-api/system/tenant-package/get`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultTenantPackageRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||TenantPackageRespVO|TenantPackageRespVO|
|&emsp;&emsp;id|套餐编号|integer(int64)||
|&emsp;&emsp;name|套餐名|string||
|&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举|integer(int32)||
|&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;menuIds|关联的菜单编号|array|integer(int64)|
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 1024,
		"name": "VIP",
		"status": 1,
		"remark": "好",
		"menuIds": [],
		"createTime": ""
	}
}
```


## 获取租户套餐精简信息列表


**接口地址**:`/admin-api/system/tenant-package/get-simple-list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>只包含被开启的租户套餐，主要用于前端的下拉选项</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListTenantPackageSimpleRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|TenantPackageSimpleRespVO|
|&emsp;&emsp;id|套餐编号|integer(int64)||
|&emsp;&emsp;name|套餐名|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 1024,
			"name": "VIP"
		}
	]
}
```


## 获取租户套餐精简信息列表


**接口地址**:`/admin-api/system/tenant-package/simple-list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>只包含被开启的租户套餐，主要用于前端的下拉选项</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListTenantPackageSimpleRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|TenantPackageSimpleRespVO|
|&emsp;&emsp;id|套餐编号|integer(int64)||
|&emsp;&emsp;name|套餐名|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 1024,
			"name": "VIP"
		}
	]
}
```


# 管理后台 - 社交用户


## 取消社交绑定


**接口地址**:`/admin-api/system/social-user/unbind`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "type": 10,
  "openid": "IPRmJ0wvBptiPIlGEZiPewGwiEiE"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|socialUserUnbindReqVO|管理后台 - 取消社交绑定 Request VO|body|true|SocialUserUnbindReqVO|SocialUserUnbindReqVO|
|&emsp;&emsp;type|社交平台的类型，参见 UserSocialTypeEnum 枚举值||true|integer(int32)||
|&emsp;&emsp;openid|社交用户的 openid||true|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 社交绑定，使用 code 授权码


**接口地址**:`/admin-api/system/social-user/bind`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "type": 10,
  "code": 1024,
  "state": "9b2ffbc1-7425-4155-9894-9d5c08541d62"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|socialUserBindReqVO|管理后台 - 社交绑定 Request VO，使用 code 授权码|body|true|SocialUserBindReqVO|SocialUserBindReqVO|
|&emsp;&emsp;type|社交平台的类型，参见 UserSocialTypeEnum 枚举值||true|integer(int32)||
|&emsp;&emsp;code|授权码||true|string||
|&emsp;&emsp;state|state||true|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 获得社交用户分页


**接口地址**:`/admin-api/system/social-user/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|type|社交平台的类型|query|false|integer(int32)||
|nickname|用户昵称|query|false|string||
|openid|社交 openid|query|false|string||
|createTime|创建时间|query|false|array|string|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultPageResultSocialUserRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||PageResultSocialUserRespVO|PageResultSocialUserRespVO|
|&emsp;&emsp;total|总量|integer(int64)||
|&emsp;&emsp;list|数据|array|SocialUserRespVO|
|&emsp;&emsp;&emsp;&emsp;id|主键(自增策略)|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;type|社交平台的类型|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;openid|社交 openid|string||
|&emsp;&emsp;&emsp;&emsp;token|社交 token|string||
|&emsp;&emsp;&emsp;&emsp;rawTokenInfo|原始 Token 数据，一般是 JSON 格式|string||
|&emsp;&emsp;&emsp;&emsp;nickname|用户昵称|string||
|&emsp;&emsp;&emsp;&emsp;avatar|用户头像|string||
|&emsp;&emsp;&emsp;&emsp;rawUserInfo|原始用户数据，一般是 JSON 格式|string||
|&emsp;&emsp;&emsp;&emsp;code|最后一次的认证 code|string||
|&emsp;&emsp;&emsp;&emsp;state|最后一次的认证 state|string||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string(date-time)||
|&emsp;&emsp;&emsp;&emsp;updateTime|更新时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 14569,
				"type": 30,
				"openid": 666,
				"token": 666,
				"rawTokenInfo": "{}",
				"nickname": "wmt",
				"avatar": "https://www.wmt.cn/xxx.png",
				"rawUserInfo": "{}",
				"code": 666666,
				"state": 123456,
				"createTime": "",
				"updateTime": ""
			}
		]
	}
}
```


## 获得社交用户


**接口地址**:`/admin-api/system/social-user/get`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultSocialUserRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||SocialUserRespVO|SocialUserRespVO|
|&emsp;&emsp;id|主键(自增策略)|integer(int64)||
|&emsp;&emsp;type|社交平台的类型|integer(int32)||
|&emsp;&emsp;openid|社交 openid|string||
|&emsp;&emsp;token|社交 token|string||
|&emsp;&emsp;rawTokenInfo|原始 Token 数据，一般是 JSON 格式|string||
|&emsp;&emsp;nickname|用户昵称|string||
|&emsp;&emsp;avatar|用户头像|string||
|&emsp;&emsp;rawUserInfo|原始用户数据，一般是 JSON 格式|string||
|&emsp;&emsp;code|最后一次的认证 code|string||
|&emsp;&emsp;state|最后一次的认证 state|string||
|&emsp;&emsp;createTime|创建时间|string(date-time)||
|&emsp;&emsp;updateTime|更新时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 14569,
		"type": 30,
		"openid": 666,
		"token": 666,
		"rawTokenInfo": "{}",
		"nickname": "wmt",
		"avatar": "https://www.wmt.cn/xxx.png",
		"rawUserInfo": "{}",
		"code": 666666,
		"state": 123456,
		"createTime": "",
		"updateTime": ""
	}
}
```


## 获得绑定社交用户列表


**接口地址**:`/admin-api/system/social-user/get-bind-list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListSocialUserRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|SocialUserRespVO|
|&emsp;&emsp;id|主键(自增策略)|integer(int64)||
|&emsp;&emsp;type|社交平台的类型|integer(int32)||
|&emsp;&emsp;openid|社交 openid|string||
|&emsp;&emsp;token|社交 token|string||
|&emsp;&emsp;rawTokenInfo|原始 Token 数据，一般是 JSON 格式|string||
|&emsp;&emsp;nickname|用户昵称|string||
|&emsp;&emsp;avatar|用户头像|string||
|&emsp;&emsp;rawUserInfo|原始用户数据，一般是 JSON 格式|string||
|&emsp;&emsp;code|最后一次的认证 code|string||
|&emsp;&emsp;state|最后一次的认证 state|string||
|&emsp;&emsp;createTime|创建时间|string(date-time)||
|&emsp;&emsp;updateTime|更新时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 14569,
			"type": 30,
			"openid": 666,
			"token": 666,
			"rawTokenInfo": "{}",
			"nickname": "wmt",
			"avatar": "https://www.wmt.cn/xxx.png",
			"rawUserInfo": "{}",
			"code": 666666,
			"state": 123456,
			"createTime": "",
			"updateTime": ""
		}
	]
}
```


# 管理后台 - 社交客户端


## 更新社交客户端


**接口地址**:`/admin-api/system/social-client/update`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 27162,
  "name": "wmt商城",
  "socialType": 31,
  "userType": 2,
  "clientId": "wwd411c69a39ad2e54",
  "clientSecret": "peter",
  "agentId": 2000045,
  "publicKey": 2000045,
  "status": 1
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|socialClientSaveReqVO|管理后台 - 社交客户端创建/修改 Request VO|body|true|SocialClientSaveReqVO|SocialClientSaveReqVO|
|&emsp;&emsp;id|编号||false|integer(int64)||
|&emsp;&emsp;name|应用名||true|string||
|&emsp;&emsp;socialType|社交平台的类型||true|integer(int32)||
|&emsp;&emsp;userType|用户类型||true|integer(int32)||
|&emsp;&emsp;clientId|客户端编号||true|string||
|&emsp;&emsp;clientSecret|客户端密钥||true|string||
|&emsp;&emsp;agentId|授权方的网页应用编号||true|string||
|&emsp;&emsp;publicKey|publicKey 公钥||false|string||
|&emsp;&emsp;status|状态||true|integer(int32)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 发送订阅消息


**接口地址**:`/admin-api/system/social-client/send-subscribe-message`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "userId": 0,
  "userType": 0,
  "templateTitle": "",
  "page": "",
  "messages": {}
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|socialWxaSubscribeMessageSendReqDTO|SocialWxaSubscribeMessageSendReqDTO|body|true|SocialWxaSubscribeMessageSendReqDTO|SocialWxaSubscribeMessageSendReqDTO|
|&emsp;&emsp;userId|||true|integer(int64)||
|&emsp;&emsp;userType|||true|integer(int32)||
|&emsp;&emsp;templateTitle|||true|string||
|&emsp;&emsp;page|||false|string||
|&emsp;&emsp;messages|||false|object||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||


**响应参数**:


暂无


**响应示例**:
```javascript

```


## 删除社交客户端


**接口地址**:`/admin-api/system/social-client/delete`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 批量删除社交客户端


**接口地址**:`/admin-api/system/social-client/delete-list`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|ids|编号列表|query|true|array|integer|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 创建社交客户端


**接口地址**:`/admin-api/system/social-client/create`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 27162,
  "name": "wmt商城",
  "socialType": 31,
  "userType": 2,
  "clientId": "wwd411c69a39ad2e54",
  "clientSecret": "peter",
  "agentId": 2000045,
  "publicKey": 2000045,
  "status": 1
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|socialClientSaveReqVO|管理后台 - 社交客户端创建/修改 Request VO|body|true|SocialClientSaveReqVO|SocialClientSaveReqVO|
|&emsp;&emsp;id|编号||false|integer(int64)||
|&emsp;&emsp;name|应用名||true|string||
|&emsp;&emsp;socialType|社交平台的类型||true|integer(int32)||
|&emsp;&emsp;userType|用户类型||true|integer(int32)||
|&emsp;&emsp;clientId|客户端编号||true|string||
|&emsp;&emsp;clientSecret|客户端密钥||true|string||
|&emsp;&emsp;agentId|授权方的网页应用编号||true|string||
|&emsp;&emsp;publicKey|publicKey 公钥||false|string||
|&emsp;&emsp;status|状态||true|integer(int32)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultLong|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": 0
}
```


## 获得社交客户端分页


**接口地址**:`/admin-api/system/social-client/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|name|应用名|query|false|string||
|socialType|社交平台的类型|query|false|integer(int32)||
|userType|用户类型|query|false|integer(int32)||
|clientId|客户端编号|query|false|string||
|status|状态|query|false|integer(int32)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultPageResultSocialClientRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||PageResultSocialClientRespVO|PageResultSocialClientRespVO|
|&emsp;&emsp;total|总量|integer(int64)||
|&emsp;&emsp;list|数据|array|SocialClientRespVO|
|&emsp;&emsp;&emsp;&emsp;id|编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;name|应用名|string||
|&emsp;&emsp;&emsp;&emsp;socialType|社交平台的类型|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;userType|用户类型|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;clientId|客户端编号|string||
|&emsp;&emsp;&emsp;&emsp;clientSecret|客户端密钥|string||
|&emsp;&emsp;&emsp;&emsp;agentId|授权方的网页应用编号|string||
|&emsp;&emsp;&emsp;&emsp;publicKey|publicKey 公钥|string||
|&emsp;&emsp;&emsp;&emsp;status|状态|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 27162,
				"name": "wmt商城",
				"socialType": 31,
				"userType": 2,
				"clientId": "wwd411c69a39ad2e54",
				"clientSecret": "peter",
				"agentId": 2000045,
				"publicKey": 2000045,
				"status": 1,
				"createTime": ""
			}
		]
	}
}
```


## 获得社交客户端


**接口地址**:`/admin-api/system/social-client/get`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultSocialClientRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||SocialClientRespVO|SocialClientRespVO|
|&emsp;&emsp;id|编号|integer(int64)||
|&emsp;&emsp;name|应用名|string||
|&emsp;&emsp;socialType|社交平台的类型|integer(int32)||
|&emsp;&emsp;userType|用户类型|integer(int32)||
|&emsp;&emsp;clientId|客户端编号|string||
|&emsp;&emsp;clientSecret|客户端密钥|string||
|&emsp;&emsp;agentId|授权方的网页应用编号|string||
|&emsp;&emsp;publicKey|publicKey 公钥|string||
|&emsp;&emsp;status|状态|integer(int32)||
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 27162,
		"name": "wmt商城",
		"socialType": 31,
		"userType": 2,
		"clientId": "wwd411c69a39ad2e54",
		"clientSecret": "peter",
		"agentId": 2000045,
		"publicKey": 2000045,
		"status": 1,
		"createTime": ""
	}
}
```


# 管理后台 - 短信回调


## 腾讯云短信的回调


**接口地址**:`/admin-api/system/sms/callback/tencent`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>参见 <a href="https://cloud.tencent.com/document/product/382/52077">https://cloud.tencent.com/document/product/382/52077</a> 文档</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 七牛云短信的回调


**接口地址**:`/admin-api/system/sms/callback/qiniu`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>参见 <a href="https://developer.qiniu.com/sms/5910/message-push">https://developer.qiniu.com/sms/5910/message-push</a> 文档</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 华为云短信的回调


**接口地址**:`/admin-api/system/sms/callback/huawei`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>参见 <a href="https://support.huaweicloud.com/api-msgsms/sms_05_0003.html">https://support.huaweicloud.com/api-msgsms/sms_05_0003.html</a> 文档</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 阿里云短信的回调


**接口地址**:`/admin-api/system/sms/callback/aliyun`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>参见 <a href="https://help.aliyun.com/document_detail/120998.html">https://help.aliyun.com/document_detail/120998.html</a> 文档</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


# 管理后台 - 短信模板


## 更新短信模板


**接口地址**:`/admin-api/system/sms-template/update`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "type": 1,
  "status": 1,
  "code": "test_01",
  "name": "wmt",
  "content": "你好，{name}。你长的太{like}啦！",
  "remark": "哈哈哈",
  "apiTemplateId": 4383920,
  "channelId": 10
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|smsTemplateSaveReqVO|管理后台 - 短信模板创建/修改 Request VO|body|true|SmsTemplateSaveReqVO|SmsTemplateSaveReqVO|
|&emsp;&emsp;id|编号||false|integer(int64)||
|&emsp;&emsp;type|短信类型，参见 SmsTemplateTypeEnum 枚举类||true|integer(int32)||
|&emsp;&emsp;status|开启状态，参见 CommonStatusEnum 枚举类||true|integer(int32)||
|&emsp;&emsp;code|模板编码||true|string||
|&emsp;&emsp;name|模板名称||true|string||
|&emsp;&emsp;content|模板内容||true|string||
|&emsp;&emsp;remark|备注||false|string||
|&emsp;&emsp;apiTemplateId|短信 API 的模板编号||true|string||
|&emsp;&emsp;channelId|短信渠道编号||true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 发送短信


**接口地址**:`/admin-api/system/sms-template/send-sms`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "mobile": 15601691300,
  "templateCode": "test_01",
  "templateParams": {}
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|smsTemplateSendReqVO|管理后台 - 短信模板的发送 Request VO|body|true|SmsTemplateSendReqVO|SmsTemplateSendReqVO|
|&emsp;&emsp;mobile|手机号||true|string||
|&emsp;&emsp;templateCode|模板编码||true|string||
|&emsp;&emsp;templateParams|模板参数||false|object||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultLong|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": 0
}
```


## 删除短信模板


**接口地址**:`/admin-api/system/sms-template/delete`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 批量删除短信模板


**接口地址**:`/admin-api/system/sms-template/delete-list`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|ids|编号列表|query|true|array|integer|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 创建短信模板


**接口地址**:`/admin-api/system/sms-template/create`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "type": 1,
  "status": 1,
  "code": "test_01",
  "name": "wmt",
  "content": "你好，{name}。你长的太{like}啦！",
  "remark": "哈哈哈",
  "apiTemplateId": 4383920,
  "channelId": 10
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|smsTemplateSaveReqVO|管理后台 - 短信模板创建/修改 Request VO|body|true|SmsTemplateSaveReqVO|SmsTemplateSaveReqVO|
|&emsp;&emsp;id|编号||false|integer(int64)||
|&emsp;&emsp;type|短信类型，参见 SmsTemplateTypeEnum 枚举类||true|integer(int32)||
|&emsp;&emsp;status|开启状态，参见 CommonStatusEnum 枚举类||true|integer(int32)||
|&emsp;&emsp;code|模板编码||true|string||
|&emsp;&emsp;name|模板名称||true|string||
|&emsp;&emsp;content|模板内容||true|string||
|&emsp;&emsp;remark|备注||false|string||
|&emsp;&emsp;apiTemplateId|短信 API 的模板编号||true|string||
|&emsp;&emsp;channelId|短信渠道编号||true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultLong|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": 0
}
```


## 获得短信模板分页


**接口地址**:`/admin-api/system/sms-template/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|type|短信签名|query|false|integer(int32)||
|status|开启状态|query|false|integer(int32)||
|code|模板编码，模糊匹配|query|false|string||
|content|模板内容，模糊匹配|query|false|string||
|apiTemplateId|短信 API 的模板编号，模糊匹配|query|false|string||
|channelId|短信渠道编号|query|false|integer(int64)||
|createTime|创建时间|query|false|array|string|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultPageResultSmsTemplateRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||PageResultSmsTemplateRespVO|PageResultSmsTemplateRespVO|
|&emsp;&emsp;total|总量|integer(int64)||
|&emsp;&emsp;list|数据|array|SmsTemplateRespVO|
|&emsp;&emsp;&emsp;&emsp;id|编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;type|短信类型，参见 SmsTemplateTypeEnum 枚举类|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;status|开启状态，参见 CommonStatusEnum 枚举类|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;code|模板编码|string||
|&emsp;&emsp;&emsp;&emsp;name|模板名称|string||
|&emsp;&emsp;&emsp;&emsp;content|模板内容|string||
|&emsp;&emsp;&emsp;&emsp;params|参数数组|array|string|
|&emsp;&emsp;&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;&emsp;&emsp;apiTemplateId|短信 API 的模板编号|string||
|&emsp;&emsp;&emsp;&emsp;channelId|短信渠道编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;channelCode|短信渠道编码|string||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 1024,
				"type": 1,
				"status": 1,
				"code": "test_01",
				"name": "wmt",
				"content": "你好，{name}。你长的太{like}啦！",
				"params": "name,code",
				"remark": "哈哈哈",
				"apiTemplateId": 4383920,
				"channelId": 10,
				"channelCode": "ALIYUN",
				"createTime": ""
			}
		]
	}
}
```


## 获得短信模板


**接口地址**:`/admin-api/system/sms-template/get`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultSmsTemplateRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||SmsTemplateRespVO|SmsTemplateRespVO|
|&emsp;&emsp;id|编号|integer(int64)||
|&emsp;&emsp;type|短信类型，参见 SmsTemplateTypeEnum 枚举类|integer(int32)||
|&emsp;&emsp;status|开启状态，参见 CommonStatusEnum 枚举类|integer(int32)||
|&emsp;&emsp;code|模板编码|string||
|&emsp;&emsp;name|模板名称|string||
|&emsp;&emsp;content|模板内容|string||
|&emsp;&emsp;params|参数数组|array|string|
|&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;apiTemplateId|短信 API 的模板编号|string||
|&emsp;&emsp;channelId|短信渠道编号|integer(int64)||
|&emsp;&emsp;channelCode|短信渠道编码|string||
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 1024,
		"type": 1,
		"status": 1,
		"code": "test_01",
		"name": "wmt",
		"content": "你好，{name}。你长的太{like}啦！",
		"params": "name,code",
		"remark": "哈哈哈",
		"apiTemplateId": 4383920,
		"channelId": 10,
		"channelCode": "ALIYUN",
		"createTime": ""
	}
}
```


## 导出短信模板 Excel


**接口地址**:`/admin-api/system/sms-template/export-excel`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|type|短信签名|query|false|integer(int32)||
|status|开启状态|query|false|integer(int32)||
|code|模板编码，模糊匹配|query|false|string||
|content|模板内容，模糊匹配|query|false|string||
|apiTemplateId|短信 API 的模板编号，模糊匹配|query|false|string||
|channelId|短信渠道编号|query|false|integer(int64)||
|createTime|创建时间|query|false|array|string|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||


**响应参数**:


暂无


**响应示例**:
```javascript

```


# 管理后台 - 短信渠道


## 更新短信渠道


**接口地址**:`/admin-api/system/sms-channel/update`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "signature": "AHC源码",
  "code": "YUN_PIAN",
  "status": 1,
  "remark": "好吃！",
  "apiKey": "wmt",
  "apiSecret": "yuanma",
  "callbackUrl": "http://www.wmt.cn"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|smsChannelSaveReqVO|管理后台 - 短信渠道创建/修改 Request VO|body|true|SmsChannelSaveReqVO|SmsChannelSaveReqVO|
|&emsp;&emsp;id|编号||false|integer(int64)||
|&emsp;&emsp;signature|短信签名||true|string||
|&emsp;&emsp;code|渠道编码，参见 SmsChannelEnum 枚举类||true|string||
|&emsp;&emsp;status|启用状态||true|integer(int32)||
|&emsp;&emsp;remark|备注||false|string||
|&emsp;&emsp;apiKey|短信 API 的账号||true|string||
|&emsp;&emsp;apiSecret|短信 API 的密钥||false|string||
|&emsp;&emsp;callbackUrl|短信发送回调 URL||false|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 删除短信渠道


**接口地址**:`/admin-api/system/sms-channel/delete`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 批量删除短信渠道


**接口地址**:`/admin-api/system/sms-channel/delete-list`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|ids|编号列表|query|true|array|integer|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 创建短信渠道


**接口地址**:`/admin-api/system/sms-channel/create`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "signature": "AHC源码",
  "code": "YUN_PIAN",
  "status": 1,
  "remark": "好吃！",
  "apiKey": "wmt",
  "apiSecret": "yuanma",
  "callbackUrl": "http://www.wmt.cn"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|smsChannelSaveReqVO|管理后台 - 短信渠道创建/修改 Request VO|body|true|SmsChannelSaveReqVO|SmsChannelSaveReqVO|
|&emsp;&emsp;id|编号||false|integer(int64)||
|&emsp;&emsp;signature|短信签名||true|string||
|&emsp;&emsp;code|渠道编码，参见 SmsChannelEnum 枚举类||true|string||
|&emsp;&emsp;status|启用状态||true|integer(int32)||
|&emsp;&emsp;remark|备注||false|string||
|&emsp;&emsp;apiKey|短信 API 的账号||true|string||
|&emsp;&emsp;apiSecret|短信 API 的密钥||false|string||
|&emsp;&emsp;callbackUrl|短信发送回调 URL||false|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultLong|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": 0
}
```


## 获得短信渠道分页


**接口地址**:`/admin-api/system/sms-channel/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|status|任务状态|query|false|integer(int32)||
|signature|短信签名，模糊匹配|query|false|string||
|createTime|创建时间|query|false|array|string|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultPageResultSmsChannelRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||PageResultSmsChannelRespVO|PageResultSmsChannelRespVO|
|&emsp;&emsp;total|总量|integer(int64)||
|&emsp;&emsp;list|数据|array|SmsChannelRespVO|
|&emsp;&emsp;&emsp;&emsp;id|编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;signature|短信签名|string||
|&emsp;&emsp;&emsp;&emsp;code|渠道编码，参见 SmsChannelEnum 枚举类|string||
|&emsp;&emsp;&emsp;&emsp;status|启用状态|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;&emsp;&emsp;apiKey|短信 API 的账号|string||
|&emsp;&emsp;&emsp;&emsp;apiSecret|短信 API 的密钥|string||
|&emsp;&emsp;&emsp;&emsp;callbackUrl|短信发送回调 URL|string||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 1024,
				"signature": "AHC源码",
				"code": "YUN_PIAN",
				"status": 1,
				"remark": "好吃！",
				"apiKey": "wmt",
				"apiSecret": "yuanma",
				"callbackUrl": "https://www.wmt.cn",
				"createTime": ""
			}
		]
	}
}
```


## 获得短信渠道精简列表


**接口地址**:`/admin-api/system/sms-channel/simple-list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>包含被禁用的短信渠道</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListSmsChannelSimpleRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|SmsChannelSimpleRespVO|
|&emsp;&emsp;id|编号|integer(int64)||
|&emsp;&emsp;signature|短信签名|string||
|&emsp;&emsp;code|渠道编码，参见 SmsChannelEnum 枚举类|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 1024,
			"signature": "AHC源码",
			"code": "YUN_PIAN"
		}
	]
}
```


## 获得短信渠道精简列表


**接口地址**:`/admin-api/system/sms-channel/list-all-simple`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>包含被禁用的短信渠道</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListSmsChannelSimpleRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|SmsChannelSimpleRespVO|
|&emsp;&emsp;id|编号|integer(int64)||
|&emsp;&emsp;signature|短信签名|string||
|&emsp;&emsp;code|渠道编码，参见 SmsChannelEnum 枚举类|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 1024,
			"signature": "AHC源码",
			"code": "YUN_PIAN"
		}
	]
}
```


## 获得短信渠道


**接口地址**:`/admin-api/system/sms-channel/get`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultSmsChannelRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||SmsChannelRespVO|SmsChannelRespVO|
|&emsp;&emsp;id|编号|integer(int64)||
|&emsp;&emsp;signature|短信签名|string||
|&emsp;&emsp;code|渠道编码，参见 SmsChannelEnum 枚举类|string||
|&emsp;&emsp;status|启用状态|integer(int32)||
|&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;apiKey|短信 API 的账号|string||
|&emsp;&emsp;apiSecret|短信 API 的密钥|string||
|&emsp;&emsp;callbackUrl|短信发送回调 URL|string||
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 1024,
		"signature": "AHC源码",
		"code": "YUN_PIAN",
		"status": 1,
		"remark": "好吃！",
		"apiKey": "wmt",
		"apiSecret": "yuanma",
		"callbackUrl": "https://www.wmt.cn",
		"createTime": ""
	}
}
```


# 管理后台 - 角色


## 修改角色


**接口地址**:`/admin-api/system/role/update`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1,
  "name": "管理员",
  "code": "ADMIN",
  "sort": 1024,
  "status": 0,
  "remark": "我是一个角色"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|roleSaveReqVO|管理后台 - 角色创建/更新 Request VO|body|true|RoleSaveReqVO|RoleSaveReqVO|
|&emsp;&emsp;id|角色编号||false|integer(int64)||
|&emsp;&emsp;name|角色名称||true|string||
|&emsp;&emsp;code|角色标志||true|string||
|&emsp;&emsp;sort|显示顺序||true|integer(int32)||
|&emsp;&emsp;status|状态||true|integer(int32)||
|&emsp;&emsp;remark|备注||false|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 删除角色


**接口地址**:`/admin-api/system/role/delete`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|角色编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 批量删除角色


**接口地址**:`/admin-api/system/role/delete-list`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|ids|编号列表|query|true|array|integer|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 创建角色


**接口地址**:`/admin-api/system/role/create`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1,
  "name": "管理员",
  "code": "ADMIN",
  "sort": 1024,
  "status": 0,
  "remark": "我是一个角色"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|roleSaveReqVO|管理后台 - 角色创建/更新 Request VO|body|true|RoleSaveReqVO|RoleSaveReqVO|
|&emsp;&emsp;id|角色编号||false|integer(int64)||
|&emsp;&emsp;name|角色名称||true|string||
|&emsp;&emsp;code|角色标志||true|string||
|&emsp;&emsp;sort|显示顺序||true|integer(int32)||
|&emsp;&emsp;status|状态||true|integer(int32)||
|&emsp;&emsp;remark|备注||false|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultLong|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": 0
}
```


## 获得角色分页


**接口地址**:`/admin-api/system/role/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|name|角色名称，模糊匹配|query|false|string||
|code|角色标识，模糊匹配|query|false|string||
|status|展示状态，参见 CommonStatusEnum 枚举类|query|false|integer(int32)||
|createTime|创建时间|query|false|array|string|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultPageResultRoleRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||PageResultRoleRespVO|PageResultRoleRespVO|
|&emsp;&emsp;total|总量|integer(int64)||
|&emsp;&emsp;list|数据|array|RoleRespVO|
|&emsp;&emsp;&emsp;&emsp;id|角色编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;name|角色名称|string||
|&emsp;&emsp;&emsp;&emsp;code|角色标志|string||
|&emsp;&emsp;&emsp;&emsp;sort|显示顺序|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举类|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;type|角色类型，参见 RoleTypeEnum 枚举类|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;&emsp;&emsp;dataScope|数据范围，参见 DataScopeEnum 枚举类|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;dataScopeDeptIds|数据范围(指定部门数组)|array|integer|
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 1,
				"name": "管理员",
				"code": "admin",
				"sort": 1024,
				"status": 1,
				"type": 1,
				"remark": "我是一个角色",
				"dataScope": 1,
				"dataScopeDeptIds": 1,
				"createTime": "时间戳格式"
			}
		]
	}
}
```


## 根据用户ID获取角色列表


**接口地址**:`/admin-api/system/role/list-by-user-id`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListRoleRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|RoleRespVO|
|&emsp;&emsp;id|角色编号|integer(int64)||
|&emsp;&emsp;name|角色名称|string||
|&emsp;&emsp;code|角色标志|string||
|&emsp;&emsp;sort|显示顺序|integer(int32)||
|&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举类|integer(int32)||
|&emsp;&emsp;type|角色类型，参见 RoleTypeEnum 枚举类|integer(int32)||
|&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;dataScope|数据范围，参见 DataScopeEnum 枚举类|integer(int32)||
|&emsp;&emsp;dataScopeDeptIds|数据范围(指定部门数组)|array|integer(int64)|
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 1,
			"name": "管理员",
			"code": "admin",
			"sort": 1024,
			"status": 1,
			"type": 1,
			"remark": "我是一个角色",
			"dataScope": 1,
			"dataScopeDeptIds": 1,
			"createTime": "时间戳格式"
		}
	]
}
```


## 获取角色精简信息列表


**接口地址**:`/admin-api/system/role/list-all-simple`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>只包含被开启的角色，主要用于前端的下拉选项</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListRoleRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|RoleRespVO|
|&emsp;&emsp;id|角色编号|integer(int64)||
|&emsp;&emsp;name|角色名称|string||
|&emsp;&emsp;code|角色标志|string||
|&emsp;&emsp;sort|显示顺序|integer(int32)||
|&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举类|integer(int32)||
|&emsp;&emsp;type|角色类型，参见 RoleTypeEnum 枚举类|integer(int32)||
|&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;dataScope|数据范围，参见 DataScopeEnum 枚举类|integer(int32)||
|&emsp;&emsp;dataScopeDeptIds|数据范围(指定部门数组)|array|integer(int64)|
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 1,
			"name": "管理员",
			"code": "admin",
			"sort": 1024,
			"status": 1,
			"type": 1,
			"remark": "我是一个角色",
			"dataScope": 1,
			"dataScopeDeptIds": 1,
			"createTime": "时间戳格式"
		}
	]
}
```


## 获取角色精简信息列表


**接口地址**:`/admin-api/system/role/simple-list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>只包含被开启的角色，主要用于前端的下拉选项</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListRoleRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|RoleRespVO|
|&emsp;&emsp;id|角色编号|integer(int64)||
|&emsp;&emsp;name|角色名称|string||
|&emsp;&emsp;code|角色标志|string||
|&emsp;&emsp;sort|显示顺序|integer(int32)||
|&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举类|integer(int32)||
|&emsp;&emsp;type|角色类型，参见 RoleTypeEnum 枚举类|integer(int32)||
|&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;dataScope|数据范围，参见 DataScopeEnum 枚举类|integer(int32)||
|&emsp;&emsp;dataScopeDeptIds|数据范围(指定部门数组)|array|integer(int64)|
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 1,
			"name": "管理员",
			"code": "admin",
			"sort": 1024,
			"status": 1,
			"type": 1,
			"remark": "我是一个角色",
			"dataScope": 1,
			"dataScopeDeptIds": 1,
			"createTime": "时间戳格式"
		}
	]
}
```


## 获得角色信息


**接口地址**:`/admin-api/system/role/get`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id||query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultRoleRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||RoleRespVO|RoleRespVO|
|&emsp;&emsp;id|角色编号|integer(int64)||
|&emsp;&emsp;name|角色名称|string||
|&emsp;&emsp;code|角色标志|string||
|&emsp;&emsp;sort|显示顺序|integer(int32)||
|&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举类|integer(int32)||
|&emsp;&emsp;type|角色类型，参见 RoleTypeEnum 枚举类|integer(int32)||
|&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;dataScope|数据范围，参见 DataScopeEnum 枚举类|integer(int32)||
|&emsp;&emsp;dataScopeDeptIds|数据范围(指定部门数组)|array|integer(int64)|
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 1,
		"name": "管理员",
		"code": "admin",
		"sort": 1024,
		"status": 1,
		"type": 1,
		"remark": "我是一个角色",
		"dataScope": 1,
		"dataScopeDeptIds": 1,
		"createTime": "时间戳格式"
	}
}
```


## 导出角色 Excel


**接口地址**:`/admin-api/system/role/export-excel`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|name|角色名称，模糊匹配|query|false|string||
|code|角色标识，模糊匹配|query|false|string||
|status|展示状态，参见 CommonStatusEnum 枚举类|query|false|integer(int32)||
|createTime|创建时间|query|false|array|string|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||


**响应参数**:


暂无


**响应示例**:
```javascript

```


# 管理后台 - 岗位


## 修改岗位


**接口地址**:`/admin-api/system/post/update`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "name": "小土豆",
  "code": "wmt",
  "sort": 1024,
  "status": 1,
  "remark": "快乐的备注"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|postSaveReqVO|管理后台 - 岗位创建/修改 Request VO|body|true|PostSaveReqVO|PostSaveReqVO|
|&emsp;&emsp;id|岗位编号||false|integer(int64)||
|&emsp;&emsp;name|岗位名称||true|string||
|&emsp;&emsp;code|岗位编码||true|string||
|&emsp;&emsp;sort|显示顺序||true|integer(int32)||
|&emsp;&emsp;status|状态||true|integer(int32)||
|&emsp;&emsp;remark|备注||false|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 删除岗位


**接口地址**:`/admin-api/system/post/delete`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id||query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 批量删除岗位


**接口地址**:`/admin-api/system/post/delete-list`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|ids||query|true|array|integer|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 创建岗位


**接口地址**:`/admin-api/system/post/create`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "name": "小土豆",
  "code": "wmt",
  "sort": 1024,
  "status": 1,
  "remark": "快乐的备注"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|postSaveReqVO|管理后台 - 岗位创建/修改 Request VO|body|true|PostSaveReqVO|PostSaveReqVO|
|&emsp;&emsp;id|岗位编号||false|integer(int64)||
|&emsp;&emsp;name|岗位名称||true|string||
|&emsp;&emsp;code|岗位编码||true|string||
|&emsp;&emsp;sort|显示顺序||true|integer(int32)||
|&emsp;&emsp;status|状态||true|integer(int32)||
|&emsp;&emsp;remark|备注||false|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultLong|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": 0
}
```


## 获得岗位分页列表


**接口地址**:`/admin-api/system/post/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|code|岗位编码，模糊匹配|query|false|string||
|name|岗位名称，模糊匹配|query|false|string||
|status|展示状态，参见 CommonStatusEnum 枚举类|query|false|integer(int32)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultPageResultPostRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||PageResultPostRespVO|PageResultPostRespVO|
|&emsp;&emsp;total|总量|integer(int64)||
|&emsp;&emsp;list|数据|array|PostRespVO|
|&emsp;&emsp;&emsp;&emsp;id|岗位序号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;name|岗位名称|string||
|&emsp;&emsp;&emsp;&emsp;code|岗位编码|string||
|&emsp;&emsp;&emsp;&emsp;sort|显示顺序|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举类|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 1024,
				"name": "小土豆",
				"code": "wmt",
				"sort": 1024,
				"status": 1,
				"remark": "快乐的备注",
				"createTime": ""
			}
		]
	}
}
```


## 获取岗位全列表


**接口地址**:`/admin-api/system/post/list-all-simple`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>只包含被开启的岗位，主要用于前端的下拉选项</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListPostSimpleRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|PostSimpleRespVO|
|&emsp;&emsp;id|岗位序号|integer(int64)||
|&emsp;&emsp;name|岗位名称|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 1024,
			"name": "小土豆"
		}
	]
}
```


## 获取岗位全列表


**接口地址**:`/admin-api/system/post/simple-list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>只包含被开启的岗位，主要用于前端的下拉选项</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListPostSimpleRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|PostSimpleRespVO|
|&emsp;&emsp;id|岗位序号|integer(int64)||
|&emsp;&emsp;name|岗位名称|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 1024,
			"name": "小土豆"
		}
	]
}
```


## 获得岗位信息


**接口地址**:`/admin-api/system/post/get`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|岗位编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultPostRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||PostRespVO|PostRespVO|
|&emsp;&emsp;id|岗位序号|integer(int64)||
|&emsp;&emsp;name|岗位名称|string||
|&emsp;&emsp;code|岗位编码|string||
|&emsp;&emsp;sort|显示顺序|integer(int32)||
|&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举类|integer(int32)||
|&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 1024,
		"name": "小土豆",
		"code": "wmt",
		"sort": 1024,
		"status": 1,
		"remark": "快乐的备注",
		"createTime": ""
	}
}
```


## 岗位管理


**接口地址**:`/admin-api/system/post/export-excel`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|code|岗位编码，模糊匹配|query|false|string||
|name|岗位名称，模糊匹配|query|false|string||
|status|展示状态，参见 CommonStatusEnum 枚举类|query|false|integer(int32)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||


**响应参数**:


暂无


**响应示例**:
```javascript

```


# 管理后台 - 权限


## 赋予用户角色


**接口地址**:`/admin-api/system/permission/assign-user-role`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "userId": 1,
  "roleIds": "1,3,5"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|permissionAssignUserRoleReqVO|管理后台 - 赋予用户角色 Request VO|body|true|PermissionAssignUserRoleReqVO|PermissionAssignUserRoleReqVO|
|&emsp;&emsp;userId|用户编号||true|integer(int64)||
|&emsp;&emsp;roleIds|角色编号列表||false|array|integer(int64)|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 赋予角色菜单


**接口地址**:`/admin-api/system/permission/assign-role-menu`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "roleId": 1,
  "menuIds": "1,3,5"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|permissionAssignRoleMenuReqVO|管理后台 - 赋予角色菜单 Request VO|body|true|PermissionAssignRoleMenuReqVO|PermissionAssignRoleMenuReqVO|
|&emsp;&emsp;roleId|角色编号||true|integer(int64)||
|&emsp;&emsp;menuIds|菜单编号列表||false|array|integer(int64)|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 赋予角色数据权限


**接口地址**:`/admin-api/system/permission/assign-role-data-scope`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "roleId": 1,
  "dataScope": 1,
  "dataScopeDeptIds": "1,3,5"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|permissionAssignRoleDataScopeReqVO|管理后台 - 赋予角色数据权限 Request VO|body|true|PermissionAssignRoleDataScopeReqVO|PermissionAssignRoleDataScopeReqVO|
|&emsp;&emsp;roleId|角色编号||true|integer(int64)||
|&emsp;&emsp;dataScope|数据范围，参见 DataScopeEnum 枚举类||true|integer(int32)||
|&emsp;&emsp;dataScopeDeptIds|部门编号列表，只有范围类型为 DEPT_CUSTOM 时，该字段才需要||false|array|integer(int64)|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 获得管理员拥有的角色编号列表


**接口地址**:`/admin-api/system/permission/list-user-roles`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultSetLong|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": []
}
```


## 获得角色拥有的菜单编号


**接口地址**:`/admin-api/system/permission/list-role-menus`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|roleId|角色编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultSetLong|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": []
}
```


# 管理后台 - OAuth2.0 用户


## 更新用户基本信息


**接口地址**:`/admin-api/system/oauth2/user/update`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "nickname": "wmt",
  "email": "wmt@cn",
  "mobile": 15601691300,
  "sex": 1
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|oAuth2UserUpdateReqVO|管理后台 - OAuth2 更新用户基本信息 Request VO|body|true|OAuth2UserUpdateReqVO|OAuth2UserUpdateReqVO|
|&emsp;&emsp;nickname|用户昵称||true|string||
|&emsp;&emsp;email|用户邮箱||false|string(email)||
|&emsp;&emsp;mobile|手机号码||false|string||
|&emsp;&emsp;sex|用户性别，参见 SexEnum 枚举类||false|integer(int32)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 获得用户基本信息


**接口地址**:`/admin-api/system/oauth2/user/get`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultOAuth2UserInfoRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||OAuth2UserInfoRespVO|OAuth2UserInfoRespVO|
|&emsp;&emsp;id|用户编号|integer(int64)||
|&emsp;&emsp;username|用户账号|string||
|&emsp;&emsp;nickname|用户昵称|string||
|&emsp;&emsp;email|用户邮箱|string||
|&emsp;&emsp;mobile|手机号码|string||
|&emsp;&emsp;sex|用户性别，参见 SexEnum 枚举类|integer(int32)||
|&emsp;&emsp;avatar|用户头像|string||
|&emsp;&emsp;dept||Dept|Dept|
|&emsp;&emsp;&emsp;&emsp;id|部门编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;name|部门名称|string||
|&emsp;&emsp;posts||array|Post|
|&emsp;&emsp;&emsp;&emsp;id|岗位编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;name|岗位名称|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 1,
		"username": "wmt",
		"nickname": "AHC",
		"email": "wmt@cn",
		"mobile": 15601691300,
		"sex": 1,
		"avatar": "https://www.wmt.cn/xxx.png",
		"dept": {
			"id": 1,
			"name": "研发部"
		},
		"posts": [
			{
				"id": 1,
				"name": "开发"
			}
		]
	}
}
```


# 管理后台 - OAuth2.0 授权


## 获得访问令牌


**接口地址**:`/admin-api/system/oauth2/token`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>适合 code 授权码模式，或者 implicit 简化模式；在 sso.vue 单点登录界面被【获取】调用</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|grant_type|授权类型|query|true|string||
|code|授权范围|query|false|string||
|redirect_uri|重定向 URI|query|false|string||
|state|状态|query|false|string||
|username||query|false|string||
|password||query|false|string||
|scope||query|false|string||
|refresh_token||query|false|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultOAuth2OpenAccessTokenRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||OAuth2OpenAccessTokenRespVO|OAuth2OpenAccessTokenRespVO|
|&emsp;&emsp;scope|授权范围,如果多个授权范围，使用空格分隔|string||
|&emsp;&emsp;access_token|访问令牌|string||
|&emsp;&emsp;refresh_token|刷新令牌|string||
|&emsp;&emsp;token_type|令牌类型|string||
|&emsp;&emsp;expires_in|过期时间,单位：秒|integer(int64)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"scope": "user_info",
		"access_token": "tudou",
		"refresh_token": "nice",
		"token_type": "bearer",
		"expires_in": 42430
	}
}
```


## 删除访问令牌


**接口地址**:`/admin-api/system/oauth2/delete-token`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|token|访问令牌|query|true|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 校验访问令牌


**接口地址**:`/admin-api/system/oauth2/check-token`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|token|访问令牌|query|true|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultOAuth2OpenCheckTokenRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||OAuth2OpenCheckTokenRespVO|OAuth2OpenCheckTokenRespVO|
|&emsp;&emsp;scopes|授权范围|array|string|
|&emsp;&emsp;exp|过期时间，时间戳 / 1000，即单位：秒|integer(int64)||
|&emsp;&emsp;user_id|用户编号|integer(int64)||
|&emsp;&emsp;user_type|用户类型，参见 UserTypeEnum 枚举|integer(int32)||
|&emsp;&emsp;tenant_id|租户编号|integer(int64)||
|&emsp;&emsp;client_id|客户端编号|string||
|&emsp;&emsp;access_token|访问令牌|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"scopes": "user_info",
		"exp": 1593092157,
		"user_id": 666,
		"user_type": 2,
		"tenant_id": 1024,
		"client_id": "car",
		"access_token": "tudou"
	}
}
```


## 获得授权信息


**接口地址**:`/admin-api/system/oauth2/authorize`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>适合 code 授权码模式，或者 implicit 简化模式；在 sso.vue 单点登录界面被【获取】调用</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|clientId|客户端编号|query|true|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultOAuth2OpenAuthorizeInfoRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||OAuth2OpenAuthorizeInfoRespVO|OAuth2OpenAuthorizeInfoRespVO|
|&emsp;&emsp;client||Client|Client|
|&emsp;&emsp;&emsp;&emsp;name|应用名|string||
|&emsp;&emsp;&emsp;&emsp;logo|应用图标|string||
|&emsp;&emsp;scopes|scope 的选中信息,使用 List 保证有序性，Key 是 scope，Value 为是否选中|array|KeyValueStringBoolean|
|&emsp;&emsp;&emsp;&emsp;key||string||
|&emsp;&emsp;&emsp;&emsp;value||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"client": {
			"name": "土豆",
			"logo": "https://www.wmt.cn/xx.png"
		},
		"scopes": [
			{
				"key": "",
				"value": true
			}
		]
	}
}
```


## 申请授权


**接口地址**:`/admin-api/system/oauth2/authorize`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>适合 code 授权码模式，或者 implicit 简化模式；在 sso.vue 单点登录界面被【提交】调用</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|response_type|响应类型|query|true|string||
|client_id|客户端编号|query|true|string||
|redirect_uri|重定向 URI|query|true|string||
|auto_approve|用户是否接受|query|true|boolean||
|scope|授权范围|query|false|string||
|state||query|false|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


# 管理后台 - OAuth2.0 令牌


## 删除访问令牌


**接口地址**:`/admin-api/system/oauth2-token/delete`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|accessToken|访问令牌|query|true|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 批量删除访问令牌


**接口地址**:`/admin-api/system/oauth2-token/delete-list`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|accessTokens|访问令牌数组|query|true|array|string|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 获得访问令牌分页


**接口地址**:`/admin-api/system/oauth2-token/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>只返回有效期内的</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户编号|query|true|integer(int64)||
|userType|用户类型，参见 UserTypeEnum 枚举|query|true|integer(int32)||
|clientId|客户端编号|query|true|string||
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultPageResultOAuth2AccessTokenRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||PageResultOAuth2AccessTokenRespVO|PageResultOAuth2AccessTokenRespVO|
|&emsp;&emsp;total|总量|integer(int64)||
|&emsp;&emsp;list|数据|array|OAuth2AccessTokenRespVO|
|&emsp;&emsp;&emsp;&emsp;id|编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;accessToken|访问令牌|string||
|&emsp;&emsp;&emsp;&emsp;refreshToken|刷新令牌|string||
|&emsp;&emsp;&emsp;&emsp;userId|用户编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;userType|用户类型，参见 UserTypeEnum 枚举|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;clientId|客户端编号|string||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string(date-time)||
|&emsp;&emsp;&emsp;&emsp;expiresTime|过期时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 1024,
				"accessToken": "tudou",
				"refreshToken": "nice",
				"userId": 666,
				"userType": 2,
				"clientId": 2,
				"createTime": "",
				"expiresTime": ""
			}
		]
	}
}
```


# 管理后台 - OAuth2 客户端


## 更新 OAuth2 客户端


**接口地址**:`/admin-api/system/oauth2-client/update`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "clientId": "tudou",
  "secret": "fan",
  "name": "土豆",
  "logo": "https://www.wmt.cn/xx.png",
  "description": "我是一个应用",
  "status": 1,
  "accessTokenValiditySeconds": 8640,
  "refreshTokenValiditySeconds": 8640000,
  "redirectUris": "https://www.wmt.cn",
  "authorizedGrantTypes": "password",
  "scopes": "user_info",
  "autoApproveScopes": "user_info",
  "authorities": "system:user:query",
  "resourceIds": 1024,
  "additionalInformation": "{yunai: true}",
  "additionalInformationJson": true
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|oAuth2ClientSaveReqVO|管理后台 - OAuth2 客户端创建/修改 Request VO|body|true|OAuth2ClientSaveReqVO|OAuth2ClientSaveReqVO|
|&emsp;&emsp;id|编号||false|integer(int64)||
|&emsp;&emsp;clientId|客户端编号||true|string||
|&emsp;&emsp;secret|客户端密钥||true|string||
|&emsp;&emsp;name|应用名||true|string||
|&emsp;&emsp;logo|应用图标||true|string||
|&emsp;&emsp;description|应用描述||false|string||
|&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举||true|integer(int32)||
|&emsp;&emsp;accessTokenValiditySeconds|访问令牌的有效期||true|integer(int32)||
|&emsp;&emsp;refreshTokenValiditySeconds|刷新令牌的有效期||true|integer(int32)||
|&emsp;&emsp;redirectUris|可重定向的 URI 地址||true|array|string|
|&emsp;&emsp;authorizedGrantTypes|授权类型，参见 OAuth2GrantTypeEnum 枚举||true|array|string|
|&emsp;&emsp;scopes|授权范围||false|array|string|
|&emsp;&emsp;autoApproveScopes|自动通过的授权范围||false|array|string|
|&emsp;&emsp;authorities|权限||false|array|string|
|&emsp;&emsp;resourceIds|资源||false|array|string|
|&emsp;&emsp;additionalInformation|附加信息||false|string||
|&emsp;&emsp;additionalInformationJson|||false|boolean||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 删除 OAuth2 客户端


**接口地址**:`/admin-api/system/oauth2-client/delete`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 批量删除 OAuth2 客户端


**接口地址**:`/admin-api/system/oauth2-client/delete-list`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|ids|编号列表|query|true|array|integer|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 创建 OAuth2 客户端


**接口地址**:`/admin-api/system/oauth2-client/create`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "clientId": "tudou",
  "secret": "fan",
  "name": "土豆",
  "logo": "https://www.wmt.cn/xx.png",
  "description": "我是一个应用",
  "status": 1,
  "accessTokenValiditySeconds": 8640,
  "refreshTokenValiditySeconds": 8640000,
  "redirectUris": "https://www.wmt.cn",
  "authorizedGrantTypes": "password",
  "scopes": "user_info",
  "autoApproveScopes": "user_info",
  "authorities": "system:user:query",
  "resourceIds": 1024,
  "additionalInformation": "{yunai: true}",
  "additionalInformationJson": true
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|oAuth2ClientSaveReqVO|管理后台 - OAuth2 客户端创建/修改 Request VO|body|true|OAuth2ClientSaveReqVO|OAuth2ClientSaveReqVO|
|&emsp;&emsp;id|编号||false|integer(int64)||
|&emsp;&emsp;clientId|客户端编号||true|string||
|&emsp;&emsp;secret|客户端密钥||true|string||
|&emsp;&emsp;name|应用名||true|string||
|&emsp;&emsp;logo|应用图标||true|string||
|&emsp;&emsp;description|应用描述||false|string||
|&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举||true|integer(int32)||
|&emsp;&emsp;accessTokenValiditySeconds|访问令牌的有效期||true|integer(int32)||
|&emsp;&emsp;refreshTokenValiditySeconds|刷新令牌的有效期||true|integer(int32)||
|&emsp;&emsp;redirectUris|可重定向的 URI 地址||true|array|string|
|&emsp;&emsp;authorizedGrantTypes|授权类型，参见 OAuth2GrantTypeEnum 枚举||true|array|string|
|&emsp;&emsp;scopes|授权范围||false|array|string|
|&emsp;&emsp;autoApproveScopes|自动通过的授权范围||false|array|string|
|&emsp;&emsp;authorities|权限||false|array|string|
|&emsp;&emsp;resourceIds|资源||false|array|string|
|&emsp;&emsp;additionalInformation|附加信息||false|string||
|&emsp;&emsp;additionalInformationJson|||false|boolean||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultLong|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": 0
}
```


## 获得 OAuth2 客户端分页


**接口地址**:`/admin-api/system/oauth2-client/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|name|应用名，模糊匹配|query|false|string||
|status|状态，参见 CommonStatusEnum 枚举|query|false|integer(int32)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultPageResultOAuth2ClientRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||PageResultOAuth2ClientRespVO|PageResultOAuth2ClientRespVO|
|&emsp;&emsp;total|总量|integer(int64)||
|&emsp;&emsp;list|数据|array|OAuth2ClientRespVO|
|&emsp;&emsp;&emsp;&emsp;id|编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;clientId|客户端编号|string||
|&emsp;&emsp;&emsp;&emsp;secret|客户端密钥|string||
|&emsp;&emsp;&emsp;&emsp;name|应用名|string||
|&emsp;&emsp;&emsp;&emsp;logo|应用图标|string||
|&emsp;&emsp;&emsp;&emsp;description|应用描述|string||
|&emsp;&emsp;&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;accessTokenValiditySeconds|访问令牌的有效期|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;refreshTokenValiditySeconds|刷新令牌的有效期|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;redirectUris|可重定向的 URI 地址|array|string|
|&emsp;&emsp;&emsp;&emsp;authorizedGrantTypes|授权类型，参见 OAuth2GrantTypeEnum 枚举|array|string|
|&emsp;&emsp;&emsp;&emsp;scopes|授权范围|array|string|
|&emsp;&emsp;&emsp;&emsp;autoApproveScopes|自动通过的授权范围|array|string|
|&emsp;&emsp;&emsp;&emsp;authorities|权限|array|string|
|&emsp;&emsp;&emsp;&emsp;resourceIds|资源|array|string|
|&emsp;&emsp;&emsp;&emsp;additionalInformation|附加信息|string||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 1024,
				"clientId": "tudou",
				"secret": "fan",
				"name": "土豆",
				"logo": "https://www.wmt.cn/xx.png",
				"description": "我是一个应用",
				"status": 1,
				"accessTokenValiditySeconds": 8640,
				"refreshTokenValiditySeconds": 8640000,
				"redirectUris": "https://www.wmt.cn",
				"authorizedGrantTypes": "password",
				"scopes": "user_info",
				"autoApproveScopes": "user_info",
				"authorities": "system:user:query",
				"resourceIds": 1024,
				"additionalInformation": "{yunai: true}",
				"createTime": ""
			}
		]
	}
}
```


## 获得 OAuth2 客户端


**接口地址**:`/admin-api/system/oauth2-client/get`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultOAuth2ClientRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||OAuth2ClientRespVO|OAuth2ClientRespVO|
|&emsp;&emsp;id|编号|integer(int64)||
|&emsp;&emsp;clientId|客户端编号|string||
|&emsp;&emsp;secret|客户端密钥|string||
|&emsp;&emsp;name|应用名|string||
|&emsp;&emsp;logo|应用图标|string||
|&emsp;&emsp;description|应用描述|string||
|&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举|integer(int32)||
|&emsp;&emsp;accessTokenValiditySeconds|访问令牌的有效期|integer(int32)||
|&emsp;&emsp;refreshTokenValiditySeconds|刷新令牌的有效期|integer(int32)||
|&emsp;&emsp;redirectUris|可重定向的 URI 地址|array|string|
|&emsp;&emsp;authorizedGrantTypes|授权类型，参见 OAuth2GrantTypeEnum 枚举|array|string|
|&emsp;&emsp;scopes|授权范围|array|string|
|&emsp;&emsp;autoApproveScopes|自动通过的授权范围|array|string|
|&emsp;&emsp;authorities|权限|array|string|
|&emsp;&emsp;resourceIds|资源|array|string|
|&emsp;&emsp;additionalInformation|附加信息|string||
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 1024,
		"clientId": "tudou",
		"secret": "fan",
		"name": "土豆",
		"logo": "https://www.wmt.cn/xx.png",
		"description": "我是一个应用",
		"status": 1,
		"accessTokenValiditySeconds": 8640,
		"refreshTokenValiditySeconds": 8640000,
		"redirectUris": "https://www.wmt.cn",
		"authorizedGrantTypes": "password",
		"scopes": "user_info",
		"autoApproveScopes": "user_info",
		"authorities": "system:user:query",
		"resourceIds": 1024,
		"additionalInformation": "{yunai: true}",
		"createTime": ""
	}
}
```


# 管理后台 - 站内信模版


## 更新站内信模版


**接口地址**:`/admin-api/system/notify-template/update`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "name": "测试模版",
  "code": "SEND_TEST",
  "type": 1,
  "nickname": "土豆",
  "content": "我是模版内容",
  "status": 1,
  "remark": "我是备注"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|notifyTemplateSaveReqVO|管理后台 - 站内信模版创建/修改 Request VO|body|true|NotifyTemplateSaveReqVO|NotifyTemplateSaveReqVO|
|&emsp;&emsp;id|ID||false|integer(int64)||
|&emsp;&emsp;name|模版名称||true|string||
|&emsp;&emsp;code|模版编码||true|string||
|&emsp;&emsp;type|模版类型，对应 system_notify_template_type 字典||true|integer(int32)||
|&emsp;&emsp;nickname|发送人名称||true|string||
|&emsp;&emsp;content|模版内容||true|string||
|&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举||true|integer(int32)||
|&emsp;&emsp;remark|备注||false|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 发送站内信


**接口地址**:`/admin-api/system/notify-template/send-notify`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "userId": 1,
  "userType": 1,
  "templateCode": "01",
  "templateParams": {}
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|notifyTemplateSendReqVO|管理后台 - 站内信模板的发送 Request VO|body|true|NotifyTemplateSendReqVO|NotifyTemplateSendReqVO|
|&emsp;&emsp;userId|用户id||true|integer(int64)||
|&emsp;&emsp;userType|用户类型||true|integer(int32)||
|&emsp;&emsp;templateCode|模板编码||true|string||
|&emsp;&emsp;templateParams|模板参数||false|object||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultLong|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": 0
}
```


## 删除站内信模版


**接口地址**:`/admin-api/system/notify-template/delete`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 批量删除站内信模版


**接口地址**:`/admin-api/system/notify-template/delete-list`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|ids|编号列表|query|true|array|integer|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 创建站内信模版


**接口地址**:`/admin-api/system/notify-template/create`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "name": "测试模版",
  "code": "SEND_TEST",
  "type": 1,
  "nickname": "土豆",
  "content": "我是模版内容",
  "status": 1,
  "remark": "我是备注"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|notifyTemplateSaveReqVO|管理后台 - 站内信模版创建/修改 Request VO|body|true|NotifyTemplateSaveReqVO|NotifyTemplateSaveReqVO|
|&emsp;&emsp;id|ID||false|integer(int64)||
|&emsp;&emsp;name|模版名称||true|string||
|&emsp;&emsp;code|模版编码||true|string||
|&emsp;&emsp;type|模版类型，对应 system_notify_template_type 字典||true|integer(int32)||
|&emsp;&emsp;nickname|发送人名称||true|string||
|&emsp;&emsp;content|模版内容||true|string||
|&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举||true|integer(int32)||
|&emsp;&emsp;remark|备注||false|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultLong|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": 0
}
```


## 获得站内信模版分页


**接口地址**:`/admin-api/system/notify-template/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|code|模版编码|query|false|string||
|name|模版名称|query|false|string||
|status|状态，参见 CommonStatusEnum 枚举类|query|false|integer(int32)||
|createTime|创建时间|query|false|array|string|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultPageResultNotifyTemplateRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||PageResultNotifyTemplateRespVO|PageResultNotifyTemplateRespVO|
|&emsp;&emsp;total|总量|integer(int64)||
|&emsp;&emsp;list|数据|array|NotifyTemplateRespVO|
|&emsp;&emsp;&emsp;&emsp;id|ID|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;name|模版名称|string||
|&emsp;&emsp;&emsp;&emsp;code|模版编码|string||
|&emsp;&emsp;&emsp;&emsp;type|模版类型，对应 system_notify_template_type 字典|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;nickname|发送人名称|string||
|&emsp;&emsp;&emsp;&emsp;content|模版内容|string||
|&emsp;&emsp;&emsp;&emsp;params|参数数组|array|string|
|&emsp;&emsp;&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 1024,
				"name": "测试模版",
				"code": "SEND_TEST",
				"type": 1,
				"nickname": "土豆",
				"content": "我是模版内容",
				"params": "name,code",
				"status": 1,
				"remark": "我是备注",
				"createTime": ""
			}
		]
	}
}
```


## 获得站内信模版


**接口地址**:`/admin-api/system/notify-template/get`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultNotifyTemplateRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||NotifyTemplateRespVO|NotifyTemplateRespVO|
|&emsp;&emsp;id|ID|integer(int64)||
|&emsp;&emsp;name|模版名称|string||
|&emsp;&emsp;code|模版编码|string||
|&emsp;&emsp;type|模版类型，对应 system_notify_template_type 字典|integer(int32)||
|&emsp;&emsp;nickname|发送人名称|string||
|&emsp;&emsp;content|模版内容|string||
|&emsp;&emsp;params|参数数组|array|string|
|&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举|integer(int32)||
|&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 1024,
		"name": "测试模版",
		"code": "SEND_TEST",
		"type": 1,
		"nickname": "土豆",
		"content": "我是模版内容",
		"params": "name,code",
		"status": 1,
		"remark": "我是备注",
		"createTime": ""
	}
}
```


# 管理后台 - 我的站内信


## 标记站内信为已读


**接口地址**:`/admin-api/system/notify-message/update-read`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|ids|编号列表|query|true|array|integer|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 标记所有站内信为已读


**接口地址**:`/admin-api/system/notify-message/update-all-read`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 获得站内信分页


**接口地址**:`/admin-api/system/notify-message/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|userId|用户编号|query|false|integer(int64)||
|userType|用户类型|query|false|integer(int32)||
|templateCode|模板编码|query|false|string||
|templateType|模版类型|query|false|integer(int32)||
|createTime|创建时间|query|false|array|string|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultPageResultNotifyMessageRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||PageResultNotifyMessageRespVO|PageResultNotifyMessageRespVO|
|&emsp;&emsp;total|总量|integer(int64)||
|&emsp;&emsp;list|数据|array|NotifyMessageRespVO|
|&emsp;&emsp;&emsp;&emsp;id|ID|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;userId|用户编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;userType|用户类型，参见 UserTypeEnum 枚举|string(byte)||
|&emsp;&emsp;&emsp;&emsp;templateId|模版编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;templateCode|模板编码|string||
|&emsp;&emsp;&emsp;&emsp;templateNickname|模版发送人名称|string||
|&emsp;&emsp;&emsp;&emsp;templateContent|模版内容|string||
|&emsp;&emsp;&emsp;&emsp;templateType|模版类型|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;templateParams|模版参数|object||
|&emsp;&emsp;&emsp;&emsp;readStatus|是否已读|boolean||
|&emsp;&emsp;&emsp;&emsp;readTime|阅读时间|string(date-time)||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 1024,
				"userId": 25025,
				"userType": 1,
				"templateId": 13013,
				"templateCode": "test_01",
				"templateNickname": "wmt",
				"templateContent": "测试内容",
				"templateType": 2,
				"templateParams": {},
				"readStatus": true,
				"readTime": "",
				"createTime": ""
			}
		]
	}
}
```


## 获得我的站内信分页


**接口地址**:`/admin-api/system/notify-message/my-page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|readStatus|是否已读|query|false|boolean||
|createTime|创建时间|query|false|array|string|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultPageResultNotifyMessageRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||PageResultNotifyMessageRespVO|PageResultNotifyMessageRespVO|
|&emsp;&emsp;total|总量|integer(int64)||
|&emsp;&emsp;list|数据|array|NotifyMessageRespVO|
|&emsp;&emsp;&emsp;&emsp;id|ID|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;userId|用户编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;userType|用户类型，参见 UserTypeEnum 枚举|string(byte)||
|&emsp;&emsp;&emsp;&emsp;templateId|模版编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;templateCode|模板编码|string||
|&emsp;&emsp;&emsp;&emsp;templateNickname|模版发送人名称|string||
|&emsp;&emsp;&emsp;&emsp;templateContent|模版内容|string||
|&emsp;&emsp;&emsp;&emsp;templateType|模版类型|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;templateParams|模版参数|object||
|&emsp;&emsp;&emsp;&emsp;readStatus|是否已读|boolean||
|&emsp;&emsp;&emsp;&emsp;readTime|阅读时间|string(date-time)||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 1024,
				"userId": 25025,
				"userType": 1,
				"templateId": 13013,
				"templateCode": "test_01",
				"templateNickname": "wmt",
				"templateContent": "测试内容",
				"templateType": 2,
				"templateParams": {},
				"readStatus": true,
				"readTime": "",
				"createTime": ""
			}
		]
	}
}
```


## 获得站内信


**接口地址**:`/admin-api/system/notify-message/get`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultNotifyMessageRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||NotifyMessageRespVO|NotifyMessageRespVO|
|&emsp;&emsp;id|ID|integer(int64)||
|&emsp;&emsp;userId|用户编号|integer(int64)||
|&emsp;&emsp;userType|用户类型，参见 UserTypeEnum 枚举|string(byte)||
|&emsp;&emsp;templateId|模版编号|integer(int64)||
|&emsp;&emsp;templateCode|模板编码|string||
|&emsp;&emsp;templateNickname|模版发送人名称|string||
|&emsp;&emsp;templateContent|模版内容|string||
|&emsp;&emsp;templateType|模版类型|integer(int32)||
|&emsp;&emsp;templateParams|模版参数|object||
|&emsp;&emsp;readStatus|是否已读|boolean||
|&emsp;&emsp;readTime|阅读时间|string(date-time)||
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 1024,
		"userId": 25025,
		"userType": 1,
		"templateId": 13013,
		"templateCode": "test_01",
		"templateNickname": "wmt",
		"templateContent": "测试内容",
		"templateType": 2,
		"templateParams": {},
		"readStatus": true,
		"readTime": "",
		"createTime": ""
	}
}
```


## 获取当前用户的最新站内信列表，默认 10 条


**接口地址**:`/admin-api/system/notify-message/get-unread-list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|size|10|query|false|integer(int32)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListNotifyMessageRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|NotifyMessageRespVO|
|&emsp;&emsp;id|ID|integer(int64)||
|&emsp;&emsp;userId|用户编号|integer(int64)||
|&emsp;&emsp;userType|用户类型，参见 UserTypeEnum 枚举|string(byte)||
|&emsp;&emsp;templateId|模版编号|integer(int64)||
|&emsp;&emsp;templateCode|模板编码|string||
|&emsp;&emsp;templateNickname|模版发送人名称|string||
|&emsp;&emsp;templateContent|模版内容|string||
|&emsp;&emsp;templateType|模版类型|integer(int32)||
|&emsp;&emsp;templateParams|模版参数|object||
|&emsp;&emsp;readStatus|是否已读|boolean||
|&emsp;&emsp;readTime|阅读时间|string(date-time)||
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 1024,
			"userId": 25025,
			"userType": 1,
			"templateId": 13013,
			"templateCode": "test_01",
			"templateNickname": "wmt",
			"templateContent": "测试内容",
			"templateType": 2,
			"templateParams": {},
			"readStatus": true,
			"readTime": "",
			"createTime": ""
		}
	]
}
```


## 获得当前用户的未读站内信数量


**接口地址**:`/admin-api/system/notify-message/get-unread-count`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultLong|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": 0
}
```


# 管理后台 - 通知公告


## 修改通知公告


**接口地址**:`/admin-api/system/notice/update`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "title": "小博主",
  "type": "小博主",
  "content": "半生编码",
  "status": 1
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|noticeSaveReqVO|管理后台 - 通知公告创建/修改 Request VO|body|true|NoticeSaveReqVO|NoticeSaveReqVO|
|&emsp;&emsp;id|岗位公告编号||false|integer(int64)||
|&emsp;&emsp;title|公告标题||true|string||
|&emsp;&emsp;type|公告类型||true|integer(int32)||
|&emsp;&emsp;content|公告内容||true|string||
|&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举类||true|integer(int32)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 推送通知公告


**接口地址**:`/admin-api/system/notice/push`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>只发送给 websocket 连接在线的用户</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 删除通知公告


**接口地址**:`/admin-api/system/notice/delete`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 批量删除通知公告


**接口地址**:`/admin-api/system/notice/delete-list`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|ids|编号列表|query|true|array|integer|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 创建通知公告


**接口地址**:`/admin-api/system/notice/create`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "title": "小博主",
  "type": "小博主",
  "content": "半生编码",
  "status": 1
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|noticeSaveReqVO|管理后台 - 通知公告创建/修改 Request VO|body|true|NoticeSaveReqVO|NoticeSaveReqVO|
|&emsp;&emsp;id|岗位公告编号||false|integer(int64)||
|&emsp;&emsp;title|公告标题||true|string||
|&emsp;&emsp;type|公告类型||true|integer(int32)||
|&emsp;&emsp;content|公告内容||true|string||
|&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举类||true|integer(int32)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultLong|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": 0
}
```


## 获取通知公告列表


**接口地址**:`/admin-api/system/notice/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|title|通知公告名称，模糊匹配|query|false|string||
|status|展示状态，参见 CommonStatusEnum 枚举类|query|false|integer(int32)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultPageResultNoticeRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||PageResultNoticeRespVO|PageResultNoticeRespVO|
|&emsp;&emsp;total|总量|integer(int64)||
|&emsp;&emsp;list|数据|array|NoticeRespVO|
|&emsp;&emsp;&emsp;&emsp;id|通知公告序号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;title|公告标题|string||
|&emsp;&emsp;&emsp;&emsp;type|公告类型|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;content|公告内容|string||
|&emsp;&emsp;&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举类|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 1024,
				"title": "小博主",
				"type": "小博主",
				"content": "半生编码",
				"status": 1,
				"createTime": "时间戳格式"
			}
		]
	}
}
```


## 获得通知公告


**接口地址**:`/admin-api/system/notice/get`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultNoticeRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||NoticeRespVO|NoticeRespVO|
|&emsp;&emsp;id|通知公告序号|integer(int64)||
|&emsp;&emsp;title|公告标题|string||
|&emsp;&emsp;type|公告类型|integer(int32)||
|&emsp;&emsp;content|公告内容|string||
|&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举类|integer(int32)||
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 1024,
		"title": "小博主",
		"type": "小博主",
		"content": "半生编码",
		"status": 1,
		"createTime": "时间戳格式"
	}
}
```


# 管理后台 - 菜单


## 修改菜单


**接口地址**:`/admin-api/system/menu/update`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "name": "AHC",
  "permission": "sys:menu:add",
  "type": 1,
  "sort": 1024,
  "parentId": 1024,
  "path": "post",
  "icon": "/menu/list",
  "component": "system/post/index",
  "componentName": "SystemUser",
  "status": 1,
  "visible": false,
  "keepAlive": false,
  "alwaysShow": false
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|menuSaveVO|管理后台 - 菜单创建/修改 Request VO|body|true|MenuSaveVO|MenuSaveVO|
|&emsp;&emsp;id|菜单编号||false|integer(int64)||
|&emsp;&emsp;name|菜单名称||true|string||
|&emsp;&emsp;permission|权限标识,仅菜单类型为按钮时，才需要传递||false|string||
|&emsp;&emsp;type|类型，参见 MenuTypeEnum 枚举类||true|integer(int32)||
|&emsp;&emsp;sort|显示顺序||true|integer(int32)||
|&emsp;&emsp;parentId|父菜单 ID||true|integer(int64)||
|&emsp;&emsp;path|路由地址,仅菜单类型为菜单或者目录时，才需要传||false|string||
|&emsp;&emsp;icon|菜单图标,仅菜单类型为菜单或者目录时，才需要传||false|string||
|&emsp;&emsp;component|组件路径,仅菜单类型为菜单时，才需要传||false|string||
|&emsp;&emsp;componentName|组件名||false|string||
|&emsp;&emsp;status|状态,见 CommonStatusEnum 枚举||true|integer(int32)||
|&emsp;&emsp;visible|是否可见||false|boolean||
|&emsp;&emsp;keepAlive|是否缓存||false|boolean||
|&emsp;&emsp;alwaysShow|是否总是显示||false|boolean||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 删除菜单


**接口地址**:`/admin-api/system/menu/delete`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|菜单编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 批量删除菜单


**接口地址**:`/admin-api/system/menu/delete-list`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|ids|编号列表|query|true|array|integer|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 创建菜单


**接口地址**:`/admin-api/system/menu/create`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "name": "AHC",
  "permission": "sys:menu:add",
  "type": 1,
  "sort": 1024,
  "parentId": 1024,
  "path": "post",
  "icon": "/menu/list",
  "component": "system/post/index",
  "componentName": "SystemUser",
  "status": 1,
  "visible": false,
  "keepAlive": false,
  "alwaysShow": false
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|menuSaveVO|管理后台 - 菜单创建/修改 Request VO|body|true|MenuSaveVO|MenuSaveVO|
|&emsp;&emsp;id|菜单编号||false|integer(int64)||
|&emsp;&emsp;name|菜单名称||true|string||
|&emsp;&emsp;permission|权限标识,仅菜单类型为按钮时，才需要传递||false|string||
|&emsp;&emsp;type|类型，参见 MenuTypeEnum 枚举类||true|integer(int32)||
|&emsp;&emsp;sort|显示顺序||true|integer(int32)||
|&emsp;&emsp;parentId|父菜单 ID||true|integer(int64)||
|&emsp;&emsp;path|路由地址,仅菜单类型为菜单或者目录时，才需要传||false|string||
|&emsp;&emsp;icon|菜单图标,仅菜单类型为菜单或者目录时，才需要传||false|string||
|&emsp;&emsp;component|组件路径,仅菜单类型为菜单时，才需要传||false|string||
|&emsp;&emsp;componentName|组件名||false|string||
|&emsp;&emsp;status|状态,见 CommonStatusEnum 枚举||true|integer(int32)||
|&emsp;&emsp;visible|是否可见||false|boolean||
|&emsp;&emsp;keepAlive|是否缓存||false|boolean||
|&emsp;&emsp;alwaysShow|是否总是显示||false|boolean||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultLong|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": 0
}
```


## 获取菜单列表


**接口地址**:`/admin-api/system/menu/list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>用于【菜单管理】界面</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|name|菜单名称，模糊匹配|query|false|string||
|status|展示状态，参见 CommonStatusEnum 枚举类|query|false|integer(int32)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListMenuRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|MenuRespVO|
|&emsp;&emsp;id|菜单编号|integer(int64)||
|&emsp;&emsp;name|菜单名称|string||
|&emsp;&emsp;permission|权限标识,仅菜单类型为按钮时，才需要传递|string||
|&emsp;&emsp;type|类型，参见 MenuTypeEnum 枚举类|integer(int32)||
|&emsp;&emsp;sort|显示顺序|integer(int32)||
|&emsp;&emsp;parentId|父菜单 ID|integer(int64)||
|&emsp;&emsp;path|路由地址,仅菜单类型为菜单或者目录时，才需要传|string||
|&emsp;&emsp;icon|菜单图标,仅菜单类型为菜单或者目录时，才需要传|string||
|&emsp;&emsp;component|组件路径,仅菜单类型为菜单时，才需要传|string||
|&emsp;&emsp;componentName|组件名|string||
|&emsp;&emsp;status|状态,见 CommonStatusEnum 枚举|integer(int32)||
|&emsp;&emsp;visible|是否可见|boolean||
|&emsp;&emsp;keepAlive|是否缓存|boolean||
|&emsp;&emsp;alwaysShow|是否总是显示|boolean||
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 1024,
			"name": "AHC",
			"permission": "sys:menu:add",
			"type": 1,
			"sort": 1024,
			"parentId": 1024,
			"path": "post",
			"icon": "/menu/list",
			"component": "system/post/index",
			"componentName": "SystemUser",
			"status": 1,
			"visible": false,
			"keepAlive": false,
			"alwaysShow": false,
			"createTime": "时间戳格式"
		}
	]
}
```


## 获取菜单精简信息列表


**接口地址**:`/admin-api/system/menu/list-all-simple`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>只包含被开启的菜单，用于【角色分配菜单】功能的选项。在多租户的场景下，会只返回租户所在套餐有的菜单</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListMenuSimpleRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|MenuSimpleRespVO|
|&emsp;&emsp;id|菜单编号|integer(int64)||
|&emsp;&emsp;name|菜单名称|string||
|&emsp;&emsp;parentId|父菜单 ID|integer(int64)||
|&emsp;&emsp;type|类型，参见 MenuTypeEnum 枚举类|integer(int32)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 1024,
			"name": "AHC",
			"parentId": 1024,
			"type": 1
		}
	]
}
```


## 获取菜单精简信息列表


**接口地址**:`/admin-api/system/menu/simple-list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>只包含被开启的菜单，用于【角色分配菜单】功能的选项。在多租户的场景下，会只返回租户所在套餐有的菜单</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListMenuSimpleRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|MenuSimpleRespVO|
|&emsp;&emsp;id|菜单编号|integer(int64)||
|&emsp;&emsp;name|菜单名称|string||
|&emsp;&emsp;parentId|父菜单 ID|integer(int64)||
|&emsp;&emsp;type|类型，参见 MenuTypeEnum 枚举类|integer(int32)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 1024,
			"name": "AHC",
			"parentId": 1024,
			"type": 1
		}
	]
}
```


## 获取菜单信息


**接口地址**:`/admin-api/system/menu/get`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id||query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultMenuRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||MenuRespVO|MenuRespVO|
|&emsp;&emsp;id|菜单编号|integer(int64)||
|&emsp;&emsp;name|菜单名称|string||
|&emsp;&emsp;permission|权限标识,仅菜单类型为按钮时，才需要传递|string||
|&emsp;&emsp;type|类型，参见 MenuTypeEnum 枚举类|integer(int32)||
|&emsp;&emsp;sort|显示顺序|integer(int32)||
|&emsp;&emsp;parentId|父菜单 ID|integer(int64)||
|&emsp;&emsp;path|路由地址,仅菜单类型为菜单或者目录时，才需要传|string||
|&emsp;&emsp;icon|菜单图标,仅菜单类型为菜单或者目录时，才需要传|string||
|&emsp;&emsp;component|组件路径,仅菜单类型为菜单时，才需要传|string||
|&emsp;&emsp;componentName|组件名|string||
|&emsp;&emsp;status|状态,见 CommonStatusEnum 枚举|integer(int32)||
|&emsp;&emsp;visible|是否可见|boolean||
|&emsp;&emsp;keepAlive|是否缓存|boolean||
|&emsp;&emsp;alwaysShow|是否总是显示|boolean||
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 1024,
		"name": "AHC",
		"permission": "sys:menu:add",
		"type": 1,
		"sort": 1024,
		"parentId": 1024,
		"path": "post",
		"icon": "/menu/list",
		"component": "system/post/index",
		"componentName": "SystemUser",
		"status": 1,
		"visible": false,
		"keepAlive": false,
		"alwaysShow": false,
		"createTime": "时间戳格式"
	}
}
```


# 管理后台 - 邮件模版


## 修改邮件模版


**接口地址**:`/admin-api/system/mail-template/update`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "name": "测试名字",
  "code": "test",
  "accountId": 1,
  "nickname": "wmt",
  "title": "注册成功",
  "content": "你好，注册成功啦",
  "status": 1,
  "remark": "奥特曼"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|mailTemplateSaveReqVO|管理后台 - 邮件模版创建/修改 Request VO|body|true|MailTemplateSaveReqVO|MailTemplateSaveReqVO|
|&emsp;&emsp;id|编号||false|integer(int64)||
|&emsp;&emsp;name|模版名称||true|string||
|&emsp;&emsp;code|模版编号||true|string||
|&emsp;&emsp;accountId|发送的邮箱账号编号||true|integer(int64)||
|&emsp;&emsp;nickname|发送人名称||false|string||
|&emsp;&emsp;title|标题||true|string||
|&emsp;&emsp;content|内容||true|string||
|&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举||true|integer(int32)||
|&emsp;&emsp;remark|备注||false|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 发送短信


**接口地址**:`/admin-api/system/mail-template/send-mail`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "toMails": "[user1@example.com, user2@example.com]",
  "ccMails": "[user3@example.com, user4@example.com]",
  "bccMails": "[user5@example.com, user6@example.com]",
  "templateCode": "test_01",
  "templateParams": {}
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|mailTemplateSendReqVO|管理后台 - 邮件发送 Req VO|body|true|MailTemplateSendReqVO|MailTemplateSendReqVO|
|&emsp;&emsp;toMails|接收邮箱||true|array|string|
|&emsp;&emsp;ccMails|抄送邮箱||true|array|string|
|&emsp;&emsp;bccMails|密送邮箱||true|array|string|
|&emsp;&emsp;templateCode|模板编码||true|string||
|&emsp;&emsp;templateParams|模板参数||false|object||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultLong|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": 0
}
```


## 删除邮件模版


**接口地址**:`/admin-api/system/mail-template/delete`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 批量删除邮件模版


**接口地址**:`/admin-api/system/mail-template/delete-list`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|ids|编号列表|query|true|array|integer|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 创建邮件模版


**接口地址**:`/admin-api/system/mail-template/create`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "name": "测试名字",
  "code": "test",
  "accountId": 1,
  "nickname": "wmt",
  "title": "注册成功",
  "content": "你好，注册成功啦",
  "status": 1,
  "remark": "奥特曼"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|mailTemplateSaveReqVO|管理后台 - 邮件模版创建/修改 Request VO|body|true|MailTemplateSaveReqVO|MailTemplateSaveReqVO|
|&emsp;&emsp;id|编号||false|integer(int64)||
|&emsp;&emsp;name|模版名称||true|string||
|&emsp;&emsp;code|模版编号||true|string||
|&emsp;&emsp;accountId|发送的邮箱账号编号||true|integer(int64)||
|&emsp;&emsp;nickname|发送人名称||false|string||
|&emsp;&emsp;title|标题||true|string||
|&emsp;&emsp;content|内容||true|string||
|&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举||true|integer(int32)||
|&emsp;&emsp;remark|备注||false|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultLong|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": 0
}
```


## 获得邮件模版分页


**接口地址**:`/admin-api/system/mail-template/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|status|状态，参见 CommonStatusEnum 枚举|query|false|integer(int32)||
|code|标识，模糊匹配|query|false|string||
|name|名称，模糊匹配|query|false|string||
|accountId|账号编号|query|false|integer(int64)||
|createTime|创建时间|query|false|array|string|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultPageResultMailTemplateRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||PageResultMailTemplateRespVO|PageResultMailTemplateRespVO|
|&emsp;&emsp;total|总量|integer(int64)||
|&emsp;&emsp;list|数据|array|MailTemplateRespVO|
|&emsp;&emsp;&emsp;&emsp;id|编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;name|模版名称|string||
|&emsp;&emsp;&emsp;&emsp;code|模版编号|string||
|&emsp;&emsp;&emsp;&emsp;accountId|发送的邮箱账号编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;nickname|发送人名称|string||
|&emsp;&emsp;&emsp;&emsp;title|标题|string||
|&emsp;&emsp;&emsp;&emsp;content|内容|string||
|&emsp;&emsp;&emsp;&emsp;params|参数数组|array|string|
|&emsp;&emsp;&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 1024,
				"name": "测试名字",
				"code": "test",
				"accountId": 1,
				"nickname": "wmt",
				"title": "注册成功",
				"content": "你好，注册成功啦",
				"params": "name,code",
				"status": 1,
				"remark": "奥特曼",
				"createTime": ""
			}
		]
	}
}
```


## 获得邮件模版精简列表


**接口地址**:`/admin-api/system/mail-template/list-all-simple`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListMailTemplateSimpleRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|MailTemplateSimpleRespVO|
|&emsp;&emsp;id|模版编号|integer(int64)||
|&emsp;&emsp;name|模版名字|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 1024,
			"name": "哒哒哒"
		}
	]
}
```


## 获得邮件模版精简列表


**接口地址**:`/admin-api/system/mail-template/simple-list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListMailTemplateSimpleRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|MailTemplateSimpleRespVO|
|&emsp;&emsp;id|模版编号|integer(int64)||
|&emsp;&emsp;name|模版名字|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 1024,
			"name": "哒哒哒"
		}
	]
}
```


## 获得邮件模版


**接口地址**:`/admin-api/system/mail-template/get`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultMailTemplateRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||MailTemplateRespVO|MailTemplateRespVO|
|&emsp;&emsp;id|编号|integer(int64)||
|&emsp;&emsp;name|模版名称|string||
|&emsp;&emsp;code|模版编号|string||
|&emsp;&emsp;accountId|发送的邮箱账号编号|integer(int64)||
|&emsp;&emsp;nickname|发送人名称|string||
|&emsp;&emsp;title|标题|string||
|&emsp;&emsp;content|内容|string||
|&emsp;&emsp;params|参数数组|array|string|
|&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举|integer(int32)||
|&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 1024,
		"name": "测试名字",
		"code": "test",
		"accountId": 1,
		"nickname": "wmt",
		"title": "注册成功",
		"content": "你好，注册成功啦",
		"params": "name,code",
		"status": 1,
		"remark": "奥特曼",
		"createTime": ""
	}
}
```


# 管理后台 - 邮箱账号


## 修改邮箱账号


**接口地址**:`/admin-api/system/mail-account/update`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "mail": "wmtyuanma@123.com",
  "username": "wmt",
  "password": 123456,
  "host": "www.wmt.cn",
  "port": 80,
  "sslEnable": true,
  "starttlsEnable": true
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|mailAccountSaveReqVO|管理后台 - 邮箱账号创建/修改 Request VO|body|true|MailAccountSaveReqVO|MailAccountSaveReqVO|
|&emsp;&emsp;id|编号||false|integer(int64)||
|&emsp;&emsp;mail|邮箱||true|string(email)||
|&emsp;&emsp;username|用户名||true|string||
|&emsp;&emsp;password|密码||true|string||
|&emsp;&emsp;host|SMTP 服务器域名||true|string||
|&emsp;&emsp;port|SMTP 服务器端口||true|integer(int32)||
|&emsp;&emsp;sslEnable|是否开启 ssl||true|boolean||
|&emsp;&emsp;starttlsEnable|是否开启 starttls||true|boolean||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 删除邮箱账号


**接口地址**:`/admin-api/system/mail-account/delete`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 批量删除邮箱账号


**接口地址**:`/admin-api/system/mail-account/delete-list`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|ids|编号列表|query|true|array|integer|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 创建邮箱账号


**接口地址**:`/admin-api/system/mail-account/create`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "mail": "wmtyuanma@123.com",
  "username": "wmt",
  "password": 123456,
  "host": "www.wmt.cn",
  "port": 80,
  "sslEnable": true,
  "starttlsEnable": true
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|mailAccountSaveReqVO|管理后台 - 邮箱账号创建/修改 Request VO|body|true|MailAccountSaveReqVO|MailAccountSaveReqVO|
|&emsp;&emsp;id|编号||false|integer(int64)||
|&emsp;&emsp;mail|邮箱||true|string(email)||
|&emsp;&emsp;username|用户名||true|string||
|&emsp;&emsp;password|密码||true|string||
|&emsp;&emsp;host|SMTP 服务器域名||true|string||
|&emsp;&emsp;port|SMTP 服务器端口||true|integer(int32)||
|&emsp;&emsp;sslEnable|是否开启 ssl||true|boolean||
|&emsp;&emsp;starttlsEnable|是否开启 starttls||true|boolean||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultLong|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": 0
}
```


## 获得邮箱账号分页


**接口地址**:`/admin-api/system/mail-account/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|mail|邮箱|query|true|string||
|username|用户名|query|true|string||
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultPageResultMailAccountRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||PageResultMailAccountRespVO|PageResultMailAccountRespVO|
|&emsp;&emsp;total|总量|integer(int64)||
|&emsp;&emsp;list|数据|array|MailAccountRespVO|
|&emsp;&emsp;&emsp;&emsp;id|编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;mail|邮箱|string||
|&emsp;&emsp;&emsp;&emsp;username|用户名|string||
|&emsp;&emsp;&emsp;&emsp;password|密码|string||
|&emsp;&emsp;&emsp;&emsp;host|SMTP 服务器域名|string||
|&emsp;&emsp;&emsp;&emsp;port|SMTP 服务器端口|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;sslEnable|是否开启 ssl|boolean||
|&emsp;&emsp;&emsp;&emsp;starttlsEnable|是否开启 starttls|boolean||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 1024,
				"mail": "wmtyuanma@123.com",
				"username": "wmt",
				"password": 123456,
				"host": "www.wmt.cn",
				"port": 80,
				"sslEnable": true,
				"starttlsEnable": true,
				"createTime": ""
			}
		]
	}
}
```


## 获得邮箱账号精简列表


**接口地址**:`/admin-api/system/mail-account/list-all-simple`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListMailAccountSimpleRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|MailAccountSimpleRespVO|
|&emsp;&emsp;id|邮箱编号|integer(int64)||
|&emsp;&emsp;mail|邮箱|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 1024,
			"mail": "768541388@qq.com"
		}
	]
}
```


## 获得邮箱账号精简列表


**接口地址**:`/admin-api/system/mail-account/simple-list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListMailAccountSimpleRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|MailAccountSimpleRespVO|
|&emsp;&emsp;id|邮箱编号|integer(int64)||
|&emsp;&emsp;mail|邮箱|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 1024,
			"mail": "768541388@qq.com"
		}
	]
}
```


## 获得邮箱账号


**接口地址**:`/admin-api/system/mail-account/get`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultMailAccountRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||MailAccountRespVO|MailAccountRespVO|
|&emsp;&emsp;id|编号|integer(int64)||
|&emsp;&emsp;mail|邮箱|string||
|&emsp;&emsp;username|用户名|string||
|&emsp;&emsp;password|密码|string||
|&emsp;&emsp;host|SMTP 服务器域名|string||
|&emsp;&emsp;port|SMTP 服务器端口|integer(int32)||
|&emsp;&emsp;sslEnable|是否开启 ssl|boolean||
|&emsp;&emsp;starttlsEnable|是否开启 starttls|boolean||
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 1024,
		"mail": "wmtyuanma@123.com",
		"username": "wmt",
		"password": 123456,
		"host": "www.wmt.cn",
		"port": 80,
		"sslEnable": true,
		"starttlsEnable": true,
		"createTime": ""
	}
}
```


# 管理后台 - 字典类型


## 修改字典类型


**接口地址**:`/admin-api/system/dict-type/update`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "name": "性别",
  "type": "sys_common_sex",
  "status": 1,
  "remark": "快乐的备注"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dictTypeSaveReqVO|管理后台 - 字典类型创建/修改 Request VO|body|true|DictTypeSaveReqVO|DictTypeSaveReqVO|
|&emsp;&emsp;id|字典类型编号||false|integer(int64)||
|&emsp;&emsp;name|字典名称||true|string||
|&emsp;&emsp;type|字典类型||true|string||
|&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举类||true|integer(int32)||
|&emsp;&emsp;remark|备注||false|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 删除字典类型


**接口地址**:`/admin-api/system/dict-type/delete`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 批量删除字典类型


**接口地址**:`/admin-api/system/dict-type/delete-list`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|ids|编号列表|query|true|array|integer|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 创建字典类型


**接口地址**:`/admin-api/system/dict-type/create`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "name": "性别",
  "type": "sys_common_sex",
  "status": 1,
  "remark": "快乐的备注"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dictTypeSaveReqVO|管理后台 - 字典类型创建/修改 Request VO|body|true|DictTypeSaveReqVO|DictTypeSaveReqVO|
|&emsp;&emsp;id|字典类型编号||false|integer(int64)||
|&emsp;&emsp;name|字典名称||true|string||
|&emsp;&emsp;type|字典类型||true|string||
|&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举类||true|integer(int32)||
|&emsp;&emsp;remark|备注||false|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultLong|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": 0
}
```


## 获得字典类型的分页列表


**接口地址**:`/admin-api/system/dict-type/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|name|字典类型名称，模糊匹配|query|false|string||
|type|字典类型，模糊匹配|query|false|string||
|status|展示状态，参见 CommonStatusEnum 枚举类|query|false|integer(int32)||
|createTime|创建时间|query|false|array|string|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultPageResultDictTypeRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||PageResultDictTypeRespVO|PageResultDictTypeRespVO|
|&emsp;&emsp;total|总量|integer(int64)||
|&emsp;&emsp;list|数据|array|DictTypeRespVO|
|&emsp;&emsp;&emsp;&emsp;id|字典类型编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;name|字典名称|string||
|&emsp;&emsp;&emsp;&emsp;type|字典类型|string||
|&emsp;&emsp;&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举类|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 1024,
				"name": "性别",
				"type": "sys_common_sex",
				"status": 1,
				"remark": "快乐的备注",
				"createTime": "时间戳格式"
			}
		]
	}
}
```


## 获得全部字典类型列表


**接口地址**:`/admin-api/system/dict-type/list-all-simple`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>包括开启 + 禁用的字典类型，主要用于前端的下拉选项</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListDictTypeSimpleRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|DictTypeSimpleRespVO|
|&emsp;&emsp;id|字典类型编号|integer(int64)||
|&emsp;&emsp;name|字典类型名称|string||
|&emsp;&emsp;type|字典类型|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 1024,
			"name": "AHC",
			"type": "sys_common_sex"
		}
	]
}
```


## 获得全部字典类型列表


**接口地址**:`/admin-api/system/dict-type/simple-list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>包括开启 + 禁用的字典类型，主要用于前端的下拉选项</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListDictTypeSimpleRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|DictTypeSimpleRespVO|
|&emsp;&emsp;id|字典类型编号|integer(int64)||
|&emsp;&emsp;name|字典类型名称|string||
|&emsp;&emsp;type|字典类型|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 1024,
			"name": "AHC",
			"type": "sys_common_sex"
		}
	]
}
```


## -查询字典类型详细


**接口地址**:`/admin-api/system/dict-type/get`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultDictTypeRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||DictTypeRespVO|DictTypeRespVO|
|&emsp;&emsp;id|字典类型编号|integer(int64)||
|&emsp;&emsp;name|字典名称|string||
|&emsp;&emsp;type|字典类型|string||
|&emsp;&emsp;status|状态，参见 CommonStatusEnum 枚举类|integer(int32)||
|&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 1024,
		"name": "性别",
		"type": "sys_common_sex",
		"status": 1,
		"remark": "快乐的备注",
		"createTime": "时间戳格式"
	}
}
```


## 导出数据类型


**接口地址**:`/admin-api/system/dict-type/export-excel`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|name|字典类型名称，模糊匹配|query|false|string||
|type|字典类型，模糊匹配|query|false|string||
|status|展示状态，参见 CommonStatusEnum 枚举类|query|false|integer(int32)||
|createTime|创建时间|query|false|array|string|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||


**响应参数**:


暂无


**响应示例**:
```javascript

```


# 管理后台 - 字典数据


## 修改字典数据


**接口地址**:`/admin-api/system/dict-data/update`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "sort": 1024,
  "label": "AHC",
  "value": "iocoder",
  "dictType": "sys_common_sex",
  "status": 1,
  "colorType": "default",
  "cssClass": "btn-visible",
  "remark": "我是一个角色"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dictDataSaveReqVO|管理后台 - 字典数据创建/修改 Request VO|body|true|DictDataSaveReqVO|DictDataSaveReqVO|
|&emsp;&emsp;id|字典数据编号||false|integer(int64)||
|&emsp;&emsp;sort|显示顺序||true|integer(int32)||
|&emsp;&emsp;label|字典标签||true|string||
|&emsp;&emsp;value|字典值||true|string||
|&emsp;&emsp;dictType|字典类型||true|string||
|&emsp;&emsp;status|状态,见 CommonStatusEnum 枚举||true|integer(int32)||
|&emsp;&emsp;colorType|颜色类型,default、primary、success、info、warning、danger||false|string||
|&emsp;&emsp;cssClass|css 样式||false|string||
|&emsp;&emsp;remark|备注||false|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 删除字典数据


**接口地址**:`/admin-api/system/dict-data/delete`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 批量删除字典数据


**接口地址**:`/admin-api/system/dict-data/delete-list`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|ids|编号列表|query|true|array|integer|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 新增字典数据


**接口地址**:`/admin-api/system/dict-data/create`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "sort": 1024,
  "label": "AHC",
  "value": "iocoder",
  "dictType": "sys_common_sex",
  "status": 1,
  "colorType": "default",
  "cssClass": "btn-visible",
  "remark": "我是一个角色"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dictDataSaveReqVO|管理后台 - 字典数据创建/修改 Request VO|body|true|DictDataSaveReqVO|DictDataSaveReqVO|
|&emsp;&emsp;id|字典数据编号||false|integer(int64)||
|&emsp;&emsp;sort|显示顺序||true|integer(int32)||
|&emsp;&emsp;label|字典标签||true|string||
|&emsp;&emsp;value|字典值||true|string||
|&emsp;&emsp;dictType|字典类型||true|string||
|&emsp;&emsp;status|状态,见 CommonStatusEnum 枚举||true|integer(int32)||
|&emsp;&emsp;colorType|颜色类型,default、primary、success、info、warning、danger||false|string||
|&emsp;&emsp;cssClass|css 样式||false|string||
|&emsp;&emsp;remark|备注||false|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultLong|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": 0
}
```


## 获得字典类型的分页


**接口地址**:`/admin-api/system/dict-data/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|label|字典标签|query|false|string||
|dictType|字典类型，模糊匹配|query|false|string||
|status|展示状态，参见 CommonStatusEnum 枚举类|query|false|integer(int32)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultPageResultDictDataRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||PageResultDictDataRespVO|PageResultDictDataRespVO|
|&emsp;&emsp;total|总量|integer(int64)||
|&emsp;&emsp;list|数据|array|DictDataRespVO|
|&emsp;&emsp;&emsp;&emsp;id|字典数据编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;sort|显示顺序|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;label|字典标签|string||
|&emsp;&emsp;&emsp;&emsp;value|字典值|string||
|&emsp;&emsp;&emsp;&emsp;dictType|字典类型|string||
|&emsp;&emsp;&emsp;&emsp;status|状态,见 CommonStatusEnum 枚举|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;colorType|颜色类型,default、primary、success、info、warning、danger|string||
|&emsp;&emsp;&emsp;&emsp;cssClass|css 样式|string||
|&emsp;&emsp;&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 1024,
				"sort": 1024,
				"label": "AHC",
				"value": "iocoder",
				"dictType": "sys_common_sex",
				"status": 1,
				"colorType": "default",
				"cssClass": "btn-visible",
				"remark": "我是一个角色",
				"createTime": "时间戳格式"
			}
		]
	}
}
```


## 获得全部字典数据列表


**接口地址**:`/admin-api/system/dict-data/list-all-simple`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>一般用于管理后台缓存字典数据在本地</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListDictDataSimpleRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|DictDataSimpleRespVO|
|&emsp;&emsp;dictType|字典类型|string||
|&emsp;&emsp;value|字典键值|string||
|&emsp;&emsp;label|字典标签|string||
|&emsp;&emsp;colorType|颜色类型，default、primary、success、info、warning、danger|string||
|&emsp;&emsp;cssClass|css 样式|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"dictType": "gender",
			"value": 1,
			"label": "男",
			"colorType": "default",
			"cssClass": "btn-visible"
		}
	]
}
```


## 获得全部字典数据列表


**接口地址**:`/admin-api/system/dict-data/simple-list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>一般用于管理后台缓存字典数据在本地</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListDictDataSimpleRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|DictDataSimpleRespVO|
|&emsp;&emsp;dictType|字典类型|string||
|&emsp;&emsp;value|字典键值|string||
|&emsp;&emsp;label|字典标签|string||
|&emsp;&emsp;colorType|颜色类型，default、primary、success、info、warning、danger|string||
|&emsp;&emsp;cssClass|css 样式|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"dictType": "gender",
			"value": 1,
			"label": "男",
			"colorType": "default",
			"cssClass": "btn-visible"
		}
	]
}
```


## -查询字典数据详细


**接口地址**:`/admin-api/system/dict-data/get`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultDictDataRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||DictDataRespVO|DictDataRespVO|
|&emsp;&emsp;id|字典数据编号|integer(int64)||
|&emsp;&emsp;sort|显示顺序|integer(int32)||
|&emsp;&emsp;label|字典标签|string||
|&emsp;&emsp;value|字典值|string||
|&emsp;&emsp;dictType|字典类型|string||
|&emsp;&emsp;status|状态,见 CommonStatusEnum 枚举|integer(int32)||
|&emsp;&emsp;colorType|颜色类型,default、primary、success、info、warning、danger|string||
|&emsp;&emsp;cssClass|css 样式|string||
|&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 1024,
		"sort": 1024,
		"label": "AHC",
		"value": "iocoder",
		"dictType": "sys_common_sex",
		"status": 1,
		"colorType": "default",
		"cssClass": "btn-visible",
		"remark": "我是一个角色",
		"createTime": "时间戳格式"
	}
}
```


## 导出字典数据


**接口地址**:`/admin-api/system/dict-data/export-excel`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|label|字典标签|query|false|string||
|dictType|字典类型，模糊匹配|query|false|string||
|status|展示状态，参见 CommonStatusEnum 枚举类|query|false|integer(int32)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||


**响应参数**:


暂无


**响应示例**:
```javascript

```


# 管理后台 - 部门


## 更新部门


**接口地址**:`/admin-api/system/dept/update`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "name": "AHC",
  "parentId": 1024,
  "sort": 1024,
  "leaderUserId": 2048,
  "phone": 15601691000,
  "email": "wmt@cn",
  "status": 1
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|deptSaveReqVO|管理后台 - 部门创建/修改 Request VO|body|true|DeptSaveReqVO|DeptSaveReqVO|
|&emsp;&emsp;id|部门编号||false|integer(int64)||
|&emsp;&emsp;name|部门名称||true|string||
|&emsp;&emsp;parentId|父部门 ID||false|integer(int64)||
|&emsp;&emsp;sort|显示顺序||true|integer(int32)||
|&emsp;&emsp;leaderUserId|负责人的用户编号||false|integer(int64)||
|&emsp;&emsp;phone|联系电话||false|string||
|&emsp;&emsp;email|邮箱||false|string(email)||
|&emsp;&emsp;status|状态,见 CommonStatusEnum 枚举||true|integer(int32)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 删除部门


**接口地址**:`/admin-api/system/dept/delete`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 批量删除部门


**接口地址**:`/admin-api/system/dept/delete-list`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|ids|编号列表|query|true|array|integer|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 创建部门


**接口地址**:`/admin-api/system/dept/create`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 1024,
  "name": "AHC",
  "parentId": 1024,
  "sort": 1024,
  "leaderUserId": 2048,
  "phone": 15601691000,
  "email": "wmt@cn",
  "status": 1
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|deptSaveReqVO|管理后台 - 部门创建/修改 Request VO|body|true|DeptSaveReqVO|DeptSaveReqVO|
|&emsp;&emsp;id|部门编号||false|integer(int64)||
|&emsp;&emsp;name|部门名称||true|string||
|&emsp;&emsp;parentId|父部门 ID||false|integer(int64)||
|&emsp;&emsp;sort|显示顺序||true|integer(int32)||
|&emsp;&emsp;leaderUserId|负责人的用户编号||false|integer(int64)||
|&emsp;&emsp;phone|联系电话||false|string||
|&emsp;&emsp;email|邮箱||false|string(email)||
|&emsp;&emsp;status|状态,见 CommonStatusEnum 枚举||true|integer(int32)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultLong|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": 0
}
```


## 获取部门列表


**接口地址**:`/admin-api/system/dept/list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|name|部门名称，模糊匹配|query|false|string||
|status|展示状态，参见 CommonStatusEnum 枚举类|query|false|integer(int32)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListDeptRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|DeptRespVO|
|&emsp;&emsp;id|部门编号|integer(int64)||
|&emsp;&emsp;name|部门名称|string||
|&emsp;&emsp;parentId|父部门 ID|integer(int64)||
|&emsp;&emsp;sort|显示顺序|integer(int32)||
|&emsp;&emsp;leaderUserId|负责人的用户编号|integer(int64)||
|&emsp;&emsp;phone|联系电话|string||
|&emsp;&emsp;email|邮箱|string||
|&emsp;&emsp;status|状态,见 CommonStatusEnum 枚举|integer(int32)||
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 1024,
			"name": "AHC",
			"parentId": 1024,
			"sort": 1024,
			"leaderUserId": 2048,
			"phone": 15601691000,
			"email": "wmt@cn",
			"status": 1,
			"createTime": "时间戳格式"
		}
	]
}
```


## 获取部门精简信息列表


**接口地址**:`/admin-api/system/dept/list-all-simple`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>只包含被开启的部门，主要用于前端的下拉选项</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListDeptSimpleRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|DeptSimpleRespVO|
|&emsp;&emsp;id|部门编号|integer(int64)||
|&emsp;&emsp;name|部门名称|string||
|&emsp;&emsp;parentId|父部门 ID|integer(int64)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 1024,
			"name": "AHC",
			"parentId": 1024
		}
	]
}
```


## 获取部门精简信息列表


**接口地址**:`/admin-api/system/dept/simple-list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>只包含被开启的部门，主要用于前端的下拉选项</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListDeptSimpleRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|DeptSimpleRespVO|
|&emsp;&emsp;id|部门编号|integer(int64)||
|&emsp;&emsp;name|部门名称|string||
|&emsp;&emsp;parentId|父部门 ID|integer(int64)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 1024,
			"name": "AHC",
			"parentId": 1024
		}
	]
}
```


## 获得部门信息


**接口地址**:`/admin-api/system/dept/get`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultDeptRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||DeptRespVO|DeptRespVO|
|&emsp;&emsp;id|部门编号|integer(int64)||
|&emsp;&emsp;name|部门名称|string||
|&emsp;&emsp;parentId|父部门 ID|integer(int64)||
|&emsp;&emsp;sort|显示顺序|integer(int32)||
|&emsp;&emsp;leaderUserId|负责人的用户编号|integer(int64)||
|&emsp;&emsp;phone|联系电话|string||
|&emsp;&emsp;email|邮箱|string||
|&emsp;&emsp;status|状态,见 CommonStatusEnum 枚举|integer(int32)||
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 1024,
		"name": "AHC",
		"parentId": 1024,
		"sort": 1024,
		"leaderUserId": 2048,
		"phone": 15601691000,
		"email": "wmt@cn",
		"status": 1,
		"createTime": "时间戳格式"
	}
}
```


# 管理后台 - 验证码


## 获得验证码


**接口地址**:`/admin-api/system/captcha/get`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "captchaId": "",
  "projectCode": "",
  "captchaType": "",
  "captchaOriginalPath": "",
  "captchaFontType": "",
  "captchaFontSize": 0,
  "secretKey": "",
  "originalImageBase64": "",
  "point": {
    "secretKey": "",
    "x": 0,
    "y": 0
  },
  "jigsawImageBase64": "",
  "wordList": [],
  "pointList": [
    {
      "x": 0,
      "y": 0
    }
  ],
  "pointJson": "",
  "token": "",
  "result": true,
  "captchaVerification": "",
  "clientUid": "",
  "ts": 0,
  "browserInfo": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|captchaVO|CaptchaVO|body|true|CaptchaVO|CaptchaVO|
|&emsp;&emsp;captchaId|||false|string||
|&emsp;&emsp;projectCode|||false|string||
|&emsp;&emsp;captchaType|||false|string||
|&emsp;&emsp;captchaOriginalPath|||false|string||
|&emsp;&emsp;captchaFontType|||false|string||
|&emsp;&emsp;captchaFontSize|||false|integer(int32)||
|&emsp;&emsp;secretKey|||false|string||
|&emsp;&emsp;originalImageBase64|||false|string||
|&emsp;&emsp;point|||false|PointVO|PointVO|
|&emsp;&emsp;&emsp;&emsp;secretKey|||false|string||
|&emsp;&emsp;&emsp;&emsp;x|||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;y|||false|integer(int32)||
|&emsp;&emsp;jigsawImageBase64|||false|string||
|&emsp;&emsp;wordList|||false|array|string|
|&emsp;&emsp;pointList|||false|array|object|
|&emsp;&emsp;pointJson|||false|string||
|&emsp;&emsp;token|||false|string||
|&emsp;&emsp;result|||false|boolean||
|&emsp;&emsp;captchaVerification|||false|string||
|&emsp;&emsp;clientUid|||false|string||
|&emsp;&emsp;ts|||false|integer(int64)||
|&emsp;&emsp;browserInfo|||false|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResponseModel|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|repCode||string||
|repMsg||string||
|repData||string||
|success||boolean||
|repCodeEnum|可用值:SUCCESS,ERROR,EXCEPTION,BLANK_ERROR,NULL_ERROR,NOT_NULL_ERROR,NOT_EXIST_ERROR,EXIST_ERROR,PARAM_TYPE_ERROR,PARAM_FORMAT_ERROR,API_CAPTCHA_INVALID,API_CAPTCHA_COORDINATE_ERROR,API_CAPTCHA_ERROR,API_CAPTCHA_BASEMAP_NULL,API_REQ_LIMIT_GET_ERROR,API_REQ_INVALID,API_REQ_LOCK_GET_ERROR,API_REQ_LIMIT_CHECK_ERROR,API_REQ_LIMIT_VERIFY_ERROR|string||


**响应示例**:
```javascript
{
	"repCode": "",
	"repMsg": "",
	"repData": {},
	"success": true,
	"repCodeEnum": ""
}
```


## 校验验证码


**接口地址**:`/admin-api/system/captcha/check`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "captchaId": "",
  "projectCode": "",
  "captchaType": "",
  "captchaOriginalPath": "",
  "captchaFontType": "",
  "captchaFontSize": 0,
  "secretKey": "",
  "originalImageBase64": "",
  "point": {
    "secretKey": "",
    "x": 0,
    "y": 0
  },
  "jigsawImageBase64": "",
  "wordList": [],
  "pointList": [
    {
      "x": 0,
      "y": 0
    }
  ],
  "pointJson": "",
  "token": "",
  "result": true,
  "captchaVerification": "",
  "clientUid": "",
  "ts": 0,
  "browserInfo": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|captchaVO|CaptchaVO|body|true|CaptchaVO|CaptchaVO|
|&emsp;&emsp;captchaId|||false|string||
|&emsp;&emsp;projectCode|||false|string||
|&emsp;&emsp;captchaType|||false|string||
|&emsp;&emsp;captchaOriginalPath|||false|string||
|&emsp;&emsp;captchaFontType|||false|string||
|&emsp;&emsp;captchaFontSize|||false|integer(int32)||
|&emsp;&emsp;secretKey|||false|string||
|&emsp;&emsp;originalImageBase64|||false|string||
|&emsp;&emsp;point|||false|PointVO|PointVO|
|&emsp;&emsp;&emsp;&emsp;secretKey|||false|string||
|&emsp;&emsp;&emsp;&emsp;x|||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;y|||false|integer(int32)||
|&emsp;&emsp;jigsawImageBase64|||false|string||
|&emsp;&emsp;wordList|||false|array|string|
|&emsp;&emsp;pointList|||false|array|object|
|&emsp;&emsp;pointJson|||false|string||
|&emsp;&emsp;token|||false|string||
|&emsp;&emsp;result|||false|boolean||
|&emsp;&emsp;captchaVerification|||false|string||
|&emsp;&emsp;clientUid|||false|string||
|&emsp;&emsp;ts|||false|integer(int64)||
|&emsp;&emsp;browserInfo|||false|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResponseModel|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|repCode||string||
|repMsg||string||
|repData||string||
|success||boolean||
|repCodeEnum|可用值:SUCCESS,ERROR,EXCEPTION,BLANK_ERROR,NULL_ERROR,NOT_NULL_ERROR,NOT_EXIST_ERROR,EXIST_ERROR,PARAM_TYPE_ERROR,PARAM_FORMAT_ERROR,API_CAPTCHA_INVALID,API_CAPTCHA_COORDINATE_ERROR,API_CAPTCHA_ERROR,API_CAPTCHA_BASEMAP_NULL,API_REQ_LIMIT_GET_ERROR,API_REQ_INVALID,API_REQ_LOCK_GET_ERROR,API_REQ_LIMIT_CHECK_ERROR,API_REQ_LIMIT_VERIFY_ERROR|string||


**响应示例**:
```javascript
{
	"repCode": "",
	"repMsg": "",
	"repData": {},
	"success": true,
	"repCodeEnum": ""
}
```


# 管理后台 - 认证


## 社交快捷登录，使用 code 授权码


**接口地址**:`/admin-api/system/auth/social-login`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>适合未登录的用户，但是社交账号已绑定用户</p>



**请求示例**:


```javascript
{
  "type": 10,
  "code": 1024,
  "state": "9b2ffbc1-7425-4155-9894-9d5c08541d62"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|authSocialLoginReqVO|管理后台 - 社交绑定登录 Request VO，使用 code 授权码 + 账号密码|body|true|AuthSocialLoginReqVO|AuthSocialLoginReqVO|
|&emsp;&emsp;type|社交平台的类型，参见 UserSocialTypeEnum 枚举值||true|integer(int32)||
|&emsp;&emsp;code|授权码||true|string||
|&emsp;&emsp;state|state||true|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultAuthLoginRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||AuthLoginRespVO|AuthLoginRespVO|
|&emsp;&emsp;userId|用户编号|integer(int64)||
|&emsp;&emsp;accessToken|访问令牌|string||
|&emsp;&emsp;refreshToken|刷新令牌|string||
|&emsp;&emsp;expiresTime|过期时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"userId": 1024,
		"accessToken": "happy",
		"refreshToken": "nice",
		"expiresTime": ""
	}
}
```


## 使用短信验证码登录


**接口地址**:`/admin-api/system/auth/sms-login`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "mobile": "wmtyuanma",
  "code": 1024
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|authSmsLoginReqVO|管理后台 - 短信验证码的登录 Request VO|body|true|AuthSmsLoginReqVO|AuthSmsLoginReqVO|
|&emsp;&emsp;mobile|手机号||true|string||
|&emsp;&emsp;code|短信验证码||true|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultAuthLoginRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||AuthLoginRespVO|AuthLoginRespVO|
|&emsp;&emsp;userId|用户编号|integer(int64)||
|&emsp;&emsp;accessToken|访问令牌|string||
|&emsp;&emsp;refreshToken|刷新令牌|string||
|&emsp;&emsp;expiresTime|过期时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"userId": 1024,
		"accessToken": "happy",
		"refreshToken": "nice",
		"expiresTime": ""
	}
}
```


## 发送手机验证码


**接口地址**:`/admin-api/system/auth/send-sms-code`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "captchaVerification": "PfcH6mgr8tpXuMWFjvW6YVaqrswIuwmWI5dsVZSg7sGpWtDCUbHuDEXl3cFB1+VvCC/rAkSwK8Fad52FSuncVg==",
  "mobile": "wmtyuanma",
  "scene": 1
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|authSmsSendReqVO|管理后台 - 发送手机验证码 Request VO|body|true|AuthSmsSendReqVO|AuthSmsSendReqVO|
|&emsp;&emsp;captchaVerification|验证码，验证码开启时，需要传递||true|string||
|&emsp;&emsp;mobile|手机号||true|string||
|&emsp;&emsp;scene|短信场景||true|integer(int32)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 重置密码


**接口地址**:`/admin-api/system/auth/reset-password`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "password": 1234,
  "mobile": 13312341234,
  "code": 123456
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|authResetPasswordReqVO|管理后台 - 短信重置账号密码 Request VO|body|true|AuthResetPasswordReqVO|AuthResetPasswordReqVO|
|&emsp;&emsp;password|密码||true|string||
|&emsp;&emsp;mobile|手机号||true|string||
|&emsp;&emsp;code|手机短信验证码||true|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 注册用户


**接口地址**:`/admin-api/system/auth/register`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "captchaVerification": "PfcH6mgr8tpXuMWFjvW6YVaqrswIuwmWI5dsVZSg7sGpWtDCUbHuDEXl3cFB1+VvCC/rAkSwK8Fad52FSuncVg==",
  "username": "wmt",
  "nickname": "wmt",
  "password": 123456
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|authRegisterReqVO|管理后台 - Register Request VO|body|true|AuthRegisterReqVO|AuthRegisterReqVO|
|&emsp;&emsp;captchaVerification|验证码，验证码开启时，需要传递||true|string||
|&emsp;&emsp;username|用户账号||true|string||
|&emsp;&emsp;nickname|用户昵称||true|string||
|&emsp;&emsp;password|密码||true|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultAuthLoginRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||AuthLoginRespVO|AuthLoginRespVO|
|&emsp;&emsp;userId|用户编号|integer(int64)||
|&emsp;&emsp;accessToken|访问令牌|string||
|&emsp;&emsp;refreshToken|刷新令牌|string||
|&emsp;&emsp;expiresTime|过期时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"userId": 1024,
		"accessToken": "happy",
		"refreshToken": "nice",
		"expiresTime": ""
	}
}
```


## 刷新令牌


**接口地址**:`/admin-api/system/auth/refresh-token`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|refreshToken|刷新令牌|query|true|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultAuthLoginRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||AuthLoginRespVO|AuthLoginRespVO|
|&emsp;&emsp;userId|用户编号|integer(int64)||
|&emsp;&emsp;accessToken|访问令牌|string||
|&emsp;&emsp;refreshToken|刷新令牌|string||
|&emsp;&emsp;expiresTime|过期时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"userId": 1024,
		"accessToken": "happy",
		"refreshToken": "nice",
		"expiresTime": ""
	}
}
```


## 登出系统


**接口地址**:`/admin-api/system/auth/logout`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultBoolean|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": true
}
```


## 使用账号密码登录


**接口地址**:`/admin-api/system/auth/login`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "captchaVerification": "PfcH6mgr8tpXuMWFjvW6YVaqrswIuwmWI5dsVZSg7sGpWtDCUbHuDEXl3cFB1+VvCC/rAkSwK8Fad52FSuncVg==",
  "username": "wmtyuanma",
  "password": "buzhidao",
  "socialType": 10,
  "socialCode": 1024,
  "socialState": "9b2ffbc1-7425-4155-9894-9d5c08541d62",
  "socialCodeValid": true
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|authLoginReqVO|管理后台 - 账号密码登录 Request VO，如果登录并绑定社交用户，需要传递 social 开头的参数|body|true|AuthLoginReqVO|AuthLoginReqVO|
|&emsp;&emsp;captchaVerification|验证码，验证码开启时，需要传递||true|string||
|&emsp;&emsp;username|账号||true|string||
|&emsp;&emsp;password|密码||true|string||
|&emsp;&emsp;socialType|社交平台的类型，参见 SocialTypeEnum 枚举值||true|integer(int32)||
|&emsp;&emsp;socialCode|授权码||true|string||
|&emsp;&emsp;socialState|state||true|string||
|&emsp;&emsp;socialCodeValid|||false|boolean||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultAuthLoginRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||AuthLoginRespVO|AuthLoginRespVO|
|&emsp;&emsp;userId|用户编号|integer(int64)||
|&emsp;&emsp;accessToken|访问令牌|string||
|&emsp;&emsp;refreshToken|刷新令牌|string||
|&emsp;&emsp;expiresTime|过期时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"userId": 1024,
		"accessToken": "happy",
		"refreshToken": "nice",
		"expiresTime": ""
	}
}
```


## 社交授权的跳转


**接口地址**:`/admin-api/system/auth/social-auth-redirect`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|type|社交类型|query|true|integer(int32)||
|redirectUri|回调路径|query|true|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 获取登录用户的权限信息


**接口地址**:`/admin-api/system/auth/get-permission-info`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultAuthPermissionInfoRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||AuthPermissionInfoRespVO|AuthPermissionInfoRespVO|
|&emsp;&emsp;user|用户信息|UserVO|UserVO|
|&emsp;&emsp;&emsp;&emsp;id|用户编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;nickname|用户昵称|string||
|&emsp;&emsp;&emsp;&emsp;avatar|用户头像|string||
|&emsp;&emsp;&emsp;&emsp;deptId|部门编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;username|用户账号|string||
|&emsp;&emsp;&emsp;&emsp;email|用户邮箱|string||
|&emsp;&emsp;roles|角色标识数组|array|string|
|&emsp;&emsp;permissions|操作权限数组|array|string|
|&emsp;&emsp;menus|菜单树|array|MenuVO|
|&emsp;&emsp;&emsp;&emsp;id|菜单名称|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;parentId|父菜单 ID|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;name|菜单名称|string||
|&emsp;&emsp;&emsp;&emsp;path|路由地址,仅菜单类型为菜单或者目录时，才需要传|string||
|&emsp;&emsp;&emsp;&emsp;component|组件路径,仅菜单类型为菜单时，才需要传|string||
|&emsp;&emsp;&emsp;&emsp;componentName|组件名|string||
|&emsp;&emsp;&emsp;&emsp;icon|菜单图标,仅菜单类型为菜单或者目录时，才需要传|string||
|&emsp;&emsp;&emsp;&emsp;visible|是否可见|boolean||
|&emsp;&emsp;&emsp;&emsp;keepAlive|是否缓存|boolean||
|&emsp;&emsp;&emsp;&emsp;alwaysShow|是否总是显示|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"user": {
			"id": 1024,
			"nickname": "AHC源码",
			"avatar": "https://www.wmt.cn/xx.jpg",
			"deptId": 2048,
			"username": "wmt",
			"email": "wmt@cn"
		},
		"roles": [],
		"permissions": [],
		"menus": [
			{
				"id": "AHC",
				"parentId": 1024,
				"name": "AHC",
				"path": "post",
				"component": "system/post/index",
				"componentName": "SystemUser",
				"icon": "/menu/list",
				"visible": false,
				"keepAlive": false,
				"alwaysShow": false
			}
		]
	}
}
```


# 用户 App - 租户


## 使用域名，获得租户信息


**接口地址**:`/app-api/system/tenant/get-by-website`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>根据用户的域名，获得租户信息</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|website|域名|query|true|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultAppTenantRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||AppTenantRespVO|AppTenantRespVO|
|&emsp;&emsp;id|租户编号|integer(int64)||
|&emsp;&emsp;name|租户名|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 1024,
		"name": "AHC"
	}
}
```


# 用户 App - 字典数据


## 根据字典类型查询字典数据信息


**接口地址**:`/app-api/system/dict-data/type`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|type|字典类型|query|true|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListAppDictDataRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|AppDictDataRespVO|
|&emsp;&emsp;id|字典数据编号|integer(int64)||
|&emsp;&emsp;label|字典标签|string||
|&emsp;&emsp;value|字典值|string||
|&emsp;&emsp;dictType|字典类型|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 1024,
			"label": "AHC",
			"value": "iocoder",
			"dictType": "sys_common_sex"
		}
	]
}
```


# 用户 App - 地区


## 获得地区树


**接口地址**:`/app-api/system/area/tree`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListAppAreaNodeRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|AppAreaNodeRespVO|
|&emsp;&emsp;id|编号|integer(int32)||
|&emsp;&emsp;name|名字|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 110000,
			"name": "北京"
		}
	]
}
```


# 管理后台 - 短信日志


## 获得短信日志分页


**接口地址**:`/admin-api/system/sms-log/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|channelId|短信渠道编号|query|false|integer(int64)||
|templateId|模板编号|query|false|integer(int64)||
|mobile|手机号|query|false|string||
|sendStatus|发送状态，参见 SmsSendStatusEnum 枚举类|query|false|integer(int32)||
|sendTime|发送时间|query|false|array|string|
|receiveStatus|接收状态，参见 SmsReceiveStatusEnum 枚举类|query|false|integer(int32)||
|receiveTime|接收时间|query|false|array|string|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultPageResultSmsLogRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||PageResultSmsLogRespVO|PageResultSmsLogRespVO|
|&emsp;&emsp;total|总量|integer(int64)||
|&emsp;&emsp;list|数据|array|SmsLogRespVO|
|&emsp;&emsp;&emsp;&emsp;id|编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;channelId|短信渠道编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;channelCode|短信渠道编码|string||
|&emsp;&emsp;&emsp;&emsp;templateId|模板编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;templateCode|模板编码|string||
|&emsp;&emsp;&emsp;&emsp;templateType|短信类型|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;templateContent|短信内容|string||
|&emsp;&emsp;&emsp;&emsp;templateParams|短信参数|object||
|&emsp;&emsp;&emsp;&emsp;apiTemplateId|短信 API 的模板编号|string||
|&emsp;&emsp;&emsp;&emsp;mobile|手机号|string||
|&emsp;&emsp;&emsp;&emsp;userId|用户编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;userType|用户类型|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;sendStatus|发送状态|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;sendTime|发送时间|string(date-time)||
|&emsp;&emsp;&emsp;&emsp;apiSendCode|短信 API 发送结果的编码|string||
|&emsp;&emsp;&emsp;&emsp;apiSendMsg|短信 API 发送失败的提示|string||
|&emsp;&emsp;&emsp;&emsp;apiRequestId|短信 API 发送返回的唯一请求 ID|string||
|&emsp;&emsp;&emsp;&emsp;apiSerialNo|短信 API 发送返回的序号|string||
|&emsp;&emsp;&emsp;&emsp;receiveStatus|接收状态|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;receiveTime|接收时间|string(date-time)||
|&emsp;&emsp;&emsp;&emsp;apiReceiveCode|API 接收结果的编码|string||
|&emsp;&emsp;&emsp;&emsp;apiReceiveMsg|API 接收结果的说明|string||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 1024,
				"channelId": 10,
				"channelCode": "ALIYUN",
				"templateId": 20,
				"templateCode": "test-01",
				"templateType": 1,
				"templateContent": "你好，你的验证码是 1024",
				"templateParams": "name,code",
				"apiTemplateId": "SMS_207945135",
				"mobile": 15601691300,
				"userId": 10,
				"userType": 1,
				"sendStatus": 1,
				"sendTime": "",
				"apiSendCode": "SUCCESS",
				"apiSendMsg": "成功",
				"apiRequestId": "3837C6D3-B96F-428C-BBB2-86135D4B5B99",
				"apiSerialNo": 62923244790,
				"receiveStatus": 0,
				"receiveTime": "",
				"apiReceiveCode": "DELIVRD",
				"apiReceiveMsg": "用户接收成功",
				"createTime": ""
			}
		]
	}
}
```


## 获得短信日志


**接口地址**:`/admin-api/system/sms-log/get`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultSmsLogRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||SmsLogRespVO|SmsLogRespVO|
|&emsp;&emsp;id|编号|integer(int64)||
|&emsp;&emsp;channelId|短信渠道编号|integer(int64)||
|&emsp;&emsp;channelCode|短信渠道编码|string||
|&emsp;&emsp;templateId|模板编号|integer(int64)||
|&emsp;&emsp;templateCode|模板编码|string||
|&emsp;&emsp;templateType|短信类型|integer(int32)||
|&emsp;&emsp;templateContent|短信内容|string||
|&emsp;&emsp;templateParams|短信参数|object||
|&emsp;&emsp;apiTemplateId|短信 API 的模板编号|string||
|&emsp;&emsp;mobile|手机号|string||
|&emsp;&emsp;userId|用户编号|integer(int64)||
|&emsp;&emsp;userType|用户类型|integer(int32)||
|&emsp;&emsp;sendStatus|发送状态|integer(int32)||
|&emsp;&emsp;sendTime|发送时间|string(date-time)||
|&emsp;&emsp;apiSendCode|短信 API 发送结果的编码|string||
|&emsp;&emsp;apiSendMsg|短信 API 发送失败的提示|string||
|&emsp;&emsp;apiRequestId|短信 API 发送返回的唯一请求 ID|string||
|&emsp;&emsp;apiSerialNo|短信 API 发送返回的序号|string||
|&emsp;&emsp;receiveStatus|接收状态|integer(int32)||
|&emsp;&emsp;receiveTime|接收时间|string(date-time)||
|&emsp;&emsp;apiReceiveCode|API 接收结果的编码|string||
|&emsp;&emsp;apiReceiveMsg|API 接收结果的说明|string||
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 1024,
		"channelId": 10,
		"channelCode": "ALIYUN",
		"templateId": 20,
		"templateCode": "test-01",
		"templateType": 1,
		"templateContent": "你好，你的验证码是 1024",
		"templateParams": "name,code",
		"apiTemplateId": "SMS_207945135",
		"mobile": 15601691300,
		"userId": 10,
		"userType": 1,
		"sendStatus": 1,
		"sendTime": "",
		"apiSendCode": "SUCCESS",
		"apiSendMsg": "成功",
		"apiRequestId": "3837C6D3-B96F-428C-BBB2-86135D4B5B99",
		"apiSerialNo": 62923244790,
		"receiveStatus": 0,
		"receiveTime": "",
		"apiReceiveCode": "DELIVRD",
		"apiReceiveMsg": "用户接收成功",
		"createTime": ""
	}
}
```


## 导出短信日志 Excel


**接口地址**:`/admin-api/system/sms-log/export-excel`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|channelId|短信渠道编号|query|false|integer(int64)||
|templateId|模板编号|query|false|integer(int64)||
|mobile|手机号|query|false|string||
|sendStatus|发送状态，参见 SmsSendStatusEnum 枚举类|query|false|integer(int32)||
|sendTime|发送时间|query|false|array|string|
|receiveStatus|接收状态，参见 SmsReceiveStatusEnum 枚举类|query|false|integer(int32)||
|receiveTime|接收时间|query|false|array|string|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||


**响应参数**:


暂无


**响应示例**:
```javascript

```


# 管理后台 - 操作日志


## 查看操作日志分页列表


**接口地址**:`/admin-api/system/operate-log/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|userId|用户编号|query|false|integer(int64)||
|bizId|操作模块业务编号|query|false|integer(int64)||
|type|操作模块，模拟匹配|query|false|string||
|subType|操作名，模拟匹配|query|false|string||
|action|操作明细，模拟匹配|query|false|string||
|createTime|开始时间|query|false|array|string|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultPageResultOperateLogRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||PageResultOperateLogRespVO|PageResultOperateLogRespVO|
|&emsp;&emsp;total|总量|integer(int64)||
|&emsp;&emsp;list|数据|array|OperateLogRespVO|
|&emsp;&emsp;&emsp;&emsp;id|日志编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;traceId|链路追踪编号|string||
|&emsp;&emsp;&emsp;&emsp;userId|用户编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;userName|用户昵称|string||
|&emsp;&emsp;&emsp;&emsp;userType|用户类型|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;type|操作模块类型|string||
|&emsp;&emsp;&emsp;&emsp;subType|操作名|string||
|&emsp;&emsp;&emsp;&emsp;bizId|操作模块业务编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;action|操作明细|string||
|&emsp;&emsp;&emsp;&emsp;extra|拓展字段|string||
|&emsp;&emsp;&emsp;&emsp;requestMethod|请求方法名|string||
|&emsp;&emsp;&emsp;&emsp;requestUrl|请求地址|string||
|&emsp;&emsp;&emsp;&emsp;userIp|用户 IP|string||
|&emsp;&emsp;&emsp;&emsp;userAgent|浏览器 UserAgent|string||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string(date-time)||
|&emsp;&emsp;&emsp;&emsp;transMap||object||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 1024,
				"traceId": "89aca178-a370-411c-ae02-3f0d672be4ab",
				"userId": 1024,
				"userName": "wmt",
				"userType": 1,
				"type": "订单",
				"subType": "创建订单",
				"bizId": 1,
				"action": "修改编号为 1 的用户信息，将性别从男改成女，将姓名从AHC改成源码。",
				"extra": "{'orderId': 1}",
				"requestMethod": "GET",
				"requestUrl": "/xxx/yyy",
				"userIp": "127.0.0.1",
				"userAgent": "Mozilla/5.0",
				"createTime": "",
				"transMap": {}
			}
		]
	}
}
```


## 查看操作日志


**接口地址**:`/admin-api/system/operate-log/get`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultOperateLogRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||OperateLogRespVO|OperateLogRespVO|
|&emsp;&emsp;id|日志编号|integer(int64)||
|&emsp;&emsp;traceId|链路追踪编号|string||
|&emsp;&emsp;userId|用户编号|integer(int64)||
|&emsp;&emsp;userName|用户昵称|string||
|&emsp;&emsp;userType|用户类型|integer(int32)||
|&emsp;&emsp;type|操作模块类型|string||
|&emsp;&emsp;subType|操作名|string||
|&emsp;&emsp;bizId|操作模块业务编号|integer(int64)||
|&emsp;&emsp;action|操作明细|string||
|&emsp;&emsp;extra|拓展字段|string||
|&emsp;&emsp;requestMethod|请求方法名|string||
|&emsp;&emsp;requestUrl|请求地址|string||
|&emsp;&emsp;userIp|用户 IP|string||
|&emsp;&emsp;userAgent|浏览器 UserAgent|string||
|&emsp;&emsp;createTime|创建时间|string(date-time)||
|&emsp;&emsp;transMap||object||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 1024,
		"traceId": "89aca178-a370-411c-ae02-3f0d672be4ab",
		"userId": 1024,
		"userName": "wmt",
		"userType": 1,
		"type": "订单",
		"subType": "创建订单",
		"bizId": 1,
		"action": "修改编号为 1 的用户信息，将性别从男改成女，将姓名从AHC改成源码。",
		"extra": "{'orderId': 1}",
		"requestMethod": "GET",
		"requestUrl": "/xxx/yyy",
		"userIp": "127.0.0.1",
		"userAgent": "Mozilla/5.0",
		"createTime": "",
		"transMap": {}
	}
}
```


## 导出操作日志


**接口地址**:`/admin-api/system/operate-log/export-excel`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|userId|用户编号|query|false|integer(int64)||
|bizId|操作模块业务编号|query|false|integer(int64)||
|type|操作模块，模拟匹配|query|false|string||
|subType|操作名，模拟匹配|query|false|string||
|action|操作明细，模拟匹配|query|false|string||
|createTime|开始时间|query|false|array|string|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||


**响应参数**:


暂无


**响应示例**:
```javascript

```


# 管理后台 - 邮件日志


## 获得邮箱日志分页


**接口地址**:`/admin-api/system/mail-log/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|userId|用户编号|query|false|integer(int64)||
|userType|用户类型，参见 UserTypeEnum 枚举|query|false|integer(int32)||
|toMail|接收邮箱地址，模糊匹配|query|false|string||
|accountId|邮箱账号编号|query|false|integer(int64)||
|templateId|模板编号|query|false|integer(int64)||
|sendStatus|发送状态，参见 MailSendStatusEnum 枚举|query|false|integer(int32)||
|sendTime|发送时间|query|false|array|string|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultPageResultMailLogRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||PageResultMailLogRespVO|PageResultMailLogRespVO|
|&emsp;&emsp;total|总量|integer(int64)||
|&emsp;&emsp;list|数据|array|MailLogRespVO|
|&emsp;&emsp;&emsp;&emsp;id|编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;userId|用户编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;userType|用户类型，参见 UserTypeEnum 枚举|string(byte)||
|&emsp;&emsp;&emsp;&emsp;toMails|接收邮箱地址|array|string|
|&emsp;&emsp;&emsp;&emsp;ccMails|抄送邮箱地址|array|string|
|&emsp;&emsp;&emsp;&emsp;bccMails|密送邮箱地址|array|string|
|&emsp;&emsp;&emsp;&emsp;accountId|邮箱账号编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;fromMail|发送邮箱地址|string||
|&emsp;&emsp;&emsp;&emsp;templateId|模板编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;templateCode|模板编码|string||
|&emsp;&emsp;&emsp;&emsp;templateNickname|模版发送人名称|string||
|&emsp;&emsp;&emsp;&emsp;templateTitle|邮件标题|string||
|&emsp;&emsp;&emsp;&emsp;templateContent|邮件内容|string||
|&emsp;&emsp;&emsp;&emsp;templateParams|邮件参数|object||
|&emsp;&emsp;&emsp;&emsp;sendStatus|发送状态，参见 MailSendStatusEnum 枚举|string(byte)||
|&emsp;&emsp;&emsp;&emsp;sendTime|发送时间|string(date-time)||
|&emsp;&emsp;&emsp;&emsp;sendMessageId|发送返回的消息 ID|string||
|&emsp;&emsp;&emsp;&emsp;sendException|发送异常|string||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 31020,
				"userId": 30883,
				"userType": 2,
				"toMails": "user1@example.com, user2@example.com",
				"ccMails": "user3@example.com, user4@example.com",
				"bccMails": "user5@example.com, user6@example.com",
				"accountId": 18107,
				"fromMail": "85757@qq.com",
				"templateId": 5678,
				"templateCode": "test_01",
				"templateNickname": "李四",
				"templateTitle": "测试标题",
				"templateContent": "测试内容",
				"templateParams": {},
				"sendStatus": 1,
				"sendTime": "",
				"sendMessageId": 28568,
				"sendException": "",
				"createTime": ""
			}
		]
	}
}
```


## 获得邮箱日志


**接口地址**:`/admin-api/system/mail-log/get`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|编号|query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultMailLogRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||MailLogRespVO|MailLogRespVO|
|&emsp;&emsp;id|编号|integer(int64)||
|&emsp;&emsp;userId|用户编号|integer(int64)||
|&emsp;&emsp;userType|用户类型，参见 UserTypeEnum 枚举|string(byte)||
|&emsp;&emsp;toMails|接收邮箱地址|array|string|
|&emsp;&emsp;ccMails|抄送邮箱地址|array|string|
|&emsp;&emsp;bccMails|密送邮箱地址|array|string|
|&emsp;&emsp;accountId|邮箱账号编号|integer(int64)||
|&emsp;&emsp;fromMail|发送邮箱地址|string||
|&emsp;&emsp;templateId|模板编号|integer(int64)||
|&emsp;&emsp;templateCode|模板编码|string||
|&emsp;&emsp;templateNickname|模版发送人名称|string||
|&emsp;&emsp;templateTitle|邮件标题|string||
|&emsp;&emsp;templateContent|邮件内容|string||
|&emsp;&emsp;templateParams|邮件参数|object||
|&emsp;&emsp;sendStatus|发送状态，参见 MailSendStatusEnum 枚举|string(byte)||
|&emsp;&emsp;sendTime|发送时间|string(date-time)||
|&emsp;&emsp;sendMessageId|发送返回的消息 ID|string||
|&emsp;&emsp;sendException|发送异常|string||
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 31020,
		"userId": 30883,
		"userType": 2,
		"toMails": "user1@example.com, user2@example.com",
		"ccMails": "user3@example.com, user4@example.com",
		"bccMails": "user5@example.com, user6@example.com",
		"accountId": 18107,
		"fromMail": "85757@qq.com",
		"templateId": 5678,
		"templateCode": "test_01",
		"templateNickname": "李四",
		"templateTitle": "测试标题",
		"templateContent": "测试内容",
		"templateParams": {},
		"sendStatus": 1,
		"sendTime": "",
		"sendMessageId": 28568,
		"sendException": "",
		"createTime": ""
	}
}
```


# 管理后台 - 登录日志


## 获得登录日志分页列表


**接口地址**:`/admin-api/system/login-log/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|userIp|用户 IP，模拟匹配|query|false|string||
|username|用户账号，模拟匹配|query|false|string||
|status|操作状态|query|false|boolean||
|createTime|登录时间|query|false|array|string|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultPageResultLoginLogRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||PageResultLoginLogRespVO|PageResultLoginLogRespVO|
|&emsp;&emsp;total|总量|integer(int64)||
|&emsp;&emsp;list|数据|array|LoginLogRespVO|
|&emsp;&emsp;&emsp;&emsp;id|日志编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;logType|日志类型，参见 LoginLogTypeEnum 枚举类|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;userId|用户编号|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;userType|用户类型，参见 UserTypeEnum 枚举|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;traceId|链路追踪编号|string||
|&emsp;&emsp;&emsp;&emsp;username|用户账号|string||
|&emsp;&emsp;&emsp;&emsp;result|登录结果，参见 LoginResultEnum 枚举类|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;userIp|用户 IP|string||
|&emsp;&emsp;&emsp;&emsp;userAgent|浏览器 UserAgent|string||
|&emsp;&emsp;&emsp;&emsp;createTime|登录时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 1024,
				"logType": 1,
				"userId": 666,
				"userType": 2,
				"traceId": "89aca178-a370-411c-ae02-3f0d672be4ab",
				"username": "wmt",
				"result": 1,
				"userIp": "127.0.0.1",
				"userAgent": "Mozilla/5.0",
				"createTime": ""
			}
		]
	}
}
```


## 获得登录日志


**接口地址**:`/admin-api/system/login-log/get`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id||query|true|integer(int64)||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultLoginLogRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||LoginLogRespVO|LoginLogRespVO|
|&emsp;&emsp;id|日志编号|integer(int64)||
|&emsp;&emsp;logType|日志类型，参见 LoginLogTypeEnum 枚举类|integer(int32)||
|&emsp;&emsp;userId|用户编号|integer(int64)||
|&emsp;&emsp;userType|用户类型，参见 UserTypeEnum 枚举|integer(int32)||
|&emsp;&emsp;traceId|链路追踪编号|string||
|&emsp;&emsp;username|用户账号|string||
|&emsp;&emsp;result|登录结果，参见 LoginResultEnum 枚举类|integer(int32)||
|&emsp;&emsp;userIp|用户 IP|string||
|&emsp;&emsp;userAgent|浏览器 UserAgent|string||
|&emsp;&emsp;createTime|登录时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 1024,
		"logType": 1,
		"userId": 666,
		"userType": 2,
		"traceId": "89aca178-a370-411c-ae02-3f0d672be4ab",
		"username": "wmt",
		"result": 1,
		"userIp": "127.0.0.1",
		"userAgent": "Mozilla/5.0",
		"createTime": ""
	}
}
```


## 导出登录日志 Excel


**接口地址**:`/admin-api/system/login-log/export-excel`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNo|页码，从 1 开始|query|true|integer(int32)||
|pageSize|每页条数，最大值为 200|query|true|integer(int32)||
|userIp|用户 IP，模拟匹配|query|false|string||
|username|用户账号，模拟匹配|query|false|string||
|status|操作状态|query|false|boolean||
|createTime|登录时间|query|false|array|string|
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||


**响应参数**:


暂无


**响应示例**:
```javascript

```


# 管理后台 - 地区


## 获得地区树


**接口地址**:`/admin-api/system/area/tree`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultListAreaNodeRespVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||array|AreaNodeRespVO|
|&emsp;&emsp;id|编号|integer(int32)||
|&emsp;&emsp;name|名字|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 110000,
			"name": "北京"
		}
	]
}
```


## 获得 IP 对应的地区名


**接口地址**:`/admin-api/system/area/get-by-ip`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|ip|IP|query|true|string||
|tenant-id|租户编号|header|false|integer(int32)||
|Authorization|认证 Token|header|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|CommonResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|msg||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```