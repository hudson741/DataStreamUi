<div class="row">
    <div class="col-xs-12">
        <div class="box">
            <div class="box-header">
                <h3 class="box-title">集群管理</h3>
            </div>
    <div ng-show="!showLoading" ng-controller="nodeManager" class="box-body">
        <h3>已注册的物理服务器</h3>
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>ip</th>
                <th>host</th>
                <th>user</th>
                <th>管理</th>
                <th>yarn进程管理</th>
                <th>hdfs进程管理</th>
            </tr>
            </thead>
            <tbody>
                <#list nodes as node>
                <tr class="active">
                <td>${node.ip}</td>
                <td>${node.host}</td>
                <td>${node.user}</td>
                <td>
                    <input type="button" value="检测" onclick="checkConnect('${node.id}')" class="btn btn-default">
                    <input type="button" value="在运行hadoop进程" onclick="runningProcess('${node.id}')" class="btn btn-default">
                    <input type="button" value="配置同步" onclick="confSyn('${node.id}')" class="btn btn-default">
                    <input type="button" value="删除" onclick="delPNodes('${node.id}')" class="btn btn-default">

                </td>
                <td>
                    <div class="form-group">
                        <div class="btn-group">
                            <button type="button" class="btn btn-info">yarn节点管理</button>
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
                </td>
                <td>
                    <div class="form-group">
                        <div class="btn-group">
                            <button type="button" class="btn btn-info">hdfs节点管理</button>
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
                </td>
                </#list>
            </tr>
            </tbody>
        </table>
    </div>
</div>
    </div>
</div>

<script>
function checkConnect(id) {
    $.ajax({
        url: '/checkConnect',
        type: 'get',
        data : {
            id:id
        },
        success: function (data) {
            alertMsg(data,"success");
        },
        error: function(data){
            alertMsg(data);
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
            alertMsg(data,"success");
        },
        error: function(data){
            alertMsg(data);
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
            alertMsg(data,"success");
        },
        error: function(data){
            alertMsg(data);
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
            alertMsg(data,"success");
        },
        error: function(data){
            alertMsg(data);
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
            alertMsg(data,"success");
        },
        error: function(data){
            alertMsg(data);
        }
    });
};
</script>