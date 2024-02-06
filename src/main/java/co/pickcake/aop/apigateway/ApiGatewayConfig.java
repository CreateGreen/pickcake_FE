package co.pickcake.aop.apigateway;


import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component @Getter
public class ApiGatewayConfig {

    private String httpPortocol = "http://";

//    private String imageServerGateWay = "http://localhost:8080/";

    @Value("http://${server.address}:${server.port}/")
    private String imageServerGateWay;

    private String uploadApi = "api/image/store";

//    private String searchCakeGateWay = "http://localhost:8080/";

    @Value("http://${server.address}:${server.port}/")
    private String searchCakeGateWay;

    private String searchCakeApi = "api/cake";

}
