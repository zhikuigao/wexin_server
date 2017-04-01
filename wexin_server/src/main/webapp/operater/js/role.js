var role_module = 'role';

// 从easyui 官网文档中拷贝
// http://www.jeasyui.com/demo/main/index.php?plugin=DataGrid&theme=default&dir=ltr&pitem=#
$(function() {
	var pager = $(getDataGridIdWithPound(role_module)).datagrid().datagrid(
			'getPager');
	pager.pagination({
		pageList : [ 10, 20, 30 ],
		displayMsg : '共 {total} 条记录，从 {from} 到 {to} ',
		buttons : [ {
			iconCls : 'icon-search',
			text : '查询',
			handler : function() {
				initRoleSearchWindow();
			}
		}, {
			iconCls : 'icon-add',
			text : '新增',
			handler : function() {
				initRoleAddWindow();
			}
		}, {
			iconCls : 'icon-remove',
			text : '删除',
			handler : function() {
				deleteRows(role_module);
			}
		}, {
			iconCls : 'icon-undo',
			text : '取消查询',
			handler : function() {
				list(role_module);
			}
		}, {
			iconCls : 'icon-add',
			text : '绑定权限',
			handler : function() {
				openRolePermitDragDropWindow();
			}
		} ]
	});
	$('#role_permit_leftDataGrid').datagrid('enableDnd');
	$('#role_permit_leftDataGrid').datagrid({// 
		onLoadSuccess : function() {
			$(this).datagrid('enableDnd');
		}
	});
	$('#role_permit_rightDataGrid').datagrid({// 
		onLoadSuccess : function() {
			$(this).datagrid('enableDnd');
		}
	});
	var permitPager = $('#role_permit_leftDataGrid').datagrid().datagrid(
			'getPager'); // get the pager of datagrid
	permitPager.pagination({
		showPageList : false,
		showRefresh : true,
		displayMsg : '',
		beforePageText : '第',
		afterPageText : '页    共 {pages} 页'
	});
	$('#role_permit_allLeftToRight').unbind('click').click(function(event) {
		rolePermitAllLetfToRight();
	});
	$('#role_permit_allRightToLeft').unbind('click').click(function(event) {
		rolePermitAllRightToLeft();
	});
	$('#role_permit_rightToLeft').unbind('click').click(function(event) {
		rolePermitRightToLeft();
	});
	$('#role_permit_leftToRight').unbind('click').click(function(event) {
		rolePermitLetfToRight();
	});
});

/**
 * 开启新增窗口
 */
function initRoleAddWindow() {
	openWindow(role_module, '新增', 423, 158);
	var win = getWinIdWithPound(role_module);
	$(win + '_submit').unbind('click').click(function(event) {
		saveRow(role_module);
	});

	setRequiredRoleCommons(true);
}
/**
 * 开启查询窗口
 */
function initRoleSearchWindow() {
	openWindow(role_module, '查询', 424, 158);
	var win = getWinIdWithPound(role_module);
	$(win + '_submit').unbind('click').click(function(event) {
		searchRows(role_module);
	});
	setRequiredRoleCommons(false);
}

/**
 * 双击行，开启修改窗口
 * 
 * @param rowIndex
 * @param rowData
 */
function dblClickRoleRow(rowIndex, rowData) {
	initRoleEditWindow(rowData);
}

/**
 * 单击行
 * 
 * @param rowIndex
 * @param rowData
 */
function clickRoleRow(rowIndex, rowData) {
	$.getJSON("../permit/listBindedByRole.do?roleId=" + rowData['id'],
			function(data) {
				if (data && data.rows) {
					$('#role_binded_permit_tree').tree({
						data : data.rows
					});
				}
			});
}
/**
 * 开启修改窗口
 * 
 * @param rowData
 */
function initRoleEditWindow(rowData) {
	openWindow(role_module, '修改', 423, 158);
	var win = getWinIdWithPound(role_module);
	$(win).form('load', rowData);
	$(win + '_submit').unbind('click').click(function(event) {
		updateRow(role_module, rowData['id']);
	});
	setRequiredRoleCommons(true);
}

function setRequiredRoleCommons(isRequired) {
	// 用prop/attr都没有效果
	var win = getWinIdWithPound(role_module);
	$(win + '_name').validatebox({
		required : isRequired
	});
	var win = getWinIdWithPound(role_module);
	$(win + '_remark').validatebox({
		required : isRequired
	});
}
// ===================拖拽窗口相关代码===============================

