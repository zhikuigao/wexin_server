/**
 * 这是最核心的js文件，增、删、查、改都在这里实现了，其他具体的js，比如operater.js 是通过这个js来实现具体的数据处理.
 * 达到这个效果的核心是命名必须遵照一定的规则。比如：dataGrid ID：operater_dg;window ID: operater_window……
 * 命名规则：模块名 + '_' + 组件,如果组件的父组件被定义了，则，组件的命名规则为：模块名 + '_' + 父组件 + '_' + 子组件
 */
// 提示框,中文显示
$.messager.defaults = {
	ok : "是",
	cancel : "否"
};
// 时间格式化
$.fn.datebox.defaults.formatter = function(date) {
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	var d = date.getDate();
	if (m < 10) {
		m = "0" + m;
	}
	if (d < 10) {
		d = "0" + d;
	}
	return y + '-' + m + '-' + d;
};

/**
 * hash map实现
 * 
 * @returns
 */
function HashMap() {
	this._hash = new Object();
	this.length = 0;
	this.put = function(key, value) {
		if (!this.get(key))
			this.length++;
		this._hash[key] = value;
	};
	this.remove = function(key) {
		delete this._hash[key];
		this.length--;
	};
	this.size = function() {
		return this.length;
	};
	this.get = function(key) {
		return typeof (this._hash[key]) == "undefined" ? null : this._hash[key];
	};
	this.contains = function(key) {
		return typeof (this._hash[key]) != "undefined";
	};
	this.removeAll = function() {
		for ( var k in this._hash) {
			delete this._hash[k];
		}
	};
	this.keySet = function() {
		var arrKeySet = new Array();
		var index = 0;
		for ( var key in this._hash) {
			arrKeySet[index++] = key;
		}
		return arrKeySet.length == 0 ? null : arrKeySet;
	};
	this.values = function() {
		var arrValues = new Array();
		var index = 0;
		for ( var key in this._hash) {
			arrValues[index++] = this._hash[key];
		}
		return arrValues.length == 0 ? null : arrValues;
	};
}

/**
 * 设置jquery的ajax全局请求参数
 */
$.ajaxSetup({
	cache : false,
	complete : function(XMLHttpRequest, textStatus) {
		if (XMLHttpRequest.status >= 400) {
			eval(XMLHttpRequest.responseText);
		}
	}
});

/**
 * 处理IE缓存，防止IE缓存，用于构造动态的URL
 */
function addDynamicParam(url) {
	if ($.browser.msie) {
		if (url.indexOf('?') > 0) {
			url = url + '&doNotCache=' + (new Date().getTime());
		} else {
			url = url + '?doNotCache=' + (new Date().getTime());
		}
	}
	return url;
}

// http://stackoverflow.com/questions/1184624/convert-form-data-to-js-object-with-jquery
$.fn.serializeObject = function() {
	var o = {};
	var a = this.serializeArray();
	$.each(a, function() {
		if (o[this.name] !== undefined) {
			if (!o[this.name].push) {
				o[this.name] = [ o[this.name] ];
			}
			o[this.name].push(this.value || '');
		} else {
			o[this.name] = this.value || '';
		}
	});
	return o;
};

/**
 * 新增tab
 * 
 * @param title
 * @param url
 */
function addTab(title, url) {
	if ($('#tt').tabs('exists', title)) {
		$('#tt').tabs('select', title);
		return;
	}
	var content = '<iframe scrolling="auto" frameborder="0"  src="' + url
			+ '" style="width:100%;height:100%;display:block;"></iframe>';
	$('#tt').tabs('add', {
		title : title,
		content : content,
		closable : true
	});
	// 增加鼠标移动聚焦
	var tabs = $('#tt').tabs('tabs');
	$('#tt').tabs('getTab', title).panel('options').tab.unbind().bind(
			'mouseenter', {
				title : title
			}, function(e) {
				$('#tt').tabs('select', e.data.title);
			});
}

/**
 * 删除tab
 */
function removeTab() {
	var tab = $('#tt').tabs('getSelected');
	if (tab) {
		var index = $('#tt').tabs('getTabIndex', tab);
		$('#tt').tabs('close', index);
	}
}

/**
 * 根据模块获取dataGrid ID,比如：#operater_dg
 * 
 * @param module
 * @return String
 */
function getDataGridIdWithPound(module) {
	return '#' + module + '_dg';
}

/**
 * 根据模块获取弹窗 ID,比如：#operater_window
 * 
 * @param module
 * @return String
 */
function getWinIdWithPound(module) {
	return '#' + module + '_window';
}

/**
 * 根据模块获取弹窗 form ID, 比如：#operater_window_form
 * 
 * @param module
 * @return
 */
function getFormIdWithPound(module) {
	return getWinIdWithPound(module) + '_form';
}

