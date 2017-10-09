<div class="row">
  <!-- left column -->
  <div class="col-md-12">
    <!-- general form elements -->
    <div class="box box-primary">
      <div class="box-header with-border" ng-app="">
        <h3 class="box-title">DATA STREAM 全局配置</h3>
      </div>
      <!-- /.box-header -->
      <!-- form start -->
      <form role="form" method="post" id="settingsConf" action="settingsSet" enctype="multipart/form-data">
        <div class="box-body">
          <div class="form-group">
            <label>stormZookeeper</label>
            <input type="text" class="form-control" name="stormZk" id="stormZk" placeholder="sofastream 所连接的storm集群地址" value="${stormZk!}">
          </div>
          <div class="form-group">
            <label>fs.defaultFS</label>
            <input type="text" class="form-control" name="hdfs" id="hdfs" placeholder="hdfs地址" value="${hdfs!}">
          </div>
          <div class="form-group">
            <label>yarn.resourcemanager.address</label>
            <input type="text" class="form-control" name="yarn" id="yarn" placeholder="yarn.resourcemanager.address-url"  value="${yarn!}">
          </div>
          <div class="form-group">
            <label> yarn.resourcemanager.scheduler.address</label>
            <input type="text" class="form-control" name="yarns" id="yarns" placeholder="yarn.resourcemanager.scheduler.address-url" value="${yarns!}">
          </div>
          <div class="form-group">
            <label>yarn.resourcemanager.ui.address</label>
            <input type="text" class="form-control" name="yarnui" id="yarnui" placeholder="yarn.resourcemanager.ui.address-url"   value="${yarnui!}">
          </div>
        </div>
        <!-- /.box-body -->

        <div class="box-footer">
          <button type="button" class="btn btn-primary" onclick="confsetting()">更新</button>
        </div>
      </form>
    </div>
  </div>
</div>
</body>
<script type="text/javascript">
    function confsetting(){
        $.ajax({
            url: '/settingsSet',
            type: 'post',
            data: $("#settingsConf").serialize(),
            success: function (data) {
                alertMsg(data);
            },
            error: function(data){
                alertMsg(data);
            }
        });
    };

</script>