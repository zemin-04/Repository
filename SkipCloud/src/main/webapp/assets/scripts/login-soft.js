var Login = function() {

    var handleLogin = function() {
        $('.login-form').validate({
            errorElement : 'span', // default input error message container
            errorClass : 'help-block', // default input error message class
            focusInvalid : false, // do not focus the last invalid input
            rules : {
                username : {
                    required : true
                },
                password : {
                    required : true
                },
                remember : {
                    required : false
                }
            },

            messages : {
            	username : {
                    required : "用户名不能为空."
                },
                password : {
                    required : "密码不能为空."
                }
            },

            invalidHandler : function(event, validator) { // display error
                // alert on form
                // submit
                $('.alert-danger', $('.login-form')).show();
            },

            highlight : function(element) { // hightlight error inputs
                $(element).closest('.form-group').addClass('has-error'); // set
                // error
                // class
                // to
                // the
                // control
                // group
            },

            success : function(label) {
                label.closest('.form-group').removeClass('has-error');
                label.remove();
            },

            errorPlacement : function(error, element) {
                error.insertAfter(element.closest('.input-icon'));
            },

            submitHandler : function(form) {
                var passwordInput = $('[name="password"]');
                passwordInput.val(sha256_digest(passwordInput.val()));
                form.submit();
            }
        });

        $('.login-form input').keypress(function(e) {
            if (e.which == 13) {
                if ($('.login-form').validate().form()) {
                    $('.login-form').submit();
                }
                return false;
            }
        });
    }

    var handleForgetPassword = function() {
        $('.forget-form').validate({
            errorElement : 'span', // default input error message container
            errorClass : 'help-block', // default input error message class
            focusInvalid : false, // do not focus the last invalid input
            ignore : "",
            rules : {
                email : {
                    required : true,
                    email : true
                }
            },

            messages : {
                email : {
                    required : "Email is required."
                }
            },

            invalidHandler : function(event, validator) { // display error
                // alert on form
                // submit

            },

            highlight : function(element) { // hightlight error inputs
                $(element).closest('.form-group').addClass('has-error'); // set
                // error
                // class
                // to
                // the
                // control
                // group
            },

            success : function(label) {
                label.closest('.form-group').removeClass('has-error');
                label.remove();
            },

            errorPlacement : function(error, element) {
                error.insertAfter(element.closest('.input-icon'));
            },

            submitHandler : function(form) {
                form.submit();
            }
        });

        $('.forget-form input').keypress(function(e) {
            if (e.which == 13) {
                if ($('.forget-form').validate().form()) {
                    $('.forget-form').submit();
                }
                return false;
            }
        });

        jQuery('#forget-password').click(function() {
            jQuery('.login-form').hide();
            jQuery('.forget-form').show();
        });

        jQuery('#back-btn').click(function() {
            jQuery('.login-form').show();
            jQuery('.forget-form').hide();
        });

    }

    var handleRegister = function() {

        function format(state) {
            if (!state.id)
                return state.text; // optgroup
            return "<img class='flag' src='assets/img/flags/"
                    + state.id.toLowerCase() + ".png'/>&nbsp;&nbsp;"
                    + state.text;
        }

        $("#select2_sample4")
                .select2(
                        {
                            placeholder : '<i class="fa fa-map-marker"></i>&nbsp;Select a Country',
                            allowClear : true,
                            formatResult : format,
                            formatSelection : format,
                            escapeMarkup : function(m) {
                                return m;
                            }
                        });

        $('#select2_sample4').change(function() {
            $('.register-form').validate().element($(this)); // revalidate
            // the chosen
            // dropdown
            // value and
            // show error or
            // success
            // message for
            // the input
        });
        
        $('.register-form').validate({
            errorElement : 'span', // default input error message container
            errorClass : 'help-block', // default input error message class
            focusInvalid : false, // do not focus the last invalid input
            ignore : "",
            rules : {

                fullname : {
                    required : true
                },
                email : {
                    required : true
                },
                address : {
                    required : true
                },
                city : {
                    required : true
                },
                country : {
                    required : true
                },

                username : {
                    required : true
                },
                password : {
                    required : true
                },
                rpassword : {
                    equalTo : "#register_password"
                },

                tnc : {
                    required : true
                }
            },

            messages : { // custom messages for radio buttons and checkboxes
            	email : {
                    required : "请输入邮箱"
                },
                tnc : {
                    required : "Please accept TNC first."
                }
            },

            invalidHandler : function(event, validator) { // display error
                // alert on form
                // submit

            },

            highlight : function(element) { // hightlight error inputs
                $(element).closest('.form-group').addClass('has-error'); // set
                // error
                // class
                // to
                // the
                // control
                // group
            },

            success : function(label) {
                label.closest('.form-group').removeClass('has-error');
                label.remove();
            },

            errorPlacement : function(error, element) {
                if (element.attr("name") == "tnc") { // insert checkbox
                    // errors after the
                    // container
                    error.insertAfter($('#register_tnc_error'));
                } else if (element.closest('.input-icon').size() === 1) {
                    error.insertAfter(element.closest('.input-icon'));
                } else {
                    error.insertAfter(element);
                }
            },

            submitHandler : function(form) {
            	var passwordInput = $('#register_password');
                passwordInput.val(sha256_digest(passwordInput.val()));
                form.submit();
            }
        });
        
        $('.register-form input').keypress(function(e) {
        	var passwordInput = $('#register_password');
            if (e.which == 13) {
                if ($('.register-form').validate().form()) {
                    passwordInput.val(sha256_digest(passwordInput.val()));
                    $('.register-form').submit();
                }
                return false;
            }
        });

        jQuery('#register-btn').click(function() {
            jQuery('.login-form').hide();
            jQuery('.register-form').show();
        });

        jQuery('#register-back-btn').click(function() {
            jQuery('.login-form').show();
            jQuery('.register-form').hide();
        });
    }
    
    //检查用户名是否注册了
    $("#register_username").blur(function() {
    		$(".ya").remove();
    		$.ajax({
    			url : "rest/user/checkUsername",
    			type : "post",
    			dataType : "json",
    			async : false,
    			data : {
    				username : $("#register_username").val()
    			},
    			success : function(data) {
    				//var jsonObj = JSON.parse(date);
    				$.each(data, function(key, value) {
    					if (value==1) {
    						$("#register_username").after(function(){
    							return "<label class='ya'>该用户名已注册，请重新输入用户名</label>";
    						});
    						//用户输入框获取焦点
    						//FF的focus只能在blur之前,这里不能调用focus函数，获取焦点
    						window.setTimeout (function(){ document.getElementById ('register_username'). select();},0 ); 
    					}
    				});
    			},
    			error : function(XMLHttpRequest, textStatus,
    					errorThrown) {
    				alert('XMLHttpRequest.status  '
    						+ XMLHttpRequest.status);
    				alert('XMLHttpRequest.readyState  '
    						+ XMLHttpRequest.readyState);
    				alert('textStatus  ' + textStatus);
    			}

    		});
    	});
    
    //检查邮箱是否注册了
    $("#register_email").blur(function(){
    	$(".ya").remove();
		$.ajax({
			url : "rest/user/checkEmail",
			type : "post",
			dataType : "json",
			async : false,
			data : {
				email : $("#register_email").val()
			},
			success : function(data) {
				//var jsonObj = JSON.parse(date);
				$.each(data, function(key, value) {
					if (value==1) {
						$("#register_email").after(function(){
							return "<label class='ya'>该用邮箱已注册，请重新输入邮箱</label>";
						});
						//邮箱获取焦点
						window.setTimeout (function(){ document.getElementById ('register_email'). select();},0 );
					}
				});
			},
			error : function(XMLHttpRequest, textStatus,
					errorThrown) {
				alert('XMLHttpRequest.status  '
						+ XMLHttpRequest.status);
				alert('XMLHttpRequest.readyState  '
						+ XMLHttpRequest.readyState);
				alert('textStatus  ' + textStatus);
			}
		});
	});
    //检查邮箱是否存在
    $("#email").blur(function(){
    	$(".ya").remove();
		$.ajax({
			url : "rest/user/checkEmail",
			type : "post",
			dataType : "json",
			async : false,
			data : {
				email : $("#email").val()
			},
			success : function(data) {
				//var jsonObj = JSON.parse(date);
				$.each(data, function(key, value) {
					if (value==2) {
						$("#email").after(function(){
							return "<label class='ya'>邮箱不存在，请重新输入</label>";
						});
						//邮箱获取焦点
						window.setTimeout (function(){ document.getElementById ('email'). select();},0 );
					}
				});
			},
			error : function(XMLHttpRequest, textStatus,
					errorThrown) {
				alert('XMLHttpRequest.status  '
						+ XMLHttpRequest.status);
				alert('XMLHttpRequest.readyState  '
						+ XMLHttpRequest.readyState);
				alert('textStatus  ' + textStatus);
			}
		});
	});
    
    return {
        // main function to initiate the module
        init : function() {
            console.log(12);
            handleLogin();
            handleForgetPassword();
            handleRegister();
            $.backstretch([ "assets/img/bg/1.jpg", "assets/img/bg/2.jpg",
                    "assets/img/bg/3.jpg", "assets/img/bg/4.jpg" ], {
                fade : 1000,
                duration : 8000
            });
        }

    };
    
}();


