$("#enviar").click(function () {
//Pega o valor do arquivo escolhido
    var id = $('input[name=arquivo]:checked', '#jnlpForm').val();
    $.getJSON("rest/token/generate/" + id, function (data) {
        $("#hash").val(data.message);
        $('#jnlpForm').submit();
    });

});