WS = window.WS || {};

$(document).ready(function(){    
    /* setup navigation, content boxes, etc... */
    WS.wysiwyg = false;
    WS.setup();
    WS.initValidator();
    $('#bottom').load("./Footer.html");
    $('#uploader').preview();
    // validate signup form on keyup and submit
    $("#loginform").validate({
    	onkeyup: false,
        rules: {
            storenumber:   true,
            username: true,
            password: true,
            validator: {
            	required: true,
            	remote: WS.validateUrl+"?id=loginValidator"
            }
        },
        messages: {
            storenumber: "表店名必填",
            username: "用户名必填",
            password: "密码必填",
            validator: "验证码错误"
        },
        // the errorPlacement has to take the layout into account
        errorPlacement: function(error, element) {
            error.insertAfter(element.parent().find('label:first'));
        },
        // set new class to error-labels to indicate valid fields
        success: function(label) {
            // set &nbsp; as text for IE
            if(label[0].htmlFor === 'validator')label.html("&nbsp;").addClass("ok");
        }
    });
    $("#register").validate({
    	onkeyup: false,
        rules: {
        	founder: {
        		required: true,
            	minlength: 1,
            	allLetters: true,
            	remote: WS.validateUrl
            },
            password: {
            	required: true,
            	minlength: 6
            },
            repeat_password: {
            	required: true
            },
            email: {
            	required: true,
            	email: true,
            	remote:WS.validateUrl
            },
            storename: {
            	required: true,
            	minlength: 1
            },
            cnbrand: {
            	required: true,
            	minlength: 1
            },
            enbrand: {
            	required:true,
            	allLetters: true
            },
            telephone: {
            	required: true,
            	isTelephone: true
            },
            address: {
            	required: true,
            	minlength: 1
            },
            postcode: {
            	required: true,
            	isPostCode: true
            },
            fax: {
            	required: true,
            	digits: true
            },
            website: {
            	required: true,
            	minlength: 1
            },
            validator: {
            	required: true,
            	remote: WS.validateUrl+"?id=registerValidator"
            }
        },
        messages: {
        	founder: {
        		required: "用户名必填",
            	minlength: "用户名至少为六位",
            	allLetters: "用户名须为英文字符串",
            	remote: "该用户名已注册"
            },
            password: {
            	required: "密码必填",
            	minlength: "密码最少为六位"
            },
            repeat_password: {
            	required: "重复密码必填",
            	equalTo: "与密码不相同"
            },
            email: {
            	required: "电子邮件为必填",
            	email: "邮件地址不合法"
            },
            storename: "店铺名为必填",
            cnbrand: "品牌名为必填",
            enbrand: {
            	required: "英文品牌名必填",
            	allLetters: "英文品牌名须为英文字母",
            	remote: "该品牌名已注册"
            },
            telephone: {
            	required: "电话号码必填",
            	isTelephone: "电话号码不合法，参照135XXXXXXXX或010-XXXXXXXX"
            },
            address: "地址必填",
            postcode: {
            	required: "邮政编码必填",
            	isPostCode: "邮政编码不合法"
            },
            fax: {
            	required: "传真为必填",
            	digits: "传真地址不合法"
            },
            website: "网址为必填",
            validator: "验证码错误"
        },
        // the errorPlacement has to take the layout into account
        errorPlacement: function(error, element) {
            error.insertAfter(element.parent().find('label:first'));
        },
        // set new class to error-labels to indicate valid fields
        success: function(label) {
            // set &nbsp; as text for IE
            if(label[0].htmlFor === 'validator')label.html("&nbsp;").addClass("ok");
        }
    });
    $('#to_register_page2').click(function() {
        $("#register_user_section").hide();
        $("#register_store_section").show();
        $("#submit_action").show();
        if (!WS.wysiwyg) {
            $('#storedesc').wysiwyg({
                controls: {
                  strikeThrough : { visible : true },
                  underline     : { visible : true },
                  
                  justifyLeft   : { visible : true },
                  justifyCenter : { visible : true },
                  justifyRight  : { visible : true },
                  justifyFull   : { visible : true },
                  
                  indent  : { visible : true },
                  outdent : { visible : true },
                  
                  subscript   : { visible : true },
                  superscript : { visible : true },
                  
                  undo : { visible : true },
                  redo : { visible : true },
                  
                  insertOrderedList    : { visible : true },
                  insertUnorderedList  : { visible : true },
                  insertHorizontalRule : { visible : true },

                  h4: {
                          visible: true,
                          className: 'h4',
                          command: $.browser.msie ? 'formatBlock' : 'heading',
                          arguments: [$.browser.msie ? '<h4>' : 'h4'],
                          tags: ['h4'],
                          tooltip: 'Header 4'
                  },
                  h5: {
                          visible: true,
                          className: 'h5',
                          command: $.browser.msie ? 'formatBlock' : 'heading',
                          arguments: [$.browser.msie ? '<h5>' : 'h5'],
                          tags: ['h5'],
                          tooltip: 'Header 5'
                  },
                  h6: {
                          visible: true,
                          className: 'h6',
                          command: $.browser.msie ? 'formatBlock' : 'heading',
                          arguments: [$.browser.msie ? '<h6>' : 'h6'],
                          tags: ['h6'],
                          tooltip: 'Header 6'
                  },
                  
                  cut   : { visible : true },
                  copy  : { visible : true },
                  paste : { visible : true },
                  html  : { visible: true }
                }
            });
            WS.wysiwyg = true;
        }
    });

    $('#to_register_page1').click(function(){
        $("#register_user_section").show();
        $("#register_store_section").hide();
        $("#submit_action").hide();
    });
    
    initGeoSelector("province","","city","","district","");
   
});

function login () {
    $("#login_section").show();
    $("#register_user_section").hide();
    $("#register_store_section").hide();
    $("#submit_action").hide();
    $('#menu_login').addClass("current");
    $('#menu_register').removeClass("current");
}

function register () {
    $("#login_section").hide();
    $("#register_user_section").show();
    $("#register_store_section").hide();
    $("#submit_action").hide();
    $('#menu_login').removeClass("current");
    $('#menu_register').addClass("current");
}