const userAjaxUrl = "ajax/admin/users/";

function enable(checkbox, id) {
    const enabled = checkbox.is(":checked");
    // https://stackoverflow.com/a/22213543/548473
    $.ajax({
        url: userAjaxUrl + id,
        type: "POST",
        data: "enabled=" + enabled
    }).done(function () {
        checkbox.closest("tr").attr("data-userEnabled", enabled);
        successNoty(enabled ? "Enabled" : "Disabled");
    }).fail(function () {
        $(checkbox).prop("checked", !enabled);
    });
}

// $(document).ready(function () {
$(function () {
    makeEditable({
        ajaxUrl: userAjaxUrl,
        datatableApi: $("#datatable").DataTable({
            "ajax": {
                "url": userAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email",
                    "render": function (data, type, row) {
                        if (type === "display") {
                            return "<a href='mailto:" + data + "'>" + data + "</a>";
                        }
                        return data;
                    }
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled",
                    "render": function (data, type, row) {
                        if (type === "display") {
                            return "<input type='checkbox' " + (data ? "checked" : "") + " onclick='enable($(this)," + row.id + ");'/>";
                        }
                        return data;
                    }
                },
                {
                    "data": "registered",
                    "render": function (data, type, row) {
                        if (type === "display") {
                            return data.substring(0, 10);
                        }
                        return data;
                    }
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false,
                    "render": renderEditButton
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false,
                    "render": renderDeleteButton
                },
            ],
            "order": [
                [0, "asc"]
            ],
            "createRow": function (row, data, dataIndex) {
                if (!data.enabled) {
                    $(row).attr("data-userEnabled", false);
                }
            }
        }),
        updateTable: function () {
            $.get(userAjaxUrl, updateTableByData);
        }
    });
});