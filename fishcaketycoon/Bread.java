package seungju.fishcaketycoon;

/**
 * Created by SeungJu on 2015-12-01.
 */
public class Bread {

    int flour_paat, flour_shu, gflour_paat, gflour_shu;

    public Bread() {
        flour_paat = flour_shu = gflour_paat = gflour_shu = 0;
    }

    public Bread(int _flour_paat, int _flour_shu, int _gflour_paat, int _gflour_shu) {
        flour_paat = _flour_paat;
        flour_shu = _flour_shu;
        gflour_paat = _gflour_paat;
        gflour_shu = _gflour_shu;
    }
}
