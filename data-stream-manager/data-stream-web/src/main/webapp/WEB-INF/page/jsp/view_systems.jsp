<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=utf-8" language="java"  pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="./header.jsp"/>
    <script type="text/javascript">
        $(document).ready(function () {

            //select all check boxes
            $("#select_frm_systemSelectAll").click(function () {
                if ($(this).is(':checked')) {
                    $(".systemSelect").prop('checked', true);
                } else {
                    $(".systemSelect").prop('checked', false);
                }
            });


            $(".submit_btn").button().click(function () {
                var v = $(this).parents('.modal').find('form').serialize();
                $.ajax({
                    type: 'GET',
                    url: '/admin/saveSystem?' + v,
                    contentType: "application/json",
                    success: function (r) {
                        jsonObj = $.parseJSON(r)
                        if (jsonObj.code == 0) {
                            window.location.reload()
                        } else {
                            alert(jsonObj.msg)
                        }
                    }
                });
            });

            $(".del_btn").button().click(function () {
                var id = $(this).attr('id').replace("del_btn_", "");
                alert(id)
            });


            $(".select_frm_btn").button().click(function () {

                if($("input[type='checkbox']").is(':checked')){
                    $("#select_frm").submit();
                }else {
                    alert('请选择主机');
                }

            });


            <c:if test="${sortedSet.orderByField!=null && sortedSet.orderByField!=''}">
            $('#<c:out value="${sortedSet.orderByField}"/>').attr('class', '<c:out value="${sortedSet.orderByDirection}"/>');
            </c:if>

        });
    </script>


    <title>KeyBox - Manage Systems</title>
</head>
<body>


