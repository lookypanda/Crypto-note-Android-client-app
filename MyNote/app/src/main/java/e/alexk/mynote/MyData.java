package e.alexk.mynote;

import java.util.ArrayList;

public class MyData {
    ArrayList<String> nameArray = new ArrayList<String>();
    ArrayList<String> versionArray = new ArrayList<String>();


    ArrayList<Integer> id_ = new ArrayList<Integer>();

    int add(String name, String version, int id) {
        if (nameArray.add(name) && versionArray.add(version) && id_.add(id))
            return 1;
        else return 0;
    }


}

