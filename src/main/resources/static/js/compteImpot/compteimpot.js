
$('#saveCompteImpot').click(function (e) {
    e.preventDefault();
    const code = $('#code').val();
    const libelle = $('#libelle').val();
    const typeRecette = $('#typeRecette').val();
    const codePrevision = $('#code_prevision').val();
    const codeBudget = $('#code_budget').val();

    if (code === null || code === undefined || code.trim().length === 0) {
        iziToast.warning({
            title: '',
            position: "center",
            message: 'Le code est obligatoire',
        });
        return;
    }

    if (libelle === null || libelle === undefined || libelle.trim().length === 0) {
        iziToast.warning({
            title: '',
            position: "center",
            message: 'Le libéllé est obligatoire',
        });
        return;
    }

    $.post('/compteimpot',
        {
            'code': code,
            'libelle': libelle,
            'typeRecette': typeRecette,
            'codePrevision': codePrevision,
            'codeBudget': codeBudget,
        }, function () {
        }).done(() => {
        resetForm('compteimpot_form');
        loadAllCodeRecettes();
        iziToast.success({
            title: 'OK',
            position: "center",
            message: 'Enregistrement effectué',
        });
    }).fail((jqXHR, textStatus, errorThrown) => {
        iziToast.warning({
            title: 'OK',
            message: jqXHR.responseJSON.message,
            position: 'center'
        });
    });
});


function loadCompteImpot(id) {
    if (id === null || id === undefined) {
        switchButton('save');
        return;
    } else {
        switchButton('update');
        $('#idCompteImpot').val(id);

    }

    $.ajax({
        url: '/loadcompteimpot/' + id,
        type: 'GET',
        success: function (data) {

            $('#code').val(data.code);
            $('#libelle').val(data.libelle);
            $('#typeRecette').val(data.typeRecette).change();
            $('#code_prevision').val(data.codePrevision);
            $('#code_budget').val(data.codeBudget);
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

$('#updateCompteImpot').click(function (e) {
    e.preventDefault();
    const idToUpdate = $('#idCompteImpot').val();
    const code = $('#code').val();
    const libelle = $('#libelle').val();
    const typeRectte = $('#typeRecette').val();
    const codePrevision = $('#code_prevision').val();
    const codeBudget = $('#code_budget').val();

    if (code === null || code === undefined || code.trim().length === 0) {
        iziToast.warning({
            title: '',
            position:"center",
            message: 'Le code est obligatoire',
        });
        return;
    }

    if (libelle === null || libelle === undefined || libelle.trim().length === 0) {
        iziToast.warning({
            title: '',
            position:"center",
            message: 'Le libéllé est obligatoire',
        });
        return;
    }


    $.ajax({
        url: '/compteimpot/update/' + idToUpdate,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({
            'id': idToUpdate,
            'code': code,
            'libelle': libelle,
            'typeRecette': typeRectte,
            'codePrevision': $('#code_prevision').val(),
            'codeBudget' : $('#code_budget').val()
        }),
        success: function (data) {
            // clean a form
            resetForm('compteimpot_form');
            switchButton('save');
            //Reload all compte impots
            loadAllCodeRecettes();
            iziToast.success({
                title: 'OK',
                position:"center",
                message: 'Modification effectuée',
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            iziToast.warning({
                title: '',
                position:"center",
                message: jqXHR.responseJSON.message
            });
        }
    });

});

const loadAllCodeRecettes = () => {
    $.ajax({
        url: '/codeRecettes/loadAll',
        type: 'GET',
        contentType: 'application/json',
        success: function (data) {
            tableCodeRecettes.ajax.reload();
        },
        error: function (jqXHR, textStatus, errorThrown) {
        }
    });
};

const resetForm = (formId) => {
    $("#" + formId).trigger("reset");
    switchButton('save');
}

$("#resetCompteImpot").click((e) => {
    e.preventDefault();
    resetForm('compteimpot_form');
});

//Hide and show Mise à jour or Enregistrer
const switchButton = (typeButton) => {

    if (typeButton === 'update') {
        $('#updateCompteImpot').css('display','inline-flex').addClass('custom-btn');
        $('#saveCompteImpot').css('display', 'none');
    } else {
        $('#updateCompteImpot').css('display', 'none');
        $('#saveCompteImpot').css('display','inline-flex').addClass('custom-btn');
    }
}



const loadPage = (page, size) => {
    if (size === undefined || size === null) {
        size = 10;
    }
    if (page === undefined || page === null) {
        page = 0;
    }
    const url = '/compteimpot/load?page=' + page + '&size=' + size;
    $.ajax({
        url: url,
        type: 'GET',
        contentType: 'application/json',
        success: function (data) {
            $('#all_compte_impot').replaceWith(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
        }
    })
}

//loadTodeleteExercice
const loadToDeleteCompteImpot = (idCompteImpot) => {
    $.ajax({
        url: '/loadcompteimpot/' + idCompteImpot,
        type: 'GET',
        success: function (data) {
            $('#codeHint').text(data.code);
            $('#id_compte_impot_hidden').val(data.id);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            iziToast.warning({
                title: 'OK',
                position:"center",
                message: jqXHR.responseText,
            });
        }
    });
}

//enableDeleteBtn
const enableDeleteBtn = () => {
    $('#delete_action_btn').prop('disabled', true);
    const codeCompteImpot = $('#codeHint').text();
    const codeCompteImpotEntered = $('#code_compte_impot_delete').val();

    if (codeCompteImpot === codeCompteImpotEntered) {
        $('#delete_action_btn').prop('disabled', false);
    }
}

$('#delete_action_btn').click(function (e) {
    e.preventDefault();
    const idToDelete = $('#id_compte_impot_hidden').val();
    $.ajax({
        url: '/compteimpot/delete/' + idToDelete,
        type: 'DELETE',
        success: function (data) {
            $('#exampleModalToggle').fadeOut().modal('hide');
            loadPage(0, 5);
            iziToast.success({
                title: 'OK',
                position:"center",
                message: 'Suppression effectuée',
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            iziToast.warning({
                title: 'OK',
                position:"center",
                message: jqXHR.responseText,
            });
        }
    });
});

$('#exampleModalToggle').on('hidden.bs.modal', function () {
    $('#code_compte_impot_delete').val('');
    $('#deleteActionBtn').prop('disabled', true);
    $('#id_compte_impot_hidden').val('');
    $('#code_compte_impot_hidden').val('');

});

$('#uploadCompteImpot').click((e) => {

    const realUploadFile = document.getElementById('file_upload');
    realUploadFile.click();
});