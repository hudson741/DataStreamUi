<div class="row">
    <div class="col-xs-12">
        <div class="box">
<div class="box-body">
    <table id="yarn_status" class="nowrap table table-condensed dataTable no-footer" width="100%" cellspacing="0" role="grid" style="width: 100%;">
        <tbody>
        <tr class="odd">
            <td class="sorting_disabled" rowspan="1" colspan="1" style="width: 564px;"><a href="http://${overview.stormui!}" target="_blank">storm内置UI</a></td>
        </tr>
        </tbody>
    </table>
    <h3>Cluster Summary</h3>
    <table class="table table-hover table-condensed">
        <thead>
        <tr>
            <th>Version</th>
            <th>Nimbus uptime</th>
            <th>Supervisors</th>
            <th>Used slots</th>
            <th>Free slots</th>
            <th>Total slots</th>
            <th>Executors</th>
            <th>Tasks</th>
        </tr>
        </thead>
        <tbody>
        <tr class="active">
            <td>${overview.clusterSummary.stormVersion!}</td>
            <td>${overview.clusterSummary.nimbusUptime!}</td>
            <td>${overview.clusterSummary.supervisors!}</td>
            <td>${overview.clusterSummary.slotsUsed!}</td>
            <td>${overview.clusterSummary.slotsFree!}</td>
            <td>${overview.clusterSummary.slotsTotal!}</td>
            <td>${overview.clusterSummary.executorsTotal!}</td>
            <td>${overview.clusterSummary.tasksTotal!}</td>
        </tr>
        </tbody>
    </table>

    <h3>Nimbus Summary</h3>
    <table class="table table-hover table-condensed">
        <thead>
        <tr>
            <th>Host</th>
            <th>Port</th>
            <th>Status</th>
            <th>Version</th>
            <th>Uptime</th>
        </tr>
        </thead>
        <tbody>
        <#if (overview.nimbusSummary.nimbuses)??>
        <#list overview.nimbusSummary.nimbuses as nimbus>
        <tr class="active">
            <td>${nimbus.host!}</td>
            <td>${nimbus.port!}</td>
            <td>${nimbus.status!}</td>
            <td>${nimbus.version!}</td>
            <td>${nimbus.nimbusUpTime!}</td>
        </tr>
        </#list>
        </#if>
        </tbody>
    </table>


    <h3>Topology Summary</h3>
    <table class="table table-hover table-condensed">
        <thead>
        <tr>
            <th>Name</th>
            <th>Id</th>
            <th>Status</th>
            <th>Uptime</th>
            <th>Num workers</th>
            <th>Num executors</th>
            <th>Num tasks</th>
        </tr>
        </thead>
        <tbody>
        <#if (overview.topos)??>
        <#list overview.topos as topo>
        <tr class="active">
            <td><a target="navTab"  href="/topoftl?topoid=${topo.id!}">${topo.name!}</></td>
            <td>${topo.id!}</td>
            <td>${topo.status!}</td>
            <td>${topo.uptime!}</td>
            <td>${topo.workersTotal!}</td>
            <td>${topo.executorsTotal!}</td>
            <td>${topo.tasksTotal!}</td>
        </tr>
        </#list>
        </#if>
        </tbody>
    </table>


    <h3>Supervisor Summary</h3>
    <table class="table table-hover table-condensed">
        <thead>
        <tr>
            <th>
                <a href="" ng-click="reverseSort.overview.ss = !reverseSort.overview.ss; orderByField.overview.ss='id';">
                    Id
                </a>

            </th>
            <th>
                <a href="" ng-click="reverseSort.overview.ss = !reverseSort.overview.ss; orderByField.overview.ss='host';">
                    Host
                </span>
                </a>
            </th>
            <th>
                <a href="" ng-click="reverseSort.overview.ss = !reverseSort.overview.ss; orderByField.overview.ss='ip';">
                    IP
                </span>
                </a>
            </th>
            <th>
                <a href=""
                   ng-click="reverseSort.overview.ss = !reverseSort.overview.ss; orderByField.overview.ss='uptime';">
                    Uptime
                </span>
                </a>
            </th>
            <th>
                <a href=""
                   ng-click="reverseSort.overview.ss = !reverseSort.overview.ss; orderByField.overview.ss='slotsTotal';">
                    Slots
                </span>
                </a>
            </th>
            <th>
                <a href=""
                   ng-click="reverseSort.overview.ss = !reverseSort.overview.ss; orderByField.overview.ss='slotsUsed';">
                    Used slots
                </span>
                </a>
            </th>
        </tr>
        </thead>
        <tbody>
        <#if (overview.supervisorSummary.supervisors)??>
        <#list overview.supervisorSummary.supervisors as sup>
        <tr class="active">
            <td>${sup.id}</td>
            <td>
                <a target="navTab"  href="/hostsftl?">${sup.host!}</a>

            </td>
            <td>${sup.ip!}</td>
            <td>${sup.uptime!}</td>
            <td>${sup.slotsTotal!}</td>
            <td>${sup.slotsUsed!}</td>
        </tr>
        </#list>
        </#if>
        </tbody>
    </table>


    <h3>Cluster Config</h3>

    <table class="table table-hover table-condensed">
        <thead>
        <tr>
            <th>
                Key
            </th>
            <th>Value</th>
        </tr>
        </thead>
        <tbody>

        <tbody>
        <#if (overview.cluserConfig)??>
        <#assign map = overview.cluserConfig>
        <#list map? keys as key>
        <tr class="active">
            <td>${key!}</td>
            <#if map[key]! ?is_sequence>
              <#if (map[key]!?size>0)>
                  <td>[
                  <#list map[key] as v>
                      ${v},
                  </#list>
                  ]</td>
              <#else>
                <td>[]</td>
              </#if>
            <#else>
                <td>${map[key]!?string }</td>
            </#if>
        </tr>
        </#list>
       </#if>
        </tbody>
    </table>
        </tbody>
    </table>

</div>
        </div>
    </div>
</div>
