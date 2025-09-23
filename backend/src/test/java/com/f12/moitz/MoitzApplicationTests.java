package com.f12.moitz;

import com.f12.moitz.application.SetupService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class MoitzApplicationTests {

    @MockitoBean
    private SetupService setupService;

    @Test
    void contextLoads() {
        Mockito.doNothing().when(setupService).setup();
    }

}
