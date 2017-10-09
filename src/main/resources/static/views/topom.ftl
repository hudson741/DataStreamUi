<div align="center" ng-show="showLoading">
    <img src="images/loading.gif"> &nbsp;&nbsp; loading topology...
</div>


<div ng-show="!showLoading" ng-controller="topoManager">
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
            <td>{{topo.name}}</td>
            <td>{{topo.status}}</td>
            <td>{{topo.workersTotal}}</td>
            <td>{{topo.executorsTotal}}</td>
            <td>{{topo.tasksTotal}}</td>
            <td>
                <input type="button" value="worker+1" ng-click='topoWorkNumModify(topo.id,topo.workersTotal,1)' class="btn btn-default">
                <input type="button" value="worker-1" ng-click='topoWorkNumModify(topo.id,topo.workersTotal,-1)' class="btn btn-default">
            </td>
        </tr>
        </tbody>
    </table>

    <h3>输出组件</h3>
    <table class="table table-hover table-condensed">
        <thead>
        <tr>
            <th>
                <a href=""
                   ng-click="reverseSort.topo.spouts = !reverseSort.topo.spouts; orderByField.topo.spouts='spoutId';">
                    Id
                    <span ng-show="orderByField.topo.spouts == 'spoutId'">
                    <span ng-show="!reverseSort.topo.spouts">^</span>
                    <span ng-show="reverseSort.topo.spouts">v</span>
                </span>
                </a>
            </th>
            <th>
                <a href=""
                   ng-click="reverseSort.topo.spouts = !reverseSort.topo.spouts; orderByField.topo.spouts='executors';">
                    Executors
                    <span ng-show="orderByField.topo.spouts == 'executors'">
                    <span ng-show="!reverseSort.topo.spouts">^</span>
                    <span ng-show="reverseSort.topo.spouts">v</span>
                </span>
                </a>
            </th>
            <th>
                <a href=""
                   ng-click="reverseSort.topo.spouts = !reverseSort.topo.spouts; orderByField.topo.spouts='tasks';">
                    Tasks
                    <span ng-show="orderByField.topo.spouts == 'tasks'">
                    <span ng-show="!reverseSort.topo.spouts">^</span>
                    <span ng-show="reverseSort.topo.spouts">v</span>
                </span>
                </a>
            </th>
            <th>
               并行线程数
            </th>
        </tr>
        </thead>
        <tbody>
        <tr ng-repeat-start="spout in topo.spouts|orderBy:orderByField.topo.spouts:reverseSort.topo.spouts" class="active">
            <td>{{spout.spoutId}}</td>
            <td>{{spout.executors}}</td>
            <td>{{spout.tasks}}</td>
            <td>
                <input type="button" value="executors +1" ng-click='topoCExeModify(topo.id,spout.spoutId,spout.executors,1)' class="btn btn-default">
                <input type="button" value="executors -1" ng-click='topoCExeModify(topo.id,spout.spoutId,spout.executors,-1)' class="btn btn-default">
            </td>
        </tr>
        <tr ng-repeat-end collapse="!isSpoutsShow[$index]">
        </tbody>
    </table>


    <h3>计算组件</h3>
    <table class="table table-hover table-condensed">
        <thead>
        <tr>
            <th>
                <a href=""
                   ng-click="reverseSort.topo.bolts = !reverseSort.topo.bolts; orderByField.topo.bolts='boltId';">
                    Id
                    <span ng-show="orderByField.topo.bolts == 'boltId'">
                    <span ng-show="!reverseSort.topo.bolts">^</span>
                    <span ng-show="reverseSort.topo.bolts">v</span>
                </span>
                </a>
            </th>
            <th>
                <a href=""
                   ng-click="reverseSort.topo.bolts = !reverseSort.topo.bolts; orderByField.topo.bolts='executors';">
                    Executers
                    <span ng-show="orderByField.topo.bolts == 'executors'">
                    <span ng-show="!reverseSort.topo.bolts">^</span>
                    <span ng-show="reverseSort.topo.bolts">v</span>
                </span>
                </a>
            </th>
            <th>
                <a href=""
                   ng-click="reverseSort.topo.bolts = !reverseSort.topo.bolts; orderByField.topo.bolts='tasks';">
                    Tasks
                    <span ng-show="orderByField.topo.bolts == 'tasks'">
                    <span ng-show="!reverseSort.topo.bolts">^</span>
                    <span ng-show="reverseSort.topo.bolts">v</span>
                </span>
                </a>
            </th>
            <th>并行线程数</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-repeat-start="bolt in topo.bolts|orderBy:orderByField.topo.bolts:reverseSort.topo.bolts" class="active">
            <td>{{bolt.boltId}}</td>
            <td>{{bolt.executors}}</td>
            <td>{{bolt.tasks}}</td>
            <td>
                <input type="button" value="executors +1" ng-click='topoCExeModify(topo.id,bolt.boltId,bolt.executors,1)' class="btn btn-default">
                <input type="button" value="executors -1" ng-click='topoCExeModify(topo.id,bolt.boltId,bolt.executors,-1)' class="btn btn-default">
            </td>
        </tr>
        <tr ng-repeat-end collapse="!isBoltsShow[$index]" class="table-bordered sub-table">
        </tbody>
    </table>
</div>
