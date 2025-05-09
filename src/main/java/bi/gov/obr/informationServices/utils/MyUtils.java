package bi.gov.obr.informationServices.utils;

import java.util.ArrayList;

public class MyUtils {
  public static <T> void iterateList(ArrayList<T> anyList) {
    for (T temp : anyList) {
      System.out.println(temp);
    }
  }
}
