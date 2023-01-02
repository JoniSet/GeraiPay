package com.optima.gerai_pay.Helper;

import java.util.ArrayList;
import java.util.List;

public class DropwdownListHelper {

    public List<String> listMarkup() {
        List<String> kawinList = new ArrayList<>();
        kawinList.add("Rp. 100");
        kawinList.add("Rp. 150");
        kawinList.add("Rp. 200");
        return kawinList;
    }

    public List<String> listPendidikan() {
        List<String> pendidikanList = new ArrayList<>();
        pendidikanList.add("SD");
        pendidikanList.add("SMP");
        pendidikanList.add("SLTA");
        pendidikanList.add("D III");
        pendidikanList.add("S I");
        pendidikanList.add("S II");
        pendidikanList.add("lainnya");
        return pendidikanList;
    }
}
