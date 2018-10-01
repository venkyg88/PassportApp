package com.passport.venkatgonuguntala.passportapp.util;


import com.passport.venkatgonuguntala.passportapp.model.PersonProfile;
import java.util.Comparator;

/**
 * Created by venkatgonuguntala on 9/20/18.
 */

public class SortByAgeAscending implements Comparator<PersonProfile> {


    @Override
    public int compare(PersonProfile o1, PersonProfile o2) {
        if(Integer.valueOf(o1.getAge()) > Integer.valueOf(o2.getAge())) {
            return 1;
        }
        return -1;
    }
}
