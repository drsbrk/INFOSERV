$("#displayRapport_2").click((e) => {
    e.preventDefault();
    const reportTwoForm = catchingFormTwo();
$("#loadingGif").css("display", "block");

    if (reportTwoForm.startingDate === "" || reportTwoForm.endingDate === "" || reportTwoForm.startingDate > reportTwoForm.endingDate) { 
        iziToast.destroy();
        iziToast.warning({
            title: '',
            position: 'center',
            message: 'Veuillez choisir correctement vos dates',
        });
        return;
    } 

    console.log(JSON.stringify(reportTwoForm));
    $.ajax({
        url: "/reportTwo",
        contentType: 'application/json',
        type: 'POST',
        data: JSON.stringify(reportTwoForm),
        success: (data) => {
            $('#incomesReports').replaceWith(data);
            iziToast.destroy();
            iziToast.success({
                title: '',
                position: 'center',
                message: 'Recherche terminée.',
            });
        }, error: function (jqXHR, textStatus, errorThrown) {
            $("#loadingGif").css("display", "none");
            iziToast.destroy();
            iziToast.warning({
                title: 'OK',
                message: jqXHR.responseText,
            });
        },
        complete:function(){
            $("#loadingGif").css("display", "none");
        }
    });
});

const catchingFormTwo = (page = 1, size = 5) => {
    size = $('#changePageSizeR2').val();
    const startingDateFromForm = $('#startingDate').val();
    const endingDateFromForm = $('#endingDate').val();
    const selectedCentreDeCollecte = $('#centreDeCollectes').val();
    const selectedTypeRecettes = $('#typesRecettesRap2').val();
    const selectedDevises = $('#devisesRap2').val();

    console.log(" selectedDevises " + selectedDevises);
    let posteCollectesSelectedFromForm = [];
    let typeRecettesSelectedFromForm = [];
    let deviseSelectedFromForm = [];
    for (var i = 0; i < selectedCentreDeCollecte.length; i++) {
        console.log(" iii " + selectedCentreDeCollecte[i]);
        if (selectedCentreDeCollecte[i].trim().length > 0)
            posteCollectesSelectedFromForm.push(selectedCentreDeCollecte[i]);
    }

    for (var i = 0; i < selectedTypeRecettes.length; i++) {

        console.log(" typ iii " + selectedTypeRecettes[i]);
         if(selectedTypeRecettes[i] != "")
        if(selectedTypeRecettes[i].trim().length > 0)
        typeRecettesSelectedFromForm.push(selectedTypeRecettes[i]);
    }

    for (var i = 0; i < selectedDevises.length; i++) {

        if(selectedDevises[i] != "")
        if(selectedDevises[i].trim().length > 0)
        deviseSelectedFromForm.push(selectedDevises[i]);
    }

    let reportTwoForm = {
        startingDate: startingDateFromForm,
        endingDate: endingDateFromForm,
        centreDeCollectesSelected: posteCollectesSelectedFromForm,
        selectedTypeRecette: typeRecettesSelectedFromForm,
        devisesSelected: deviseSelectedFromForm,
        pageIndex: page,
        size: size
    }
    console.log("Object to be sent to server: "+JSON.stringify(reportTwoForm));
    return reportTwoForm;
}

const loadMoreReport2 = (pageNum, pageSize) => {
    event.preventDefault();
    let reportTwoForm = catchingFormTwo(pageNum, pageSize);
    queryReportTwo(reportTwoForm);

}

const queryReportTwo = (reportTwoForm) => {
    
    $.ajax({
        url: "/reportTwo",
        contentType: 'application/json',
        type: 'POST',
        data: JSON.stringify(reportTwoForm),
        success: (data) => {
            $('#incomesReports').replaceWith(data);
            iziToast.destroy();
            iziToast.success({
                title: '',
                position: 'center',
                message: 'Recherche terminé.',
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            $("#loadingGif").css("display", "none");
            iziToast.destroy();
            iziToast.warning({
                title: 'OK',
                message: jqXHR.responseText,
            });
        },
        complete:function(){
            $("#loadingGif").css("display", "none");
        }
    });
};


$('#changePageSizeR2').change((e) => {
    e.preventDefault();
    const pageSize = $('#changePageSizeR2').val();
    const reportTwoForm = catchingFormTwo(1, pageSize);
    queryReportTwo(reportTwoForm);
});

const queryResetReportTwo = () => {
    $.ajax({
        url: "/reportTwoReset",
        contentType: 'application/json',
        type: 'GET',
        success: (data) => {
            $('#incomesReports').replaceWith(data);
        },
        complete: ()=>{
            $("#loadingGif").css('display', 'none');
        },
        error:function(){
            $("#loadingGif").css("display","none");
        }
    });
};

$('#resetReportTwo').click((e) => {
    e.preventDefault();
    $('#centreDeCollectes').val('').trigger('change');
    $('#typesRecettesRap2').val('').trigger('change');
    $('#devisesRap2').val('').trigger('change');
    $('#startingDate').val('').trigger('change');
    $('#endingDate').val('').trigger('change');
    $("#incomesReports").hide();
    //queryResetReportTwo();
});