/**
 * 获取选中的记录的ID（对应数据库中的ID）
 * 
 * @param {}
 *            dataGrid 传入参数如：operater
 * @return {String}
 */
function getSelectedRowIds(module) {
	var rows = $(getDataGridIdWithPound(module)).datagrid('getSelections');
	if (rows.length <= 0) {
		return '';
	}
	var ids = rows[0]['id'];
	for (var i = 1; i < rows.length; i++) {
		ids = ids + ',' + rows[i]['id'];
	}
	return ids;
}

/**
 * 返回选中的行数据
 * 
 * @param module
 * @returns
 */
function getSelectedRowData(module) {
	var rows = $(getDataGridIdWithPound(module)).datagrid('getSelections');
	if (rows.length <= 0 || rows.length > 1) {
		return '';
	}
	return rows[0];
}

/**
 * 格式化状态（用小图片显示）
 * 
 * @param value
 * @param row
 * @return String
 */
function formatStatus(value, row) {
	if (value == 1 || value == '1') {
		return "<img src='../common/widget/jquery/themes/icons/ok.png'>";
	} else {
		return "<img src='../common/widget/jquery/themes/icons/no.png'>";
	}
}

/**
 * 初始化弹窗
 * 
 * @param window,
 *            弹窗ID
 * @param title,
 *            弹窗标题
 * @param width,
 *            弹窗宽度,https://api.jquery.com/width/
 * @param height,
 *            弹窗高度,https://api.jquery.com/height/
 */
function openWindow(module, title, width, height) {
	// 打开窗口，清空上一次输入的信息
	$(getWinIdWithPound(module)).form('clear');
	if (/^\d+$/.test(height) && /^\d+$/.test(width)) {
		$(getWinIdWithPound(module)).window({
			width : width,
			height : height,
			top : ($(window).height() - height) * 0.5,
			left : ($(window).width() - width) * 0.5,
			title : title
		});
	}
	$(getWinIdWithPound(module)).window('open');
}

/**
 * 关闭窗口
 * 
 * @param module
 */
function closeWindow(module) {
	var win = getWinIdWithPound(module);
	$(win).form('clear');
	$(win).window('close');
}

/**
 * 获取记录列表
 * 
 * @param module
 */
function list(module) {
	var dg = getDataGridIdWithPound(module);
	$(dg).datagrid('options').url = addDynamicParam('../' + module + '/list.do');
	$(dg).datagrid('options').method = 'post';
	$(dg).datagrid('load');
}

/**
 * 保存行到数据库
 * 
 * @param module
 */
function saveRow(module) {
	$(getFormIdWithPound(module)).form('submit', {
		url : '../' + module + '/add.do',
		success : function(data) {
			var jsonData = eval('(' + data + ')');// 不能用JSON.stringify(data);
			$.messager.alert('提 示', jsonData.message);
			if (data != null && data != '' && jsonData.success == 1) {
				closeWindow(module);
				$(getDataGridIdWithPound(module)).datagrid('reload');
			}
		}
	});
}

/**
 * 更新行到数据库
 * 
 * @param module
 * @param id
 */
function updateRow(module, id) {
	$(getFormIdWithPound(module)).form('submit', {
		url : '../' + module + '/update.do?id=' + id,
		success : function(data) {
			var jsonData = eval('(' + data + ')');// 不能用JSON.stringify(data);
			$.messager.alert('提 示', jsonData.message);
			if (data != null && data != '' && jsonData.success == true) {
				closeWindow(module);
				$(getDataGridIdWithPound(module)).datagrid('reload');
			}
		}
	});
}

/**
 * 同时删数据库除记录
 * 
 * @param dataGrid
 * @param module
 *            模块名
 */
function deleteRows(module) {
	var ids = getSelectedRowIds(module);
	if (ids == null || ids == '') {
		$.messager.alert('提 示', '请选择要删除的记录');
		return;
	}
	$.messager.confirm('提 示', '确定删除?', function(r) {
		if (r) {
			$.post('../' + module + '/delete.do', {
				ids : ids
			}, function(data) {
				var jsonData = eval('(' + data + ')');
				$.messager.alert('提 示', jsonData.message);
				$(getDataGridIdWithPound(module)).datagrid('reload');
			});
		}
	});
}

/**
 * 查询记录
 * 
 * @param module
 */
function searchRows(module) {
	var formSer = $(getFormIdWithPound(module)).serializeObject();
	var dg = getDataGridIdWithPound(module);
	$(dg).datagrid('options').url = addDynamicParam('../' + module
			+ '/search.do');
	$(dg).datagrid('options').queryParams = formSer;
	$(dg).datagrid('options').method = 'post';// 解决url参数为乱码问题，因为get请求，参数放在url上会出现编码问题
	$(dg).datagrid('load');
	closeWindow(module);
}