<div style="margin-bottom: 10px">

    <!-- Font for title -->
    <link href="https://fonts.googleapis.com/css?family=Finger+Paint" rel="stylesheet">

    <script>

    </script>

    <h4>创建本地hadoop用户</h4>

    <form method="post" action="registerRoot" enctype="multipart/form-data">
        admin:<input type="text" name="admin" id="admin" class="form-control" placeholder="服务器超管，通常为root，或具有root权限用户,用以创建本地hadoop用户,如果已创建，则可忽略,但需已实现本地免密，本地hadoop用户用以同步其他节点配置信息，免密信息"
                  value="" maxlength="245"  style="margin-bottom:10px" />

        passowrd:<input type="text" name="pwd" id="pwd" class="form-control" placeholder="admin密码" ng-trim="true"
                    value="" maxlength="245"  style="margin-bottom: 10px" />

        <input type="submit" type="button" class="btn btn-info" style="margin-bottom: 10px"/>
    </form>

    <script type="text/javascript">
        function check(form) {

            if(form.IP.value=='') {
                alert("请输入IP!");
                form.IP.focus();
                return false;
            }
            if(form.host.value==''){
                alert("请输入host!");
                form.host.focus();
                return false;
            }
            if(form.rAdmin.value=='') {
                alert("请输入管理员账户!");
                form.rAdmin.focus();
                return false;
            }
            if(form.password.value==''){
                alert("请输入管理员密码!");
                form.password.focus();
                return false;
            }

            alert("全程包括物理节点注册，hadoop，jdk安装包的传输以及安装，以及环境变量的设置，全场大约3至5分钟左右,安装完成后，可查看部署日志");

            return true;
        }
    </script>

    <h3>新注册物理服务器</h3>
    <form method="post" action="expand" enctype="multipart/form-data">
        IP:<input type="text" name="IP" id="IP" class="form-control" placeholder="新注册物理服务器的ip地址"
                              value="" maxlength="245"  style="margin-bottom:10px" />

        host:<input type="text" name="host" id="host" class="form-control" placeholder="物理节点host" ng-trim="true"
                            value="" maxlength="245"  style="margin-bottom: 10px" />

        admin:<input type="text" name="rAdmin" id="rAdmin" class="form-control" placeholder="用于创建hadoop用户的超级用户，通常为root用户，或具有较高权限"
                                            value="" maxlength="245"  style="margin-bottom: 10px" />

        password:<input type="text" name="password" id="password" class="form-control" placeholder="密码"
                                                      value="" maxlength="245"  style="margin-bottom: 10px" />

        <input type="submit" type="button" class="btn btn-info" style="margin-bottom: 10px"  onclick="return check(this.form)"  />
    </form>

    <div class="container-fluid">
        <div ng-show="!showLoading" ng-controller="nodeManager">
            <h3>已注册的物理服务器</h3>
            <table class="table table-hover table-condensed">
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
                <tr ng-repeat="node in nodes.nodes" class="active">
                    <td>{{node.ip}}</td>
                    <td>{{node.host}}</td>
                    <td>{{node.user}}</td>
                    <td>
                        <input type="button" value="检测" ng-click='checkConnect(node.id)' class="btn btn-default">
                        <input type="button" value="在运行hadoop进程" ng-click='runningProcess(node.id)' class="btn btn-default">
                        <input type="button" value="部署日志" ng-click='logio(node.id)' class="btn btn-default">
                        <input type="button" value="配置同步" ng-click='confSyn(node.id,node.ip)' class="btn btn-default">
                        <input type="button" value="删除" ng-click='delPNodes(node.id,node.ip)' class="btn btn-default">

                    </td>
                    <td>
                        <select ng-model="selectedYcmd">
                            <option ng-repeat="x in ycmds" value="{{x.cmd}}">{{x.show}}</option>
                        </select>
                        <input type="button" value="执行" ng-click='exehcmd(selectedYcmd,node.id)' class="btn btn-default">

                    </td>
                    <td>
                        <select ng-model="selectedHcmd">
                            <option ng-repeat="x in hcmds" value="{{x.cmd}}">{{x.show}}</option>
                        </select>
                        <input type="button" value="执行" ng-click='exehcmd(selectedHcmd,node.id)' class="btn btn-default">
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>

</div>