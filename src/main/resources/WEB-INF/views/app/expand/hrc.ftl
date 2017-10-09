<div class="row">
    <div class="col-md-6">
        <div class="box box-solid">
            <div class="box-header with-border">
                <h3 class="box-title">部署说明</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
                <div class="box-group" id="accordion">
                    <!-- we are adding the .panel class so bootstrap.js collapse plugin detects it -->
                    <div class="panel box box-primary">
                        <div class="box-header with-border">
                            <h4 class="box-title">
                                <a data-toggle="collapse" data-parent="#accordion" >
                                   本地hadoop用户创建说明
                                </a>
                            </h4>
                        </div>
                        <div id="collapseOne" class="panel-collapse collapse in">
                            <div class="box-body">
                                <div>1. 本地hadoop用户用于与所有远程服务器实现配置传输，秘钥同步等</div>
                                <div>2. 用户名与远程服务器用户名一致</div>
                                <div>3. 本地hadoop用户使用root创建,请在右侧填写超管用户名，密码并提交</div>
                                <div>4. 如果已创建，忽略即可，但需已与本地实现免密，秘钥文件应在/home/hadoop/.ssh下</div>
                                <div>5. 如果本地hadoop用户创建过程中出现问题，请删除/home/hadoop目录下所有文件，并执行userdel hadoop删除用户，重新创建</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- left column -->
    <div class="col-md-6">
        <!-- general form elements -->
        <div class="box box-primary">
            <div class="box-header with-border" ng-app="">
                <h3 class="box-title">创建本地hadoop用户</h3>
            </div>
            <!-- /.box-header -->
            <!-- form start -->
            <form role="form" method="post" id="lres" action="registerRoot" enctype="multipart/form-data">
                <div class="box-body">
                    <div class="form-group">
                        <label>ROOT</label>
                        <input type="text" class="form-control" name="admin" id="admin" placeholder="超管用户名">
                    </div>
                    <div class="form-group">
                        <label>password</label>
                        <input type="password" class="form-control" name="pwd" id="pwd" placeholder="超管密码">
                    </div>
                </div>
                <!-- /.box-body -->

                <div class="box-footer">
                    <button type="button" class="btn btn-primary" onclick="hlcel()">提交</button>
                </div>
            </form>
        </div>
    </div>
</div>





<div class="row">
    <div class="col-md-6">
        <div class="panel box box-danger">
            <div class="box-header with-border">
                <h4 class="box-title">
                    <a data-toggle="collapse" >
                        远程hadoop用户创建说明
                    </a>
                </h4>
            </div>
            <div id="collapseTwo" class="panel-collapse collapse in">
                <div class="box-body">
                    <div>本过程主要执行如下步骤：</div>
                    <div>1. 检验远程hadoop用户是否存在，不存在给予创建，并生成秘钥信息，并将hadoop用户添加sudo权限</div>
                    <div>2. 将远程hadoop用户的秘钥信息保存至本地，并将本地秘钥信息复制至远程，实现远程机器与本地以及所有注册机器免密</div>
                    <div>3. 更新本地，远程，以及所有注册节点的/etc/hosts加入新增服务器的host映射</div>
                    <div>4. 传输hadoop,jdk安装包至远程，并进行远程安装(此过程时间略长)</div>
                    <div>5. 安装完成后，待成功设置好远程服务器环境变量后，执行hadoop的配置同步</div>
                    <div>6. 过程中如果出现异常，难以修复，删除远程/home/hadoop下所有文件，远程执行userdel hadoop，重试即可。</div>
                    <div>7. 请保证节点间网络情况较好，避免失败率高，如果多次执行失败，请查看后台系统日志，联系相关开发人员，</div>
                </div>
            </div>
        </div>
    </div>

    <!-- left column -->
    <div class="col-md-6">
        <!-- general form elements -->
        <div class="box box-primary">
            <div class="box-header with-border" ng-app="">
                <h3 class="box-title">远程服务器注册</h3>
            </div>
            <!-- /.box-header -->
            <!-- form start -->
            <form role="form" method="post" id="expandr" action="expand" enctype="multipart/form-data">
                <div class="box-body">
                    <div class="form-group">
                        <label>IP</label>
                        <input type="text" class="form-control" name="IP" id="IP" placeholder="新注册物理服务器的ip地址">
                    </div>
                    <div class="form-group">
                        <label>HOST</label>
                        <input type="text" class="form-control" name="host" id="host" placeholder="物理节点host">
                    </div>
                    <div class="form-group">
                        <label>ROOT</label>
                        <input type="text" class="form-control" name="rAdmin" id="rAdmin" placeholder="超管用户">
                    </div>
                    <div class="form-group">
                        <label>password</label>
                        <input type="password" class="form-control" name="password" id="password" placeholder="超管密码">
                    </div>
                </div>
                <!-- /.box-body -->
                <div class="box-footer">
                    <button type="button" class="btn btn-primary" onclick="hrcel()">提交</button>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
<script type="text/javascript">
    function hrcel(){
        alert("全程需要大约1至3分钟左右");
        $.ajax({
            url: '/expand',
            type: 'post',
            data: $("#expandr").serialize(),
            success: function (data) {
                alertMsg(data);
            },
            error: function(data){
                alertMsg(data);
            }

        });
    }

    function hlcel(){
        $.ajax({
            url: '/registerRoot',
            type: 'post',
            data: $("#lres").serialize(),
            success: function (data) {
                alertMsg(data);
            },
            error: function(data){
                alertMsg(data);
            }
        });
    };

</script>