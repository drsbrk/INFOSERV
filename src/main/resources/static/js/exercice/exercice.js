console.log("Preparing Data to sent");
$('#saveExercice').click(function (e) {
    e.preventDefault();
    // Collect data from your form, similar to how it's done in postecollecte.js
    const exercice = {
        'code': $('#code').val(),
        'libelle': $('#libelle').val(),
        'dateDebut': $('#dateDebut').val(),
        'dateFin': $('#dateFin').val(),
    };
    // Validate your data
    if (exercice.code === null || exercice.code === undefined || exercice.code.trim().length === 0) {
        // Show error message with iziToast
        iziToast.warning({
            title: 'OK',
            message: 'Code exercice obligatoire',
        });
    }
    if (exercice.libelle === null || exercice.libelle === undefined || exercice.libelle.trim().length === 0) {
        iziToast.warning({
            title: 'OK',
            message: 'Libelle exercice obligatoire',
        });

    }

    if (exercice.dateDebut === null || exercice.dateDebut === undefined) {
        iziToast.warning({
            title: 'OK',
            message: 'La date de début est obligatoire.',
        });
        return;

    }
    if (exercice.dateFin === null || exercice.dateFin === undefined) {
        iziToast.warning({
            title: 'OK',
            message: 'La date de fin est obligatoire.',
        });
        return;
    }
    const dateDebutD = new Date(exercice.dateDebut);
    const dateFinD = new Date(exercice.dateFin);


    if (dateDebutD >= dateFinD) {
        iziToast.warning({
            title: 'OK',
            message: 'La date de début ne doit pas être antérieure à la date de fin',
        });
        return;
    }

// Send a POST request to '/exercice'
    $.post('/exercice', exercice, function (data) {
    }).done((data) => {
        resetForm('exercice_form');
        // loadAllExercice(0, 5);
        loadAllExercices();
        iziToast.success({
            title: 'OK',
            message: 'Enregistrement effectué',
        });
    }).fail((jqXHR, textStatus, errorThrown) => {

        console.log("2 I fail shamelessly with " + JSON.stringify(jqXHR));
        iziToast.warning({
            title: 'OK',
            position: 'center',
            message: jqXHR.responseJSON.message
        });
    });
});

$('#updateExercice').click(function (e) {
    e.preventDefault();
    updateExercice();
});

const loadAllExercice = (page, size) => {

    // Send a GET request to '/exercice/load'
    $.ajax({
        url: '/exercice/load?size='+size+'&page='+page,
        type: 'GET',
        success: function (data) {
            // replace the content of the table with the new data
            tableExercice.ajax.reload();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            // Show error message with iziToast
            console.log("I fail shamelessly with " + JSON.stringify(jqXHR.responseText));
            iziToast.warning({
                title: 'OK',
                message: jqXHR.responseText,
            });
        }
    });
};

const loadAllExercices = () => {

    // Send a GET request to '/exercice/load'
    $.ajax({
        url: '/exercice/all',
        type: 'GET',
        success: function (data) {
            tableExercice.ajax.reload();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            // Show error message with iziToast
            iziToast.warning({
                title: 'OK',
                message: jqXHR.responseJson.message,
            });
        }
    });
};

const switchButton = (typeButton) => {
    // Similar to switchButton in postecollecte.js
    if (typeButton === 'save') {
        $('#saveExercice').show();
        $('#updateExercice').hide();
    } else {
        $('#saveExercice').hide();
        $('#updateExercice').show();
    }
}

const loadExercice = (id) => {
    //Load from server
    $.ajax({'url': '/loadexercice/' + id,
        'type': 'GET',
        'success': function (data) {
            console.log("Data sent to server " + JSON.stringify(data));
            $('#idExercice').val(data.id);
            $('#code').val(data.code);
            $('#libelle').val(data.libelle);
            $('#dateDebut').val(data.dateDebut);
            $('#dateFin').val(data.dateFin);
            switchButton('update');
        },'error': function (jqXHR, textStatus, errorThrown) {
            iziToast.warning({
                title: 'OK',
                message: jqXHR.responseText,
            });
        },
    });

}

const updateExercice = () => {

    const idExercice = $('#idExercice').val();
    // Similar to updatePosteCollecte in postecollecte.js
    if(idExercice === null || idExercice === undefined){
        switchButton('save');
        return;
    }
    else{
        switchButton('update');
        $('#idExercice').val(idExercice);
    }
    // grab the data from the form
    const exercice = {
        'id': idExercice,
        'code': $('#code').val(),
        'libelle': $('#libelle').val(),
        'dateDebut': $('#dateDebut').val(),
        'dateFin': $('#dateFin').val(),
    };
    // Send a PUT request to '/exercice/update/' + id
    $.ajax({
        url: '/exercice/update/' + idExercice,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(exercice),
        success: function (data) {
            console.log("Data sent to server " + data);
            resetForm('exercice_form');
            switchButton('save');
            loadAllExercice(0,5);
            iziToast.success({
                title: 'OK',
                message: 'Modification effectué',
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log("I fail shamelessly with " + JSON.stringify(jqXHR.responseText));
            iziToast.warning({
                title: 'OK',
                message: jqXHR.responseText,
            });
        }
    });


}
const loadTodeleteExercice = (idExercice) => {

    $.ajax({
        url: '/loadexercice/' + idExercice,
        type: 'GET',
        success: function (data) {
            console.log("Data sent from server " + JSON.stringify(data));
            $('#codeHint').text(data.code);
            $('#idExerciceHidden').val(data.id);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log("I fail shamelessly with " + JSON.stringify(jqXHR.responseText));
            iziToast.warning({
                title: 'OK',
                message: jqXHR.responseText,
            });
        }
    });
    // ('#codeExerciceHidden').val(codeExercice);
};

const enableDeleteBtn = () => {

    $('#deleteActionBtn').prop('disabled', true);
    const codeExercice = $('#codeHint').text();
    console.log("Code exercice " + codeExercice);
    const codeExerciceEntered = $('#codeExerciceDelete').val();
    if(codeExercice === codeExerciceEntered)
    $('#deleteActionBtn').prop('disabled', false);
}
$('#deleteActionBtn').click(function (e) {
    e.preventDefault();
    const idExercice = $('#idExerciceHidden').val();
    $.ajax({
        url: '/exercice/delete/' + idExercice,
        type: 'DELETE',
        contentType: 'application/json',
        success: function (data) {
            $('#exampleModalToggle').fadeOut().modal('hide');
            loadAllExercice(0,5);
            iziToast.success({
                title: 'OK',
                position: 'center',
                message: 'Suppression effectuée',
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log("I fail shamelessly with " + JSON.stringify(jqXHR));
            iziToast.warning({
                title: 'OK',
                position: 'center',
                message: jqXHR.responseJSON.message,
            });
        }
    });
});
const resetForm = (formId) => {
    // Similar to resetForm in postecollecte.js
    $("#" + formId).trigger("reset");
}
//capture modal close event
$('#exampleModalToggle').on('hidden.bs.modal', function () {
    $('#codeExerciceDelete').val('');
    $('#deleteActionBtn').prop('disabled', true);
    $('#idExerciceHidden').val('');
    $('#codeExerciceHidden').val('');

});