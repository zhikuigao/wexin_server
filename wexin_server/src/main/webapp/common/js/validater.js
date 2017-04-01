// ---------------------------------------
// http://www.jeasyui.com/documentation/validatebox.php
// ---------------------------------------
$.extend($.fn.validatebox.defaults.rules, {
			// 只能包括 数字 字母
			numOrLetter : {
				validator : function(value, param) {
					if (value == null || value.length == 0) {
						return false;
					}
					if (/^[A-Za-z0-9]+$/.test(value)) {
						return true;
					} else {
						return false;
					}
				},
				message : '只能包含数字、字母'
			},
			equals : {
				validator : function(value, param) {
					return value == $(param[0]).val();
				},
				message : '输入不一致'
			},
			commonText : {
				validator : function(value, param) {
					if (value == null || value.length == 0) {
						return false;
					}
					if (/^[\w,.!\u4e00-\u9fa5]+$/gi.test(value)) {
						return true;
					} else {
						return false;
					}
				}
			}
		});