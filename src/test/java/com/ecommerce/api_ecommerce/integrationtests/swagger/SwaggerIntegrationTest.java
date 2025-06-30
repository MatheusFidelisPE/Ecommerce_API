package com.ecommerce.api_ecommerce.integrationtests.swagger;

import com.ecommerce.api_ecommerce.config.TestConfigs;
import com.ecommerce.api_ecommerce.integrationtests.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
class SwaggerIntegrationTest extends AbstractIntegrationTest {


//	@DisplayName("Test Swagger UI page")
//	@Test
	void testShouldDisplaySwaggerUIPage(){
	//	Given
		var content = given()
						.basePath("/swagger-ui/index.html")
						.port(TestConfigs.SERVER_PORT_SWAGGER)
						.when()
							.get()
						.then()
							.statusCode(200)
							.extract()
								.body()
								.asString();
	assertTrue(content.contains("Swagger UI"));
	}


}
