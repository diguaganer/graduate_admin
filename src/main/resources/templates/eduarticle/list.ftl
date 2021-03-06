<#--  niushuai 2019-05-07 01:07:31-->
<!DOCTYPE html>
<html>
<head>
    <title>文章信息表列表</title>
    <#include "../resource.ftl"/>
    <script type="text/javascript" src="/eduarticle/js/list.js"></script>
</head>
<body>
<form class="layui-form " action="">
    <div class="layui-form-item">
        <label class="layui-form-label">名称:</label>
        <div class="layui-input-inline">
            <input type="text" name="titleVague" placeholder="请输入名称" class="layui-input">
        </div>

        <div class="layui-input-normal">
            <button class="layui-btn layui-btn-green" lay-submit="" lay-filter="moreSearch">
                <i class="fa fa-chevron-down">&nbsp;</i>更多
            </button>
            <button class="layui-btn search-btn" table-id="eduArticleTable" lay-submit="" lay-filter="search">
                <i class="fa fa-search">&nbsp;</i>查询
            </button>
            <button type="reset" class="layui-btn layui-btn-primary"><i class="fa fa-refresh">&nbsp;</i>重置</button>
        </div>
    </div>
    <div class="layui-form-item more-search">
        <#-- 更多条件-->
    </div>
</form>
<div class="layui-btn-group">
    <@shiro.hasPermission name="eduarticle:save">
        <button class="layui-btn" onclick="addPage('/eduarticle/add')">
            <i class="fa fa-plus">&nbsp;</i>增加
        </button>
    </@shiro.hasPermission>
    <@shiro.hasPermission name="eduarticle:update">
        <button class="layui-btn" onclick="editPage('eduArticleTable','/eduarticle/edit')">
            <i class="fa fa-pencil-square-o">&nbsp;</i>修改
        </button>
        <button class="layui-btn layui-btn-green" onclick="updateState('批量启用','eduArticleTable','/eduarticle/enable')">
            <i class="fa fa-check-square-o">&nbsp;</i>启用
        </button>
        <button class="layui-btn  layui-btn-danger"
                onclick="updateState('批量禁用','eduArticleTable','/eduarticle/disable')">
            <i class="fa fa-expeditedssl">&nbsp;</i>禁用
        </button>
    </@shiro.hasPermission>
    <@shiro.hasPermission name="eduarticle:delete">
        <button class="layui-btn layui-btn-delete"
                onclick="deleteBatch('批量删除','eduArticleTable','/eduarticle/delete');">
            <i class="fa fa-trash-o">&nbsp;</i>删除
        </button>
    </@shiro.hasPermission>

</div>
<div class="layui-form ">
    <input type="hidden" id="articleAccessPath" value="${(articleAccessPath)!""}"/>
    <table class="layui-table" id="eduArticleTable" cyType="pageGrid"
           cyProps="url:'/eduarticle/listData',checkbox:'true',pageColor:'#2991d9'">
        <thead>
        <tr>
            <!--复选框-->
            <th width="1%" param="{type:'checkbox'}">
                <input type="checkbox" lay-skin="primary" lay-filter="allChoose">
            </th>
            <!--isPrimary：是否是主键-->
            <th width="10%" param="{name:'articleId',isPrimary:'true',hide:'true'}">文章ID</th>

            <th width="10%" param="{name:'title'}">文章标题</th>

            <th width="10%" param="{name:'imageUrl', render:'Render.picImg'}">封面图片</th>

            <th width="5%" param="{name:'clickNum'}">点击量</th>

            <th width="5%" param="{name:'praiseCount'}">点赞量</th>

            <th width="5%" param="{name:'commentNum'}">评论量</th>

            <th width="5%" param="{name:'sort'}">排序</th>

            <th width="5%" param="{name:'createBy'}">创建人</th>

            <th width="6%" param="{name:'createTime'}">创建时间</th>

            <th width="5%" param="{name:'updateBy'}">更新人</th>

            <th width="6%" param="{name:'updateDate'}">更新时间</th>
            <!--isPrimary：渲染列-->
            <th width="5%" param="{name:'state',enumName:'StateEnum',render:'Render.customState'}">状态</th>
            <th width="15%" param="{operate:'true',buttons:'Render.visit,Render.state,Render.edit,Render.delete'}">操作</th>
        </tr>
        </thead>
    </table>
</div>
</body>
</html>