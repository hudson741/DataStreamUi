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


            <div class="box-header">
                <h3 class="box-title">物理节点</h3>
            </div>

            <div id="cvmList" class="tc-table-grid tc-table-grid-cvm"><div data-role="grid-view" style="">
                <div data-grid-panel="" class="tc-15-table-panel">
                    <div data-grid-head="" class="tc-15-table-fixed-head" style="width:auto;padding-right: 0px; display: block; ">
                        <table class="tc-15-table-box" style="min-width: 100%">
                        <#--<colgroup>-->
                        <#--<!--if start&ndash;&gt;<col style="width: 26px; "><!--if end&ndash;&gt;-->
                        <#--<!--repeat start&ndash;&gt;<col style="width: auto; min-width: 94px; " class="col-svrid"><col style="width: 44px; min-width: 44px; " class=""><col style="width: 100px; min-width: 100px; " class=""><col style="width: 92px; min-width: 92px; " class=""><col style="width: 94px; min-width: 94px; " class=""><col style="width: 116px; " class=""><col style="width: 172px; " class=""><col style="width: 114px; " class=""><col style="width: 136px; " class=""><!--repeat end&ndash;&gt;-->
                        <#--</colgroup>-->
                            <thead>
                            <tr>
                                </th><!--if end-->
                                <!--repeat start--><th class="text-left">
                                <div>
                                    <!--if start--><span class="text-overflow" title="address">address</span><!--if end--><!--content end-->
                                </div>
                                <!--if start--><!--if end-->
                            </th><th  class="text-left">
                                <div>
                                    <!--if start--><span class="text-overflow" title="state">state</span><!--if end--><!--content end-->
                                </div>
                            </th><th class="text-left">
                                <div>
                                    <!--if start--><span class="text-overflow" title="containers">containers</span><!--if end--><!--content end-->
                                </div>
                            </th><th class="text-left">
                                    <div>
                                        <!--content start--><span class="text-overflow" id="zoneIdSelector">memoryUsed(MB)</span><!--content end-->
                                    </div>
                            </th><th class="text-left">
                                <div>
                                    <!--content start--><span class="text-overflow" id="zoneIdSelector">memoryAvail(MB)</span><!--content end-->
                                </div>
                            </th><th class="text-left">
                                <div>
                                    <!--content start--><span class="text-overflow" id="deviceClassSelector">vCoresUsed</span><!--content end-->
                                </div>
                            </th><th class="text-left">
                                    <div>
                                        <!--content start--><span class="text-overflow" id="deviceClassSelector">vCoresAvailable</span><!--content end-->
                                    </div>
                                </th><!--repeat end-->
                            </tr>
                            </thead>
                        </table>
                    </div>
                    <div data-grid-body="" class="tc-15-table-fixed-body" style="min-height: 200px; height: auto; max-height: auto; ">
                        <table style="min-width:100%" class="tc-15-table-box tc-15-table-rowhover">
                        <#--<colgroup>-->
                        <#--<!--if start&ndash;&gt;<col style="width: 26px; "><!--if end&ndash;&gt;-->
                        <#--<!--repeat start&ndash;&gt;<col style="width: auto; " class="col-svrid"><col style="width: 44px; " class=""><col style="width: 100px; " class=""><col style="width: 92px; " class=""><col style="width: 94px; " class=""><col style="width: 116px; " class=""><col style="width: 172px; " class=""><col style="width: 114px; " class=""><col style="width: 136px; " class=""><!--repeat end&ndash;&gt;-->
                        <#--</colgroup>-->
                            <tbody>

                            <#if yarn.nodes.nodes.node??>
                                <#list yarn.nodes.nodes.node as node>
                                <tr class="item-row
               " data-uinstanceid="ins-6qaa9ksm" data-role="cvm_row" data-devicelanip="10.104.133.159" data-uuid="a155d137-dbf6-4fcd-b8fe-afe8db36d3eb" data-index="0">
                                    <!--if start--><td class="text-left">
                                    <div>
                                        <!--content start--><div><span class="status-col"><span class="text-overflow text-success">${node.nodeHTTPAddress!}</span></span></div><!--content end-->
                                    </div>
                                </td><td class="text-left">
                                    <div>
                                        <!--content start--><div>${node.state!}</div><!--content end-->
                                    </div>
                                </td><td class="text-left">
                                    <div>
                                        <!--content start--><div>${node.numContainers!}</div><!--content end-->
                                    </div>
                                </td><td class="text-left">
                                    <div>
                                        <div>
                                            <!--content start--><div>${node.usedMemoryMB!}</div><!--content end-->
                                        </div><!--content end-->
                                    </div>
                                </td><td class="text-left">
                                    <div>
                                        <!--content start--><div>${node.availMemoryMB!}</div><!--content end-->
                                    </div>
                                </td><td class="text-left">
                                    <div>
                                        <!--content start--><div>${node.usedVirtualCores!}</div><!--content end-->
                                    </div>
                                </td><td class="text-left">
                                    <div>
                                        <!--content start--><div>${node.availableVirtualCores!}</div><!--content end-->
                                    </div>
                                </td><!--repeat end-->
                                </tr>
                                </#list>
                            </#if>
                            <!--repeat end-->
                            <!--if start--><!--if end-->
                            </tbody>
                        </table>
                    </div>

                    <!-- 固定列 -->
                    <!--if start--><!--if end-->
                </div>

            </div>





        </div>

            <div class="box-header">
                <h3 class="box-title">已发布应用</h3>
            </div>


            <div id="cvmList" class="tc-table-grid tc-table-grid-cvm"><div data-role="grid-view" style="">
                <div data-grid-panel="" class="tc-15-table-panel">
                    <div data-grid-head="" class="tc-15-table-fixed-head" style="width:auto;padding-right: 0px; display: block; ">
                        <table class="tc-15-table-box" style="min-width: 100%">
                        <#--<colgroup>-->
                        <#--<!--if start&ndash;&gt;<col style="width: 26px; "><!--if end&ndash;&gt;-->
                        <#--<!--repeat start&ndash;&gt;<col style="width: auto; min-width: 94px; " class="col-svrid"><col style="width: 44px; min-width: 44px; " class=""><col style="width: 100px; min-width: 100px; " class=""><col style="width: 92px; min-width: 92px; " class=""><col style="width: 94px; min-width: 94px; " class=""><col style="width: 116px; " class=""><col style="width: 172px; " class=""><col style="width: 114px; " class=""><col style="width: 136px; " class=""><!--repeat end&ndash;&gt;-->
                        <#--</colgroup>-->
                            <thead>
                            <tr>
                              <th  class="text-left">
                                <div>
                                    <!--if start--><span class="text-overflow" >type</span><!--if end--><!--content end-->
                                </div>
                            </th><th class="text-left">
                                <div>
                                    <!--if start--><span class="text-overflow" >user</span><!--if end--><!--content end-->
                                </div>
                            </th><th class="text-left">
                                <div>
                                    <!--content start--><span class="text-overflow" id="zoneIdSelector">State</span><!--content end-->
                                </div>
                            </th><th class="text-left">
                                <div>
                                    <!--content start--><span class="text-overflow" id="zoneIdSelector">Containers</span><!--content end-->
                                </div>
                            </th><th class="text-left">
                                <div>
                                    <!--content start--><span class="text-overflow" id="zoneIdSelector">VCPUs</span><!--content end-->
                                </div>
                            </th><th class="text-left">
                                <div>
                                    <!--content start--><span class="text-overflow" id="zoneIdSelector">Memory(GB)</span><!--content end-->
                                </div>
                            </th><th class="text-left">
                                <div>
                                    <!--content start--><span class="text-overflow" id="zoneIdSelector">Queue</span><!--content end-->
                                </div>
                            </th><th class="text-left">
                                <div>
                                    <!--content start--><span class="text-overflow" id="zoneIdSelector">amAddress</span><!--content end-->
                                </div>
                            </th><th class="text-left">
                                <div>
                                    <!--content start--><span class="text-overflow" id="zoneIdSelector">StartTime</span><!--content end-->
                                </div>
                            </th><th class="text-left">
                                <div>
                                    <!--content start--><span class="text-overflow" id="deviceClassSelector">操作</span><!--content end-->
                                </div>
                            </th><!--repeat end-->
                            </tr>
                            </thead>
                        </table>
                    </div>
                    <div data-grid-body="" class="tc-15-table-fixed-body" style="min-height: 350px; height: auto; max-height: auto; ">
                        <table style="min-width:100%" class="tc-15-table-box tc-15-table-rowhover">
                        <#--<colgroup>-->
                        <#--<!--if start&ndash;&gt;<col style="width: 26px; "><!--if end&ndash;&gt;-->
                        <#--<!--repeat start&ndash;&gt;<col style="width: auto; " class="col-svrid"><col style="width: 44px; " class=""><col style="width: 100px; " class=""><col style="width: 92px; " class=""><col style="width: 94px; " class=""><col style="width: 116px; " class=""><col style="width: 172px; " class=""><col style="width: 114px; " class=""><col style="width: 136px; " class=""><!--repeat end&ndash;&gt;-->
                        <#--</colgroup>-->
                            <tbody>

                            <#if yarn.apps??>
                                <#list yarn.apps as app>
                                <tr class="item-row
               " data-uinstanceid="ins-6qaa9ksm" data-role="cvm_row" data-devicelanip="10.104.133.159" data-uuid="a155d137-dbf6-4fcd-b8fe-afe8db36d3eb" data-index="0">
                                    <td class="text-left">
                                    <div>
                                        <!--content start--><div>${app.name!}</div><!--content end-->
                                    </div>
                                </td><td class="text-left">
                                    <div>
                                        <!--content start--><div>${app.user!}</div><!--content end-->
                                    </div>
                                </td><td class="text-left">
                                    <div>
                                        <!--content start--><div>${app.state!}</div><!--content end-->
                                    </div>
                                </td><td class="text-left">
                                    <div>
                                        <!--content start--><div>${app.runningContainers!}</div><!--content end-->
                                    </div>
                                </td><td class="text-left">
                                    <div>
                                        <!--content start--><div>${app.allocatedVCores!}</div><!--content end-->
                                    </div>
                                </td><td class="text-left">
                                    <div>
                                        <!--content start--><div>${app.allocatedMB!}</div><!--content end-->
                                    </div>
                                </td><td class="text-left">
                                    <div>
                                        <!--content start--><div>${app.queue!}</div><!--content end-->
                                    </div>
                                </td><td class="text-left">
                                    <div>
                                        <!--content start--><div>${app.amHostHttpAddress!}</div><!--content end-->
                                    </div>
                                </td><td class="text-left">
                                    <div>
                                        <!--content start--><div>${app.startedTime!}</div><!--content end-->
                                    </div>
                                </td><td class="text-left">

                                    <div>
                                     <input type="button" value="kill" onclick="yarnKillApp('${app.id!}')" <#if auth!=1> disabled="disabled"</#if>>
                                    </div>

                                </td>

                                </tr>
                                </#list>
                            </#if>
                            <!--repeat end-->
                            <!--if start--><!--if end-->
                            </tbody>
                        </table>
                    </div>

                    <!-- 固定列 -->
                    <!--if start--><!--if end-->
                </div>

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