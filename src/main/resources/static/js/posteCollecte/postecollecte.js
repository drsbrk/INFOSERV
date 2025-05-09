
$('#savePosteCollecte').click(function (e) {
    e.preventDefault();
    const code = $('#code').val();
    const libelle = $('#libelle').val();

    if (code === null || code === undefined || code.trim().length === 0) {
        iziToast.warning({
            title: '',
            message: 'Le code est obligatoire',
        });
        return;
    }

    if (libelle === null || libelle === undefined || libelle.trim().length === 0) {
        iziToast.warning({
            title: '',
            message: 'Le libéllé est obligatoire',
        });
        return;
    }


    $.post('/postecollecte',
        {
            'code': code,
            'libelle': libelle,
        }, function (data) {
            console.log("Data sent to server " + data);
        }).done((data) => {
        console.log("I'm done with this " + data);
        resetForm('postecollecte_form');
        loadPage(0,5);
        iziToast.success({
            title: 'OK',
            message: 'Enregistrement effectué',
        });
    }).fail((jqXHR, textStatus, errorThrown) => {
        console.log("I fail shamelessly with " + JSON.stringify(errorThrown));
        console.log("2 I fail shamelessly with " + JSON.stringify(jqXHR));
        iziToast.warning({
            title: 'OK',
            message: jqXHR.responseText,
            position: 'center'
        });
    });
});

function loadPosteCollecte(id) {
    
    if (id === null || id === undefined) {
        switchButton('save');
        return;
    } else {
        switchButton('update');
        $('#idPosteCollecte').val(id);

        console.log("id " + id);
    }

    $.ajax({
        url: '/loadpostecollecte/' + id,
        type: 'GET',
        success: function (data) {
            console.log("Data sent to server " + JSON.stringify(data));
            $('#code').val(data.code);
            $('#libelle').val(data.libelle);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            iziToast.warning({
                title: 'OK',
                message: jqXHR.responseText,
                position: 'center'
            });
        }
    });
}

$('#updatePosteCollecte').click(function (e) {
    e.preventDefault();
    const idToUpdate = $('#idPosteCollecte').val();
    const code = $('#code').val();
    const libelle = $('#libelle').val();

    if (code === null || code === undefined || code.trim().length === 0) {
        iziToast.warning({
            title: '',
            position : "center",
            message: 'Le code est obligatoire',
        });
        return;
    }

    if (libelle === null || libelle === undefined || libelle.trim().length === 0) {
        iziToast.warning({
            title: '',
            position : "center",
            message: 'Le libéllé est obligatoire',
        });
        return;
    }

    $.ajax({
        url: '/postecollecte/update/' + idToUpdate,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({
            'id': idToUpdate,
            'code': code,
            'libelle': libelle,
        }),
        success: function (data) {
            console.log("Data sent to server " + data);
            // clean a form
            resetForm('postecollecte_form');
            switchButton('save');
            //Reload all poste de collectes
            loadPage(0,5);
            iziToast.success({
                title: 'OK',
                message: 'Modification effectuée',
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log("1 I fail shamelessly with " + JSON.stringify(jqXHR));
            console.log("1 I fail shamelessly with " + JSON.stringify(jqXHR.responseText.message));
            console.log("2 I fail shamelessly with " + jqXHR.responseText.message);
            iziToast.warning({
                title: '',
                position : "center",
                message: jqXHR.responseJSON.message
            });
        }
    });

});

const loadAllPosteCollecte = () => {
    $.ajax({
        url: '/postecollecte/load',
        type: 'GET',
        contentType: 'application/json',
        success: function (data) {
            console.log("Data sent to server " + data);
            $('#all_poste_collecte').replaceWith(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log("2 I fail shamelessly with " + JSON.stringify(jqXHR));
        }
    });
};

//Hide and show Mise à jour or Enregistrer
const switchButton = (typeButton) => {

    if (typeButton === 'update') {
        $('#updatePosteCollecte').css('display', 'block');
        $('#savePosteCollecte').css('display', 'none');
    } else {
        $('#updatePosteCollecte').css('display', 'none');
        $('#savePosteCollecte').css('display', 'block');
    }
}

const resetForm = (formId) => {
    $("#" + formId).trigger("reset");
}

 const loadPage = (page,size) => {
    if(size === undefined || size === null){
        size = 5;
    }
    if(page === undefined || page === null){
        page = 0;
    }
    const url = '/postecollecte/load?page='+page+'&size='+size;
    console.log("URL " + url);
    $.ajax({
        url: url,
        type: 'GET',
        contentType: 'application/json',
        success: function (data) {
            console.log("Data sent to server " + JSON.stringify(data));
            $('#all_poste_collecte').replaceWith(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log("2 I fail shamelessly with " + JSON.stringify(jqXHR));
        }
    })
}

const loadToDeletePosteCollecte = (idPosteCollecte) => {
    $.ajax({
        url: '/loadpostecollecte/' + idPosteCollecte,
        type: 'GET',
        success: function (data) {
            $('#codeHint').text(data.code);
            $('#id_poste_collecte_hidden').val(data.id);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            iziToast.warning({
                title: 'OK',
                message: jqXHR.responseText,
            });
        }
    });
}

const enableDeleteBtn = () => {
    $('#delete_action_btn').prop('disabled', true);
    const codePosteCollecte = $('#codeHint').text();
    const codePosteCollecteEntered = $('#code_poste_collecte_delete').val();

    if (codePosteCollecte === codePosteCollecteEntered) {
        $('#delete_action_btn').prop('disabled', false);
    }
}

$('#delete_action_btn').click(function (e) {
    e.preventDefault();
    const idToDelete = $('#id_poste_collecte_hidden').val();
    $.ajax({
        url: '/postecollecte/delete/' + idToDelete,
        type: 'DELETE',
        success: function (data) {
            $('#exampleModalToggle').fadeOut().modal('hide');
            loadPage(0, 5);
            iziToast.success({
                title: 'OK',
                message: 'Suppression effectuée',
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            iziToast.warning({
                title: 'OK',
                message: jqXHR.responseText,
            });
        }
    });
});

$('#exampleModalToggle').on('hidden.bs.modal', function () {
    $('#code_poste_collecte_delete').val('');
    $('#deleteActionBtn').prop('disabled', true);
    $('#id_poste_collecte_hidden').val('');
    $('#code_poste_collecte_hidden').val('');
});
