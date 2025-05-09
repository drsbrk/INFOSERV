package bi.gov.obr.informationServices.utils;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.validation.BindingResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bi.gov.obr.informationServices.entity.AppSetting;
import bi.gov.obr.informationServices.entity.CompteImpot;
import bi.gov.obr.informationServices.entity.Prevision;
import bi.gov.obr.informationServices.entity.RecetteObr;
import bi.gov.obr.informationServices.enums.TypeCodeRecette;

public class Helper {

  public static DecimalFormat formaterMontant() {

    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.FRENCH);
    dfs.setGroupingSeparator(' ');
    return new DecimalFormat("#,###", dfs);
  }

  public static String getErrorMessageForField(BindingResult bindingResult, String fieldName) {
    return bindingResult.getFieldError(fieldName).getDefaultMessage();
  }

  public static String getJsonString(Object object) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static List<CompteImpot> readCompteImpotFromExcel(InputStream inputStream) {

    List<CompteImpot> compteImpots = new ArrayList<>();
    try {
      // Create a Workbook from an Excel file (.xls or .xlsx)
      Workbook workbook = new XSSFWorkbook(inputStream);
      // Get the first Sheet from workbook
      Sheet sheet = workbook.getSheetAt(0);
      // Iterate through each rows one by one
      int rowOffset = 1;

      for (Row row : sheet) {
        // For each row, iterate through all the columns
        CompteImpot compteImpot = new CompteImpot();
        if (row.getRowNum() < rowOffset) {
          continue;
        }

        for (Cell cell : row) {
          try {
            switch (cell.getColumnIndex()) {
            case 0:
              compteImpot.setCode(String.valueOf(cell.getNumericCellValue()).replace(".0", ""));
              break;
            case 1:
              compteImpot.setLibelle(cell.getStringCellValue());
              break;
            case 2:
              int typeRecetteFromExcel = (int) cell.getNumericCellValue();
              if (typeRecetteFromExcel == 1) {
                compteImpot.setTypeRecette(TypeCodeRecette.RECETTE_FISCALE.getCode());
              } else if (typeRecetteFromExcel == 2) {
                compteImpot.setTypeRecette(TypeCodeRecette.RECETTE_DOUANIERE.getCode());
              } else if (typeRecetteFromExcel == 3) {
                compteImpot.setTypeRecette(TypeCodeRecette.RECETTE_NON_FISCALE.getCode());
              }
              break;
            }
          } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("A La ligne " + row.getRowNum() + " cellule " + cell.getColumnIndex()
            + " le format n'est pas correcte. Veuillez vÃ©rifier le fichier fourni.");
          }

        }

        compteImpots.add(compteImpot);
      }
      workbook.close();

    } catch (Exception e) {
      System.out.println(" =====>> catch catch =====> ");
      e.printStackTrace();
    }
    return compteImpots;
  }

  public static Set<Prevision> readPrevisionFromExcel(InputStream inputStream, AppSetting appSetting) {

    Set<Prevision> previsions = new HashSet<>();
    Prevision prevision;
    String[] monthTitle = new String[12];
    int rowIdex = 0;
    int columnIdex = 0;
    ////////// *******////////
    int firstMonthColumnIndex = 3;
    int lastMonthColumnIndex = 15;
    BigDecimal totalPerRow = BigDecimal.ZERO;
    if (appSetting != null && appSetting.getPrevisionFirstMonthColumn() > 0) {
      firstMonthColumnIndex = appSetting.getPrevisionFirstMonthColumn();
    }
    if (appSetting != null && appSetting.getPrevisionLastMonthColumn() > 0) {
      lastMonthColumnIndex = appSetting.getPrevisionLastMonthColumn();
    }
    int codeRecetteColumnIndex = 0;
    int libelleRecetteColumnIndex = 1;
    int typeRecetteColumnIndex = 2;

    boolean skipRow = false;
    try {
      // Create a Workbook from an Excel file (.xls or .xlsx)
      Workbook workbook = new XSSFWorkbook(inputStream);
      // Get the first Sheet from workbook
      Sheet sheet = workbook.getSheetAt(0);
      // Iterate through each rows one by one
      int rowToSkip = 0;
      for (Row row : sheet) {
        totalPerRow = BigDecimal.ZERO;
        rowIdex = row.getRowNum();
        String codeRecette = null;
        String typeRecette = null;
        String libelleRecette = null;

        if (row.getRowNum() < rowToSkip) {
          continue;
        }

        // For each row, iterate through all the columns
        if (row.getRowNum() == rowToSkip) {
          for (int i = firstMonthColumnIndex; i < lastMonthColumnIndex; i++) {
            columnIdex = i;
            rowIdex = rowToSkip;
            Cell cell = row.getCell(i);
            if (cell != null) {
              monthTitle[i - firstMonthColumnIndex] = cell.getStringCellValue().split("_")[0];
            }
          }
          Arrays.stream(monthTitle).forEach(System.out::println);
          continue;
        }

        for (Cell cell : row) {
          columnIdex = cell.getColumnIndex();

          // Check the cell type and format accordingly
          if (codeRecetteColumnIndex == cell.getColumnIndex()) {
            if (cell.getCellType() != CellType.NUMERIC) {
              break;
            }
            if (cell.getCellType().equals(CellType.NUMERIC)) {
              codeRecette = String.valueOf(cell.getNumericCellValue()).replace(".0", "");
            } else if (cell.getCellType().equals(CellType.STRING)) {
              codeRecette = String.valueOf(cell.getStringCellValue());
            }

            if (codeRecette.length() >= 6) {
              codeRecette = codeRecette.substring(0, 6);
            }

            if (codeRecette == null || codeRecette.trim().isEmpty() || codeRecette.length() < 6) {
              break;
            }

          } else if (libelleRecetteColumnIndex == cell.getColumnIndex()) {
            libelleRecette = cell.getStringCellValue();
          } else if (typeRecetteColumnIndex == cell.getColumnIndex()) {
            typeRecette = String.valueOf(cell.getNumericCellValue()).replace(".0", "");
            typeRecette = "0" + typeRecette;
          } else if (cell.getColumnIndex() >= firstMonthColumnIndex && cell.getColumnIndex() < lastMonthColumnIndex) {
            if (codeRecette == null) {
              break;
            }

            // Don't count cell with formula
            // Ex : D7 + D8
            if (cell.getCellType().name() == CellType.FORMULA.name()
                && !cell.getCellFormula().contains("oefficients")) {

              // System.out.println(" ====> ===> " + codeRecette + " cell " +
              // cell.getColumnIndex());
              break;
            }
            for (Month month : Month.values()) {
              if (month.getDisplayName(TextStyle.FULL, Locale.FRENCH)
                  .equalsIgnoreCase(monthTitle[cell.getColumnIndex() - firstMonthColumnIndex])) {
                {

                  prevision = new Prevision();
                  prevision.setPrevisionMonth(month);
                  prevision.calculateMonthPriority();
                  if (typeRecette.equals("01")) {
                    prevision.setMontantCti(BigDecimal.valueOf(cell.getNumericCellValue()));
                  } else if (typeRecette.equals("02")) {
                    prevision.setMontantCda(BigDecimal.valueOf(cell.getNumericCellValue()));
                  }
                  CompteImpot compteImpot = new CompteImpot(codeRecette, libelleRecette, typeRecette);
                  prevision.setCodeRecette(compteImpot);
                  // totalPerRow = totalPerRow.add(prevision.getMontantCti());

                  if (!previsions.add(prevision)) {
                    Prevision finalPrevision = prevision;

                    Prevision previsionFound = previsions.stream()
                        .filter(
                            prev -> prev.getCodeRecette().getCode().equals(finalPrevision.getCodeRecette().getCode())
                            && prev.getPrevisionMonth().getValue() == finalPrevision.getPrevisionMonth().getValue())
                        .findFirst().get();
                    if (typeRecette.equals("01")) {
                      if (previsionFound.getMontantCti() == null) {
                        previsionFound.setMontantCti(finalPrevision.getMontantCti());
                      } else {
                        previsionFound
                        .setMontantCti(previsionFound.getMontantCti().add(finalPrevision.getMontantCti()));
                      }
                    } else {
                      if (previsionFound.getMontantCda() == null) {
                        previsionFound.setMontantCda(finalPrevision.getMontantCda());
                      } else {
                        previsionFound
                        .setMontantCda(previsionFound.getMontantCda().add(finalPrevision.getMontantCda()));
                      }
                    }
                  }
                }
              }

            }

          }
        }

        if (totalPerRow.longValue() > 0) {

          System.out.println("Checking row number " + rowIdex + " and total is " + totalPerRow.longValue());

        }
      }

      workbook.close();
    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalArgumentException("Erreur sur la cellule " + columnIdex + " de la ligne " + rowIdex
          + ". Veuillez vÃ©rifier le fichier fourni.");
    }
    return previsions;
  }

  public static List<RecetteObr> readRecetteObrFromExcel(InputStream inputStream) {

    List<RecetteObr> recettesObr = new ArrayList<>();
    RecetteObr recetteObr = null;

    int rowOffset = 1;
    int columnIdex = 0;
    try {
      // Create a Workbook from an Excel file (.xls or .xlsx)
      Workbook workbook = new XSSFWorkbook(inputStream);
      // Get the first Sheet from workbook
      Sheet sheet = workbook.getSheetAt(0);
      // Iterate through each rows one by one
      for (Row row : sheet) {
        // For each row, iterate through all the columns
        if (row.getRowNum() < rowOffset) {
          continue;
        }

        recetteObr = new RecetteObr();
        for (Cell cell : row) {
          columnIdex = cell.getColumnIndex();
          // Check the cell type and format accordingly
          switch (cell.getColumnIndex()) {
          case 0:
            recetteObr.setNumeroQuittance(String.valueOf(cell.getNumericCellValue()).replace(".0", ""));
            break;
          case 1:
            recetteObr.setDateQuittance(
                LocalDate.parse(cell.getStringCellValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            break;
          case 2:
            recetteObr
            .setDatePaiement(LocalDate.parse(cell.getStringCellValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            break;
          case 3:
            String typeRecetteIndice = cell.getStringCellValue();
            recetteObr.setTypeCodeRecette(TypeCodeRecette.getTypeRecetteByCode(typeRecetteIndice).getCode());
            break;
          case 4:
            recetteObr.setCentreCollecte(cell.getStringCellValue());
            break;
          case 5:
            recetteObr.setCodeRecette(String.valueOf(cell.getNumericCellValue()).replace(".0", ""));
            break;
          case 6:
            recetteObr.setDeviseRecette(cell.getStringCellValue());
            break;
          case 7:
            recetteObr.setMontantRecette((long) cell.getNumericCellValue());
            break;
            // case 8:
            // recetteObr.setNomContribuable(cell.getStringCellValue());
            // break;
            // case 9:
            // recetteObr.setPrenomContribuable(cell.getStringCellValue());
            // break;
            // case 10:
            // recetteObr.setNif(cell.getStringCellValue());
            // break;
          case 11:
            recetteObr.setModePaiement(cell.getStringCellValue());
            break;
          case 12:
            recetteObr.setApprobationRecette(cell.getStringCellValue());
            break;
          }
        }
        recettesObr.add(recetteObr);
      }
      workbook.close();
    } catch (Exception e) {
      System.err.println("Error on cell " + columnIdex);
      e.printStackTrace();
    }
    return recettesObr;
  }

}
