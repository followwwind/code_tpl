<#assign isRest = param.isRest!true/>
<#assign isEnglish = param.isEnglish!false/>
'use strict'
app.controller('${property}Controller', function($scope, $http, $timeout, time, obj, $stateParams) {
    
    $scope.query = {
        "lineNumber": 10,
        "pageNumber": 1,
        "name": ""
    };
    
    $scope.form = {
        <#list columnList as column>
        <#if !(['createTime', 'updateTime']?seq_contains(column.alias))>
        <#if column_index < (columnList?size - 1)>
        "${column.alias}": '',
        <#else>
        "${column.alias}": ''
        </#if>
        </#if>
        </#list>
    };

    $scope.dialog = {
        "isAdd": true,
        "title": "${isEnglish?string('Add', '新增')}",
    };

    $scope.body = {
        "queryTitle": "${isEnglish?string('Query Condition', '查询条件')}",
        "tableTitle": "${isEnglish?string('Data List', '数据列表')}"
    };

    $scope.returnData = {
        recordsTotal: 0,
        recordsFiltered: 0,
        data: []
    };


    var tbl;
    var layer = layui.layer;

    <#list columnList as column>
    <#if column.javaType == "java.util.Date" && !column.name?contains("create") && !column.name?contains("update")>
    layui.laydate.render({
        elem: '#${column.alias}',
        type: 'datetime',
        done: function(value, date){
            $scope.form.${column.alias} = value;
        }
    });
    </#if>
    </#list>

    $timeout(function(){
        tbl = $('#tbl').DataTable({
            "ajax" : function (data, callback, settings) {
                //封装请求参数
                $scope.query.lineNumber = data.length;//页面显示记录条数，在页面显示每页显示多少项的时候
                $scope.query.pageNumber = (data.start / data.length)+1;//当前页码
                $http.post($scope.serverUrl + 'api/${alias}/page/list', $scope.query).then(function(response){
                    // console.log(response.data);
                    var resultData = response.data.data;
                    $scope.returnData.recordsTotal = resultData.totalCount;//返回数据全部记录
                    $scope.returnData.recordsFiltered = resultData.totalCount;//后台不实现过滤功能，每次查询均视作全部结果
                    $scope.returnData.data = resultData.data;//返回的数据列表
                    callback($scope.returnData);
                }, 
                function(err){
                    layer.msg('${isEnglish?string('api service exception', '接口请求异常')}', {icon: 5});
                });
            },
            "serverSide":true,
            <#if isEnglish>
            //"language":$scope.datatables_lang, //提示信息
            <#else>
            "language":$scope.datatables_lang, //提示信息
            </#if>
            "renderer": "bootstrap", //渲染样式：Bootstrap和jquery-ui
            "pagingType": "full_numbers", //分页样式：simple,simple_numbers,full,full_numbers
            "bFilter": true,
            "bSort": false,
            /*"scrollX": true,*/
            "fnInitComplete":function (oSettings, json) {
            
            },
            "columns": [
                {
                    "targets": 0,
                    "render": function(data, type, row){
                        return '<label class="i-checks m-b-none"><input type="checkbox"><i></i></label>';
                    }
                },
                <#list columnList as column>
                <#if !(["password", "id", "uuid"]?seq_contains(column.alias))>
                {
                    "render": function(data, type, row){
                        return row.${column.alias};
                    }
                },
                </#if>
                </#list>
                {
                    "render": function(data, type, row){
                        var html = '';
                        html += '<span class="btn btn-info btn-sm" name="edit">${isEnglish?string('Edit', '修改')}</span>';
                        html += '<span class="btn btn-danger btn-sm" style="margin-left:3px;" name="del">${isEnglish?string('Del', '删除')}</span>';
                        return html;
                    }
                },
            ]
        })
    }, 0);

    $('#tbl tbody').on('click', 'span[name="edit"]', function(e){
        var entity = tbl.row($(e.target).parents("tr")).data();
        $scope.dialog.isAdd = false;
        $scope.dialog.title = '${isEnglish?string('Edit', '修改')}';
        obj.copy(entity, $scope.form);
        $scope.$apply();
        $("#add").modal("show");
    });

    $('#tbl tbody').on('click', 'span[name="del"]', function(e){
        var entity = tbl.row($(e.target).parents("tr")).data();
        <#if isRest>
        $http.delete($scope.serverUrl + "api/${alias}/" + entity.id, {}).then(function(response){
            if(response.data.code == 200){
                layer.msg('${isEnglish?string('success', '删除成功')}', {icon: 1});
            }else{
                layer.msg(response.data.msg, {icon: 5});
            }
            tbl.ajax.reload();
        },
        function(err){
            layer.msg('${isEnglish?string('api service exception', '接口请求异常')}', {icon: 5});
        });
        <#else>
        $http.get($scope.serverUrl + "api/${alias}/delete/" + entity.id, {}).then(function(response){
            if(response.data.code == 200){
                layer.msg('${isEnglish?string('success', '删除成功')}', {icon: 1});
            }else{
                layer.msg(response.data.msg, {icon: 5});
            }
            tbl.ajax.reload();
        },
        function(err){
            layer.msg('${isEnglish?string('api service exception', '接口请求异常')}', {icon: 5});
        });
        </#if>
    });

    $scope.add = function(){
        $scope.dialog.isAdd = true;
        $scope.dialog.title = '${isEnglish?string('Add', '新增')}';
        $scope.form = {};
        $("#add").modal("show");
    };

    $scope.search = function(){
        tbl.ajax.reload();
    };

    $scope.reset = function(){
        $scope.query = {
            "lineNumber": 10,
            "pageNumber": 1,
            "name": ""
        };
        tbl.ajax.reload();
    };

    $scope.save = function(){
        <#if isRest>
        var url =  "api/${alias}/";
        if($scope.dialog.isAdd){
            $http.post($scope.serverUrl + url, $scope.form).then(function(response){
                if(response.data.code == 200){
                    layer.msg('${isEnglish?string('success', '操作成功')}', {icon: 1});
                }else{
                    layer.msg(response.data.msg, {icon: 5});
                }
                tbl.ajax.reload();
                $("#add").modal("hide");
            },
            function(err){
                layer.msg('${isEnglish?string('api service exception', '接口请求异常')}', {icon: 5});
            });
        }else{
            $http.put($scope.serverUrl + url, $scope.form).then(function(response){
                if(response.data.code == 200){
                    layer.msg('${isEnglish?string('success', '操作成功')}', {icon: 1});
                }else{
                    layer.msg(response.data.msg, {icon: 5});
                }
                tbl.ajax.reload();
                $("#add").modal("hide");
            },
            function(err){
                layer.msg('${isEnglish?string('api service exception', '接口请求异常')}', {icon: 5});
            });
        }
        <#else>
        var url =  $scope.dialog.isAdd == 1 ? "api/${alias}/save" : "api/${alias}/update";
        $http.post($scope.serverUrl + url, $scope.form).then(function(response){
            if(response.data.code == 200){
                layer.msg('${isEnglish?string('success', '操作成功')}', {icon: 1});
            }else{
                layer.msg(response.data.msg, {icon: 5});
            }
            tbl.ajax.reload();
            $("#add").modal("hide");
        },
        function(err){
            layer.msg('${isEnglish?string('api service exception', '接口请求异常')}', {icon: 5});
        });
        </#if>
    };

    $scope.uploadImg = function(event){
        //console.log(event);
        var param = new FormData();
        var files = event.target.files;
        for(var i = 0; i < files.length; i++){
            param.append("files", files[0]);
        }
        $http.post($scope.serverUrl + 'api/file/uploadFile', param, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).then(function(response){
            // console.log(response);
            $scope.form.entity.img = response.data.data;
        }, function(err){
            
        });
    };


});