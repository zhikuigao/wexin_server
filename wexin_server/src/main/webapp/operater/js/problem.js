var problem_module = 'problem';


function go(value, row, index) {  
    return "<a href='" + row.spareField3 + "' target='_blank'>" + "查看" + "</a>";//_top,  
}  

// 从easyui 官网文档中拷贝
// http://www.jeasyui.com/demo/main/index.php?plugin=DataGrid&theme=default&dir=ltr&pitem=#
$(function() {
	var pager = $(getDataGridIdWithPound(problem_module)).datagrid().datagrid('getPager');
	pager.pagination({
		pageList : [ 10 ],
		displayMsg : '共 {total} 条记录，从 {from} 到 {to} ',
		buttons : [ ]
	});
	$(getWinIdWithPound(problem_module)).window({
		onBeforeClose : function() {
			resetAllAlt();
		}

	});
	$('#problem_role_leftDataGrid').datagrid('enableDnd');
	$('#problem_role_leftDataGrid').datagrid({// 
		onLoadSuccess : function() {
			$(this).datagrid('enableDnd');
		}
	});
	$('#problem_role_rightDataGrid').datagrid({// 
		onLoadSuccess : function() {
			$(this).datagrid('enableDnd');
		}
	});
	var permitPager = $('#problem_role_leftDataGrid').datagrid().datagrid('getPager'); // get the pager of datagrid
	permitPager.pagination({
		showPageList : false,
		showRefresh : true,
		displayMsg : '',
		beforePageText : '第',
		afterPageText : '页    共 {pages} 页'
	});
	$('#problem_role_allLeftToRight').unbind('click').click(function(event) {
		problemRoleAllLetfToRight();
	});
	$('#problem_role_allRightToLeft').unbind('click').click(function(event) {
		problemRoleAllRightToLeft();
	});
	$('#problem_role_rightToLeft').unbind('click').click(function(event) {
		problemRoleRightToLeft();
	});
	$('#problem_role_leftToRight').unbind('click').click(function(event) {
		problemRoleLetfToRight();
	});
	$('#problem_role_dragDropWindow_submit').unbind('click').click(
			function(event) {
				bandRoles();
			});
});

function initproblemAddWindow() {
	openWindow(problem_module, '新增', 455, 327);
	var win = getWinIdWithPound(problem_module);
	$(win + '_type').combobox('setValue', '0');
	$(win + '_submit').unbind('click').click(function(event) {
		saveRow(problem_module);
	});
	setRequiredproblemPassword(true);

	setRequiredproblemCommons(true);
	showPassword();
}

function initproblemSearchWindow() {
	openWindow(problem_module, '查询', 455, 285);
	var win = getWinIdWithPound(problem_module);
	$(win + '_submit').unbind('click').click(function(event) {
		searchRows(problem_module);
	});
	setRequiredproblemCommons(false);
	hidePassword();
}

function dblClickproblemRow(rowIndex, rowData) {
	initproblemEditWindow(rowData);
}

/**
 * 单击行
 * 
 * @param rowIndex
 * @param rowData
 */
function clickproblemRow(rowIndex, rowData) {
	if (!rowData['id']) {
		return;
	}
	var roleDg = $(getDataGridIdWithPound(problem_module + '_role'));
	roleDg.datagrid('options').url = '../problemBindRole/listBindedRole.do?userId='
			+ rowData['id'];
	roleDg.datagrid('load');
}

function initproblemEditWindow(rowData) {
	openWindow(problem_module, '修改', 423, 310);
	showPassword();
	var win = getWinIdWithPound(problem_module);
	$(win).form('load', rowData);
	$(win + '_submit').unbind('click').click(function(event) {
		updateRow(problem_module, rowData['id']);
	});
	setRequiredproblemPassword(false);
	setRequiredproblemCommons(true);
}

function showPassword() {
	var win = getWinIdWithPound(problem_module);
	$(win + '_repeat_password_tr').show();
	$(win + '_password_tr').show();
}

function hidePassword() {
	var win = getWinIdWithPound(problem_module);
	$(win + '_repeat_password_tr').hide();
	$(win + '_password_tr').hide();
}

/**
 * 将所有的提示信息去除(easyUI在关闭窗口时，会有一些提示信息没有隐藏，比如：“不能为空”)
 */
