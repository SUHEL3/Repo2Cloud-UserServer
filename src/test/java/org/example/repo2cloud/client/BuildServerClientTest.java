package org.example.repo2cloud.client;

import com.repo2cloud.buildserver.entity.BuilderType;
import com.repo2cloud.buildserver.entity.DeploymentStatus;
import com.repo2cloud.buildserver.entity.LogLevel;
import com.repo2cloud.buildserver.entity.LogSource;
import org.example.repo2cloud.dto.build_server_DTO.DeployRequestDto;
import org.example.repo2cloud.dto.build_server_DTO.DeploymentDto;
import org.example.repo2cloud.dto.build_server_DTO.DeploymentLogDto;
import org.example.repo2cloud.dto.build_server_DTO.DeploymentResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class BuildServerClientTest {

    private MockRestServiceServer mockServer;
    private BuildServerClient client;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        mockServer = MockRestServiceServer.bindTo(builder).build();

        RestClient restClient = builder
                .baseUrl("http://localhost:9090")
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        client = factory.createClient(BuildServerClient.class);
    }

    @Test
    @DisplayName("deploy() sends POST /deploy and returns 202 ACCEPTED with DeploymentResponseDto")
    void testDeploy() {
        String responseJson = """
                {
                    "deploymentId": 101,
                    "ownerUserId": 1,
                    "repositoryUrl": "https://github.com/spring-projects/spring-petclinic",
                    "branch": "main",
                    "status": "PENDING"
                }
                """;

        mockServer.expect(requestTo("http://localhost:9090/deploy"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.deploymentId").value(101))
                .andExpect(jsonPath("$.ownerUserId").value(1))
                .andExpect(jsonPath("$.repositoryUrl").value("https://github.com/spring-projects/spring-petclinic"))
                .andExpect(jsonPath("$.branch").value("main"))
                .andRespond(withStatus(HttpStatus.ACCEPTED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(responseJson));

        DeployRequestDto request = DeployRequestDto.builder()
                .deploymentId(101L)
                .ownerUserId(1L)
                .repositoryUrl("https://github.com/spring-projects/spring-petclinic")
                .branch("main")
                .build();

        DeploymentResponseDto response = client.deploy(request);

        assertThat(response).isNotNull();
        assertThat(response.getDeploymentId()).isEqualTo(101L);
        assertThat(response.getOwnerUserId()).isEqualTo(1L);
        assertThat(response.getStatus()).isEqualTo(DeploymentStatus.PENDING);
        mockServer.verify();
    }

    @Test
    @DisplayName("getDeployment() sends GET /deployments/{id} and returns DeploymentResponseDto")
    void testGetDeployment() {
        String responseJson = """
                {
                    "deploymentId": 101,
                    "ownerUserId": 1,
                    "repositoryUrl": "https://github.com/spring-projects/spring-petclinic",
                    "branch": "main",
                    "imageName": "petclinic:latest",
                    "containerId": "c123456",
                    "allocatedPort": 8080,
                    "status": "RUNNING",
                    "builderType": "DOCKERFILE"
                }
                """;

        mockServer.expect(requestTo("http://localhost:9090/deployments/101"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        DeploymentResponseDto response = client.getDeployment(101L);

        assertThat(response).isNotNull();
        assertThat(response.getDeploymentId()).isEqualTo(101L);
        assertThat(response.getStatus()).isEqualTo(DeploymentStatus.RUNNING);
        assertThat(response.getBuilderType()).isEqualTo(BuilderType.DOCKERFILE);
        assertThat(response.getAllocatedPort()).isEqualTo(8080);
        mockServer.verify();
    }

    @Test
    @DisplayName("getAllDeployments() sends GET /deployments and returns List<DeploymentResponseDto>")
    void testGetAllDeployments() {
        String responseJson = """
                [
                    {
                        "deploymentId": 101,
                        "ownerUserId": 1,
                        "status": "RUNNING"
                    },
                    {
                        "deploymentId": 102,
                        "ownerUserId": 2,
                        "status": "STOPPED"
                    }
                ]
                """;

        mockServer.expect(requestTo("http://localhost:9090/deployments"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        List<DeploymentResponseDto> list = client.getAllDeployments();

        assertThat(list).hasSize(2);
        assertThat(list.get(0).getDeploymentId()).isEqualTo(101L);
        assertThat(list.get(0).getStatus()).isEqualTo(DeploymentStatus.RUNNING);
        assertThat(list.get(1).getDeploymentId()).isEqualTo(102L);
        assertThat(list.get(1).getStatus()).isEqualTo(DeploymentStatus.STOPPED);
        mockServer.verify();
    }

    @Test
    @DisplayName("getLogs() sends GET /deployments/{id}/logs and returns List<DeploymentLogDto>")
    void testGetLogs() {
        String responseJson = """
                [
                    {
                        "deploymentId": 101,
                        "logLevel": "INFO",
                        "source": "GIT",
                        "message": "Cloning repository..."
                    },
                    {
                        "deploymentId": 101,
                        "logLevel": "INFO",
                        "source": "BUILD",
                        "message": "Building image with Dockerfile..."
                    }
                ]
                """;

        mockServer.expect(requestTo("http://localhost:9090/deployments/101/logs"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        List<DeploymentLogDto> logs = client.getLogs(101L);

        assertThat(logs).hasSize(2);
        assertThat(logs.get(0).getLogLevel()).isEqualTo(LogLevel.INFO);
        assertThat(logs.get(0).getSource()).isEqualTo(LogSource.GIT);
        assertThat(logs.get(0).getMessage()).isEqualTo("Cloning repository...");
        assertThat(logs.get(1).getSource()).isEqualTo(LogSource.BUILD);
        mockServer.verify();
    }

    @Test
    @DisplayName("stop() sends POST /deployments/{id}/stop and returns DeploymentResponseDto")
    void testStop() {
        String responseJson = """
                {
                    "deploymentId": 101,
                    "status": "STOPPED"
                }
                """;

        mockServer.expect(requestTo("http://localhost:9090/deployments/101/stop"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        DeploymentResponseDto response = client.stop(101L);

        assertThat(response.getDeploymentId()).isEqualTo(101L);
        assertThat(response.getStatus()).isEqualTo(DeploymentStatus.STOPPED);
        mockServer.verify();
    }

    @Test
    @DisplayName("delete() sends DELETE /deployments/{id} and returns void")
    void testDelete() {
        mockServer.expect(requestTo("http://localhost:9090/deployments/101"))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withNoContent());

        client.delete(101L);
        mockServer.verify();
    }

    @Test
    @DisplayName("start() sends POST /deployments/{id}/start and returns DeploymentResponseDto")
    void testStart() {
        String responseJson = """
                {
                    "deploymentId": 101,
                    "status": "RUNNING"
                }
                """;

        mockServer.expect(requestTo("http://localhost:9090/deployments/101/start"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        DeploymentResponseDto response = client.start(101L);

        assertThat(response.getDeploymentId()).isEqualTo(101L);
        assertThat(response.getStatus()).isEqualTo(DeploymentStatus.RUNNING);
        mockServer.verify();
    }

    @Test
    @DisplayName("getDeploymentsByUser() sends GET /deployments/user/{userId} and returns List<DeploymentDto>")
    void testGetDeploymentsByUser() {
        String responseJson = """
                [
                    {
                        "id": 1,
                        "deploymentId": 101,
                        "ownerUserId": 42,
                        "repositoryUrl": "https://github.com/spring-projects/spring-petclinic",
                        "status": "RUNNING"
                    }
                ]
                """;

        mockServer.expect(requestTo("http://localhost:9090/deployments/user/42"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        List<DeploymentDto> list = client.getDeploymentsByUser(42L);

        assertThat(list).hasSize(1);
        assertThat(list.get(0).getDeploymentId()).isEqualTo(101L);
        assertThat(list.get(0).getOwnerUserId()).isEqualTo(42L);
        assertThat(list.get(0).getStatus()).isEqualTo(DeploymentStatus.RUNNING);
        mockServer.verify();
    }

    @Test
    @DisplayName("404 Not Found from Build Server throws HttpClientErrorException")
    void testNotFoundHandling() {
        mockServer.expect(requestTo("http://localhost:9090/deployments/999"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThatThrownBy(() -> client.getDeployment(999L))
                .isInstanceOf(HttpClientErrorException.NotFound.class);
        mockServer.verify();
    }
}
