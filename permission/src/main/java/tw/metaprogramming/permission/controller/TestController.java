package tw.metaprogramming.permission.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tw.metaprogramming.permission.dto.Request;
import tw.metaprogramming.permission.dto.Response;
import tw.metaprogramming.permission.permission.Permission;

@RestController
public class TestController {
    @GetMapping("/test/admin-and-guest")
    @Permission("hasRole('ADMIN') and hasRole('GUEST')")
    public Response adminAndGuest() {
        return new Response("you have Role ADMIN and GUEST");
    }

    @GetMapping("/test/no-authenticated")
    @Permission("!Authenticated()")
    public Object noAuthenticated() {
        return new Response("you are no authenticated");
    }

    @PostMapping("/test/number-equal-1-and-username-is-ada")
    @Permission("IsUser('ada') and #request.getNumber().equals(1)")
    public Object numberEqualOneAndUserNameIsAda(@RequestBody Request request) {
        return new Response("your name is ada and the request number equal 1");
    }
}
