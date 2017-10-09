<p>this is the topo view</p>


//
// Setup the data-model for the chart.
//

//    $scope.data = "asdfasdfasdfasdfasdf";
//
//    var chartDataModel = {
//
//        nodes: [
//            {
//                name: "Example Node 1asdfasdfasdf",
//                id: 0,
//                x: 0,
//                y: 0,
//                width: 500,
//                height: 1000,
//                inputConnectors: [
//                    {
//                        name: "aaaaa\nbbbbbb\ncccc\nddddd\n"
//                    },
//                    {
//                        name: "B"
//                    },
//                    {
//                        name: "C"
//                    }
//                ],
//                outputConnectors: [
//                    {
//                        name: "A"
//                    },
//                    {
//                        name: "B"
//                    },
//                    {
//                        name: "C"
//                    }
//                ]
//            },
//
//            {
//                name: "Example Node 2",
//                id: 1,
//                x: 400,
//                y: 200,
//                width: 1000,
//                height: 1000,
//                inputConnectors: [
//                    {
//                        name: "A"
//                    },
//                    {
//                        name: "B"
//                    },
//                    {
//                        name: "C"
//                    }
//                ],
//                outputConnectors: [
//                    {
//                        name: "A"
//                    },
//                    {
//                        name: "B"
//                    },
//                    {
//                        name: "C"
//                    }
//                ]
//            }
//
//        ],
//
//        connections: [
//            {
//                source: {
//                    nodeID: 0,
//                    connectorIndex: 1
//                },
//
//                dest: {
//                    nodeID: 1,
//                    connectorIndex: 2
//                }
//            }
//        ]
//    };
//    $scope.chartViewModel = new flowchart.ChartViewModel(chartDataModel);

{{data}}

<div style="width: 100%; overflow: hidden;" mouse-capture>
  <div style="width: 600px; float: left;">
    <textarea
      style="width: 100%; height: 100%;"
      chart-json-edit
      view-model="chartViewModel"
      >
    </textarea>
  </div>
  <br/>

  <div style="margin-left: 0;">
    <!--
    This custom element defines the flowchart.
    -->
    <flow-chart
      style="margin: 5px; width: 90%; height: 2999px;"
      chart="chartViewModel"
      >
    </flow-chart>
  </div>
</div>
