<div class="row">
  <!-- left column -->
    <div class="box-header with-border" ng-app="">
        <h3 class="box-title">DATA STREAM 全局配置</h3>
    </div>

    <form class="form-horizontal" id="settingsConf">
        <div class="form-group">
            <label class="col-sm-3 control-label">stormZookeeper地址</label>
            <div class="col-sm-5">
                <input type="text" class="form-control" name="stormZk"  placeholder="sofastream 所连接的storm集群地址" value="${stormZk!}">
            </div>
            <span>sofastream 所连接的storm集群地址</span>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">fs.defaultFS</label>
            <div class="col-sm-5">
               <input type="text" class="form-control" name="hdfs" id="hdfs" placeholder="hdfs地址" value="${hdfs!}">
            </div>
            <span>HDFS地址</span>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">yarn.resourcemanager.address</label>
            <div class="col-sm-5">
               <input type="text" class="form-control" name="yarn" id="yarn" placeholder="yarn.resourcemanager.address-url"  value="${yarn!}">
            </div>
            <span>客户端连接Yarn ResourceManager地址</span>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">yarn.resourcemanager.scheduler.address</label>
            <div class="col-sm-5">
              <input type="text" class="form-control" name="yarns" id="yarns" placeholder="yarn.resourcemanager.scheduler.address-url" value="${yarns!}">
            </div>
            <span>ApplicationMaster连接Yarn地址</span>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">yarn.resourcemanager.ui.address</label>
            <div class="col-sm-5">
                <input type="text" class="form-control" name="yarnui" id="yarnui" placeholder="yarn.resourcemanager.ui.address-url"   value="${yarnui!}">
            </div>
            <span>yarn web监控地址</span>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-3 col-sm-5 text-center">
                <button type="submit" class="btn btn-default btn-primary">更新配置</button>
            </div>
        </div>
    </form>
</div>
</body>
<script type="text/javascript">

    $(document).ready(function () {
        $(":submit").click(function(){
            var stormZk = $(':input[name="stormZk"]').val();
            var hdfs = $(':input[name="hdfs"]').val();
            var yarn = $(':input[name="yarn"]').val();
            var yarns = $(':input[name="yarns"]').val();
            var yarnui = $(':input[name="yarnui"]').val();

            if(checkNull(stormZk, "storm zookeeper 地址不能为空")){
                return false;
            }

            if(checkNull(hdfs, "HDFS 地址不能为空")){
                return false;
            }

            if(checkNull(yarn, "Yarn地址不能为空")){
                return false;
            }
            if(checkNull(yarns, "Yarn Schedule地址不能为空")){
                return false;
            }
            if(checkNull(yarnui, "Yarn UI地址不能为空")){
                return false;
            }

            $.ajax({
                url: '/settingsSet',
                type: 'POST',
                data: $("#settingsConf").serialize(),
                success: function (data) {
                    alertModel(data);
                },
                error: function(data){
                    alertModel(data);
                }
            });
            return false;
        })
    });

    function checkNull(variable, msg) {
        console.log(( variable == null || variable == undefined || variable == ''))
        if ( variable == null || variable == undefined || variable == '') {
            alertModel(msg);
            return true;
        }else {
            return false;
        }
    }


</script>