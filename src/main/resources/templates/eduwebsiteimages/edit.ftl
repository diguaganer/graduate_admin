<#--  niushuai 2019-03-19 16:36:50-->

<html>
<head>
    <title>banner图管理修改页面</title>
    <#include "../resource.ftl"/>
</head>
<body>
<div class="layui-field-box">
    <form class="layui-form" action="">
       <#include "common.ftl"/>
        <input type="hidden" name="imageId" value="${model.imageId}">
        <div class="page-footer">
            <div class="btn-list" style="height: 75px;">
                <div class="btnlist">
                    <button class="layui-btn" lay-submit="" lay-filter="submit" data-url="/eduwebsiteimages/update"><i class="fa fa-floppy-o">&nbsp;</i>保存</button>
                    <button class="layui-btn" onclick="$t.closeWindow();"><i class="fa fa-undo">&nbsp;</i>返回</button>
                </div>
            </div>
        </div>
    </form>
</div>

</body>
</html>

