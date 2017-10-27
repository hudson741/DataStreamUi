<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>DATASTREAM</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <link rel="shortcut icon" type="image/x-icon" href="/dist/img/favicon.ico">
  <!-- Bootstrap 3.3.6 -->
  <link rel="stylesheet" href="plugins/bootstrap/css/bootstrap.min.css">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="dist/css/font-awesome.min.css">
  <!-- Ionicons -->
  <link rel="stylesheet" href="dist/css/ionicons.min.css">
  <!-- Theme style -->
  <link rel="stylesheet" href="dist/css/AdminLTE.min.css">

  <link rel="stylesheet" href="dist/css/bee.css">

  <link rel="stylesheet" href="dist/css/skins/_all-skins.min.css">

  <!-- daterange picker -->
  <link rel="stylesheet" href="plugins/daterangepicker/daterangepicker.css">
  <!-- bootstrap datepicker -->
  <link rel="stylesheet" href="plugins/datepicker/datepicker3.css">
  <!-- iCheck for checkboxes and radio inputs -->
  <link rel="stylesheet" href="plugins/iCheck/all.css">
  <!-- Bootstrap Color Picker -->
  <link rel="stylesheet" href="plugins/colorpicker/bootstrap-colorpicker.min.css">
  <!-- Bootstrap time Picker -->
  <link rel="stylesheet" href="plugins/timepicker/bootstrap-timepicker.min.css">
  <!-- Select2 -->
  <link rel="stylesheet" href="plugins/select2/select2.min.css">
  <!-- bootstrap wysihtml5 - text editor -->
  <link rel="stylesheet" href="plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css">
  <!-- iCheck -->
  <link rel="stylesheet" href="plugins/iCheck/flat/blue.css">

  <!-- fullCalendar 2.2.5-->
  <link rel="stylesheet" href="plugins/fullcalendar/fullcalendar.min.css">
  <link rel="stylesheet" href="plugins/fullcalendar/fullcalendar.print.css" media="print">

  <!-- Ion Slider -->
  <link rel="stylesheet" href="plugins/ionslider/ion.rangeSlider.css">
  <!-- ion slider Nice -->
  <link rel="stylesheet" href="plugins/ionslider/ion.rangeSlider.skinNice.css">
  <!-- bootstrap slider -->
  <link rel="stylesheet" href="plugins/bootstrap-slider/slider.css">

  <!-- Morris chart -->
  <link rel="stylesheet" href="plugins/morris/morris.css">
  <!-- jvectormap -->
  <link rel="stylesheet" href="plugins/jvectormap/jquery-jvectormap-1.2.2.css">

  <link rel="stylesheet" href="http://www.jq22.com/demo/jQuerySweetAlert220160627/dist/sweetalert2.min.css">



    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
  <!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
<script>
function iFrameHeight() { 
var ifm= document.getElementById("content"); 
var subWeb = document.frames ? document.frames["content"].document : ifm.contentDocument; 
if(ifm != null && subWeb != null) { 
ifm.height = subWeb.body.scrollHeight; 
} 
} 
</script>

</head>
<body class="sidebar-mini ajax-template skin-blue fixed" height:100% margin:0px;>
<div class="wrapper">

  <header class="main-header">
    <!-- Logo -->
    <a href="index2.ftl" class="logo">
      <!-- mini logo for sidebar mini 50x50 pixels -->
      <span class="logo-mini"><b>R</b>C</span>
      <!-- logo for regular state and mobile devices -->
      <span class="logo-lg"><b>DATA_STREAM</b></span>
    </a>
    <!-- Header Navbar: style can be found in header.less -->
    <nav class="navbar navbar-static-top">
      <!-- Sidebar toggle button-->
      <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
        <span class="sr-only">切换导航</span>
      </a>

      <div class="navbar-custom-menu">
        <ul class="nav navbar-nav">
          <li>
            <a href="#" data-toggle="control-sidebar"><i class="fa fa-gears"></i></a>
          </li>
            <li>
                <a href="/logout"><i class="fa fa-fw fa-reply"></i></a>
            </li>
        </ul>
      </div>
    </nav>
  </header>

