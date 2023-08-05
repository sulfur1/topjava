let form, context;


function makeEditable(ctx) {
    context = ctx;
    form = $('#detailsForm');
    filter = $('#filter');
    $(".delete").click(function () {
        if (confirm('Are you sure?')) {
            deleteRow($(this).closest('tr').attr("id"));
        }
    });


    $(document).ajaxError(function (event, jqXHR, options, jsExc) {
        failNoty(jqXHR);
    });

    // solve problem with cache in IE: https://stackoverflow.com/a/4303862/548473
    $.ajaxSetup({cache: false});
}

function add() {
    form.find(":input").val("");
    $("#editRow").modal();
}
function updateRowMeal(id) {
    $.get((context.ajaxUrl + id), function (data) {
        form.find("input#id").val(data.id);
        form.find("input#dateTime").val(data.dateTime);
        form.find("input#description").val(data.description);
        form.find("input#calories").val(data.calories);
        $("#editRow").modal();
    }, "json");
}

function deleteRow(id) {
    $.ajax({
        url: context.ajaxUrl + id,
        type: "DELETE"
    }).done(function () {
        context.updateTable();
        successNoty("Deleted");
    });
}

function updateTableByData(data) {
    context.dataTableApi.clear().rows.add(data).draw();
}

function updateFilteredTable() {
    $.ajax({
        url: context.ajaxUrl + "filter",
        type: "GET",
        data: $('#filter').serialize()
    }).done(function (data) {
        updateTableByData(data);
        successNoty("Filtered");
    });
}
function enable(checkbox, id) {
    let enabled = checkbox.is(":checked");
    $.ajax({
        url: context.ajaxUrl + id,
        type: "POST",
        data: "enabled=" + enabled
    }).done(function () {
        checkbox.closest("tr").attr("data-userEnabled", enabled);
        successNoty(enabled ? "Enabled" : "Disabled");
    }).fail(function () {
        $(checkbox).prop("checked", !enabled);
    })
}
function save() {
    $.ajax({
        type: "POST",
        url: context.ajaxUrl,
        data: form.serialize()
    }).done(function () {
        $("#editRow").modal("hide");
        context.updateTable();
        successNoty("Saved");
    });
}
function clearFilter() {
    $('#filter')[0].reset();
    $.get("admin/meals/", updateTableByData)
}
let failedNote;

function closeNoty() {
    if (failedNote) {
        failedNote.close();
        failedNote = undefined;
    }
}

function successNoty(text) {
    closeNoty();
    new Noty({
        text: "<span class='fa fa-lg fa-check'></span> &nbsp;" + text,
        type: 'success',
        layout: "bottomRight",
        timeout: 1000
    }).show();
}

function failNoty(jqXHR) {
    closeNoty();
    failedNote = new Noty({
        text: "<span class='fa fa-lg fa-exclamation-circle'></span> &nbsp;Error status: " + jqXHR.status,
        type: "error",
        layout: "bottomRight"
    });
    failedNote.show()
}