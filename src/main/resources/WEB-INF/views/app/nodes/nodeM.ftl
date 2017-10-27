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
                        <!--if start--><span class="text-overflow" title="host">host</span><!--if end--><!--content end-->
                    </div>
                    <!--if start--><!--if end-->
                </th><th  class="text-left">
                    <div>
                        <!--if start--><span class="text-overflow" title="IP/主机">IP/主机</span><!--if end--><!--content end-->
                    </div>
                </th><th class="text-left">
                    <div>
                        <!--if start--><span class="text-overflow" title="user">user</span><!--if end--><!--content end-->
                    </div>
                </th><th class="text-left">
                    <div>
                        <!--if start--><span class="text-overflow" title="管理">管理</span><!--if end--><!--content end-->
                    </div>
                </th><th class="text-left">
                    <div>
                        <!--content start--><span class="text-overflow" >yarn进程管理</span><!--content end-->
                    </div>
                </th><th class="text-left">
                    <div>
                        <!--content start--><span class="text-overflow" >hdfs进程管理</span><!--content end-->
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

                <#if nodes??>
                    <#list nodes as node>
                    <tr class="item-row
               " data-uinstanceid="ins-6qaa9ksm" nodeId="${node.id}"  data-role="cvm_row" data-devicelanip="10.104.133.159" data-uuid="a155d137-dbf6-4fcd-b8fe-afe8db36d3eb" data-index="0">
                        <!--if start--><td class="text-left">
                        <div>
                            <!--content start--><div><span class="status-col"><span class="text-overflow text-success">${node.host!}</span></span></div><!--content end-->
                        </div>
                    </td><td class="text-left">
                        <div>
                            <!--content start--><div>${node.ip!}</div><!--content end-->
                        </div>
                    </td><td class="text-left">
                        <div>
                            <!--content start--><div>${node.user!}</div><!--content end-->
                        </div>
                    </td><td class="text-left">
                        <div>
                            <div style="width: 78px; ">
                                <input type="button" value="检测" onclick="checkConnect('${node.id}')">
                                <input type="button" value="running process" onclick="runningProcess('${node.id}')">
                                <input type="button" value="配置同步" onclick="confSyn('${node.id}')">
                                <input type="button" value="删除"  onclick="delPNodes('${node.id}')">
                            </div>
                        </div>
                    </td><td class="text-left">
                        <div class="form-group">
                            <div class="btn-group">
                                <button type="button" class="btn btn-info">yarn</button>
                                <button type="button" class="btn btn-info dropdown-toggle" data-toggle="dropdown">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu" role="menu">
                                    <li onclick="exehcmd('rstart','${node.id}')"><a href="#">启动yarn主节点</a></li>
                                    <li onclick="exehcmd('nstart','${node.id}')"><a href="#">启动yarn从节点</a></li>
                                    <li onclick="exehcmd('rstop','${node.id}')"><a href="#">停止yarn主节点</a></li>
                                    <li onclick="exehcmd('nstop','${node.id}')"><a href="#">停止yarn从节点</a></li>
                                </ul>
                            </div>
                        </div>
                    </td><td class="text-left">
                        <div class="form-group">
                            <div class="btn-group">
                                <button type="button" class="btn btn-info">hdfs</button>
                                <button type="button" class="btn btn-info dropdown-toggle" data-toggle="dropdown">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu" role="menu">
                                    <li onclick="exehcmd('hinit','${node.id}')"><a href="#">格式化namenode</a></li>
                                    <li onclick="exehcmd('hnstart','${node.id}')"><a href="#">启动hdfs主节点</a></li>
                                    <li onclick="exehcmd('hdstart','${node.id}')"><a href="#">启动hdfs从节点</a></li>
                                    <li onclick="exehcmd('hnstop','${node.id}')"><a href="#">停止hdfs主节点</a></li>
                                    <li onclick="exehcmd('dnstop','${node.id}')"><a href="#">停止hdfs从节点</a></li>
                                </ul>
                            </div>
                        </div>
                    </td><!--repeat end-->
                    </tr>
                    </#list>
                </#if>
                </tbody>
            </table>
        </div>

        <!-- 固定列 -->
        <!--if start--><!--if end-->
    </div>

</div></div>

<script type="text/javascript">
    function checkConnect(id) {
        $.ajax({
            url: '/checkConnect',
            type: 'get',
            data : {
                id:id
            },
            success: function (data) {
                alertModel(data);
            },
            error: function(data){
                alertModel(data);
            }
        });
    };

    function runningProcess(id) {
        $.ajax({
            url: '/runningProcess',
            type: 'get',
            data : {
                id:id
            },
            success: function (data) {
                alertModel(data);
            },
            error: function(data){
                alertModel(data);
            }
        });
    }

    function confSyn(id) {
        $.ajax({
            url: '/confSyn',
            type: 'get',
            data : {
                key:id
            },
            success: function (data) {
                alertModel(data);
            },
            error: function(data){
                alertModel(data);
            }
        });
    };

    function delPNodes(id) {
        $.ajax({
            url: '/delPNodes',
            type: 'get',
            data : {
                key:id
            },
            success: function (data) {
                if(data.code==0){
                    $("tr[nodeId="+id+"]").remove();
                    alertModel("删除成功")
                }else {
                    alertModel("删除失败: " + data.msg)
                }
            },
            error: function(data){
                alertModel("ajax error:" + data);
            }
        });
    };

    function exehcmd(hcmd,key) {
        $.ajax({
            url: '/hcmd',
            type: 'get',
            data : {
                key:key,
                hcmd:hcmd

            },
            success: function (data) {
                alertModel(data,"success");
            },
            error: function(data){
                alertModel(data);
            }
        });
    };

</script>