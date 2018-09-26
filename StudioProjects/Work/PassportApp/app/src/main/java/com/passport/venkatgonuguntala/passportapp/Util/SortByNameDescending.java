package com.passport.venkatgonuguntala.passportapp.Util;

import com.passport.venkatgonuguntala.passportapp.model.PersonProfile;

import java.util.Comparator;

/**
 * Created by venkatgonuguntala on 9/21/18.
 */

public class SortByNameDescending implements Comparator<PersonProfile> {
    @Override
    public int compare(PersonProfile o1, PersonProfile o2) {

        if (o1.getName() == (o2.getName())){
            return 0;
        } if (o1.getName() == null) {
            return -1;
        } if (o2.getName() == null) {
            return 1;
        }
        return o2.getName().compareToIgnoreCase(o1.getName());
    }
}
