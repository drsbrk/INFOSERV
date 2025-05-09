$('#savePrevision').click(function (e) {
    e.preventDefault();
    const prevision = {
        "selectedCodeRecetteId": $('#code_prevision').val(),
        "selectedAnneeBudgetId": $('#anneeBudget').val(),
        "selectedMonth": $('#moisPrev').val(),
        "montant": $('#montantPrev').val(),
    };

    if (prevision.selectedCodeRecetteId === null || prevision.selectedCodeRecetteId === '0' || prevision.selectedCodeRecetteId.trim().length === 0) {
        iziToast.warning({
            title: '',
            position: 'center',
            message: 'Le code recette est obligatoire',
        });
        return;
    }

    if (prevision.selectedAnneeBudgetId === null || prevision.selectedAnneeBudgetId === '0' || prevision.selectedAnneeBudgetId.trim().length === 0) {
        iziToast.warning({
            title: '',
            position: 'center',
            message: "L'année budgétaire est obligatoire",
        });
        return;
    }
    if (prevision.selectedMonth === null || prevision.selectedMonth === undefined || prevision.selectedMonth === '0') {
        iziToast.warning({
            title: '',
            position: 'center',
            message: 'Le mois de la prévision est obligatoire',
        });
        return;
    }

    if (prevision.montant === null || prevision.montant === undefined || prevision.montant === '') {
        iziToast.warning({
            title: '',
            position: 'center',
            message: 'Le montant de la prévision est obligatoire',
        });
        return;
    }

    $.ajax({
        url: '/prevision',
        contentType: 'application/json',
        type: 'POST',
        data: JSON.stringify(prevision),
        success: (data) => {
            resetForm('prevision_form');
            loadPage(0, 5);
            iziToast.success({
                title: 'OK',
                position: 'center',
                message: 'Enregistrement effectué',
            });
        },
        error: (jqXHR, textStatus, errorThrown) => {
            iziToast.warning({
                title: 'OK',
                message: jqXHR.responseJSON.message,
                position: 'center'
            });
        }
    });
});

