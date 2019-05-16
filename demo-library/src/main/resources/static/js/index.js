var $table;

//初始化bootstrap-table的内容
function InitMainTable () {
    //记录页面bootstrap-table全局变量$table，方便应用
    $table = $('#book_table').bootstrapTable({
        url: '/library/search',
        method: 'post',
        contentType:"application/x-www-form-urlencoded; charset=UTF-8",
        toolbar: '#toolbar',
        dataType: "json",
        striped: true,                       // 设置为 true 会有隔行变色效果
        undefinedText: "空",                 // 当数据为 undefined 时显示的字符
        pagination: true,                    // 分页
        sortable: true,                      // 是否启用排序
        sortOrder: "asc",                    // 排序方式
        sidePagination: "server",            // 分页方式： client 客户端分页， server  服务端分页（*）
        showPaginationSwitch:true,           // 是否显示 数据条数选择框
        showColumns: "true",                 // 是否显示 内容列下拉框
        showRefresh: true,                   // 是否显示刷新按钮
        pageNumber: 1,                       // 如果设置了分页，首页页码
        pageSize: 3,                         // 如果设置了分页，页面数据条数
        pageList: [3, 5, 7],
        paginationPreText: '上一页',         // 指定分页条中上一页按钮的图标或文字,这里是<
        paginationNextText: '下一页',        // 指定分页条中下一页按钮的图标或文字,这里是>
        data_local: "zh-US",                 // 表格汉化
        clickToSelect: true,
        queryParams: function (params) {     // 得到查询的参数
            var param = getFormParam($('#formSearch'));
            // 页大小
            param["pageSize"] =  params.limit;
            // 页码
            param["pageNumber"] =  (params.offset / params.limit);
            // 排序类型
            param["sortDir"] = params.order;
            // 排序字段
            param["ordName"] = params.sort;
            return param;
        },
        idField: "id",                       // 指定主键列
        columns: [
            {
                field: 'id',
                title: '书ID'
            }, {
                field: 'name',
                title: '书名'
            }, {
                field: 'author',
                title: '作者'
            },{
                field: 'image',
                title: '封面',
                formatter: function (value, row, index) {
                    return '<img src='+value+' style="width: auto; height: auto; max-width: 50px; max-height: 50px;" alt="图片加载失败" >'
                }
            },{
                title: '操作',
                field: 'operate',
                align: 'center',
                formatter: operateFormatter,
                events: operateEvents
            }
        ]
    });
};

//操作栏的格式化
function operateFormatter(value, row, index) {
    var result = "";
    result += "<a type='button' id='cell_view'  data-toggle='modal' class='btn btn-xs yellow' title='查看'><span class='glyphicon glyphicon-search'></span></a>";
    result += "<a type='button' id='cell_edit'  data-toggle='modal' class='btn btn-xs yellow' title='编辑'><span class='glyphicon glyphicon-pencil'></span></a>";
    result += "<a type='button' id='cell_remove' data-toggle='modal' class='btn btn-xs blue' title='删除'><span class='glyphicon glyphicon-remove'></span></a>";
    return result;
}

//指定table表体操作事件
window.operateEvents = {
    'click #cell_edit': function(e, value, row, index) {
        $('#edit_id').val(row['id']);
        $('#edit_name').val(row['name']);
        $('#edit_author').val(row['author']);
        $('#edit_image').val(row['image']);

        $('#modeEdit').modal('show');
    },
    'click #cell_view': function(e, value, row, index) {
        onBtnView(row["id"]);
    },
    'click #cell_remove': function(e, value, row, index) {
        if(confirm("是否要删除记录[" + row["id"] + "-" + row["name"] + "] ?")) {
            onBtnRemove(row["id"]);
        }
    }
};

// 页面初始化
$(function(){
    InitMainTable();
    $table.bootstrapTable('refresh');
});

// 添加书籍
function onBtnAdd() {
    var mod = $('#modeAdd');

    var para = getFormParam($('#formAdd'));

    $.ajax({
        type: "POST",
        url: "/library/save",
        data: para,
        success: function(data){
            if(data["code"] != 0){
                alert('添加失败，原因:' + data["info"] );
            }
            else {
                mod.modal('hide');
                $table.bootstrapTable('refresh');
                alert('添加成功');
            }
        },
        fail: function (e) {
            alert("执行异常，" + e);
        }
    });
}

// 编辑书籍
function onBtnEdit() {
    var mod = $('#modeEdit');
    var para = getFormParam($('#formEdit'));

    $.ajax({
        type: "POST",
        url: "/library/edit/" + para["id"],
        data: para,
        success: function(data){
            if(data["code"] != 0){
                alert('更新失败' );
            }
            else {
                mod.modal('hide');
                $table.bootstrapTable('refresh');
                alert('更新成功');
            }
        },
        fail: function (e) {
            alert("执行异常，" + e);
        }
    });
}

// 查看书籍信息
function onBtnView(id){
    $.ajax({
        type: "GET",
        url: "/library/view/" + id,
        success: function(data){
            if(data["code"] != 0){
                alert('查询失败' );
            }
            else {
                var row = data["data"];
                $('#view_id').val(row['id']);
                $('#view_name').val(row['name']);
                $('#view_author').val(row['author']);
                $('#view_image').val(row['image']);
                $("#view_image_show").attr('src',row["image"]);
                $('#modeView').modal('show');
            }
        },
        fail: function (e) {
            alert("执行异常，" + e);
        }
    });
}

// 删除书籍
function onBtnRemove(id) {
    $.ajax({
        type: "get",
        url: "/library/remove/" + id,
        success: function(data){
            $table.bootstrapTable('refresh');
            alert('删除成功');
        },
        fail: function (e) {
            alert("删除失败，" + e);
        }
    });
}

// 查询搜索
function onTableSearch(){
    $table.bootstrapTable('refresh');
};