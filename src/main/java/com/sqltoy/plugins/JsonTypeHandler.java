/**
 * 
 */
package com.sqltoy.plugins;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.sagacity.sqltoy.plugins.TypeHandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @project sagacity-sqltoy
 * @description 针对fastJson的默认实现
 * @author zhongxuchen
 * @version v1.0, Date:2020-11-25
 * @modify 2020-11-25,修改说明
 */
public class JsonTypeHandler extends TypeHandler {

	/**
	 * <li>返回true表示类型匹配上，并完成了setValue赋值</li>
	 * <li>返回false 表示常规类型,交回框架自行处理</li>
	 */
	@Override
	public boolean setValue(PreparedStatement pst, int paramIndex, int jdbcType, Object value) throws SQLException {
		// 通过quickvo.xml 中jdbc-type 里面直接设置int数字,来区分特定的类型
		if (jdbcType == 1021) {
			pst.setString(paramIndex, JSONObject.toJSONString(value));
			return true;
		}
		return false;
	}

	/*
	 * <li>1、返回null表示属于常规类型，交回框架完成处理</li>
	 * <li>2、返回非null,表示特殊类型，完成了类型转换可直接映射到VO属性</li>
	 */
	@Override
	public Object toJavaType(String javaTypeName, Class genericType, Object jdbcValue) throws Exception {
		if (javaTypeName.equalsIgnoreCase("java.lang.string")) {
			return jdbcValue.toString();
		}
		// 是一个VO对象
		if (javaTypeName.contains("com.sqltoy")) {
			return JSON.parseObject(jdbcValue.toString(), Class.forName(javaTypeName));
		}
		if (javaTypeName.toLowerCase().contains("jsonobject")) {
			return JSON.parse(jdbcValue.toString());
		} else if (javaTypeName.toLowerCase().contains("jsonarray")) {
			return JSONArray.parseArray(jdbcValue.toString());
		}

		// List<VO>形式
		if (javaTypeName.equalsIgnoreCase("java.util.List") && genericType != null) {
			return JSONArray.parseArray(jdbcValue.toString(), genericType);
		}

		// 其他场景表示非json返回null交框架自行处理
		return null;
	}

}
