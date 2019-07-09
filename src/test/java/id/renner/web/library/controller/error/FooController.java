package id.renner.web.library.controller.error;

import id.renner.web.library.controller.Controller;
import id.renner.web.library.controller.Endpoint;
import id.renner.web.library.controller.sunshine.Foo;
import id.renner.web.library.http.CustomHttpContext;

@Controller(path = "/foo")
public class FooController {
    private final Foo foo;

    public FooController(Foo foo) {
        this.foo = foo;
    }

    @Endpoint(path = "/bar/{id}")
    public void bar(CustomHttpContext httpContext) {
    }

    @Endpoint(path = "/bar/baz")
    public void baz(CustomHttpContext httpContext){
    }
}