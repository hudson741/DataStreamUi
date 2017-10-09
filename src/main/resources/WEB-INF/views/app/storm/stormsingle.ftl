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
                                    storm单容器发布说明
                                </a>
                            </h4>
                        </div>
                        <div id="collapseOne" class="panel-collapse collapse in">
                            <div class="box-body">
                                <div>1. dockerIp为需发布组件所占用的docker内网ip</div>
                                <div>2. process为要启动的storm组件类型</div>
                                <div>3. 配置类型为发布节点使用的配置</div>
                                <div>4. 如果希望强制组件启动在指定节点，则填入node，不填写将根据负载情况进行分配</div>
                                <div>5. host为docker容器内的/etc/hosts映射，如果节点需要配置映射，则发布时在此填写。发布后，若要修改请登录docker容器内修改。</div>
                                <div>6. 为避免端口映射冲突，请勿在同一节点起多个nimbus,ui </div>
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
                <h3 class="box-title">storm单容器发布</h3>
            </div>
            <!-- /.box-header -->
            <!-- form start -->
            <form role="form" id="sdp" method="post" action="stormdockerPub" enctype="multipart/form-data">
                <div class="box-body">

                    <div class="form-group">
                        <label>dockerIp</label>
                        <input type="text" class="form-control" name="dockerIp" id="dockerIp" placeholder="docker内网IP">
                    </div>

                    <div class="form-group">
                        <label>process</label>
                        <select name = "process" class="form-control">
                            <option value="nimbus">nimbus主节点</option>
                            <option value="supervisor" selected>supervisor从节点</option>
                            <option value="ui">ui监控节点</option>
                        </select>
                    </div>

                    <div class="form-group">
                            <label>配置类型</label>
                            <select name = "stormDockerConf" class="form-control">
                                <option value="0" selected>低配1core,1G(通常是测试使用，生成环境中不建议)</option>
                                <option value="1">中配1core,2G</option>
                                <option value="2">均衡高配2core,4G</option>
                                <option value="3">密集计算型4core,4G</option>
                            </select>
                    </div>

                    <div class="form-group">
                        <label>node(非必填，指定启动节点):</label>
                        <input type="text" class="form-control" name="node" id="node" placeholder="指定启动节点地址">
                    </div>

                    <div class="form-group">
                        <label>host</label>
                        <input type="text" class="form-control" name="host" id="host" placeholder="组件hosts配置，例:hostA:192.168.233.11,hostB:192.168.233.12">
                    </div>

                </div>
                <!-- /.box-body -->

                <div class="box-footer">
                    <button type="button" class="btn btn-primary" onclick="stormdockerPubjs()">发布</button>
                </div>
            </form>
        </div>
    </div>
</div>


<script type="text/javascript">
    function stormdockerPubjs(){
        $.ajax({
            url: '/stormdockerPub',
            type: 'post',
            data: $("#sdp").serialize(),
            success: function (data) {
                alertMsg(data);
            },
            error: function(data){
                alertMsg(data);
            }
        });
    };

</script>