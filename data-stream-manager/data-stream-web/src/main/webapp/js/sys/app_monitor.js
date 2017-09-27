$(function () {

    $("#jqGrid").jqGrid({
        url: '/sys/yarn/app_list',
        datatype: "json",
        colModel: [
            {label: 'ID', name: 'yarnAppId', width: 120, key: true},
            {label: 'StartTime ', name: 'startTime', width: 80},
            {label: 'consumeTime ', name: 'consumeTime', width: 80},
            {label: 'Status', name: 'status', width: 60, formatter:function (value, options, row) {
                if(value == 'SUCCEEDED'){
                    return '<span class="label label-success">SUCCEEDED</span>'
                }else {
                    return '<span class="label label-danger">'+value+'</span>'
                }
            }},
            {
                label: 'TrackingUI', name: 'trackingUI', width: 50, formatter: function (value, options, row) {
                if (row.status == 'RUNNING' && row.trackingUI != null) {
                    return '<a class="btn btn-primary" target="_blank" href=' + value + '>SparkUI</a>'
                }
                return ''
            }
            },
            {
                label: 'Operation', name: 'status', width: 80, formatter: function (value, options, row) {
                var buttons = ""
                buttons += '<a class="btn btn-primary" target="_blank" href=' + row.logUI + '>Log日志</a>'
                if (row.status == 'RUNNING' && row.trackingUI != null) {
                    buttons += "&nbsp&nbsp&nbsp"
                    buttons += '<a class="btn btn-danger" onclick=killApp("' + row.yarnAppId + '")>Kill</a>'
                }
                return buttons
            }
            }
        ],
        viewrecords: true,
        height: 345,
        rowNum: 10,
        rowList: [10, 30, 50],
        rownumWidth: 25,
        autowidth: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });

    killApp = function (s) {

        confirm("确定要kill应用" + s + "吗？", function () {
            $.ajax({
                type: "POST",
                url: "../yarn/kill/" + s,
                success: function (r) {
                    if (r.code == 0) {
                        alert('操作成功', function (index) {
                            vm.reload();
                        });
                    } else {
                        alert(r.msg);
                    }
                }
            });
        })
    }


});


var vm = new Vue({
    el: "#rrapp",
    data: {
        showList: true,
        clusterMetrics: {},
        filterStatus: "ALL"
    },
    methods: {
        reload: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {'status': vm.filterStatus},
                page: page
            }).trigger("reloadGrid");

            $.ajax({
                type: "GET",
                url: "/sys/yarn/metrics",
                contentType: "application/json",
                success: function (r) {
                    if (r.code == 0) {
                        vm.clusterMetrics = r.clusterMetrics;
                    } else {
                        alert(r.msg);
                    }
                }
            });

        },

        getAppsInfo: function () {
            $.ajax({
                type: "GET",
                url: "/sys/yarn/metrics",
                contentType: "application/json",
                success: function (r) {
                    if (r.code == 0) {
                        vm.clusterMetrics = r.clusterMetrics;
                    } else {
                        alert(r.msg);
                    }
                }
            });
        }


    },

    created: function () {
        this.getAppsInfo();
    },

});
