$("#displayRapport_3").click((e) => {
    e.preventDefault();

    const reportFourForm = catchingFormFour();


    if (reportFourForm.anneeBudgetaireSelected === "" || reportFourForm.anneeBudgetaireSelected == undefined) {
        iziToast.destroy();
        iziToast.warning({
            title: '',
            position: 'center',
            message: 'Veuillez choisir correctement une année budgétaire',
        });
        return;
    }
    if (reportFourForm.monthAnneeBudgetaireSelected === " " || reportFourForm.monthAnneeBudgetaireSelected == undefined) {
        iziToast.destroy();
        iziToast.warning({
            title: '',
            position: 'center',
            message: 'Veuillez choisir correctement le mois de l\'année budgétaire budgétaire',
        });
        return;
    }

    console.log(JSON.stringify(reportFourForm));
    $("#loadingGif").css("display", "block");
    $.ajax({
        url: "/reportFour",
        contentType: 'application/json',
        type: 'POST',
        data: JSON.stringify(reportFourForm),
        success: (data) => {
            $('#summarizedReports').replaceWith(data);
            $("#loadingGif").css("display", "none");
            iziToast.destroy();
            iziToast.success({
                title: '',
                position: 'center',
                message: 'Recherche terminée.',
                position: 'topCenter',
            });
        }, error: function(jqXHR, textStatus, errorThrown) {
            $("#loadingGif").css("display", "none");
            iziToast.destroy();
            iziToast.warning({
                title: 'OK',
                message: jqXHR.responseText,
            });
        }
    });
});

const catchingFormFour = () => {

    const anneeBudgetaireSelected = $('#anneeBudget4').val();
    const monthAnneeBudgetaireSelected = $('#moisBudg4').val();

    let reportFourForm = {
        anneeBudgetaireSelected: anneeBudgetaireSelected,
        monthAnneeBudgetaireSelected: monthAnneeBudgetaireSelected,
    }
    return reportFourForm;
}


$('#anneeBudget4').change((e) => {
    e.preventDefault();
    const choosenAnneeBudgetaire = $('#anneeBudget4').val();
    queryToBuildMonthForAnneeBudgetaire(choosenAnneeBudgetaire);
});

const queryToBuildMonthForAnneeBudgetaire = (choosenAnneeBudgetaire) => {
    console.log("++++++++>>>> " + choosenAnneeBudgetaire);
    $.ajax({
        url: "/rebuildMonth/" + choosenAnneeBudgetaire,
        contentType: 'application/json',
        type: 'GET',
        success: (data) => {
            console.log(data);
            $('#monthOfAnneeBudget').replaceWith(data);
        }
    });
}

$(document.body).on('click', "#consolideGraph", (e) => {
    e.preventDefault();
    const reportFourForm = catchingFormFour();
    console.log("reportFourForm=" + JSON.stringify(reportFourForm));
    $.ajax({
        url: "/reportFourGraph",
        contentType: 'application/json',
        type: 'POST',
        data: JSON.stringify(reportFourForm),
        success: (result) => {
            var data = JSON.parse(result);
            drawSituationConsolideeRealisations(data);
            drawSituationConsolideePrevisions(data);
        }, error: function(jqXHR, textStatus, errorThrown) {

        }
    });
});

function drawSituationConsolideeRealisations(donnees) {
    var canvas = '<canvas height="300" width="400" id="cti"></canvas>';
    $('#graph-container > .col-lg-6 > .card >  #realisations').append(canvas);

    var ctx = document.getElementById('cti').getContext('2d');
    var chart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ["CTI", "CDA"],
            datasets: [
                {
                    borderColor: [
                        'rgba(255,99,132,1)',
                        'rgba(54, 162, 235, 1)'],
                    data: [donnees.ctiData, donnees.cdaData],
                    //data: [10, 40],
                    backgroundColor: ["rgba(11,127,171,0.5)", "rgba(169, 204, 227 ,0.5)"],
                    borderWidth: .0
                },
            ]
        },
        /*options: {
            scales:{
                
            },
            plugins:{
                tooltip:{
                    enabled:false
                },
                datalabels:{
                    formatter:(value, context) => {
                        const dataPoints = context.chart.data.datasets[0].data;
                        function totalSum(total, dataPoint){
                            return total+dataPoint;
                        }
                        const totalValue = dataPoints.reduce(totalSum,0);
                        const percentageValue = (value/totalValue*100).toFixed(0)+"%";
                        const str = value.toLocaleString("fr-FR",{style:"currency", currency:"BIF",minimumFractionDigits:0,maximumFractionDigits:0});
                        const display = [ `${str}`,`${percentageValue}` ];
                        return display;
                    }
                }
            }
        },*/
        options: {
            layout:{
              padding:40  
            },
            plugins: {
                legend: {
                    display: false
                },
                labels: {
                    render:"label",
                    precision:0,
                    arc:true,
                    position:"outside",
                    render:"percentage"
                },
                
            }
        },
        //plugins: [ChartDataLabels],
        //cutoutPercentage: 40,
        responsive: false,

    });
}

function drawSituationConsolideePrevisions(donnees) {
    var canvasPrevisions = '<canvas height="300" width="400" id="cda"></canvas>';
    $('#graph-container > .col-lg-6 > .card >  #previsions').append(canvasPrevisions);
    var ctx_cda = document.getElementById('cda').getContext('2d');
    var chart = new Chart(ctx_cda, {
        type: 'doughnut',
        data: {
            labels: ["CTI", "CDA"],
            datasets: [
                {
                    borderColor: [
                        'rgba(255,99,132,1)',
                        'rgba(54, 162, 235, 1)'],
                    //data:[donnees.ctiData,donnees.cdaData],
                    data: [donnees.ctiPrevision, donnees.cdaPrevision],
                    backgroundColor: ["rgba(11,127,171,0.5)", "rgba(169, 204, 227 ,0.5)"],
                    borderWidth: .0
                },
            ]
        },
        /*options: {
            scales: {

            },
            plugins: {
                tooltip: {
                    enabled: false
                },
                datalabels: {
                    formatter: (value, context) => {
                        const dataPoints = context.chart.data.datasets[0].data;
                        function totalSum(total, dataPoint) {
                            return total + dataPoint;
                        }
                        const totalValue = dataPoints.reduce(totalSum, 0);
                        const percentageValue = (value / totalValue * 100).toFixed(0) + "%";
                        const str = value.toLocaleString("fr-FR", { style: "currency", currency: "BIF", minimumFractionDigits: 0, maximumFractionDigits: 0 });
                        const display = [`${str}`, `${percentageValue}`];
                        return display;
                    }
                }
            }
        }*/
        options: {
            layout:{
              padding:40  
            },
            plugins: {
                legend: {
                    display: false
                },
                labels: {
                    render:"label",
                    precision:0,
                    arc:true,
                    position:"outside",
                    render:"percentage"
                },
                
            }
        },
        //plugins: [ChartDataLabels],
        //cutoutPercentage: 40,
        responsive: false,
    });
}



