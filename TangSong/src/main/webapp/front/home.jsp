<%--
  Created by IntelliJ IDEA.
  User: qiao
  Date: 2019/11/20
  Time: 20:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>唐诗宋词检索系统</title>
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
    <script>
        $(function(){


            $('#searchBtn').on('click',function () {
                var msg = $('#message').val();

                $("#result").empty();
                $.ajax({
                    url:'${pageContext.request.contextPath}/repos/showAll',
                    type:'GET',
                    dataType:'JSON',
                    data:{
                        'msg':msg
                    },
                    success:function (data) {
                        $.each(data,function(i,poetry){
                            $('#result').append('<div class="col-sm-12">' +
                                '            <ul>' +
                                '                <li><span class="text-info">'+poetry.name+'</span></li>' +
                                '                <li>'+poetry.author+"&nbsp;"+poetry.type+'</li>' +
                                '                <li>'+poetry.content+'</li>' +
                                '                <li>'+poetry.authordes+'</li>' +
                                '            </ul>' +
                                '        </div>');
                        });
                        console.log(data);
                    },
                    error:function () {
                        alert('输入的数据无法匹配');
                    }
                });
            });
           // $('#searchBtn').click();
        });
    </script>
</head>
<body>
<h1 class="h1 text-center">唐诗宋词检索系统</h1>
<br>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-2" align="right">检索唐诗宋词</div>
        <div class="col-sm-8">
            <input class="form-control" id="message" type="text" placeholder="输入您喜欢的诗词...">
        </div>
        <div class="col-sm-2">
            <button class="btn btn-primary" id="searchBtn">搜索</button>
        </div>
    </div>
    <div class="row" id="result">

    </div>
</div>

</body>
</html>