function loadPrevision(id) {
    if (id === null || id === undefined) {
        switchButton('save');
        return;
    } else {
        switchButton('update');
        $('#idPrevision').val(id);

        console.log("id " + id);
    }

    $.ajax({
        url: '/loadprevision/' + id,
        type: 'GET',
        success: function (data) {
            $('#idPrevision').val(data.id);
            $('#code_prevision').val(data.codeRecette.id).change();
            $('#anneeBudget').val(data.anneeBudgetaire.id).change();
            $('#moisPrev').val(data.previsionMonth).change();
            $('#montantPrev').val(data.montant).prop('readonly', true);
            $('#displayPrevisionModalToggle').fadeOut().modal('hide');

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

$('#updatePrevision').click(function (e) {
    e.preventDefault();
    const idToUpdate = $('#idPrevision').val();


    const prevision = {
        "id": idToUpdate,
        "selectedCodeRecetteId": $('#code_prevision').val(),
        "selectedAnneeBudgetId": $('#anneeBudget').val(),
        "selectedMonth": $('#moisPrev').val(),
        "montant": $('#montantPrev').val(),
        "montantRevisE": $('#montantRevisE').val(),
    };

    if (prevision.selectedCodeRecetteId === null || prevision.selectedCodeRecetteId === '0' || prevision.selectedCodeRecetteId.trim().length === 0) {
        iziToast.warning({
            title: '',
            position: 'center',
            message: 'Le code recette est obligatoire',
        });
        return;
    }

    if (prevision.selectedAnneeBudgetId === null || prevision.selectedAnneeBudgetId === '0' || prevision.selectedAnneeBudgetId.trim().length === 0) {
        iziToast.warning({
            title: '',
            position: 'center',
            message: 'L\'année budgétaire est obligatoire',
        });
        return;
    }
    if (prevision.selectedMonth === null || prevision.selectedMonth === undefined || prevision.selectedMonth === '0') {
        iziToast.warning({
            title: '',
            position: 'center',
            message: 'Le mois de la prévision est obligatoire',
        });
        return;
    }

    if (prevision.montantRevisE === null || prevision.montantRevisE === undefined || prevision.montantRevisE === '') {
        iziToast.warning({
            title: '',
            position: 'center',
            message: 'Le montant révisé de la prévision est obligatoire',
        });
        return;
    }

    $.ajax({
        url: '/prevision/update/' + idToUpdate,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(prevision),
        success: function (data) {
            // clean a form
            resetForm('prevision_form');
            switchButton('save');
            //Reload all prevision
            // loadPage(0, 5);
            loadAllPrevision();
            iziToast.success({
                title: 'OK',
                position: 'center',
                message: 'Modification effectuée',
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            iziToast.warning({
                title: '',
                position: 'center',
                message: jqXHR.responseJSON.message
            });
        }
    });

});

$("#anneBudgetaireTable").change((e) => {
    const selectedAnneeBudgetaire = $("#anneBudgetaireTable").val();
    if (selectedAnneeBudgetaire !== 0 && selectedAnneeBudgetaire !== "") {
        let fetchDataUri = '/prevision/loadByAnneeBudgetaire/' + selectedAnneeBudgetaire;
        tablePrev.ajax.url(fetchDataUri).load();
    } else {
        iziToast.warning({
            title: '',
            position: 'center   ',
            message: 'Veuillez choisir une année budgétaire valide'
        });
    }
});

const loadAllPrevision = () => {

    const anneeBudgetaire = $('#anneBudgetaireTable').val();
    $.ajax({
        url: '/prevision/loadByAnneeBudgetaire/' + anneeBudgetaire,
        type: 'GET',
        contentType: 'application/json',
        success: function (data) {
            tablePrev.ajax.reload();
            console.log(" data " + JSON.stringify(data));
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log("jqXHR " + JSON.stringify(jqXHR));
        }
    });
};

//Hide and show Mise à jour or Enregistrer
const switchButton = (typeButton) => {

    if (typeButton === 'update') {
        $('#updatePrevision').css('display', 'block');
        $('#savePrevision').css('display', 'none');
    } else {
        $('#updatePrevision').css('display', 'none');
        $('#savePrevision').css('display', 'block');
    }
}

const resetForm = (formId) => {
    $("#" + formId).trigger("reset");
}

const loadPage = (page, size) => {
    if (size === undefined || size === null) {
        size = 5;
    }
    if (page === undefined || page === null) {
        page = 0;
    }
    const url = '/prevision/load?page=' + page + '&size=' + size;
    $.ajax({
        url: url,
        type: 'GET',
        contentType: 'application/json',
        success: function (data) {
            $('#all_prevision').replaceWith(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log("2 I fail shamelessly with " + JSON.stringify(jqXHR));
        }
    })
}

//loadTodeleteExercice
const loadToDeletePrevision = (idPrevision) => {
    $('#delete_action_btn').prop('disabled', false);
    $('#displayPrevisionModalToggle').fadeOut().modal('hide');
    $('#codeHint').text(idPrevision);
    $('#code_prevision_hidden').val(idPrevision);
    $('#id_prevision_hidden').val(idPrevision);
    $('#delete_action_btn').prop('disabled', true);

};


//loadToDisplayDetails
const loadToDisplayPrevision = (idPrevision) => {

    const selectedAnneeBudgetaire = $("#anneBudgetaireTable").val();;
    if(selectedAnneeBudgetaire !== undefined && selectedAnneeBudgetaire !== '0'){
        anneeBudgetaire = selectedAnneeBudgetaire;
    }
    console.log(" ----> id prevision " + idPrevision + " And anneeBudgetaire " + anneeBudgetaire);
    $.ajax({
        url: '/loadAllPrevisionByCodeRecetteAndAnneeBudgetaire/' + idPrevision + '/' + anneeBudgetaire,
        type: 'GET',
        success: function (data) {
            if (data != null)
                $('#previsionDetail').replaceWith(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            iziToast.warning({
                title: 'OK',
                position: 'center',
                message: jqXHR.responseText,
            });
        }
    });
}

//enableDeleteBtn
const enableDeleteBtn = () => {
    $('#delete_action_btn').prop('disabled', true);
    const codePrevision = $('#code_prevision_hidden').val();
    const codePrevisionEntered = $('#code_prevision_delete').val();
    console.log("Captured codePrevisionEntered " + codePrevisionEntered + " codePrevision " + codePrevision);

    if (codePrevision === codePrevisionEntered) {
        $('#delete_action_btn').prop('disabled', false);
    } else {
        console.log("not equal not equal")
    }
}

$('#delete_action_btn').click(function (e) {
    e.preventDefault();
    const idToDelete = $('#id_prevision_hidden').val();
    $.ajax({
        url: '/prevision/delete/' + idToDelete,
        type: 'DELETE',
        success: function (data) {
            console.log("Data sent to server " + data);
            loadAllPrevision();
            $('#exampleModalToggle').fadeOut().modal('hide');
            iziToast.success({
                title: 'OK',
                message: 'Suppression effectuée',
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
});

$('#exampleModalToggle').on('hidden.bs.modal', function () {
    $('#code_prevision_delete').val('');
    $('#deleteActionBtn').prop('disabled', true);
    $('#id_prevision_hidden').val('');
    $('#code_prevision_hidden').val('');

});

$('#uploadPrevision').click((e) => {

    const realUploadFile = document.getElementById('file_upload');
    realUploadFile.click();
});

$('#file_upload').change((e) => {
    const fileUpload = $('#file_upload')[0].files[0];
    if (fileUpload === null || fileUpload === undefined) {
        iziToast.warning({
            title: '',
            message: 'Le fichier est obligatoire',
        });
        return;
    } else {
        const formData = new FormData();
        formData.append('file', fileUpload);
        console.log(" ====> ====> " + fileUpload.name.split('.')[0]);
        $('#file_name_display').val(fileUpload.name.split('.')[0]);
    }
});


