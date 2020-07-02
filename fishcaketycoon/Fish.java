package seungju.fishcaketycoon;

/**
 * Created by SeungJu on 2015-12-01.
 */
public class Fish {

    int type, fs, bs; // bread's type, front state, back state

    boolean bread, ingre; // ready to bread, ingredient

    public Fish(){
        type = fs = bs = 0;
        bread = ingre = false;
    }

    public Fish(int _type, int _fs, int _bs){
        type = _type;
        fs = _fs;        bs = _bs;
        bread = ingre = false;
        if(type > 0){
            if(fs > 0 || bs > 0){} // is alive
            else {
                if(type == 1 || type == 4)bread = true;
                else
                    bread = ingre = true;
            }
        }else{
            bread = ingre = false;
        }
    }
}
