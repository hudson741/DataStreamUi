<!--<a href="" ng-click="gotoAnchor('SH02SVR4386')"> Go to anchor SH02SVR4386</a>-->
<div align="center" ng-show="showLoading">
  <img src="images/loading.gif"> &nbsp;&nbsp; loading hosts...
</div>


<div ng-show="!showLoading">
  <div ng-repeat="host in hosts" class="host-block">
    <div id="{{ host.host }}" class="host-title">{{ host.host }} ({{host.ip}})</div>

    <span style="font-size: 15px;padding:0 0 8px 0">
        <i>Uptime <b>{{host.uptime}}</b> ,
          total slots {{host.slotsTotal}} and slots used {{host.slotsUsed}}.
          supervisor id {{host.supId}}</i>
    </span>

    <ul ng-repeat="slot in host.slots" class="slot-block">
      <li>
        <span><i><b>Slot Port {{slot.port}}, uptime {{slot.uptime}}</b></i></span>

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
          <tr ng-repeat="exestat in slot.stats" class="active">
            <td>{{exestat.executorId}}</td>
            <td>{{exestat.uptime}}</td>
            <td>{{exestat.topoName}}</td>
            <td>{{exestat.compId}}</td>
            <td>{{exestat.compType}}</td>
            <td>{{exestat.emitted}}</td>
            <td>{{exestat.transferred}}</td>
            <!--<td>{{exestat.capacity}}</td>-->
            <!--<td>{{exestat.executeLatency}}</td>-->
            <!--<td>{{exestat.executed}}</td>-->
            <!--<td>{{exestat.processLatency}}</td>-->
            <td>{{exestat.acked}}</td>
            <td>{{exestat.failed}}</td>
          </tr>
          </tbody>
        </table>
      </li>
    </ul>
  </div>
</div>