function resetAllAlt() {
	var win = getWinIdWithPound(problem_module);
	$(win + '_repeat_password').validatebox({
		required : false
	});
	$(win + '_password').validatebox({
		required : false
	});
	$(win + '_code').validatebox({
		required : false
	});
	$(win + '_name').validatebox({
		required : false
	});
	$(win + '_repeat_password').validatebox('isValid');
	$(win + '_password').validatebox('isValid');
	$(win + '_code').validatebox('isValid');
	$(win + '_name').validatebox('isValid');
}

function setRequiredproblemPassword(isRequired) {
	// 用prop/attr都没有效果
	var win = getWinIdWithPound(problem_module);
	$(win + '_repeat_password').validatebox({
		required : isRequired
	});
	$(win + '_password').validatebox({
		required : isRequired
	});
}

function setRequiredproblemCommons(isRequired) {
	// 用prop/attr都没有效果
	var win = getWinIdWithPound(problem_module);
	$(win + '_code').validatebox({
		required : isRequired
	});
	$(win + '_name').validatebox({
		required : isRequired
	});
	if (!isRequired) {// combox bug
		$(win + '_code').validatebox('isValid');
		$(win + '_name').validatebox('isValid');
	}
}
// ===================拖拽窗口相关代码===============================

/**
 * 打开查询受众窗口
 */
function openproblemRoleDragDropWindow() {
	var rowData = getSelectedRowData(problem_module);
	if ('' == rowData) {
		$.messager.alert('提 示', '请选择一条记录(另: 只能选一条)');
		return;
	}
	$('#problem_role_rightDataGrid').datagrid('options').url = "../problemBindRole/listBindedRole.do?userId="
			+ rowData['id'];
	$("#problem_role_rightDataGrid").datagrid('load');
	$('#problem_role_leftDataGrid').datagrid('options').url = "../problemBindRole/listUnbindRole.do?userId="
			+ rowData['id'];
	$("#problem_role_leftDataGrid").datagrid('load');
	$('#problem_role_dragDropWindow').window('open');
}
/**
 * 查询权限
 */
function searchRoles() {
	var rowData = getSelectedRowData(problem_module);
	if ('' == rowData) {
		$.messager.alert('提 示', '请选择一条记录(另: 只能选一条)');
		return;
	}
	var formSer = $('#problem_role_searchForm').serializeObject();
	$('#problem_role_leftDataGrid').datagrid('options').url = "../problemBindRole/listUnbindRole.do?userId="
			+ rowData['id'];
	$('#problem_role_leftDataGrid').datagrid('options').queryParams = formSer;
	$('#problem_role_leftDataGrid').datagrid('options').method = 'post';
	$("#problem_role_leftDataGrid").datagrid('load');
}

/**
 * 绑定权限
 */
function bandRoles() {
	var roleData = getSelectedRowData(problem_module);
	if (!roleData || '' == roleData || !roleData['id']) {
		$.messager.alert('提 示', '没有选择角色记录，请先选择一条角色记录');
		return;
	}
	var rows = $("#problem_role_rightDataGrid").datagrid("getRows");
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
			$.post('updateProblem.do', {
				'data' : roleData,
				ids : ids
			}, function(data) {
				var jsonData = eval('(' + data + ')');
				$.messager.alert('提 示', jsonData.message);
				$(getDataGridIdWithPound(problem_module + '_role')).datagrid(
						'reload');
			});
		}
	});
	$('#problem_role_dragDropWindow').window('close');
}

/**
 * 将所有左边数据移到右边
 */
function problemRoleAllLetfToRight() {
	var left = $("#problem_role_leftDataGrid");
	var right = $("#problem_role_rightDataGrid");
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
function problemRoleAllRightToLeft() {
	var left = $("#problem_role_leftDataGrid");
	var right = $("#problem_role_rightDataGrid");
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
function problemRoleLetfToRight() {
	var left = $("#problem_role_leftDataGrid");
	var right = $("#problem_role_rightDataGrid");
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
function problemRoleRightToLeft() {
	var left = $("#problem_role_leftDataGrid");
	var right = $("#problem_role_rightDataGrid");
	var selectedRows = right.datagrid('getSelections');
	if (selectedRows.length > 0) {
		for (var i = 0; i < selectedRows.length; i++) {
			left.datagrid('_appendRow', selectedRows[i]);
			right.datagrid('deleteRow', right.datagrid('getRowIndex',
					selectedRows[i]));
		}
	}
}
