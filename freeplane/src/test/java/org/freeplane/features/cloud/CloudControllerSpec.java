package org.freeplane.features.cloud;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Color;

import org.freeplane.core.io.ReadManager;
import org.freeplane.core.io.WriteManager;
import org.freeplane.core.resources.ResourceController;
import org.freeplane.features.cloud.CloudController;
import org.freeplane.features.cloud.CloudShape;
import org.freeplane.features.map.MapController;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.mode.ModeController;
import org.freeplane.features.styles.LogicalStyleController.StyleOption;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

public class CloudControllerSpec {

    @Mock
    private ModeController mockModeController;
    @Mock
    private MapController mockMapController;
    @Mock
    private ResourceController mockResourceController;
    @Mock
    private ReadManager mockReadManager;
    @Mock
    private WriteManager mockWriteManager;

    // Remove @Mock here because we want a real instance
    private CloudController cloudController;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Stub MapController to return our mocked ReadManager and WriteManager.
        when(mockMapController.getReadManager()).thenReturn(mockReadManager);
        when(mockMapController.getWriteManager()).thenReturn(mockWriteManager);

        // Stub the ResourceController to return the red color code.
        when(mockResourceController.getProperty(CloudController.RESOURCES_CLOUD_COLOR))
                .thenReturn("#FF0000");

        // Use static mocking for Controller.getCurrentModeController()
        try (MockedStatic<Controller> mockedController = mockStatic(Controller.class)) {
            // When Controller.getCurrentModeController() is called, return our mockModeController.
            mockedController.when(Controller::getCurrentModeController).thenReturn(mockModeController);
            // When mockModeController.getMapController() is called, return our mockMapController.
            when(mockModeController.getMapController()).thenReturn(mockMapController);

            // Create and assign the CloudController instance to the field.
            cloudController = new CloudController(mockModeController, mockResourceController);
        }
    }

    @Test
    public void testGetStandardColor() {
        // Act: Call the method under test.
        Color result = cloudController.getStandardColor();

        // Assert: Verify that the returned color is equal to red.
        assertThat(result).isEqualTo(new Color(255, 0, 0));
    }
}