/**
 * 打开查询受众窗口
 */
function openRolePermitDragDropWindow() {
	$.messager.alert('提 示', '自己实现吧，树状拖拽，网上自己找找应该有');
	return;// 
	var rowData = getSelectedRowData(role_module);
	if ('' == rowData) {
		$.messager.alert('提 示', '请选择一条记录(另: 只能选一条)');
		return;
	}
	$('#role_permit_rightDataGrid').datagrid('options').url = "../permit/listBindedByRole.do?roleId="
			+ rowData['id'];
	$("#role_permit_rightDataGrid").datagrid('load');
	$('#role_permit_dragDropWindow').window('open');
}
/**
 * 查询权限
 */
function searchPermits() {
	var rowData = getSelectedRowData(role_module);
	if ('' == rowData) {
		$.messager.alert('提 示', '请选择一条记录(另: 只能选一条)');
		return;
	}
	var formSer = $('#role_permit_searchForm').serializeObject();
	$('#role_permit_leftDataGrid').datagrid('options').url = "../permit/search.do?roleId="
			+ rowData['id'];
	$('#role_permit_leftDataGrid').datagrid('options').queryParams = formSer;
	$('#role_permit_leftDataGrid').datagrid('options').method = 'post';
	$("#role_permit_leftDataGrid").datagrid('load');
}

/**
 * 绑定权限
 */
function bandPermits() {
	var roleData = getSelectedRowData(role_module);
	if (!roleData || '' == roleData || !roleData['id']) {
		$.messager.alert('提 示', '没有选择角色记录，请先选择一条角色记录');
		return;
	}
	var rows = $("#role_permit_rightDataGrid").datagrid("getRows");
	if (!rows || rows == null || rows.length <= 0) {
		$.messager.alert('提 示', '请选择权限');
		return;
	}
	var ids = rows[0]['id'];
	for (var i = 1; i < rows.length; i++) {
		ids = ids + ',' + rows[i]['id'];
	}
	$.messager.confirm('提 示', '确定绑定?', function(r) {
		if (r) {
			$.post('../roleBindPermit/update.do', {
				'role.id' : roleData['id'],
				ids : ids
			}, function(data) {
				var jsonData = eval('(' + data + ')');
				$.messager.alert('提 示', jsonData.message);
				$('#role_binded_permit_tree').tree('reload');
			});
		}
	});
	$('#role_permit_dragDropWindow').window('close');
}

/**
 * 将所有左边数据移到右边
 */
function rolePermitAllLetfToRight() {
	var left = $("#role_permit_leftDataGrid");
	var right = $("#role_permit_rightDataGrid");
	var leftRows = left.datagrid('getRows');
	if (leftRows && leftRows.length > 0) {
		for (var i = leftRows.length - 1; i >= 0; i--) {
			right.datagrid('_appendRow', leftRows[i]);
			left.datagrid('deleteRow', i);
		}
	}
}

/**
 * 将所有右边数据移到左边
 */
function rolePermitAllRightToLeft() {
	var left = $("#role_permit_leftDataGrid");
	var right = $("#role_permit_rightDataGrid");
	var rightRows = right.datagrid('getRows');
	if (rightRows && rightRows.length > 0) {
		for (var i = rightRows.length - 1; i >= 0; i--) {
			left.datagrid('_appendRow', rightRows[i]);
			right.datagrid('deleteRow', i);
		}
	}
}

/**
 * 将选中的左边记录移到右边
 */
function rolePermitLetfToRight() {
	var left = $("#role_permit_leftDataGrid");
	var right = $("#role_permit_rightDataGrid");
	var selectedRows = left.datagrid('getSelections');
	if (selectedRows.length > 0) {
		for (var i = 0; i < selectedRows.length; i++) {
			right.datagrid('_appendRow', selectedRows[i]);
			left.datagrid('deleteRow', left.datagrid('getRowIndex',
					selectedRows[i]));
		}
	}
}

/**
 * 将选中的右边记录移到左边
 */
function rolePermitRightToLeft() {
	var left = $("#role_permit_leftDataGrid");
	var right = $("#role_permit_rightDataGrid");
	var selectedRows = right.datagrid('getSelections');
	if (selectedRows.length > 0) {
		for (var i = 0; i < selectedRows.length; i++) {
			left.datagrid('_appendRow', selectedRows[i]);
			right.datagrid('deleteRow', right.datagrid('getRowIndex',
					selectedRows[i]));
		}
	}
}
