package tw.metaprogramming.permission.permission;

public interface PermissionInterface {
    boolean hasRole(String role);
    boolean hasAnyRole(String ...roles);
    boolean IsUser(String userName);
    boolean IsAnyUser(String ...userNames);
    boolean Authenticated();
}
