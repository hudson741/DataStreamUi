<div class="diy_table_panel">
    <div class="diy_table_head">
        <div class="box">
        <div class="box-header">
        <h3 class="box-title">docker任务</h3>
        </div>
        <div class="box-body">
        <table class="table table-bordered table-hover">
        <thead>
        <tr>
        <th>address</th>
        <th>netUrl</th>
        <th>iPBind</th>
        <th>dockerIp</th>
        <th>businessType</th>
        <th>businessTag</th>
        <th>VCPUs</th>
        <th>memory(MB)</th>
        <th>state</th>
        <th>管理</th>
        </tr>
        </thead>
        <tbody>


        <#if yarn.dockerjobs??>
        <#list yarn.dockerjobs as job>
        <tr class="active">
        <td>${job.runIp!}</td>
        <td>${job.netUrl!}</td>
        <td>${job.iPBind!}</td>
        <td>${job.dockerIp!}</td>
        <td>${job.businessType!}</td>
        <td>${job.businessTag!}</td>
        <td>${job.cpu!}</td>
        <td>${job.memory!}</td>
        <td>${job.state!}</td>
        <td>
            <div class="form-group">
                <div class="btn-group">
                    <button type="button" class="btn btn-info">docker节点管理</button>
                    <button type="button" class="btn btn-info dropdown-toggle" data-toggle="dropdown">
                        <span class="caret"></span>
                        <span class="sr-only">Toggle Dropdown</span>
                    </button>
                    <ul class="dropdown-menu" role="menu">
                        <li onclick="yarnStopDockerJob('${job.jobId!}')"><a href="#">停止</a></li>
                        <li onclick="yarnRestart('${job.jobId!}')"><a href="#">重启</a></li>
                        <li onclick="removeDockerJob('${job.jobId!}')"><a href="#">移除</a></li>
                    </ul>
                </div>
            </div>
        </td>
        </tr>
        </#list>
        </#if>
        </tbody>
        </table>
        </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    function yarnStopDockerJob(jobId){
        $.ajax({
            url: '/yarnStopDockerJob',
            type: 'post',
            data : {
                jobId:jobId
            },
            success: function (data) {
                alertMsg(data,"success");
            }
            ,
            error: function(data){
                alertMsg(data);
            }
        });
    };

    function yarnRestart(jobId){
        $.ajax({
            url: '/yarnRestart',
            type: 'post',
            data : {
                jobId:jobId
            },
            success: function (data) {
                alertMsg(data,"success");
            },
            error: function(data){
                alertMsg(data);
            }
        });
    };

    function removeDockerJob(jobId){
        $.ajax({
            url: '/removeDockerJob',
            type: 'post',
            data : {
                jobId:jobId
            },
            success: function (data) {
                alertMsg(data,"success");
            },
            error: function(data){
                alertMsg(data);
            }
        });
    };

</script>