<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
<title>角色列表</title>
</head>
<body>
<div region="center" title="角色列表">
	<table id="list"></table>
</div>
<script>
	$(function(){
		$('#list').datagrid({
			nowrap: true,
			striped: true,
			collapsible:true,
			fitColumns: true,
			rownumbers: true,
			pageList:[20],
			url:'${base}/system/role/data.json',
			idField:'id',
			frozenColumns:[[
		                    {field:'id',checkbox:true}
		                ]],
			columns:[[
						{field:'name',title:'角色描述',width:200},
						{field:'rolename',title:'角色标志',width:200,align:'center'}
					]],
					toolbar: [{
			            text: '新增',
			            iconCls: 'icon-add',
			            handler:function(){
							location.href ='${base}/system/role/input${ext}';
						}
			        }, '-', {
			            text: '修改',
			            iconCls: 'icon-edit',
			            handler:function(){
				        	var ids = [];
							var rows = $('#list').datagrid('getSelections');
							if(rows.length!=1){
								$.messager.alert('提示','必须选择一行!','warning');
								$('#list').datagrid('clearSelections');
								return false;
							}
							location.href ='${base}/system/role/input${ext}?id='+rows[0].id;
						}
			        }, '-', {
			            text: '删除',
			            iconCls: 'icon-cancel',
			            handler:function(){
				        	var ids = [];
							var rows = $('#list').datagrid('getSelections');
							if(rows.length<1){
								$.messager.alert('提示','至少选择一行!','warning');
								return false;
							}
							for(var i=0;i<rows.length;i++){
								ids.push(rows[i].id);
							}
							$.messager.confirm('提示','确认删除吗?',function(d){
					            if(d){
					            	$.ajax({
										   type: "get",
										   url: "${base}/system/role/delete${ext}",
										   data: "ids="+ids.join(','),
										   success: function(msg){
											   $.messager.show({
													title:'提示',
													msg:'删除成功！',
													timeout:3000,
													showType:'slide'
												});
										   }
									});
									$('#list').datagrid('reload');
									$('#list').datagrid('clearSelections');
					            }
					        });
						}
			        }, '-', {
			            text: '查找',
			            iconCls: 'icon-search',
			            handler:function(){
							alert('查找');
						}
			        }],
			pagination:true
		});
	});
</script>
</body>
</html>