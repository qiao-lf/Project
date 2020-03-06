<%--
  Created by IntelliJ IDEA.
  User: qiao
  Date: 2019/11/20
  Time: 20:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>唐诗宋词后台</title>
    <!--引入bootstrap css-->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/bootstrap/css/bootstrap.min.css">
    <!--引入jqgrid的bootstrap css-->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/jqgrid/css/ui.jqgrid-bootstrap.css">
    <!--引入jquery核心js-->
    <script src="${pageContext.request.contextPath}/static/js/jquery-3.4.1.min.js"></script>
    <!--引入jqgrid核心js-->
    <script src="${pageContext.request.contextPath}/static/jqgrid/js/jquery.jqGrid.min.js"></script>
    <!--引入jqgrid国际化js-->
    <script src="${pageContext.request.contextPath}/static/jqgrid/i18n/grid.locale-cn.js"></script>
    <!--引入bootstrap组件js-->
    <script src="${pageContext.request.contextPath}/static/bootstrap/js/bootstrap.min.js"></script>
    <!-- 书写代码-->
    <script>
        $(function () {
            $('#poetry').jqGrid({
                styleUI:'Bootstrap',
                url:'${pageContext.request.contextPath}/poems/Show',
                datatype:'json',
                mtype:'get',
                colNames:["编号","诗词名","作者","类型","来源","内容","作者简介"],
                colModel:[
                    {name:'id',align:'center',search:false},
                    {name:'name',align:'center',editable:true},
                    {name:'author',align:'center',editable:true},
                    {name:'type',align:'center',editable:true},
                    {name:'origin',align:'center',editable:true},
                    {name:'content',align:'center',editable:true,search:false},
                    {name:'authordes',align:'center',editable:true}
                ],
                pager:"#pager",
                rowNum:5,
                rowList:[5,10,15],
                cellurl:'/aa',
                viewrecords:true,
                caption:'唐诗宋词列表',
                cellEdit:true,
                editurl:'${pageContext.request.contextPath}/poems/cz',
                autowidth:true,
                height:240,
                loadComplete: function() {
                    var grid = $("#empList");
                    var ids = grid.getDataIDs();
                    for (var i = 0; i < ids.length; i++) {
                        grid.setRowData ( ids[i], false, {height: 35+i*2} );
                    }
                }
            }).navGrid('#pager',
                {add:true},   //参数2:开启工具栏编辑按钮
                {closeAfterEdit:true,reloadAfterSubmit:true},//编辑面板的配置
                {closeAfterAdd:true,reloadAfterSubmit:true},//添加面板的配置
                {},//删除的配置
                {
                    sopt:['eq','ne','cn']
                }
            );

            $("#clearES").click(function () {
                console.log("clear");
                //发送ajax请求到
                $.ajax({
                    url:'${pageContext.request.contextPath}/repos/clear',// 指定请求目标
                    method:'post', // 指定请求方式
                    //data:"", // 请求参数
                    success:function (data) { // 请求成功，接收的响应结果回调函数
                        console.log("清除成功");
                    },
                    error:function () {
                        alert('操作失败');
                    }
                });

            });
            $("#addES").click(function () {
                console.log("add");
                $.ajax({
                    url:'${pageContext.request.contextPath}/repos/addAll',// 指定请求目标
                    method:'post', // 指定请求方式
                    //data:'name=xiaoming', // 请求参数
                    success:function (data) { // 请求成功，接收的响应结果回调函数
                        console.log("添加成功");
                    },
                    error:function () {
                        alert('操作失败');
                    }
                });
            });
            $.ajax({
                url:'${pageContext.request.contextPath}/repos/showMh',
                method:'post',
                success:function (data) {
                    $("#hotso").empty();
                   // console.log("123")
                        // <button class="btn btn-primary" type="button">
                        // Messages <span class="badge">4</span>
                        // </button>
                    $.each(data,function(key,value) {
                        if (value >= 1.0) {
                            $('#hotso').append('<button class="btn btn-primary" type="button">' + key +
                                '<span class="badge" style="color:red">' + value +
                                '</span></button>'+'&nbsp;&nbsp;&nbsp;&nbsp;');
                        }
                        if (value < 1.0) {
                            $('#hotso').append('<button class="btn btn-primary" type="button">' + key +
                                '<span class="badge">' + value +
                               '</span></button>'+'&nbsp;&nbsp;&nbsp;&nbsp;');
                        }
                    });
                },
                error:function () {
                    console.log("显示热词失败")
                }
            });
            $("#addBtn").on('click',function () {
                var msg = $('#addInput').val();
                console.log(msg);
                //$("#myLabels").empty();
                $.ajax({
                    url:'${pageContext.request.contextPath}/repos/addtoci',//处理远程词典
                    type:'GET',
                    dataType:'text',
                    data:{
                        'msg':msg
                    },
                    success:function (data) {
                        console.log("成功");
                    }
                });
            });
            $.ajax({
                url:'${pageContext.request.contextPath}/repos/showzd',
                method:'post',
                success:function (data) {
                    $("#myLabels").empty();
                    $.each(data,function(key,value) {
                        if(value.length>2){
                            $('#myLabels').append('<div class="col-sm-4"> <div  class="alert alert-info alert-dismissible" role="alert"> ' +
                                '<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
                                '<span aria-hidden="true">&times;</span></button><span>' +value+
                                '</span></div> </div>');
                        }else{
                            $('#myLabels').append('<div class="col-sm-4"> <div  class="alert alert-success alert-dismissible" role="alert"> ' +
                                '<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
                                '<span aria-hidden="true">&times;</span></button><span>' +value+
                                '</span></div> </div>');
                        }

                    });
                },
                error:function () {
                    console.log("显示字典失败")
                }
            });
            $(document).on('close.bs.alert','.alert',function () {
                var text = $(this).children().next().html();
                console.log(text);
                $.ajax({
                    url:'${pageContext.request.contextPath}/repos/del',
                    dataType:'text',
                    data:{
                        'num':text
                    },
                    type:'GET',
                    success:function () {
                        console.log("成功");
                    }
                });

            });
        });

    </script>
</head>
<body>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">唐诗-宋词后台管理系统<small>V1.0</small></a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li class="btn-danger"><a href="javascript:0" id="clearES" >清除ES所有文档</a></li>
                <li class="btn-info"><a href="javascript:0" id="addES">基于jichu数据库重建ES索引库</a></li>
            </ul>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>
<!--布局系统 中心内容-->
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <table id="poetry"></table>
            <div id="pager" style="height:30px"></div>
        </div>
        <div class="col-sm-6">
            <br><br>
            <div class="panel panel-default">
                <div class="panel-heading" >全网热搜榜</div>
                <div class="panel-body" id="hotso">

                </div>
            </div>
        </div>
        <br><br>
        <div class="col-sm-6">
            <br><br>
            <div class="col-sm-10">
                <input type="text" class="form-control" placeholder="请输入要添加的热词..." id="addInput">
            </div>
            <div class="col-sm-2">
                <button class="btn btn-info" id="addBtn">添加远程词典</button>
            </div>
            <div class="col-sm-12" style="margin-top:20px;" id="myLabels">
                <div class="col-sm-4">
                     <div class="alert alert-success alert-dismissible" role="alert">
                         <button type="button" class="close"  data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                         碰瓷
                     </div>
                </div>

            </div>
        </div>
    </div>
</div>


</body>
</html>
