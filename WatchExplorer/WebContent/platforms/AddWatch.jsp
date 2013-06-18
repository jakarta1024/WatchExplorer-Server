<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <script type="text/javascript">
    $(document).ready(function(){
        $("#addWatch").validate({
            onkeyup: false,
            rules: {
                validator: {
                    required: true,
                    remote: WS.validateUrl+"?id=AddWatchValidator"
                }
            },
            messages: {
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
        $('#register_watch_section').attr('scrollTop', 0);
    });
    </script>
</head>
<body>
    <div id="page">
        <div id="register_watch_section" style="width: 600px; height: 800px; overflow: auto; display: true">
            <section class="full width5">
                <div class="notification information png_bg">
                    <a href="#" class="close"><img src="resources/img/platform/icons/cross_grey_small.png" title="Close this notification" alt="close" /></a>
                    <div> 输入名表信息 </div>
                </div>
                <form id="addWatch" action="store/admin/home?ctrl=addwatch&cid=admin" method="POST" enctype="multipart/form-data">
                    <p>
                        <label class="required" for="watchname">表名:</label>
                        <input type="text" id="watchname" autofocus="autofocus"
                            class="text-input large-input" value="" name="watchname" />
                    </p>

                    <p>
                        <label class="required" for="brand">品牌:</label> 
                        <input type="text" id="brand" class="text-input large-input" value="" name="brand" />
                    </p>

                    <p>
                        <label class="optional" for="discount">当前折扣（%）:</label> 
                        <input type="text" id="discount" class="text-input large-input" value="" name="discount" />
                    </p>

                    <p>
                        <label class="optional" for="simpledesc">简单描述:</label> 
                        <input type="text" id="simpledesc" class="text-input large-input" value=""  name="simpledesc" />
                    </p>
                    <p>
                        <label class="required" for="desc">详细介绍:</label>
                        <textarea id="desc" class="text-input large-input wysiwyg" name="desc"></textarea>
                    </p>

                    <p>
                        <label class="optional" for="barcode">条形码:</label>
                        <input type="text" id="barcode" class="text-input large-input" value="" name="barcode" />
                    </p>
                    <p>
                        <label class="optional" for="movement">机芯:</label>
                        <input type="text" id="movement" class="text-input large-input" value="" name="movement" />
                    </p>
                    <p>
                        <label class="optional" for="material">材质:</label> 
                        <input type="text" id="material" class="text-input large-input" value="" name="material" />
                    </p>
                    <p>
                        <label class="optional" for="size">表盘尺寸:</label> 
                        <input type="text" id="size" class="text-input large-input" value="" name="size" />
                    </p>
                    <p>
                        <label class="optional" for="architecture">工艺:</label> 
                        <input type="text" id="architecture" class="text-input large-input" value=""
                            name="architecture" />
                    </p>
                    <p>
                        <label class="optional" for="band">表带材质:</label>
                        <input type="text" id="band" class="text-input large-input" value=""
                            name="band" />
                    </p>
                    <p>
                        <label class="optional" for="style">风格:</label>
                        <input type="text" id="style" class="text-input large-input" value="" name="style" />
                    </p>
                    <p>
                        <label class="optional" for="function">功能:</label> 
                        <input type="text" id="function" class="text-input large-input" value="" name="function" />
                    </p>
                    <p>
                        <label class="required" for="watchdesc">图片:</label>
                        <div id="uploader"> </div>
                    </p>
                    <p>
                    <div class="validator">
                        <label class="required" for="validator">验证码:</label> 
                        <input type="text" id="validator" value="" name="validator" class="text-input small-input"/>
                        <img id="AddWatchValidator" src="randomCode.do?id=AddWatchValidator">
                        <a href="javascript: void(0);" onclick="WS.reloadCode('AddWatchValidator');">看不清</a>
                    </div>
                    </p>

                    <div class="horizontal_center" style="width: 150px;">
                        <p>
                            <input type="submit" id="add_watch_submit"
                                class="btn btn-green big" value="提交" />
                        </p>
                    </div>
                    <div class="clear">&nbsp;</div>
                </form>
            </section>
        </div>
    </div>
</body>
</html>