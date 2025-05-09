package bi.gov.obr.informationServices.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bi.gov.obr.informationServices.dto.UtilitaireDto;
import bi.gov.obr.informationServices.entity.Exercice;
import bi.gov.obr.informationServices.entity.PrevisionUpload;
import bi.gov.obr.informationServices.enumeration.UtilitaireImport;
import bi.gov.obr.informationServices.service.CompteImpotService;
import bi.gov.obr.informationServices.service.ExerciceService;
import bi.gov.obr.informationServices.service.PrevisionService;
import bi.gov.obr.informationServices.service.PrevisionUploadService;
import bi.gov.obr.informationServices.service.RecetteObrService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UtilitaireController {

  private final CompteImpotService compteImpotService;
  private final PrevisionService previsionService;
  private final ExerciceService exerciceService;
  private final RecetteObrService recetteObrService;
  private final PrevisionUploadService previsionUploadService;

  @PostMapping("/checkPrevisionFile")
  public ResponseEntity<String> checkPrevisionFile(@RequestBody UtilitaireDto utilitaireDto) {

    PrevisionUpload previsionUpload = previsionUploadService.findByAnneeBudgetaire(utilitaireDto.getExerciceId());

    System.out.println(" je load avec l'exercice " + utilitaireDto.getExerciceId());

    if (utilitaireDto.isRevision()) {
      if (previsionUpload != null
          && utilitaireDto.getPrevisionFileName().equals(previsionUpload.getFileNamePrevisionRevisE())) {
        return new ResponseEntity<>("found", HttpStatus.OK);
      } else {
        return new ResponseEntity<>("not found", HttpStatus.OK);
      }
    } else {

      if (previsionUpload != null
          && utilitaireDto.getPrevisionFileName().equals(previsionUpload.getFileNamePrevision())) {
        return new ResponseEntity<>("found", HttpStatus.OK);
      } else {
        return new ResponseEntity<>("not found", HttpStatus.OK);
      }
    }
  }

  @PostMapping("/loadFichierDeBaseByFile")
  public ResponseEntity<String> loadFichierDeBaseByFile(@RequestParam String utilitaireJson,
      @RequestParam("file") MultipartFile file) throws JsonProcessingException {
    UtilitaireDto utilitaire = new ObjectMapper().readValue(utilitaireJson, UtilitaireDto.class);
    boolean operationStatus = false;
    PrevisionUpload previsionUpload = null;

    if (utilitaire.getTypeFichierBase().name().equals(UtilitaireImport.CODE_RECETTE.name())) {
      compteImpotService.loadCompteImpotFromExcel(file);
    }
    // Quand c'est la prevision qui a été choisi

    else if (utilitaire.getTypeFichierBase().name().equals(UtilitaireImport.PREVISION.name())) {
      previsionUpload = previsionUploadService.findByAnneeBudgetaire(utilitaire.getExerciceId());
      if (previsionUpload == null || previsionUpload.getId() == null) {
        previsionUpload = new PrevisionUpload();
        previsionUpload.setAnneeBudgetaire(exerciceService.findById(utilitaire.getExerciceId()));

      }

      if (utilitaire.isRevision()) {
        previsionUpload.setFileNamePrevisionRevisE(utilitaire.getPrevisionFileName());
      } else {
        previsionUpload.setFileNamePrevision(utilitaire.getPrevisionFileName());
      }

      System.out.println("Prevision saving ");

    }
    switch (utilitaire.getTypeFichierBase()) {
    case CODE_RECETTE:
      operationStatus = compteImpotService.loadCompteImpotFromExcel(file);
      break;
    case PREVISION:
      operationStatus = previsionService.loadPrevisionFromExcel(file, utilitaire.getExerciceId(), previsionUpload,
          utilitaire.isRevision());
      break;
    case RECETTEVIEW:
      operationStatus = recetteObrService.loadRecetteObrFromExcel(file);
      break;
    }
    if (!operationStatus) {
      return new ResponseEntity<>(
          "L'import du fichier ne s'est pas bien passé. Veuillez contacter l'administrateur pour plus de précision.",
          HttpStatus.UNPROCESSABLE_ENTITY);
    }

    return new ResponseEntity<>("Données chargées avec succès", HttpStatus.OK);
  }

  @GetMapping("/utilitaire")
  public String utilitaire(Model model) {
    model.addAttribute("typeFichierBases",
        Arrays.stream(UtilitaireImport.values()).skip(1).collect(Collectors.toList()));
    List<Exercice> exercices = exerciceService.findAllExercice();
    model.addAttribute("exercices", exercices);
    return "utilitaire";
  }
}
