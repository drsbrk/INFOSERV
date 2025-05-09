const catchingTableauOne = (page = 1, size = 5) => {
    const selectedDateFromForm = $('#choosenDate').val();
    const selectedCentreDeCollecte = $('#centreCollectes').val();
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

    let tableauOne = {
        selectedDate: selectedDateFromForm,
        centreCollectesSelected: posteCollectesSelectedFromForm,
        typesRecettesSelected: typeRecettesSelectedFromForm,
        devisesSelected: deviseSelectedFromForm,
        page: page,
        size: size
    }
    console.log(tableauOne);
    return tableauOne;
}


$("#displayRecetteObr").click((e) => {
    e.preventDefault();
    $("#tbReport1").remove();
    $("#tbReport1USD").remove();
$("#loadingGif").css("display","block");
    const tableauOneForm = catchingTableauOne();
    console.log("Object="+tableauOneForm);

    if (tableauOneForm.selectedDate === undefined || tableauOneForm.selectedDate === "") {  
        iziToast.warning({
            title: '',
            position: 'center',
            message: 'Veuillez sélectionner une date.',
        });
        $("#loadingGif").css("display","none");
        return;
    } else {
        console.log("Selected date = " + tableauOneForm.selectedDate);
    }
    console.log(JSON.stringify(tableauOneForm));

    queryReportOne(tableauOneForm); // Ici c'est l'envoi de la requête AJAX vers SPRING BOOT
});

const queryReportOne = (tableauOneForm) => {    
    $.ajax({
        url: "/tb_report1",
        contentType: 'application/json',
        type: 'POST',
        beforeSend: function (request, settings) {
            start_time = new Date().getTime();
        },
        data: JSON.stringify(tableauOneForm),

        success:function(result){
            var data=JSON.parse(result);
            
            var codeCentre=data[0].codeCentre;
            var jMoinsDeux=data[0].jMoinsDeux;
            var jMoinsUn=data[0].jMoinsUn;;
            drawMultipleLineChart(codeCentre,jMoinsDeux,jMoinsUn);
            
            var codeCentreUSD=data[1].codeCentre;
            var jMoinsDeuxUSD=data[1].jMoinsDeux;
            var jMoinsUnUSD=data[1].jMoinsUn;;
            drawMultipleLineChartUSD(codeCentreUSD,jMoinsDeuxUSD,jMoinsUnUSD);
            let request_time = new Date().getTime() - start_time;
            console.log("Request time = "+request_time/1000+" sec");
        },
        complete:function(){
            $("#loadingGif").css("display","none");
        },
        error:function(){
            $("#loadingGif").css("display","none");
        }
    });
};


function drawMultipleLineChart(c,jMoins2,jMoins1){
var canvas='<canvas id="tbReport1"></canvas>';
$('#graph-containerbif > .row  > #bif').append(canvas);
    var ctx= document.getElementById('tbReport1').getContext('2d');
    var chart = new Chart(ctx, {
        // Type of chart we want
        type:'bar',
        data:{
            labels:c,
            datasets:[
                {
                    label:'Situation journalière en BIF J-2',
                    backgroundColor:'rgba(255,99,132,0.5)',
                    borderColor:'rgba(255,99,132,0.5)',                   
                    data:jMoins2
                },
                {
                    label:'Situation journalière en BIF J-1',
                    backgroundColor:'rgba(25,102,110,0.5)',
                    borderColor:'rgba(255,99,132,0.5)',                   
                    data:jMoins1
                },
            ],
        },
 
        options:{
            locale:'fr-BI',
            scales:{
                yAxes:[{
                    ticks:{
                        
                        callback:(label,index,labels)=>{                    
                            if(parseInt(label) >= 999){
                                return 'BIF ' + label.toLocaleString("fr-FR");
              } else {
                return  label;
              }
                        }
                    },
                    beginAtZero:true
                    
                }]
            },
            scaleLabel: function(label){
                 return  '$' + label.value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            },
title:{
    display:true,
    text:'Graph en Francs Burundais (BIF)',
    position:'top'
},
responsive: true,
            maintainAspectRatio: false,
        }
        
    });
}


function drawMultipleLineChartUSD(c,jMoins2,jMoins1){
    
var canvas='<canvas id="tbReport1USD"></canvas>';
$('#graph-containerusd > .row  > #usd').append(canvas);
    
    var ctx= document.getElementById('tbReport1USD').getContext('2d');

    var chart = new Chart(ctx, {
        // Type of chart we want
        type:'bar',
        data:{
            labels:c,
            datasets:[
                {
                    label:'Situation journalière en US $ J-2',
                    backgroundColor:'rgba(255,99,132,0.5)',
                    borderColor:'rgba(255,99,132,0.5)',                   
                    data:jMoins2
                },
                {
                    label:'Situation journalière en US $ J-1',
                    backgroundColor:'rgba(25,102,110,0.5)',
                    borderColor:'rgba(255,99,132,0.5)',                   
                    data:jMoins1
                }
            ],
        },
        options:{
            locale:'fr-BI',
            scales:{
                 yAxes:[{
                    ticks:{
                        
                        callback:(label,index,labels)=>{                    
                            if(parseInt(label) >= 999){
                                return  '$ '+label.toLocaleString("fr-FR");
              } else {
                return  label;
              }
                        }
                    },
                    beginAtZero:true,
                    stepSize:1000
                    
                }]
            },
            scaleLabel: function(label){
                return label.value.toString().replace(/\B(?=(\d{3})+(?!\d))/g," ");
            },
            title:{
    display:true,
    text:'Graph en Dollars Américains (USD)',
    position:'top'
}
        },
        tooltips: {
            callbacks: {
                label: function(tooltipItem, chart){
                    var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
                    return datasetLabel + ': $ ' + number_format(tooltipItem.yLabel, 2);
                }
            }
        }
    });
}

function number_format(number, decimals, dec_point, thousands_sep) {
// *     example: number_format(1234.56, 2, ',', ' ');
// *     return: '1 234,56'
    number = (number + '').replace(',', '').replace(' ', '');
    var n = !isFinite(+number) ? 0 : +number,
            prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
            sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep,
            dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
            s = '',
            toFixedFix = function (n, prec) {
                var k = Math.pow(10, prec);
                return '' + Math.round(n * k) / k;
            };
    // Fix for IE parseFloat(0.55).toFixed(0) = 0;
    s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.');
    if (s[0].length > 3) {
        s[0] = s[0].replace(/\B(?=(?:\d{3})+(?!\d))/g, sep);
    }
    if ((s[1] || '').length < prec) {
        s[1] = s[1] || '';
        s[1] += new Array(prec - s[1].length + 1).join('0');
    }
    return s.join(dec);
}




