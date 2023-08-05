const mealsAjaxUrl = "admin/meals/"

const ctx = {
    ajaxUrl: mealsAjaxUrl,
    dataTableApi: $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime"
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "asc"
            ]
        ]
    }),
    updateTable: function () {
        $.get("admin/meals/", updateTableByData())
    }
}

// $(document).ready(function () {
$(function () {
    makeEditable(ctx);
});