<#assign auth>${Request["cookies.auth"]?default("-1")}</#assign>
  <!-- Left side column. contains the logo and sidebar -->
  <aside class="main-sidebar">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
      <!-- sidebar menu: : style can be found in sidebar.less -->
      <ul class="sidebar-menu">
        <li class="header">主导航</li>
        <li class="treeview">
          <a target="navTab" href="/confView?">
            <i class="fa fa-files-o"></i>
            <span>全局配置</span>
          </a>
        </li>

      <#if auth=="1">
          <li>
              <a target="navTab" href="/hrc?">
                  <i class="fa fa-th"></i>
                  <span>远程部署</span>
                  <span class="pull-right-container">
              </a>
          </li>

          <li class="treeview">
              <a target="navTab" href="/nodeM2?">
                  <i class="fa fa-dashboard"></i>
                  <span>物理节点管理</span>
              </a>
          </li>

          <li class="treeview">
              <a target="navTab" href="/dockerindexftl?">
                  <i class="fa fa-laptop"></i>
                  <span>docker节点管理</span>
              </a>
          </li>
      </#if>

          <li class="treeview">
              <a target="navTab" href="/yarnindexftl?">
                  <i class="fa fa-pie-chart"></i>
                  <span>集群监控</span>
              </a>
          </li>


      <#if auth=="1">
          <li class="treeview">
              <a href="javascript:void(0);">
                  <i class="fa fa-share"></i> <span>storm发布</span>
                  <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
              </a>
              <ul class="treeview-menu">
                  <li><a target="navTab" href="/storminit?"><i class="fa fa-circle-o"></i> storm集群初始化</a></li>
                  <li><a target="navTab" href="/stormsingle?"><i class="fa fa-circle-o"></i> storm单容器发布</a></li>
              </ul>
          </li>
      </#if>

          <li class="treeview">
              <a href="javascript:void(0);">
                  <i class="fa fa-fw fa-desktop"></i> <span>storm监控管理</span>
                  <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
              </a>
              <ul class="treeview-menu">
                  <li><a target="navTab" href="/overviewftl?"><i class="fa fa-circle-o"></i> storm集群监控</a></li>
                  <li><a target="navTab" href="/topoPubV?"><i class="fa fa-circle-o"></i> 拓扑发布</a></li>
                  <li><a target="navTab" href="/hostsftl?"><i class="fa fa-circle-o"></i> Host</a></li>
                  <li><a target="navTab" href="/streamManagerftl?"><i class="fa fa-circle-o"></i> stream管理</a></li>
              </ul>
          </li>

      </ul>
    </section>
    <!-- /.sidebar -->
  </aside>

  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header-navtabs">
        <div class="tabs-page">
          <ul class="tabs-list clearfix" id="navTabs">
             <li class="active"><span>我的主页</span></li>
           </ul>
           <a href="javascript:void(0);" class="prev fa fa-step-backward"></a>
           <a href="javascript:void(0);" class="next fa fa-step-forward"></a> 
        </div>
        <div class="context-menu" id="contextMenu">
          <ul class="ct-nav">
            <li rel="reload">刷新标签页</li>
            <li rel="closeCurrent">关闭标签页</li>
            <li rel="closeOther">关闭其他标签页</li>
            <li rel="closeAll">关闭全部标签页</li>
          </ul>
        </div>
    </section>
    <!-- Main content -->
    <section class="" id="content">
      <div class="tabs-panel">
        <h1>欢迎使用DATA_STREAM </h1>
        <#--<div class="callout callout-warning">-->
          <#--<h4>温馨提示!</h4>-->

          <#--<p><b>adminLTE</b>使用jQuery 2.2.3版本，并引入很多优秀的第三方jQuery插件，开发者也可以改用自己熟悉的第三方插件，但原有插件对兼容IE低版本(6,7,8)浏览器并不友好，如果要考虑兼容低版本IE浏览器，建议替换jQuery版本到1.7.2以下并使用其他兼容低版本浏览器的jQuery插件。</p>-->
        <#--</div>-->
        <div class="row">
          <div class="col-md-6">
            <div class="box box-primary">
              <div class="box-header with-border">
                <h3 class="box-title">DATA STREAM 使用</h3>
              </div>
              <table class="table table-striped">
                <tbody>

                <tr>
                    <td><span class="text-light-blue">DataStream平台介绍</span></td>
                    <td width="130">
                        <a href="https://github.com/roncoo/roncoo-jui-springboot" class="btn bg-blue btn-xs">使用手册</a>
                    </td>
                </tr>

                  <tr>
                    <td><span class="text-light-blue">DataStream平台使用手册</span></td>
                    <td width="130">
                      <a href="https://github.com/roncoo/roncoo-jui-springboot" class="btn bg-blue btn-xs">使用手册</a>
                    </td>
                  </tr>

                </tbody>
              </table>
            </div>
          </div>
          <div class="col-md-6">
            <div class="box box-primary">
              <div class="box-header with-border">
                <h3 class="box-title">更多相关知识</h3>
              </div>
              <table class="table table-striped">
                <tbody>
                  <tr>
                    <td><strong>Hadoop生态圈</strong></td>
                    <td width="80">
                      <a href="http://www.roncoo.com/course/view/c99516ea604d4053908c1768d6deee3d" class="btn bg-blue btn-xs">view</a>
                    </td>
                  </tr>
                  <tr>
                    <td><strong>storm主要组成元素</strong></td>
                    <td width="80">
                      <a href="http://www.roncoo.com/course/view/a2d58fe08172447696412fb7af1de620" class="btn bg-blue btn-xs">view</a>
                    </td>
                  </tr>
                  <tr>
                    <td><strong>为什么互联网公司都在使用docker</strong></td>
                    <td width="80">
                      <a href="http://www.roncoo.com/course/view/a09d8badbce04bd380f56034f8e68be0" class="btn bg-blue btn-xs">view</a>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </section>
    <!-- /.content -->
  </div>
  <!-- Control Sidebar -->
  <aside class="control-sidebar control-sidebar-dark">
    <!-- Create the tabs -->
    <ul class="nav nav-tabs nav-justified control-sidebar-tabs">
    </ul>
    <!-- Tab panes -->
    <div class="tab-content">
      <!-- Home tab content -->
      <div class="tab-pane" id="control-sidebar-home-tab">
      </div>
      <div class="tab-pane" id="control-sidebar-settings-tab">
      </div>
    </div>
  </aside>
  <div class="control-sidebar-bg"></div>
