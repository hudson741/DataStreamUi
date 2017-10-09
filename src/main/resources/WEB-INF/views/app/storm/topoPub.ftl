<div class="col-md-10">


    <div class="box box-primary">
        <div class="box-header with-border" ng-app="">
            <h3 class="box-title">storm拓扑发布</h3>
        </div>
        <!-- /.box-header -->
        <!-- form start -->
        <form role="form" method="post" id="stormtp" action="stormTopoPub" enctype="multipart/form-data">
            <div class="form-group">
                <input type="file" id="file" name="file" >

                <p class="help-block">请先看说明.</p>
            </div>
            <!-- /.box-body -->

            <div class="box-footer">
                <button type="submit" class="btn btn-primary">发布</button>
            </div>
        </form>
    </div>

    <div class="panel box box-danger">
                <div class="box-header with-border">
                    <h4 class="box-title">
                        <a data-toggle="collapse" >
                           storm拓扑发布说明
                        </a>
                    </h4>
                </div>
                <div id="collapseTwo" class="panel-collapse collapse in">
                    <div class="box-body">
                        <div>1. 开发人员将开发好的topology打成jar包，通过此接口上传即可实现发布</div>
                        <div>2. 请使用storm-1.1.0进行开发，并使用provided标记scope</div>
                        <div>3. 请在拓扑包内建立yss.data.stream包，并在下面编写如下类，其中getTopology方法由开发人员实现，内部返回需要发布的storm拓扑</div>
                        <div>public class BuilderImpl implements Builder { </div>


                        <div>&nbsp;&nbsp;&nbsp;&nbsp;public StormTopology getTopology() {</div>

                        <div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return Main.buildKafkaTopology();</div>
                        <div>&nbsp;&nbsp;&nbsp;&nbsp;}</div>
                        <div>}</div>
                        </div>

                        <div>4. 拓扑返回实例：</div>
                        <div> public static StormTopology buildKafkaTopology(){</div>

                    <div>&nbsp;&nbsp;&nbsp;&nbsp;TopologyBuilder builder = new TopologyBuilder();</div>

                    <div>&nbsp;&nbsp;&nbsp;&nbsp;KafkaSpout spout = KafkaSpoutBuilder.createKafkaSpout();</div>

                    <div>&nbsp;&nbsp;&nbsp;&nbsp;KafkaBolt kafkaBolt = KafkaBoltBuilder.createKafkaBolt();</div>

                    <div>&nbsp;&nbsp;&nbsp;&nbsp;builder.setSpout("kafkaF",spout,1);</div>

                    <div>&nbsp;&nbsp;&nbsp;&nbsp;builder.setBolt("step1",new com.kafka.Step1Bolt(),1).shuffleGrouping("kafkaF");</div>

                    <div>&nbsp;&nbsp;&nbsp;&nbsp;builder.setBolt("forwordToKafka",kafkaBolt,1).shuffleGrouping("step1");</div>

                    <div>&nbsp;&nbsp;&nbsp;&nbsp;return builder.createTopology();</div>
                    <div>} </div>
                        </div>
                    </div>
                </div>
            </div>
    <!-- left column -->
        <!-- general form elements -->

</div>
</div>
</body>

<script type="text/javascript">
    function topopub(){
        $.ajax({
            url: '/stormTopoPub',
            type: 'post',
            data: $("#stormtp").serialize(),
            success: function (data) {
                alertMsg(data);
            },
            error: function(data){
                alertMsg(data);
            }
        });
    };

</script>
