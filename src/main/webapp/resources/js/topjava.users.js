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
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                },
            ],
            "order": [
                [0, "asc"]
            ]
        }),
        updateTable: function () {
            $.get(userAjaxUrl, updateTableByData);
        }
    });
});