</div>
<div id="loading" class="loading-panel">
  <div class="box">
    <i class="fa fa-refresh fa-spin"></i>
    <span class="tip">
       正在加载 · · ·
    </span>
  </div>
</div>


<div class="modal fade" id="smModal">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">×</span></button>
        <h4 class="modal-title">确认</h4>
      </div>
      <div class="modal-body">
      </div>
      <div class="modal-footer text-center">
        <button type="button" class="btn btn-warning" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary btn-confirm">确认</button>
      </div>
    </div>
    <!-- /.modal-content -->
  </div>
  <!-- /.modal-dialog -->
</div>
<div class="modal fade" id="lgModal">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">×</span></button>
          <h4 class="modal-title">提示</h4>
      </div>
      <div class="modal-body">
      </div>
      <div class="modal-footer text-center">
        <button type="button" class="btn btn-primary center-block" data-dismiss="modal">确认</button>
      </div>
    </div>
    <!-- /.modal-content -->
  </div>
  <!-- /.modal-dialog -->
</div>
<!-- ./wrapper -->
<script src="http://www.jq22.com/demo/jQuerySweetAlert220160627/dist/sweetalert2.min.js"></script>

<!-- jQuery 2.2.3 -->
<script src="plugins/jQuery/jquery-2.2.3.min.js"></script>
<!-- Bootstrap 3.3.6 -->
<script src="plugins/bootstrap/js/bootstrap.min.js"></script>
<script src="plugins/fastclick/fastclick.js"></script>
<!-- Slimscroll -->
<script src="plugins/slimScroll/jquery.slimscroll.min.js"></script>

