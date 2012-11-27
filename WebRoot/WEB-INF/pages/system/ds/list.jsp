<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="../../global/head.jsp"%>
<title>${title}——数据源配置</title>
</head>
<body class="easyui-layout">
	<%@ include file="../../global/title.jsp"%>
	<%@ include file="../../global/menu.jsp"%>
<div region="center" title="数据源配置">
	<c:forEach items="${dataSources}" var="dataSource">
			<div style=" padding: 8px;">
				<div style="border: solid 1px ; border-color :#afafaf;width: 450px;float: left;">
					<form id="${dataSource.key}Form" name="${dataSource.key}Form" action="${base}/system/ds/save${ext}" method="post">
						<input type="hidden" name="dsName" value="${dataSource.key}"/>
						<table>
							<tr>
								<td nowrap="nowrap">数据源名称:</td>
								<td>${dataSource.key}</td>
							</tr>
							<tr>
								<td nowrap="nowrap">连接地址:</td>
								<td><input name="${dataSource.key}.url" value="${dataSource.value.url}" style="width: 250px;"/></td>
							</tr>
							<tr>
								<td nowrap="nowrap">用户名:</td>
								<td><input name="${dataSource.key}.username" value="${dataSource.value.username}" style="width: 150px;"/></td>
							</tr>
							<tr>
								<td nowrap="nowrap">密码:</td>
								<td><input name="${dataSource.key}.password" value="${dataSource.value.password}" style="width: 200px;"/></td>
							</tr>
							<tr>
								<td nowrap="nowrap">连接池最大值:</td>
								<td style="height: 60px;">
								<input name="${dataSource.key}.poolMax" value="${dataSource.value.poolMax}" class="easyui-slider" showTip="true" max="200" rule="[1,'|',50,'|',100,'|',150,'|',200]" style="width:300px">
								</td>
							</tr>
							<tr>
								<td nowrap="nowrap">繁忙连接生命周期:</td>
								<td><input name="${dataSource.key}.busyConnectTimeOut" value="${dataSource.value.busyConnectTimeOut}" class="easyui-numberspinner" style="width: 100px;"/>(秒)</td>
							</tr>
							<tr>
								<td nowrap="nowrap">空闲连接生命周期:</td>
								<td><input name="${dataSource.key}.connectLife" value="${dataSource.value.connectLife}" class="easyui-numberspinner" style="width: 100px;"/>(秒)</td>
							</tr>
							<tr>
								<td nowrap="nowrap">线程监控执行周期:</td>
								<td><input name="${dataSource.key}.poolThreadTime" value="${dataSource.value.poolThreadTime}" class="easyui-numberspinner" style="width: 100px;"/>(秒)</td>
							</tr>
							<tr>
								<td nowrap="nowrap">连接超时:</td>
								<td><input name="${dataSource.key}.loginTimeout" value="${dataSource.value.loginTimeout}" class="easyui-numberspinner" style="width: 100px;"/>(秒)</td>
							</tr>
							<tr>
								<td></td>
								<td align="right">
								<button type="submit" class="easyui-linkbutton" iconCls="icon-save">提交</button>
								<a class="easyui-linkbutton" href="${base}/system/ds/monitor${ext}?dsName=${dataSource.key}" iconCls="icon-report">监控</a>
								</td>
							</tr>
						</table>
					</form>
				</div>
			</div>
	</c:forEach>
</div>
<script>
	var message = '<paramecium:successMessage/><paramecium:errorMessage/>';
	if(message!=''&&message!='null'){
		$.messager.show({title:'提示',msg:message,timeout:3000,showType:'slide'});
	}
</script>
</body>
</html>