$(function() {

	// remove
	$('.remove').on('click', function(){
		var id = $(this).attr('id');
		layer.confirm( (I18n.system_ok + I18n.jobgroup_del + '？') , {
			icon: 3,
			title: I18n.system_tips ,
            btn: [ I18n.system_ok, I18n.system_cancel ]
		}, function(index){
			layer.close(index);
			$.ajax({
				type : 'POST',
				url : base_url + '/jobgroup/remove',
				data : {"id":id},
				dataType : "json",
				success : function(data){
					if (data.code == 200) {
						layer.open({
							title: I18n.system_tips ,
                            btn: [ I18n.system_ok ],
							content: (I18n.jobgroup_del + I18n.system_success),
							icon: '1',
							end: function(layero, index){
								window.location.reload();
							}
						});
					} else {
						layer.open({
							title: I18n.system_tips,
                            btn: [ I18n.system_ok ],
							content: (data.msg || (I18n.jobgroup_del + I18n.system_fail)),
							icon: '2'
						});
					}
				},
			});
		});

	});

	// jquery.validate “low letters start, limit contants、 letters、numbers and line-through.”
	jQuery.validator.addMethod("myValid01", function(value, element) {
		var length = value.length;
		var valid = /^[a-z][a-zA-Z0-9-]*$/;
		return this.optional(element) || valid.test(value);
	}, I18n.jobgroup_field_appName_limit );

    //编辑按钮事件
    $("#joblog_group").on('click', '.job_operate', function () {
        var type = $(this).attr("_type");
        if ("group_save" == type) {
            edit(this);
        }
    });
    //双击弹出编辑
    $('#joblog_group').on('dblclick','tr',function(){
        edit($(this).children("td").children("button"));
    });

    function edit(target) {
        $("#addModal .form input[name='id']").val($(target).attr("id"));
        $("#addModal .form input[name='appName']").val($(target).attr("appName"));
        $("#addModal .form input[name='title']").val($(target).attr("title"));
        $("#addModal .form input[name='order']").val($(target).attr("order"));
        // 注册方式
        var addressType =$(target).attr("addressType");
        $("#addModal .form input[name='addressType']").removeAttr('checked');
        //$("#updateModal .form input[name='addressType'][value='"+ addressType +"']").attr('checked', 'true');
        $("#addModal .form input[name='addressType'][value='"+ addressType +"']").click();
        // 机器地址
        $("#addModal .form textarea[name='addressList']").val($(target).attr("addressList"));
        // show
        $('#addModal').modal({backdrop: false, keyboard: false}).modal('show');
    }
    // 添加按钮
    $(".add").click(function () {
        edit(this);
    });

	var addModalValidate = $("#addModal .form").validate({
		errorElement : 'span',
		errorClass : 'help-block',
		focusInvalid : true,
		rules : {
			appName : {
				required : true,
				rangelength:[4,64],
				myValid01 : true
			},
			title : {
				required : true,
				rangelength:[4, 12]
			},
			order : {
				required : true,
				digits:true,
				range:[1,1000]
			}
		},
		messages : {
			appName : {
				required : I18n.system_please_input+"AppName",
				rangelength: I18n.jobgroup_field_appName_length ,
				myValid01: I18n.jobgroup_field_appName_limit
			},
			title : {
				required : I18n.system_please_input + I18n.jobgroup_field_title ,
				rangelength: I18n.jobgroup_field_title_length
			},
			order : {
				required : I18n.system_please_input + I18n.jobgroup_field_order ,
				digits: I18n.jobgroup_field_order_digits ,
				range: I18n.jobgroup_field_orderrange
			}
		},
		highlight : function(element) {
			$(element).closest('.form-group').addClass('has-error');
		},
		success : function(label) {
			label.closest('.form-group').removeClass('has-error');
			label.remove();
		},
		errorPlacement : function(error, element) {
			element.parent('div').append(error);
		},
		submitHandler : function(form) {
			$.post(base_url + "/jobgroup/save",  $("#addModal .form").serialize(), function(data, status) {
				if (data.code == "200") {
					$('#addModal').modal('hide');
					layer.open({
						title: I18n.system_tips ,
                        btn: [ I18n.system_ok ],
						content: I18n.system_add_suc ,
						icon: '1',
						end: function(layero, index){
							window.location.reload();
						}
					});
				} else {
					layer.open({
						title: I18n.system_tips,
                        btn: [ I18n.system_ok ],
						content: (data.msg || I18n.system_add_fail  ),
						icon: '2'
					});
				}
			});
		}
	});
	$("#addModal").on('hide.bs.modal', function () {
		$("#addModal .form")[0].reset();
		addModalValidate.resetForm();
		$("#addModal .form .form-group").removeClass("has-error");
	});

	// addressType change
	$("#addModal input[name=addressType], #updateModal input[name=addressType]").click(function(){
		var addressType = $(this).val();
		var $addressList = $(this).parents("form").find("textarea[name=addressList]");
		if (addressType == 0) {
            $addressList.css("background-color", "#eee");	// 自动注册
            $addressList.attr("readonly","readonly");
			$addressList.val("");
		} else {
            $addressList.css("background-color", "white");
			$addressList.removeAttr("readonly");
		}
	});
});
