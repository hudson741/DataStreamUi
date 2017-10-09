<div class="row">
    <div class="col-md-6">
        <div class="box box-solid">
            <!-- /.box-header -->
            <div class="box-body">
                <div class="box-group" id="accordion">
                    <!-- we are adding the .panel class so bootstrap.js collapse plugin detects it -->
                    <div class="panel box box-primary">
                        <div class="box-header with-border">
                            <h4 class="box-title">
                                <a data-toggle="collapse" data-parent="#accordion" >
                                   storm集群初始化说明
                                </a>
                            </h4>
                        </div>
                        <div id="collapseOne" class="panel-collapse collapse in">
                            <div class="box-body">
                                <div>1. storm集群标识关联一个storm集群群组，可与业务关联</div>
                                <div>2. 需已确保注册机器已安装好docker运行环境</div>
                                <div>3. netUrl标识storm集群连接的overlay网络，用以实现storm组件在docker环境下网络互通</div>
                                <div>4. uiIp为storm监控组件所使用的docker内网IP</div>
                                <div>5. nimbusSeeds标识nimbus主节点使用的docker内网ip，例：192.168.10.3,192.169.10.4使用逗号分隔</div>
                                <div>6. drpcServers标识drpc组件使用的docker内网Ip，同nimbusSeeds，非必填</div>
                                <div>7. 如果集群使用storm的drpc功能，则需在集群初始化时指定ip地址，集群创建完成后，不可再创建</div>
                                <div>8. supervisor节点请使用storm单容器发布创建，并指定配置</div>
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
                <h3 class="box-title">初始化storm集群</h3>
            </div>
            <!-- /.box-header -->
            <!-- form start -->
            <form role="form" method="post" id="dpi" action="dockerPub" enctype="multipart/form-data">
                <div class="box-body">

                    <div class="form-group">
                        <label>storm集群标识</label>
                        <input type="text" class="form-control" name="appName" id="appName" placeholder="唯一区分storm集群标识">
                    </div>

                    <div class="form-group">
                        <label>netUrl</label>
                        <input type="text" class="form-control" name="netUrl" id="netUrl" placeholder="所连接的docker网络环境标识">
                    </div>

                    <div class="form-group">
                        <label>uiIp</label>
                        <input type="text" class="form-control" name="uiIp" id="uiIp" placeholder="storm-ui docker IP">
                    </div>

                    <div class="form-group">
                        <label>nimbusSeeds</label>
                        <input type="text" class="form-control" name="nimbusSeeds" id="nimbusSeeds" placeholder="storm nimbus主节点dockerIp地址">
                    </div>

                    <div class="form-group">
                        <label>drpcServers</label>
                        <input type="text" class="form-control" name="drpcServers" id="drpcServers" placeholder="storm drpc主节点dockerIp地址">
                    </div>

                </div>
                <!-- /.box-body -->

                <div class="box-footer">
                    <button type="button" class="btn btn-primary" onclick="stormdockerinitjs()">发布</button>
                </div>
            </form>
        </div>
    </div>
</div>


<script type="text/javascript">
    function stormdockerinitjs(){
        $.ajax({
            url: '/dockerPub',
            type: 'post',
            data: $("#dpi").serialize(),
            success: function (data) {
                alertMsg(data);
            },
            error: function(data){
                alertMsg(data);
            }
        });
    };

</script>