<div class="row">
    <div class="col-xs-12">
        <div class="box">
            <div class="box-body">
    <h3>计算拓扑</h3>
    <table class="table table-hover table-condensed">
        <thead>
        <tr>
            <th>Name</th>
            <th>Status</th>
            <th>Num workers</th>
            <th>Num executors</th>
            <th>Num tasks</th>
            <th>工作节点数</th>
        </tr>
        </thead>
        <tbody>
        <tr class="active">
            <td>${topo.name}</td>
            <td>${topo.status}</td>
            <td>${topo.workersTotal}</td>
            <td>${topo.executorsTotal}</td>
            <td>${topo.tasksTotal}</td>
            <td>
                <div class="btn-group">
                    <button type="button" class="btn btn-info">工作节点数调整</button>
                    <button type="button" class="btn btn-info dropdown-toggle" data-toggle="dropdown">
                        <span class="caret"></span>
                        <span class="sr-only">Toggle Dropdown</span>
                    </button>
                    <ul class="dropdown-menu" role="menu">
                        <li onclick="topoWorkNumModifyftl('${topo.id}',1)"><a href="#">worker+1</a></li>
                        <li onclick="topoWorkNumModifyftl('${topo.id}',-1)"><a href="#">worker-1</a></li>
                    </ul>
                </div>
            </td>
                <#--<input type="button" value="worker-1" ng-click='topoWorkNumModify(topo.id,topo.workersTotal,-1)' class="btn btn-default">-->
        </tr>
        </tbody>
    </table>

    <h3>输出组件</h3>
    <table class="table table-hover table-condensed">
        <thead>
        <tr>
            <th>
                <a href="">
                    Id
                </a>
            </th>
            <th>
                <a href="">
                    Executors
                </a>
            </th>
            <th>
                <a href="">
                    Tasks
                </a>
            </th>
            <th>
                并行线程数
            </th>
        </tr>
        </thead>
        <tbody>

        <#if (topo.spouts)??>
        <#list topo.spouts as spout>
        <tr class="active">
            <td>${spout.spoutId}</td>
            <td>${spout.executors}</td>
            <td>${spout.tasks}</td>
            <td>
            <td>
                <div class="btn-group">
                    <button type="button" class="btn btn-info">并行线程数调整</button>
                    <button type="button" class="btn btn-info dropdown-toggle" data-toggle="dropdown">
                        <span class="caret"></span>
                        <span class="sr-only">Toggle Dropdown</span>
                    </button>
                    <ul class="dropdown-menu" role="menu">
                        <li onclick="topoCExeModifyftl('${topo.id}','${spout.spoutId}',1)"><a href="#">executors+1</a></li>
                        <li onclick="topoCExeModifyftl('${topo.id}','${spout.spoutId}',-1)"><a href="#">executors-1</a></li>
                    </ul>
                </div>
            </td>
            </td>
        </tr>
        </#list>
        </#if>

        <tr ng-repeat-end collapse="!isSpoutsShow[$index]">
        </tbody>
    </table>


    <h3>计算组件</h3>
    <table class="table table-hover table-condensed">
        <thead>
        <tr>
            <th>
                <a href="">
                    Id
                </a>
            </th>
            <th>
                <a href="">
                    Executers
                </a>
            </th>
            <th>
                <a href="">
                    Tasks
                </a>
            </th>
            <th>并行线程数</th>
        </tr>
        </thead>
        <tbody>
        <#if (topo.bolts)??>
        <#list topo.bolts as bolt>
        <tr class="active">
            <td>${bolt.boltId}</td>
            <td>${bolt.executors}</td>
            <td>${bolt.tasks}</td>
            <td>
                <div class="btn-group">
                    <button type="button" class="btn btn-info">并行线程数调整</button>
                    <button type="button" class="btn btn-info dropdown-toggle" data-toggle="dropdown">
                        <span class="caret"></span>
                        <span class="sr-only">Toggle Dropdown</span>
                    </button>
                    <ul class="dropdown-menu" role="menu">
                        <li onclick="topoCExeModifyftl('${topo.id}','${bolt.boltId}',1)"><a href="#">executors+1</a></li>
                        <li onclick="topoCExeModifyftl('${topo.id}','${bolt.boltId}',-1)"><a href="#">executors-1</a></li>
                    </ul>
                </div>
            </td>
        </tr>
        </#list>
        </#if>

        <tr ng-repeat-end collapse="!isBoltsShow[$index]" class="table-bordered sub-table">
        </tbody>
    </table>
</div>
        </div>
    </div>
</div>
<script type="text/javascript">
    function topoWorkNumModifyftl(topId,num){
        $.ajax({
            url: '/topoWorkNumModifyftl',
            type: 'post',
            data : {
                topoid:topId,
                num:num

            },
            success: function (data) {
                alertMsg("修改成功，可刷新页签查看","success");
            },
            error: function(data){
                alertMsg(data);
            }
        });
    };

    function topoCExeModifyftl(topId,cid,num){
        $.ajax({
            url: '/topoCExeModifyftl',
            type: 'post',
            data : {
                topoid:topId,
                cid:cid,
                num:num
            },
            success: function (data) {
                alertMsg("修改成功，可刷新页签查看","success");
            },
            error: function(data){
                alertMsg(data);
            }
        });
    };
</script>