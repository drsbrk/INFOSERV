package bi.gov.obr.informationServices.controller;
/*
 * package bi.gov.obr.informationServices.controller;
 *
 * import java.util.ArrayList; import java.util.HashMap; import
 * java.util.LinkedHashMap; import java.util.List; import java.util.Map;
 *
 * import javax.servlet.http.HttpServletRequest;
 *
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.http.HttpStatus; import
 * org.springframework.http.ResponseEntity; import
 * org.springframework.security.core.annotation.AuthenticationPrincipal; import
 * org.springframework.security.core.userdetails.UserDetails; import
 * org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.RequestBody; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.ResponseBody;
 *
 * import com.google.gson.JsonArray; import com.google.gson.JsonObject;
 *
 * import bi.gov.obr.informationServices.dto.DashboardTwoResponse; import
 * bi.gov.obr.informationServices.dto.TableauDeBordOneDTO; import
 * bi.gov.obr.informationServices.dto.TableauDeBordTwoDTO; import
 * bi.gov.obr.informationServices.entity.PosteCollecte; import
 * bi.gov.obr.informationServices.enums.Devise; import
 * bi.gov.obr.informationServices.enums.TypeCodeRecette; import
 * bi.gov.obr.informationServices.service.PosteCollecteService; import
 * bi.gov.obr.informationServices.service.TableauDeBordServiceR2; import
 * bi.gov.obr.informationServices.service.UserService;
 *
 * @Controller public class TableauDeBordR2Controller {
 *
 * @Autowired private TableauDeBordServiceR2 tableauService;
 *
 * @Autowired private PosteCollecteService posteService;
 *
 * @Autowired private UserService userService;
 *
 * @GetMapping(value = "/tableaudebord") public String
 * displayTableauDeBordHomePage(@AuthenticationPrincipal UserDetails
 * userDetails, HttpServletRequest request, Model model) {
 * userService.putNavMenuIntoSessionAndModel(userDetails, request, model);
 * return "tableaudebord/homePage"; }
 *
 *
 *
 *
 *
 * @PostMapping(value = "/tb_report1")
 *
 * @ResponseBody public String displayTBOneCanvas(@RequestBody
 * TableauDeBordOneDTO tableauDeBordOneDTO, Model model) {
 *
 * List<DashboardTwoResponse> situationJournaliereResponse = tableauService
 * .findDailyRecordsUpToJMinusOneByPage(tableauDeBordOneDTO); JsonArray
 * jsonLibelleCentre = new JsonArray(); JsonArray jsonLibelleCentreUSD = new
 * JsonArray(); JsonArray jsonMontantJMoinsDeux= new JsonArray(); JsonArray
 * jsonMontantJMoinsDeuxUSD = new JsonArray(); JsonArray jsonMontantJMoinsUn=
 * new JsonArray(); JsonArray jsonMontantJMoinsUnUSD = new JsonArray();
 * JsonObject jsonBIF = new JsonObject(); JsonObject jsonUSD = new JsonObject();
 *
 * List<JsonObject> jsonResult = new ArrayList<>();
 * situationJournaliereResponse.forEach(data -> {
 *
 * if (data.getCurrency().equals("BIF")) {
 * jsonLibelleCentre.add(data.getLibelle());
 * jsonMontantJMoinsDeux.add(data.getMontant_recette_j_moins_deux());
 * jsonMontantJMoinsUn.add(data.getMontant_recette_j_moins_un()); } if
 * (data.getCurrency().equals("US $")) {
 * jsonLibelleCentreUSD.add(data.getLibelle());
 * jsonMontantJMoinsDeuxUSD.add(data.getMontant_recette_j_moins_deux());
 * jsonMontantJMoinsUnUSD.add(data.getMontant_recette_j_moins_un()); } });
 * jsonBIF.add("codeCentre", jsonLibelleCentre); jsonBIF.add("jMoinsDeux",
 * jsonMontantJMoinsDeux); jsonBIF.add("jMoinsUn", jsonMontantJMoinsUn);
 *
 * jsonUSD.add("codeCentre", jsonLibelleCentreUSD); jsonUSD.add("jMoinsDeux",
 * jsonMontantJMoinsDeuxUSD); jsonUSD.add("jMoinsUn", jsonMontantJMoinsUnUSD);
 *
 * jsonResult.add(jsonBIF); jsonResult.add(jsonUSD);
 *
 * System.out.println("Sent object:" + jsonResult); return
 * jsonResult.toString();
 *
 * }
 *
 * @RequestMapping("/multiplelinechartdata") public ResponseEntity<?>
 * getDataForMultipleLine(@RequestBody TableauDeBordOneDTO tableauDeBordOneDTO,
 * Model model) { List<DashboardTwoResponse> situationJournaliereResponse =
 * tableauService .findDailyRecordsUpToJMinusOneByPage(tableauDeBordOneDTO);
 * Map<String, List<DashboardTwoResponse>> mappedData = new HashMap<>(); for
 * (DashboardTwoResponse data : situationJournaliereResponse) {
 *
 * if (mappedData.containsKey(data.getCodesCentreDeCollecte())) {
 * mappedData.get(data.getCodesCentreDeCollecte()).add(data); } else {
 * List<DashboardTwoResponse> tempList = new ArrayList<>(); tempList.add(data);
 * mappedData.put(data.getCodesCentreDeCollecte(), tempList); }
 *
 * } System.out.println("Mapped Data:" + mappedData); return new
 * ResponseEntity<>(mappedData, HttpStatus.OK); }
 *
 * @GetMapping(value = "/rapport2") public String
 * showReportForm(@AuthenticationPrincipal UserDetails userDetails,
 * HttpServletRequest request, Model model) {
 * userService.putNavMenuIntoSessionAndModel(userDetails, request, model);
 * TableauDeBordTwoDTO tableauTwo = new TableauDeBordTwoDTO();
 * List<PosteCollecte> centreCollectes = posteService.findAllPosteCollecte();
 * for (int i = 1; i < TypeCodeRecette.values().length; i++) {
 * tableauTwo.getTypeCodeRecettes().add(TypeCodeRecette.values()[i]); }
 *
 * for (int j = 1; j < Devise.values().length; j++) {
 * tableauTwo.getDevises().add(Devise.values()[j]); }
 * tableauTwo.setCentreCollectes(centreCollectes);
 * model.addAttribute("tableauTwoForm", tableauTwo);
 *
 * model.addAttribute("graphDataBIF", new LinkedHashMap<>());
 * model.addAttribute("graphDataUSD", new LinkedHashMap<>());
 * model.addAttribute("graphDataEURO", new LinkedHashMap<>()); return
 * "tableaudebord/rapportDeux";
 *
 *
 * }
 *
 * @GetMapping(value = "/rapport1") public String
 * showReportOneHomePage(@AuthenticationPrincipal UserDetails userDetails,
 * HttpServletRequest request, Model model) {
 *
 *
 * userService.putNavMenuIntoSessionAndModel(userDetails, request, model);
 *
 * TableauDeBordOneDTO tableauOne = new TableauDeBordOneDTO();
 * List<PosteCollecte> centreCollectes = posteService.findAllPosteCollecte();
 * for (int i = 1; i < TypeCodeRecette.values().length; i++) {
 * tableauOne.getTypeCodeRecettes().add(TypeCodeRecette.values()[i]); }
 *
 * for (int j = 1; j < Devise.values().length; j++) {
 * tableauOne.getDevises().add(Devise.values()[j]); }
 * tableauOne.setCentreCollectes(centreCollectes);
 * model.addAttribute("tableauOneForm", tableauOne);
 *
 * model.addAttribute("graphDataBIF", new LinkedHashMap<>());
 * model.addAttribute("graphDataUSD", new LinkedHashMap<>());
 * model.addAttribute("graphDataEURO", new LinkedHashMap<>()); return
 * "tableaudebord/rapportUn"; } }
 */