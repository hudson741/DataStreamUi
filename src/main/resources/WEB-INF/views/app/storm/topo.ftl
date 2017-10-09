<div class="row">
    <div class="col-xs-12">
        <div class="box">
            <div class="box-body">
    <h3>计算拓扑</h3>
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
        <tr class="active">
            <td>${topo.name!}</td>
            <td>${topo.id!}</td>
            <td>${topo.status!}</td>
            <td>${topo.uptime!}</td>
            <td>${topo.workersTotal!}</td>
            <td>${topo.executorsTotal!}</td>
            <td>${topo.tasksTotal!}</td>
        </tr>
        </tbody>
    </table>

    <h3>拓扑传输</h3>
    <table class="table table-hover table-condensed">
        <thead>
        <tr>
            <th>Window</th>
            <th>Emitted</th>
            <th>Transferred</th>
            <th>Complete latency (ms)</th>
            <th>Acked</th>
            <th>Failed</th>
        </tr>
        </thead>
        <tbody>
        <#if (topo.topologyStats)??>
        <#list topo.topologyStats as stat>
        <tr ng-repeat="stat in topo.topologyStats" class="active">
            <td>${stat.windowPretty!}</td>
            <td>${stat.emitted!}</td>
            <td>${stat.transferred!}</td>
            <td>${stat.completeLatency!}</td>
            <td>${stat.acked!}</td>
            <td>${stat.failed!}</td>
        </tr>
        </#list>
        </#if>
        </tbody>
    </table>


    <h3>输出组件 (10 min)</h3>
    <table class="table table-hover table-condensed">
        <thead>
        <tr>
            <th>
                <a href=""
                   ng-click="reverseSort.topo.spouts = !reverseSort.topo.spouts; orderByField.topo.spouts='spoutId';">
                    Id
                </span>
                </a>
            </th>
            <th>
                <a href=""
                   ng-click="reverseSort.topo.spouts = !reverseSort.topo.spouts; orderByField.topo.spouts='executors';">
                    Executors
                </span>
                </a>
            </th>
            <th>
                <a href=""
                   ng-click="reverseSort.topo.spouts = !reverseSort.topo.spouts; orderByField.topo.spouts='tasks';">
                    Tasks
                </span>
                </a>
            </th>
            <th>
                <a href=""
                   ng-click="reverseSort.topo.spouts = !reverseSort.topo.spouts; orderByField.topo.spouts='emitted';">
                    Emitted
                </span>
                </a>
            </th>
            <th>
                <a href=""
                   ng-click="reverseSort.topo.spouts = !reverseSort.topo.spouts; orderByField.topo.spouts='transferred';">
                    Transferred
                </span>
                </a>
            </th>
            <th>
                <a href=""
                   ng-click="reverseSort.topo.spouts = !reverseSort.topo.spouts; orderByField.topo.spouts='completeLatency';">
                    Complete latency (ms)
                </span>
                </a>
            </th>
            <th>
                <a href=""
                   ng-click="reverseSort.topo.spouts = !reverseSort.topo.spouts; orderByField.topo.spouts='acked';">
                    Acked
                </span>
                </a>
            </th>
            <th>
                <a href=""
                   ng-click="reverseSort.topo.spouts = !reverseSort.topo.spouts; orderByField.topo.spouts='failed';">
                    Failed
                </span>
                </a>
            </th>
            <th>
                <a href=""
                   ng-click="reverseSort.topo.spouts = !reverseSort.topo.spouts; orderByField.topo.spouts='lastError';">
                    Last error
                </span>
                </a>
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
            <td class="success">${spout.emitted}</td>
            <td class="info">${spout.transferred}</td>
            <td>${spout.completeLatency}</td>
            <td>${spout.acked}</td>
            <td>${spout.failed}</td>
            <td>${spout.lastError}</td>
        </tr>
        </#list>
       </#if>
        </tbody>
    </table>


    <h3>计算组件 (10 min)</h3>
    <table class="table table-hover table-condensed">
        <thead>
        <tr>
            <th>
                <a href=""
                   ng-click="reverseSort.topo.bolts = !reverseSort.topo.bolts; orderByField.topo.bolts='boltId';">
                    Id
                </span>
                </a>
            </th>
            <th>
                <a href=""
                   ng-click="reverseSort.topo.bolts = !reverseSort.topo.bolts; orderByField.topo.bolts='executors';">
                    Executers
                </span>
                </a>
            </th>
            <th>
                <a href=""
                   ng-click="reverseSort.topo.bolts = !reverseSort.topo.bolts; orderByField.topo.bolts='tasks';">
                    Tasks
                </span>
                </a>
            </th>
            <th>
                <a href=""
                   ng-click="reverseSort.topo.bolts = !reverseSort.topo.bolts; orderByField.topo.bolts='emitted';">
                    Emitted
                </span>
                </a>
            </th>
            <th>
                <a href=""
                   ng-click="reverseSort.topo.bolts = !reverseSort.topo.bolts; orderByField.topo.bolts='transferred';">
                    Transferred
                </span>
                </a>
            </th>
            <th>
                <a href=""
                   ng-click="reverseSort.topo.bolts = !reverseSort.topo.bolts; orderByField.topo.bolts='capacity';">
                    Capacity (last 10m)
                </span>
                </a>
            </th>
            <th>
                <a href=""
                   ng-click="reverseSort.topo.bolts = !reverseSort.topo.bolts; orderByField.topo.bolts='executeLatency';">
                    Execute latency (ms)
                </span>
                </a>
            </th>
            <th>
                <a href=""
                   ng-click="reverseSort.topo.bolts = !reverseSort.topo.bolts; orderByField.topo.bolts='executed';">
                    Executed
                </span>
                </a>
            </th>
            <th>
                <a href=""
                   ng-click="reverseSort.topo.bolts = !reverseSort.topo.bolts; orderByField.topo.bolts='processLatency';">
                    Process latency (ms)
                </span>
                </a>
            </th>
            <th>
                <a href=""
                   ng-click="reverseSort.topo.bolts = !reverseSort.topo.bolts; orderByField.topo.bolts='acked';">
                    Acked
                </span>
                </a>
            </th>
            <th>
                <a href=""
                   ng-click="reverseSort.topo.bolts = !reverseSort.topo.bolts; orderByField.topo.bolts='failed';">
                    Failed
                </span>
                </a>
            </th>
            <th>
                <a href=""
                   ng-click="reverseSort.topo.bolts = !reverseSort.topo.bolts; orderByField.topo.bolts='lastError';">
                    Last error
                </span>
                </a>
            </th>
        </tr>
        </thead>
        <tbody>
        <#if (topo.bolts)??>
        <#list topo.bolts as bolt>
        <tr class="active">
            <td>${bolt.boltId}</td>
            <td>${bolt.executors}</td>
            <td>${bolt.tasks}</td>
            <td class="success">${bolt.emitted}</td>
            <td class="info">${bolt.transferred}</td>
            <td>${bolt.capacity}</td>
            <td>${bolt.executeLatency}</td>
            <td>${bolt.executed}</td>
            <td>${bolt.processLatency}</td>
            <td class="success">${bolt.acked}</td>
            <td>${bolt.failed}</td>
            <td>${bolt.lastError}</td>
        </tr>
        </#list>
        </#if>
        </tr>
        </tbody>
    </table>

    <h3>Different configs from default</h3>

    <table class="table table-hover table-condensed">
        <thead>
        <tr>
            <!--<th>类型</th>-->
            <th>Key</th>
            <th>Value</th>
        </tr>
        </thead>
        <tbody>
        <#if (topo.diffConfig)??>
            <#assign map = topo.diffConfig>
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

    <h3>Same configs with default</h3>

    <table class="table table-hover table-condensed">
        <thead>
        <tr>
            <th>Key</th>
            <th>Value</th>
        </tr>
        </thead>
        <tbody>
        <#if (topo.sameConfig)??>
            <#assign map = topo.sameConfig>
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
</div>
        </div>
    </div>
</div>
