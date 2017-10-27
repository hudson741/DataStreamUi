<div class="diy_table_panel">
    <div class="diy_table_head">
            <table id="yarn_status" class="nowrap table table-condensed dataTable no-footer" width="100%" cellspacing="0" role="grid" style="width: 100%;">
                <tbody>
                <tr class="odd">
                    <td class="sorting_disabled" rowspan="1" colspan="1" style="width: 564px;"><a href="http://${yarn.href}" target="_blank">hadoop内置监控界面</a></td>
                </tr>
                </tbody>
            </table>
        <div class="diy_table_body box">

            <div class="box-body no-padding">
                <table class="table">
                    <tr>
                        <th>资源使用</th>
                        <th>使用对比</th>
                        <th>Progress</th>
                        <th style="width: 40px">使用百分比</th>
                    </tr>
                    <tr>
                        <td>Active / Total Nodes</td>
                        <td>${yarn.cluster.activeNodes!} / ${yarn.cluster.totalNodes!}</td>
                        <td>
                            <div class="progress progress-xs">
                                <div class="progress-bar progress-bar-danger" style="width: ${active2total!}%"></div>
                            </div>
                        </td>
                        <td><span class="badge bg-red">${active2total!}%</span></td>
                    </tr>
                    <tr>
                        <td>Running / Total apps</td>
                        <td>${yarn.cluster.appsRunning!} / ${yarn.cluster.appsSubmitted!}</td>
                        <td>
                            <div class="progress progress-xs">
                                <div class="progress-bar progress-bar-yellow" style="width: ${appRun2appT!}%"></div>
                            </div>
                        </td>
                        <td><span class="badge bg-yellow">${appRun2appT!}%</span></td>
                    </tr>
                    <tr>
                        <td>Used / Total VCPUs</td>
                        <td>${yarn.cluster.allocatedVirtualCores!} / ${yarn.cluster.totalVirtualCores!}</td>
                        <td>
                            <div class="progress progress-xs progress-striped active">
                                <div class="progress-bar progress-bar-primary" style="width: ${vc2tc!}%"></div>
                            </div>
                        </td>
                        <td><span class="badge bg-light-blue">${vc2tc!}%</span></td>
                    </tr>
                    <tr>
                        <td>Used / Total RAM (GB))</td>
                        <td>${yarn.cluster.allocateGB!} / ${yarn.cluster.totalGB!}</td>
                        <td>
                            <div class="progress progress-xs progress-striped active">
                                <div class="progress-bar progress-bar-success" style="width: ${vc2tc!}%"></div>
                            </div>
                        </td>
                        <td><span class="badge bg-green">${vc2tc!}%</span></td>
                    </tr>
                </table>
            </div>


        <div class="box">
            <div class="box-header">
                <h3 class="box-title">物理节点</h3>
            </div>
            <div class="box-body">
                <table class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>address</th>
                    <th>state</th>
                    <th>containers</th>
                    <th>memoryUsed(MB)</th>
                    <th>memoryAvail(MB)</th>
                    <th>vCoresUsed</th>
                    <th>vCoresAvailable</th>
                </tr>
                </thead>
                <tbody>


                <#if yarn.nodes.nodes.node??>
                <#list yarn.nodes.nodes.node as node>
                <tr class="active">
                    <td>${node.nodeHTTPAddress}</td>
                    <td>${node.state}</td>
                    <td>${node.numContainers}</td>
                    <td>${node.usedMemoryMB}</td>
                    <td>${node.availMemoryMB}</td>
                    <td>${node.usedVirtualCores}</td>
                    <td>${node.availableVirtualCores}</td>
                </tr>
                </#list>
                </#if>
                </tbody>
            </table>
            </div>
        </div>

        <div class="box">
            <div class="box-header">
                <h3 class="box-title">已发布应用</h3>
            </div>
            <div class="box-body">
                <table class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>id</th>
                    <th>type</th>
                    <th>User</th>
                    <th>State</th>
                    <th>Containers</th>
                    <th>VCPUs</th>
                    <th>Memory(GB)</th>
                    <th>Queue</th>
                    <th>amAddress</th>
                    <th>StartTime</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <#if yarn.apps??>
                <#list yarn.apps as app>
                <tr  class="active">
                    <td>${app.id!}</td>
                    <td>${app.name!}</td>
                    <td>${app.user!}</td>
                    <td>${app.state!}</td>
                    <th>${app.runningContainers!}</th>
                    <td>${app.allocatedVCores!}</td>
                    <td>${app.allocatedMB!}</td>
                    <td>${app.queue!}</td>
                    <td>${app.amHostHttpAddress!}</td>
                    <td>${app.startedTime!}</td>
                    <td><input type="button" value="kill" onclick="yarnKillApp('${app.id!}')"></td>
                </tr>
                </#list>
                </#if>
                </tbody>
            </table>
            </div>
        </div>

        </div>
    </div>
</div>

<script>
function yarnKillApp(id) {
    $.ajax({
        url: '/yarnKillApp',
        type: 'get',
        data : {
            appId:id
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