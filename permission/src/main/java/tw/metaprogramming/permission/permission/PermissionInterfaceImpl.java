package tw.metaprogramming.permission.permission;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PermissionInterfaceImpl implements PermissionInterface{
    public boolean hasRole(String role) {
        return hasAnyRole(role);
    }
    public boolean hasAnyRole(String ...roles) {
        return Authenticated() && Arrays.stream(roles).anyMatch(this::roleMatch);
    }
    public boolean IsUser(String userName) {
        return IsAnyUser(userName);
    }

    public boolean IsAnyUser(String ...userNames) {
        return Authenticated() && Arrays.asList(userNames).contains(this.getCurUserName());
    }

    public boolean Authenticated() {
        return Objects.nonNull(SecurityContextHolder.getContext().getAuthentication())
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser");
    }

    private boolean roleMatch(String role) {
        return getCurRoles().stream().anyMatch(role::equals);
    }

    private String getCurUserName() {
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    private List<String> getCurRoles() {
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.replaceAll("ROLE_", "")).collect(Collectors.toList());
    }
}
