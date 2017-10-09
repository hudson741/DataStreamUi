<div class="row">
    <div class="col-xs-12">
        <div class="box">
            <div class="box-body">

    <h3>Topology Summary</h3>
    <table class="table table-hover table-condensed">
        <thead>
        <tr>
            <th>Name</th>
            <th>Id</th>
            <th>owner</th>
            <th>Status</th>
            <th>Uptime</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
<#if (topolist.topologies)??>
    <#list topolist.topologies as topo>
        <tr  class="active">
            <td><a target="navTab"  href="/topomftl?topoid=${topo.id}">${topo.name}</a></td>
            <td>${topo.id}</td>
            <td>${topo.owner}</td>
            <td>${topo.status}</td>
            <td>${topo.uptime}</td>
            <td>
                <input id = "a1" <#if (topo.showa)!?boolean> disabled="disabled" </#if> onclick="activeTopo('${topo.id}')"  type="button" value="Activate" class="btn btn-default">
                <input id = "a2"  <#if (topo.showd)!?boolean> disabled="disabled" </#if> onclick="deactiveTopo('${topo.id}')" type="button" value="Deactive" class="btn btn-default">
                <input id = "a3" type="button" value="Kill" onclick="killTopo('${topo.id}')" class="btn btn-default">

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
    function activeTopo(topId){
        $.ajax({
            url: '/activeTopoftl',
            type: 'post',
            data : {
                topoid:topId
            },
            success: function (data) {
                alertMsg("修改成功，可刷新页签查看","success");
                $("#a1").attr("disabled", true);
                $("#a2").removeAttr("disabled");
            },
            error: function(data){
                alertMsg(data);
            }
        });
    };

    function deactiveTopo(topId){
        $.ajax({
            url: '/deactiveTopoftl',
            type: 'post',
            data : {
                topoid:topId
            },
            success: function (data) {
                alertMsg("修改成功，可刷新页签查看","success");
                $("#a2").attr("disabled", true);
                $("#a1").removeAttr("disabled");
            },
            error: function(data){
                alertMsg(data);
            }
        });
    };

    function killTopo(topId){
        $.ajax({
            url: '/killTopologyftl',
            type: 'post',
            data : {
                topoid:topId
            },
            success: function (data) {
                alertMsg("修改成功","success");
                $("#a2").attr("disabled", true);
                $("#a1").attr("disabled",true);
            },
            error: function(data){
                alertMsg(data);
            }
        });
    };


</script>

