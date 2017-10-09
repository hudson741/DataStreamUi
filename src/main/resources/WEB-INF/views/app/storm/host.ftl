<div class="row">
    <div class="col-xs-12">
        <div class="box">
            <div class="box-body">
                <div  class="host-block">

<#if host??>
    <#assign map = host>
    <#list map? keys as key>
        <div id="${key!}">
              <span style="font-size: 30px;padding:0 0 8px 0">
            <b>
               ${ map[key].host } (${map[key].ip})
            </b>
              </span>
        </div>

        <span style="font-size: 15px;padding:0 0 8px 0">
        <i>Uptime <b>${map[key].uptime}</b> ,
          total slots ${map[key].slotsTotal} and slots used ${map[key].slotsUsed}.
          supervisor id ${map[key].supId}</i>
          </span>

<#if map[key]??>
    <#list map[key].slots as slot>
        <ul  class="slot-block">
            <li>
                <span><i><b>Slot Port ${slot.port}, uptime ${slot.uptime}</b></i></span>

                <!---->
                <table class="table table-hover table-condensed" style="margin-bottom: 0">
                    <thead>
                    <tr>
                        <th>Tasks</th>
                        <th>Uptime</th>
                        <th>Topology</th>
                        <th>Component</th>
                        <th>Type</th>
                        <th>Emitted</th>
                        <th>Transferred</th>
                        <!--<th>Capacity</th>-->
                        <!--<th>Execute Latency</th>-->
                        <!--<th>Executed</th>-->
                        <!--<th>Process Latency</th>-->
                        <th>Acked</th>
                        <th>Failed</th>
                    </tr>
                    </thead>
                     <tbody>
                     <#if slot.stats??>
                     <#list slot.stats as exestat>
                    <tr  class="active">
                        <td>${exestat.executorId!}</td>
                        <td>${exestat.uptime!}</td>
                        <td>${exestat.topoName!}</td>
                        <td>${exestat.compId!}</td>
                        <td>${exestat.compType!}</td>
                        <td>${exestat.emitted!}</td>
                        <td>${exestat.transferred!}</td>
                        <!--<td>{{exestat.capacity}}</td>-->
                        <!--<td>{{exestat.executeLatency}}</td>-->
                        <!--<td>{{exestat.executed}}</td>-->
                        <!--<td>{{exestat.processLatency}}</td>-->
                        <td>${exestat.acked!}</td>
                        <td>${exestat.failed!}</td>
                    </tr>
                     </#list>
                     </#if>
                    </tbody>
                </table>
            </li>
        </ul>
    </#list>
</#if>
    </div>
    </#list>
</#if>
</div>
        </div>
    </div>
</div>
