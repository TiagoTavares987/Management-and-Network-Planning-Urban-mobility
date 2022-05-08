package core.entities;

import core.baseEntities.Entity;
import core.interfaces.CloneI;


import java.util.Date;

public class UserPoi extends Entity implements CloneI<UserPoi> {

    public Integer user_id;
    public Integer poi;
    public Date date;

    /**
     * @return clone igual ao original
     */
    @Override
    public UserPoi Clone() {

        var userPoi = new UserPoi();
        userPoi.setId(this.getId());
        userPoi.user_id = this.user_id;
        userPoi.poi = this.poi;
        userPoi.date = this.date;

        return userPoi;
    }
}
