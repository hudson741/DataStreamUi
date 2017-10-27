<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title></title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width">
    <link rel="stylesheet" href="dist/css/user.css?v=1495101086124">
    <link rel="stylesheet" href="plugins/bootstrap/css/bootstrap.min.css">
</head>
<body>
<div class="container"  >
    <div class="col-sm-4 col-sm-offset-4 form-box" style="margin-top: 100px;">
        <form user-manage="loginIn" method="post" autocomplete="off">
            <div class="form-group">
                <input type="text" class="form-control" placeholder="用户名"  name ="username" autocomplete="off"/>
            </div>
            <div class="form-group">
                <input type="password" class="form-control" placeholder="密码"  name ="password" autocomplete="off"/>
            </div>
            <div class="form-group">
                <button class="btn btn-success form-control"  type="submit">登&nbsp录</button>
            </div>
        </form>
    </div>

    <div class="modal fade" id="lgModal">
        <div class="modal-dialog modal-sm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span></button>
                    <h4 class="modal-title">提示</h4>
                </div>
                <div class="modal-body">
                </div>
                <div class="modal-footer text-center">
                    <button type="button" class="btn btn-primary center-block" data-dismiss="modal">确认</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>


</div>

    <script src="plugins/jQuery/jquery-2.2.3.min.js"></script>
    <script src="plugins/bootstrap/js/bootstrap.min.js"></script>
    <script src="dist/js/app.js"></script>
    <script>
        $(document).ready(function () {
            $(":submit").click(function () {
                var username = $('input[name="username"]').val();
                if(username == null || username==""){
                    alertModel("用户名不能为空")
                    return false;
                }
                var password = $('input[name="password"]').val();
                if(password == null || password == ""){
                    alertModel("密码不能为空")
                    return false;
                }

                $.ajax({
                    type: "POST",
                    url: "/logind",
                    data: $("form").serialize(),
                    dataType: "json",
                    success: function(result){
                        if(result.code == 0){//登录成功
                            parent.location.href ='/index';
                        }else{
                            alertModel("登录失败: "+ result.msg)
                        }
                    }
                });
                return false;
            })
        })
    </script>
</body>
</html>