<div class="container">

    <c:choose>
        <c:when test="${script!=null}">
            <h3>Execute Script on Systems</h3>
            <p>Run <b> <a data-toggle="modal" data-target="#script_dialog"><c:out value="${script.displayNm}"/></a></b>
                on the selected systems below</p>

            <div id="script_dialog" class="modal fade">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
                            <h4 class="modal-title">View Script: <c:out value="${script.displayNm}"/></h4>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <pre><c:out value="${script.script}"/></pre>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default cancel_btn" data-dismiss="modal">Close</button>

                        </div>
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <h3>Composite SSH Terminals</h3>
            <p>Select the systems below to generate composite SSH sessions in multiple terminals</p>
        </c:otherwise>

    </c:choose>

    <form id="viewSystems" name="viewSystems" action="/admin/viewSystems.action" method="post">
        <input type="hidden" name="sortedSet.orderByDirection" value="${sortedSet.orderByDirection}">
        <input type="hidden" name="sortedSet.orderByField" value="${sortedSet.orderByField}">
        <c:if test="${script!=null}">
            <input type="hidden" value="${script.id}">
        </c:if>
        <c:if test="${profileList!= null && !profileList.isEmpty()}">
            <div>
                <table>
                    <tr>
                        <td class="align_left">
                                <%--<s:select name="sortedSet.filterMap['%{@com.keybox.manage.db.SystemDB@FILTER_BY_PROFILE_ID}']" listKey="id" listValue="nm"--%>
                                <%--class="view_frm_select"--%>
                                <%--list="profileList"--%>
                                <%--headerKey=""--%>
                                <%--headerValue="-Select Profile-"/>--%>
                        </td>
                        <td>
                            <div id="view_btn" class="btn btn-default">Filter</div>
                        </td>
                    </tr>
                </table>
            </div>
        </c:if>

    </form>


    <c:if test="${sortedSet.itemList!= null && !sortedSet.itemList.isEmpty()}">

        <form id="select_frm" action="/admin/selectSystemsForCompositeTerms.action" method="post">
            <c:if test="${script!=null}">
                <input type="hidden" name="${script.id}">
            </c:if>
            <div class="scrollWrapper">
                <table class="table-striped scrollableTable">
                    <thead>

                    <tr>
                        <th>
                            <input type="checkbox" name="systemSelectAll" id="select_frm_systemSelectAll" class="checkbox">
                        </th>
                        <th id="display_nm" class="sort">Display Name
                        </th>
                        <th id="user" class="sort">User
                        </th>
                        <th id="host" class="sort">Host
                        </th>
                        <th>Status</th>
                        <th class="sort">operation
                        </th>
                    </tr>
                    </thead>
                    <tbody>

                    <c:forEach var="i" items="${sortedSet.itemList}">
                        <tr>
                            <td>
                                <input type="checkbox" class="checkbox systemSelect" name="systemSelectId" value="${i.id}">
                            </td>
                            <td>
                                <c:out value="${i.displayNm}"/>
                            </td>
                            <td><c:out value="${i.user}"/></td>
                            <td><c:out value="${i.host}"/>:<c:out value="${i.port}"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${i.statusCd=='INITIAL'}">
                                        <div class="warning">Not Started</div>
                                    </c:when>
                                    <c:when test="${i.statusCd=='HOSTFAIL'}">
                                        <div class="error">DNS Lookup Failed</div>
                                    </c:when>
                                    <c:when test="${i.statusCd=='KEYAUTHFAIL'}">
                                        <div class="warning">Passphrase Authentication Failed</div>
                                    </c:when>
                                    <c:when test="${i.statusCd=='GENERICFAIL'}">
                                        <div class="error">Failed</div>
                                    </c:when>
                                    <c:when test="${i.statusCd=='SUCCESS'}">
                                        <div class="success">Success</div>
                                    </c:when>
                                </c:choose>

                            </td>
                            <td>
                                <div style="width:160px">
                                    <button type="button" class="btn btn-warning" data-toggle="modal" data-target="#edit_dialog_<c:out value="${i.id}"/>"><i class="fa fa-pencil-square-o" aria-hidden="true"></i> Edit</button>
                                    <a  href="/admin/delete/<c:out value="${i.id}"/>" class="btn btn-danger" style="text-decoration: none;" ><i class="fa fa-trash-o" aria-hidden="true"></i> Delete</a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>

                    </tbody>
                </table>
            </div>

        </form>

    </c:if>

    <c:if test="${sortedSet.itemList!= null && !sortedSet.itemList.isEmpty()}">
        <c:forEach var="i" items="${sortedSet.itemList}">
            <div id="edit_dialog_<c:out value="${i.id}"/>" class="modal fade">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
                            <h4 class="modal-title">Edit System</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <form action="/admin/saveSystem" method="post">
                                    <table class="save_sys_form_add">
                                        <tbody>
                                        <tr>
                                            <td class="tdLabel">Display Name</td>
                                            <td><input type="text" name="displayNm" value="${i.displayNm}"></td>
                                        </tr>
                                        <tr>
                                            <td class="tdLabel">System User</td>
                                            <td><input type="text" name="user" value="${i.user}"></td>
                                        </tr>
                                        <tr>
                                            <td class="tdLabel">Host</td>
                                            <td><input type="text" name="host" value="${i.host}"></td>
                                        </tr>
                                        <tr>
                                            <td class="tdLabel">Port</td>
                                            <td><input type="text" name="port" value="${i.port}"></td>
                                        </tr>
                                        <tr>
                                            <td class="tdLabel">Authorized Keys</td>
                                            <td><input type="text" name="authorizedKeys" value="${i.authorizedKeys}"></td>
                                        </tr>
                                        <tr>
                                            <td class="tdLabel">password</td>
                                            <td><input type="password" name="password"></td>
                                        </tr>
                                        </tbody>
                                    </table>
                                    <input type="hidden" name="id" value="${i.id}">
                                </form>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-warning cancel_btn" data-dismiss="modal">Cancel</button>
                            <button type="button" class="btn btn-primary submit_btn">Submit</button>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </c:if>




    <c:choose>
        <c:when test="${script!=null && sortedSet.itemList!= null && !sortedSet.itemList.isEmpty()}">
            <div class="btn btn-default select_frm_btn spacer spacer-bottom">Execute Script</div>
        </c:when>
        <c:when test="${sortedSet.itemList!= null }">

            <%--&& !sortedSet.itemList.isEmpty()--%>
            <div class="btn btn-default btn-primary select_frm_btn" id="create_ssh"><i class="fa fa-paper-plane" aria-hidden="true"></i> Create SSH Terminals</div>
            <div class="btn btn-default btn-primary add_btn " data-toggle="modal" data-target="#add_dialog"><i class="fa fa-plus" aria-hidden="true"></i> Add System</div>

            <div id="add_dialog" class="modal fade">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
                            <h4 class="modal-title">Add System</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <form action="/admin/saveSystem" method="post">
                                    <table class="save_sys_form_add">
                                        <tbody>
                                        <tr>
                                            <td class="tdLabel">Display Name</td>
                                            <td><input type="text" name="displayNm"></td>
                                        </tr>
                                        <tr>
                                            <td class="tdLabel">System User</td>
                                            <td><input type="text" name="user" value="root"></td>
                                        </tr>
                                        <tr>
                                            <td class="tdLabel">Host</td>
                                            <td><input type="text" name="host"></td>
                                        </tr>
                                        <tr>
                                            <td class="tdLabel">Port</td>
                                            <td><input type="text" name="port" value="22"></td>
                                        </tr>
                                        <tr>
                                            <td class="tdLabel">Authorized Keys</td>
                                            <td><input type="text" name="authorizedKeys" value="~/.ssh/authorized_keys"></td>
                                        </tr>
                                        <tr>
                                            <td class="tdLabel">password</td>
                                            <td><input type="password" name="password"></td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </form>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default btn-warning cancel_btn" data-dismiss="modal">Cancel</button>
                            <button type="button" class="btn btn-default btn-primary submit_btn">Submit</button>
                        </div>
                    </div>
                </div>
            </div>


        </c:when>
        <c:otherwise>
            <div class="actionMessage">
                <p class="error">Systems not available
                    <c:if test="${sessionScope.userType==\"M\"}">
                        (<a href="../manage/viewSystems">Manage Systems</a>)
                    </c:if>.
                </p>
            </div>
        </c:otherwise>

    </c:choose>

</div>


</body>
</html>
