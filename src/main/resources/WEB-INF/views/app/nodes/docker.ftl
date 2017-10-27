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
                        <!--if start--><span class="text-overflow" title="netUrl">netUrl</span><!--if end--><!--content end-->
                    </div>
                </th><th class="text-left">
                    <div>
                        <!--if start--><span class="text-overflow" title="iPBind">iPBind</span><!--if end--><!--content end-->
                    </div>
                </th><th class="text-left">
                    <div>
                        <!--content start--><span class="text-overflow" id="zoneIdSelector">dockerIp</span><!--content end-->
                    </div>
                </th><th class="text-left">
                    <div>
                        <!--content start--><span class="text-overflow" id="deviceClassSelector">businessType</span><!--content end-->
                    </div>
                </th><th class="text-left">
                    <div>
                        <!--if start--><span class="text-overflow" title="businessTag">businessTag</span><!--if end--><!--content end-->
                    </div>
                </th><th class="text-left">
                    <div>
                        <!--if start--><span class="text-overflow" title="VCPUs">VCPUs</span><!--if end--><!--content end-->
                    </div>
                </th><th class="text-left">
                    <div>
                     <span class="text-overflow" id="payModeSelector">memory(MB)</span><!--content end-->
                    </div>
                </th><th class="text-left">
                    <div>
                        <!--if start--><span class="text-overflow" title="VCPUs">state</span><!--if end--><!--content end-->
                    </div>
                </th><th class="text-left">
                    <div>
                        <!--if start--><span class="text-overflow" title="操作">操作</span><!--if end--><!--content end-->
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

        <#if yarn.dockerjobs??>
            <#list yarn.dockerjobs as job>
                <tr class="item-row
               " data-uinstanceid="ins-6qaa9ksm" data-role="cvm_row" data-devicelanip="10.104.133.159" data-uuid="a155d137-dbf6-4fcd-b8fe-afe8db36d3eb" data-index="0">
                    <!--if start--><td class="text-left">
                    <div>
                        <!--content start--><div><span class="status-col"><span class="text-overflow text-success">${job.runIp!}</span></span></div><!--content end-->
                    </div>
                </td><td class="text-left">
                    <div>
                        <!--content start--><div>${job.netUrl!}</div><!--content end-->
                    </div>
                </td><td class="text-left">
                    <div>
                        <!--content start--><div>${job.iPBind!}</div><!--content end-->
                    </div>
                </td><td class="text-left">
                    <div>
                        <!--content start--><div>${job.dockerIp!}</div><!--content end-->
                    </div>
                </td><td class="text-left">
                    <div>
                        <!--content start--><div>${job.businessType!}</div><!--content end-->
                    </div>
                </td><td class="text-left">
                    <div>
                        <!--content start--><div>${job.businessTag!}</div><!--content end-->
                    </div>
                </td><td class="text-left">
                    <div>
                        <!--content start--><div>${job.cpu!}</div><!--content end-->
                    </div>
                </td><td class="text-left">
                    <div>
                        <!--content start--><div>${job.memory!}</div><!--content end-->
                    </div>
                </td><td class="text-left">
                    <div>
                        <!--content start--><div>${job.state!}</div><!--content end-->
                    </div>
                </td><td class="text-left">
                    <div class="form-group">
                        <div class="btn-group">
                            <button type="button" class="btn btn-info">manage</button>
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

</div></div>

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