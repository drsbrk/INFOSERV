const hideByDefault = () => {
    $('#r1').removeClass('active');
    $('#r2').removeClass('active');
    $('#r3').removeClass('active');
    $('#r4').removeClass('active');
    $('#r5').removeClass('active');

    $('#rapport1').hide();
    $('#rapport2').hide();
    $('#rapport3').hide();
    $('#rapport4').hide();
    $('#rapport5').hide();
}

const catchingFormOne = (page = 1, size = 5) => {
    size = $('#changePageSize_1').val();
    const selectedDateFromForm = $('#choosenDate').val();
    const selectedCentreDeCollecte = $('#posteCollectes').val();
    const selectedTypeRecettes = $('#typesRecettes').val();
    const selectedDevises = $('#devises').val();

    let posteCollectesSelectedFromForm = [];
    let typeRecettesSelectedFromForm = [];
    let deviseSelectedFromForm = [];
    for (var i = 0; i < selectedCentreDeCollecte.length; i++) {
        console.log(" iii " + selectedCentreDeCollecte[i]);
        posteCollectesSelectedFromForm.push(selectedCentreDeCollecte[i]);
    }

    for (var i = 0; i < selectedTypeRecettes.length; i++) {
        console.log(" typ iii " + selectedTypeRecettes[i]);
        typeRecettesSelectedFromForm.push(selectedTypeRecettes[i]);
    }

    for (var i = 0; i < selectedDevises.length; i++) {
        console.log(" dev iii " + selectedDevises[i]);
        deviseSelectedFromForm.push(selectedDevises[i]);
    }

    let reportOneForm = {
        selectedDate: selectedDateFromForm,
        centreCollectesSelected: posteCollectesSelectedFromForm,
        typeRecettesSelected: typeRecettesSelectedFromForm,
        devisesSelected: deviseSelectedFromForm,
        page: page,
        size: size
    }
    return reportOneForm;
}


$("#displayRecetteObr_1").click((e) => {
    e.preventDefault();
    $("#loadingGif").css("display", "block");
    const reportOneForm = catchingFormOne();

    if (reportOneForm.selectedDate === undefined || reportOneForm.selectedDate === "") { // Ce code ne marche pas car le msg d'erreur ne s'affiche pas
        iziToast.warning({
            title: '',
            position: 'center',
            message: 'Veuillez sélectionner une date.',
        });
        return;
    } else {
        console.log("Selected date = " + reportOneForm.selectedDate);
    }
    console.log("SUBMITTED="+JSON.stringify(reportOneForm));
    $("#searchDiv").show();
    $("#ExcelButton").show();
    queryReportOne(reportOneForm);
});

$('.rapportRecette').click((e) => {
    const elementId = e.target.id;

    switch (elementId) {
        case 'r1':
            activateTab('r1', 'rapport1');
            break;
        case 'r2':
            activateTab('r2', 'rapport2');
            break;
        case 'r3':
            activateTab('r3', 'rapport3');
            break;
        case 'r4':
            activateTab('r4', 'rapport4');
            break;
        case 'r5':
            activateTab('r5', 'rapport5');
            break;
    }
});

const activateTab = (activeTab, activeDiv) => {
    hideByDefault();
    $('#' + activeTab).addClass('active', 10000);
    $('#' + activeDiv).show();
}

const loadMoreReport = (anneeBudget, pageNum, pageSize) => {
    event.preventDefault();
    let reportOneForm = catchingFormOne(pageNum, pageSize);

    $.ajax({
        url: "/reportOne",
        contentType: 'application/json',
        type: 'POST',
        data: JSON.stringify(reportOneForm),
        success: (data) => {
            $('#dailyReport_1').replaceWith(data);
            iziToast.success({
                title: '',
                position: 'center',
                message: 'Recherche terminée.',
            });
        }
    });
    //queryReportOne(reportOneForm);
}

const queryReportOne = (reportOneForm) => {
    $.ajax({
        url: "/reportOne",
        contentType: 'application/json',
        type: 'POST',
        data: JSON.stringify(reportOneForm),
        success: (data) => {
            $('#dailyReport_1').replaceWith(data);
            iziToast.destroy();
            iziToast.success({
                title: '',
                position: 'center',
                message: 'Recherche terminée.',
            });
        },
        complete: function() {
            $("#loadingGif").css("display", "none");
        },
        error: function() {
            $("#loadingGif").css("display", "none");
        }
    });
};

$('#changePageSize_1').change((e) => {
    e.preventDefault();
    const pageSize = $('#changePageSize_1').val();
    const reportOneForm = catchingFormOne(1, pageSize);
    queryReportOne(reportOneForm);
});

const queryResetReportOne = (hideFlag) => {
    
    hideFlag ? $("#reportOneResult").hide() : $("#reportOneResult").show();
    hideFlag ? $("#searchDiv").hide() : $("#searchDiv").show();
    hideFlag ? $("#ExcelButton").hide() : $("#ExcelButton").show();
    hideFlag ? $("#h5").hide() : $("#h5").show();
    hideFlag ? $("#resultPagination").hide() : $("#resultPagination").show();
    
};

$('#resetPrevision_1').click((e) => {
    e.preventDefault();
    $("#recette_obr_form_1")[0].reset();
    queryResetReportOne(true);
});
