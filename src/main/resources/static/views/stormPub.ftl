<div style="margin-bottom: 10px" ng-controller="dockerm">


    <form method="post" action="stormdockerPub" enctype="multipart/form-data">
        <!--<input type="file" name="file" class="form-control" placeholder="127.0.0.1:9090" ng-trim="true"-->
        <!--maxlength="255" ng-model="dockerPubURL" style="margin-bottom: 10px" />-->

        dockerIp:<input type="text" name="dockerIp" id="dockerIp" class="form-control" placeholder="docker内网IP"
                             maxlength="245"  style="margin-bottom: 10px" />

        process:<input type="text" name="process" id="process" class="form-control" placeholder="启动的storm组件名称"
                        maxlength="245"  style="margin-bottom: 10px" />

        配置类型:
        <select name="stormDockerConf" ng-init="stormDockerConf = confd[1]" ng-model="stormDockerConf" class="form-control" maxlength="245"  style="margin-bottom: 10px">
            <option ng-repeat="x in confd" value="{{x.cid}}">{{x.show}}</option>
        </select>

        node(非必填，指定启动节点):<input type="text" name="node" id="node" class="form-control" placeholder="指定启动节点地址"
                       maxlength="245"  style="margin-bottom: 10px" />

        host:<input type="text" name="host" id="host" class="form-control" placeholder="组件hosts配置，例:hostA:192.168.233.11,hostB:192.168.233.12"
                        maxlength="245"  style="margin-bottom: 10px" />

        <input type="submit" type="button" class="btn btn-info"  style="margin-bottom: 10px"/>
    </form>

    <div ng-show="showMessage">
        <alert type="info" style="margin-bottom: 10px">stormPubURL url updated to <b>{{stormPubURL}}</b></alert>
    </div>

    <div ng-show="showFailMessage">
        <alert type="danger" style="margin-bottom: 10px">stormPubURL url <b>FAILED</b> to updated to <b>{{tobeset}}</b>. Please
            check and make sure the new rest url is accessible on the host where this app is deployed.
        </alert>
    </div>

</div>
