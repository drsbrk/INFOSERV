
$('#saveTypeFichierBase').click(function (e) {

    e.preventDefault();
    if ($('#type_fichier_base').val() === 'PREVISION') {
        const fileNameExist = checkPrevisionFileName();
            console.log(" je trouve ===> <=== " + fileNameExist);
            // if(fileNameExist === undefined || fileNameExist === false){
            //     submitForm();
            // } else
            //     openDialog();
    }

});

const openDialog = () => {
    $('#exampleModalToggle').modal('show');
}


const checkPrevisionFileName = () => {

    formData = buildForm();
    let fileNameExist = false;
    const utilitaireDto = formData.get("utilitaireJson");
    $.ajax({
        url: '/checkPrevisionFile',
        type: 'POST',
        contentType: 'application/json',
        data: utilitaireDto,
        success: (data) => {
            console.log(" SF ====> ||| " + JSON.stringify(data));
            console.log(" NTSF ====> ||| " + data);

            if(data === "found")
               openDialog();
            else
                submitForm();
        },
        error: (jqXHR, textStatus, errorThrown) => {
            iziToast.warning({
                title: 'OK',
                message: jqXHR.responseJSON.message,
                position: 'center'
            });
        }

    });
    return fileNameExist;
}

const buildForm = () => {
    // const revisionVal = $('#revision_check').val();

    const fileName = $('#file_upload')[0].files[0];
    const filenameWithExt = $('#file_name_display').val();

    const utilitaire = {
        "typeFichierBase": $('#type_fichier_base').val(),
        "exerciceId": $('#exercice_select').val(),
        "revision": $('#revision_check').is(":checked"),
        "previsionFileName": filenameWithExt,
    };

    console.log(" utilitaire === > " + JSON.stringify(utilitaire));


    if (utilitaire.typeFichierBase === null || utilitaire.typeFichierBase === undefined || utilitaire.typeFichierBase === "0") {
        iziToast.warning({
            title: '',
            position: 'center',
            message: 'Le type de fichier de base est obligatoire',
        });
        return;
    }


    if (filenameWithExt.split(".")[1] !== "xlsx") {
        iziToast.warning({
            title: '',
            position: 'center',
            message: 'Le fichier doit ètre au format excel avec une extension xlsx',
        });
        return;
    }


    let formData = new FormData();
    formData.append("utilitaireJson", JSON.stringify(utilitaire));
    formData.append('file', fileName);
    console.log(" ====> " + formData.get("utilitaireJson"));
    return formData;
}

const submitForm = () => {

    formData = buildForm();
    $.ajax({
        url: '/loadFichierDeBaseByFile',
        type: 'POST',
        contentType: false,
        processData: false,
        data: formData,
        success: (data) => {
            resetForm('utilitaire_form');
            changeOrHideComponent();
            iziToast.success({
                title: 'OK',
                message: data,
                position: 'center'
            });
        },
        error: (jqXHR, textStatus, errorThrown) => {
            iziToast.warning({
                title: 'OK',
                // message: 'Erreur',
                message: jqXHR.responseJSON.message,
                position: 'center'
            });
        }
    });
};

$('#submitSameFIle').click(function (e) {
    $('#exampleModalToggle').modal('hide');
    submitForm();
});

function changeOrHideComponent() {

    if ($('#type_fichier_base').val() === 'PREVISION') {
        $('#exercice_select_div').show();
        $('#checkbox_revision_div').show();
        $('#label_revision_div').show();
    } else {
        $('#exercice_select_div').hide();
        $('#checkbox_revision_div').hide();
        $('#label_revision_div').hide();
    }
}

$('#type_fichier_base').change(function (e) {
    //Show exercice-select
    changeOrHideComponent();
});


const resetForm = (formId) => {
    $("#" + formId).trigger("reset");
}

$('#resetFichierBase').click( function (e) {

    e.preventDefault();
    resetForm('utilitaire_form');
    $('#type_fichier_base').val('0');
    changeOrHideComponent();
})


//loadTodeleteExercice

//enableDeleteBtn


$('#exampleModalToggle').on('hidden.bs.modal', function () {
    $('#code_prevision_delete').val('');
    $('#deleteActionBtn').prop('disabled', true);
    $('#id_prevision_hidden').val('');
    $('#code_prevision_hidden').val('');

});

$('#uploadFichierBase').click((e) => {

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
        $('#file_name_display').val(fileUpload.name);
    }
});


