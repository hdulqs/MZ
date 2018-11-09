package com.mz.oauth.remote.core;

import java.io.Serializable;
import java.util.Set;

/**
 * 应用系统权限集合context
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2015年11月4日 上午10:19:10
 */
public class PermissionContext implements Serializable {
	/*
	 * 角色set
	 */
    private Set<String> roles;
    /*
     * 权限URL set
     */
    private Set<String> permissions;

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }


    @Override
    public String toString() {
        return "PermissionContext{" +
                ", roles=" + roles +
                ", permissions=" + permissions +
                '}';
    }
}
