function getFormParam(form) {
    var param = {};
    form.find('[name]').each(function () {
        var value = $(this).val();
        if(value != '') {
            param[$(this).attr('name')] = value;
        }
    });
    return param;
}