<!-- AdminLTE App -->
<script src="dist/js/app.js"></script>
<!-- 以上JS为页面必须 -->

<!-- jQuery UI 1.11.4 -->
<script src="plugins/jQueryUI/jquery-ui.min.js"></script>
<!-- Morris.js charts -->
<script src="plugins/raphael/raphael.min.js"></script>
<script src="plugins/morris/morris.min.js"></script>
<!-- Sparkline -->
<script src="plugins/sparkline/jquery.sparkline.min.js"></script>
<!-- jvectormap -->
<script src="plugins/jvectormap/jquery-jvectormap-1.2.2.min.js"></script>
<script src="plugins/jvectormap/jquery-jvectormap-world-mill-en.js"></script>
<!-- jQuery Knob Chart -->
<script src="plugins/knob/jquery.knob.js"></script>
<!-- daterangepicker -->
<script src="plugins/moment/moment.min.js"></script>
<script src="plugins/daterangepicker/daterangepicker.js"></script>
<!-- datepicker -->
<script src="plugins/datepicker/bootstrap-datepicker.js"></script>
<!-- Bootstrap WYSIHTML5 -->
<script src="plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js"></script>
<!-- SlimScroll 1.3.0 -->
<script src="plugins/slimScroll/jquery.slimscroll.min.js"></script>
<!-- ChartJS 1.0.1 -->
<script src="plugins/chartjs/Chart.min.js"></script>
<!-- FLOT CHARTS -->
<script src="plugins/flot/jquery.flot.min.js"></script>
<!-- FLOT RESIZE PLUGIN - allows the chart to redraw when the window is resized -->
<script src="plugins/flot/jquery.flot.resize.min.js"></script>
<!-- FLOT PIE PLUGIN - also used to draw donut charts -->
<script src="plugins/flot/jquery.flot.pie.min.js"></script>
<!-- FLOT CATEGORIES PLUGIN - Used to draw bar charts -->
<script src="plugins/flot/jquery.flot.categories.min.js"></script>
<!-- iCheck -->
<script src="plugins/iCheck/icheck.min.js"></script>
<!-- Select2 -->
<script src="plugins/select2/select2.full.min.js"></script>
<!-- InputMask -->
<script src="plugins/input-mask/jquery.inputmask.js"></script>
<script src="plugins/input-mask/jquery.inputmask.date.extensions.js"></script>
<script src="plugins/input-mask/jquery.inputmask.extensions.js"></script>
<!-- date-range-picker -->
<script src="plugins/daterangepicker/daterangepicker.js"></script>
<!-- bootstrap datepicker -->
<script src="plugins/datepicker/bootstrap-datepicker.js"></script>
<!-- bootstrap color picker -->
<script src="plugins/colorpicker/bootstrap-colorpicker.min.js"></script>
<!-- bootstrap time picker -->
<script src="plugins/timepicker/bootstrap-timepicker.min.js"></script>
<!-- Ion Slider -->
<script src="plugins/ionslider/ion.rangeSlider.min.js"></script>
<!-- Bootstrap slider -->
<script src="plugins/bootstrap-slider/bootstrap-slider.js"></script>
<!-- DataTables -->
<script src="plugins/datatables/jquery.dataTables.min.js"></script>
<script src="plugins/datatables/dataTables.bootstrap.min.js"></script>
</body>
</html>
