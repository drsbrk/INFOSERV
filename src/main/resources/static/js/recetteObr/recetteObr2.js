
const catchingTableauThree = () => {
    const startDateFromForm = $('#startDate').val();
    const endDateFromForm = $('#endDate').val();
    const selectedCentreDeCollecte = $('#centreCollectes').val();
    const selectedTypeRecettes = $('#typesRecettes').val();
    const selectedDevises = $('#devises').val();

    let posteCollectesSelectedFromForm = [];
    let typeRecettesSelectedFromForm = [];
    let deviseSelectedFromForm = [];
    for (var i = 0; i < selectedCentreDeCollecte.length; i++) {
        posteCollectesSelectedFromForm.push(selectedCentreDeCollecte[i]);
    }

    for (var i = 0; i < selectedTypeRecettes.length; i++) {
        typeRecettesSelectedFromForm.push(selectedTypeRecettes[i]);
    }

    for (var i = 0; i < selectedDevises.length; i++) {
        deviseSelectedFromForm.push(selectedDevises[i]);
    }

    let tableauThreeDTO = {
        startDate: startDateFromForm,
        endDate:endDateFromForm,
        centreCollectesSelected: posteCollectesSelectedFromForm,
        typesRecettesSelected: typeRecettesSelectedFromForm,
        devisesSelected: deviseSelectedFromForm,
    }
    console.log(tableauThreeDTO);
    return tableauThreeDTO;
}


$("#displayRecetteObr").click((e) => {
    e.preventDefault();
$("#tbReport2").remove();
$("#tbReport2USD").remove();
$("#loadingGif").css("display","block");
console.log("send button was clicked")
    const tableauThreeDTO = catchingTableauThree();
    console.log("Object="+tableauThreeDTO);
        var debut = new Date(tableauThreeDTO.startDate);
        debut.setHours(0,0,0,0);
        var end = new Date(tableauThreeDTO.endDate);
        end.setHours(0,0,0,0);
        console.log("Date End="+end);
        console.log("Date Début="+debut);
    if (tableauThreeDTO.startDate === undefined || tableauThreeDTO.endDate === undefined 
    || tableauThreeDTO.startDate== "" || tableauThreeDTO.endDate== "") {  
        iziToast.warning({
            title: '',
            position: 'center',
            message: 'Veuillez sélectionner les dates correctement.',
        });
        $("#loadingGif").css("display","none");
        return;
    }     
  if(debut > end) {
    iziToast.warning({
        title: '',
        position: 'center',
        message: 'La date début ne peut être inférieure à la date fin.',
    });
    
     
    return;
        
  }
    queryReportOne(tableauThreeDTO); // Ici c'est l'envoi de la requête AJAX vers SPRING BOOT
});

const queryReportOne = (tableauThreeDTO) => {    
    $.ajax({
        url: "/tb_report2",
        contentType: 'application/json',
        beforeSend: function (request, settings) {
            start_time = new Date().getTime();
        },
        type: 'POST',
        data: JSON.stringify(tableauThreeDTO),

        success:function(result){
            var data=JSON.parse(result);
            
            var codeCentre=data[0].codeCentre; // Tableau des noms centre de collecte
            var montant=data[0].montant; // Tableau des montants pour chacun des centres de collecte
            drawBarChartBIF(codeCentre,montant);
            
            var codeCentreUSD=data[1].codeCentre;
            var montantUSD=data[1].montant;
            drawBarChartUSD(codeCentreUSD,montantUSD);
            
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


function drawBarChartBIF(c,m){

var canvas='<canvas height="300" width="400" id="tbReport2"></canvas>';
$('#graph-container > .col-lg-6 > .card >  #bif').append(canvas);
    var ctx= document.getElementById('tbReport2').getContext('2d');
    var chart = new Chart(ctx, {
        // Type of chart we want
        type:'bar',
        data:{
            labels:c,
            datasets:[
                {
                    label:'Situation périodique en BIF',
                    backgroundColor:'rgba(255,99,132,0.5)',
                    borderColor:'rgba(255,99,132,0.5)',                   
                    data:m
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
    text:'Graphique en Francs Burundais (BIF)',
    position:'top'
}
        }      
    });
}


function drawBarChartUSD(c,m){
    
var canvas='<canvas height="300" width="400" id="tbReport2USD"></canvas>';
$('#graph-container  > .col-lg-6 > .card > #usd').append(canvas);
    
    var ctx= document.getElementById('tbReport2USD').getContext('2d');

    var chart = new Chart(ctx, {
        // Type of chart we want
        type:'bar',
        data:{
            labels:c,
            datasets:[
                {
                    label:'Situation périodique en US $',
                    backgroundColor:'rgba(25,102,110,0.5)',
                    borderColor:'rgba(255,99,132,0.5)',                   
                    data:m
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
                text:'Graphique en Dollars Américains (USD)',
                position:'top'
            }
        },
    });
}




