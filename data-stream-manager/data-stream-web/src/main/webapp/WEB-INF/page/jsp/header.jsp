<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script src="<%= request.getContextPath() %>/js/termin/jquery-1.8.3.js"></script>
<script src="<%= request.getContextPath() %>/js/termin/jquery-ui.js"></script>
<script src="<%= request.getContextPath() %>/js/termin/jquery.tablescroll.js"></script>
<script src="<%= request.getContextPath() %>/js/termin/terms.js"></script>
<script src="<%= request.getContextPath() %>/js/termin/bootstrap.js"></script>

<link rel="stylesheet" href="<%= request.getContextPath() %>/statics/css/bootstrap.min.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/statics/css/termin/jquery-ui/base/jquery-ui.css"/>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/statics/css/termin/keybox.css"/>
<link rel="stylesheet" href="<%= request.getContextPath() %>/statics/css/font-awesome.min.css">

<script type="text/javascript">
    $(document).ready(function () {

        //重写alert
        window.alert = function(msg, callback){
            parent.layer.alert(msg, function(index){
                parent.layer.close(index);
                if(typeof(callback) === "function"){
                    callback("ok");
                }
            });
        }

        $(function () {
            $("a").tooltip({
                'selector': '',
                'placement': 'bottom',
                'container': 'body'
            });
        });
        $(function () {
            var tabindex = 1;
            $('input,textarea,select,.btn').each(function () {
                if (this.type != "hidden") {
                    var $input = $(this);
                    $input.attr("tabindex", tabindex);
                    tabindex++;
                }
            });

            $(".btn").keyup(function (event) {
                if (event.keyCode == 13) {
                    $(this).click();
                }
            });
        });

    });
</script>



