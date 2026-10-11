package org.example.repo2cloud.service;

import com.repo2cloud.buildserver.entity.DeploymentStatus;
import org.example.repo2cloud.client.BuildServerClient;
import org.example.repo2cloud.dto.build_server_DTO.DeployRequestDto;
import org.example.repo2cloud.dto.build_server_DTO.DeploymentDto;
import org.example.repo2cloud.dto.build_server_DTO.DeploymentLogDto;
import org.example.repo2cloud.dto.build_server_DTO.DeploymentResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeploymentServiceTest {

    @Mock
    private BuildServerClient buildServerClient;

    private DeploymentService deploymentService;

    @BeforeEach
    void setUp() {
        deploymentService = new DeploymentService(buildServerClient);
    }

    @Test
    @DisplayName("deploy() forwards to buildServerClient.deploy()")
    void testDeploy() {
        DeployRequestDto request = DeployRequestDto.builder()
                .deploymentId(1L)
                .ownerUserId(10L)
                .repositoryUrl("https://github.com/spring-projects/spring-petclinic")
                .branch("main")
                .build();

        DeploymentResponseDto expected = DeploymentResponseDto.builder()
                .deploymentId(1L)
                .status(DeploymentStatus.PENDING)
                .build();

        when(buildServerClient.deploy(request)).thenReturn(expected);

        DeploymentResponseDto actual = deploymentService.deploy(request);

        assertThat(actual).isEqualTo(expected);
        verify(buildServerClient, times(1)).deploy(request);
    }

    @Test
    @DisplayName("getDeployment() forwards to buildServerClient.getDeployment()")
    void testGetDeployment() {
        DeploymentResponseDto expected = DeploymentResponseDto.builder()
                .deploymentId(5L)
                .status(DeploymentStatus.RUNNING)
                .build();

        when(buildServerClient.getDeployment(5L)).thenReturn(expected);

        DeploymentResponseDto actual = deploymentService.getDeployment(5L);

        assertThat(actual).isEqualTo(expected);
        verify(buildServerClient).getDeployment(5L);
    }

    @Test
    @DisplayName("getAllDeployments() forwards to buildServerClient.getAllDeployments()")
    void testGetAllDeployments() {
        List<DeploymentResponseDto> list = List.of(
                DeploymentResponseDto.builder().deploymentId(1L).build()
        );
        when(buildServerClient.getAllDeployments()).thenReturn(list);

        List<DeploymentResponseDto> actual = deploymentService.getAllDeployments();

        assertThat(actual).hasSize(1);
        verify(buildServerClient).getAllDeployments();
    }

    @Test
    @DisplayName("getLogs() forwards to buildServerClient.getLogs()")
    void testGetLogs() {
        List<DeploymentLogDto> logs = List.of(
                DeploymentLogDto.builder().deploymentId(1L).message("test log").build()
        );
        when(buildServerClient.getLogs(1L)).thenReturn(logs);

        List<DeploymentLogDto> actual = deploymentService.getLogs(1L);

        assertThat(actual).hasSize(1);
        verify(buildServerClient).getLogs(1L);
    }

    @Test
    @DisplayName("stop() forwards to buildServerClient.stop()")
    void testStop() {
        DeploymentResponseDto expected = DeploymentResponseDto.builder()
                .deploymentId(1L)
                .status(DeploymentStatus.STOPPED)
                .build();
        when(buildServerClient.stop(1L)).thenReturn(expected);

        DeploymentResponseDto actual = deploymentService.stop(1L);

        assertThat(actual.getStatus()).isEqualTo(DeploymentStatus.STOPPED);
        verify(buildServerClient).stop(1L);
    }

    @Test
    @DisplayName("delete() forwards to buildServerClient.delete()")
    void testDelete() {
        doNothing().when(buildServerClient).delete(1L);

        deploymentService.delete(1L);

        verify(buildServerClient).delete(1L);
    }

    @Test
    @DisplayName("start() forwards to buildServerClient.start()")
    void testStart() {
        DeploymentResponseDto expected = DeploymentResponseDto.builder()
                .deploymentId(1L)
                .status(DeploymentStatus.RUNNING)
                .build();
        when(buildServerClient.start(1L)).thenReturn(expected);

        DeploymentResponseDto actual = deploymentService.start(1L);

        assertThat(actual.getStatus()).isEqualTo(DeploymentStatus.RUNNING);
        verify(buildServerClient).start(1L);
    }

    @Test
    @DisplayName("getDeploymentsByUser() forwards to buildServerClient.getDeploymentsByUser()")
    void testGetDeploymentsByUser() {
        List<DeploymentDto> list = List.of(
                DeploymentDto.builder().id(1L).ownerUserId(99L).build()
        );
        when(buildServerClient.getDeploymentsByUser(99L)).thenReturn(list);

        List<DeploymentDto> actual = deploymentService.getDeploymentsByUser(99L);

        assertThat(actual).hasSize(1);
        verify(buildServerClient).getDeploymentsByUser(99L);
    }
}
