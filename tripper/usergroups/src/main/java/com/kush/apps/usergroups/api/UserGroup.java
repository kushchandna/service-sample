package com.kush.apps.usergroups.api;

import java.util.Set;

import com.kush.apps.commons.api.Identifiable;
import com.kush.apps.users.api.User;

public interface UserGroup extends Identifiable {

    Set<User> getUsers();
}
