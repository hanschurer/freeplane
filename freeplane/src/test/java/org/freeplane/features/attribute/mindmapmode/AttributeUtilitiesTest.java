package org.freeplane.features.attribute.mindmapmode;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.freeplane.features.map.NodeModel;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

public class AttributeUtilitiesTest {
    @Mock
    private NodeModel node;
    
    @Mock
    private NodeAttributeTableModel tableModel;
    
    private AttributeUtilities attributeUtilities;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        attributeUtilities = new AttributeUtilities();
    }

    @Test
    public void testHasAttributesWhenNodeHasAttributes() {
        // Arrange
        when(NodeAttributeTableModel.getModel(node)).thenReturn(tableModel);
        when(tableModel.getRowCount()).thenReturn(1);
        
        // Act
        boolean result = attributeUtilities.hasAttributes(node);
        
        // Assert
        assertThat(result).isTrue();
        verify(tableModel).getRowCount();
    }

    @Test
    public void testHasAttributesWhenNodeHasNoAttributes() {
        // Arrange
        when(NodeAttributeTableModel.getModel(node)).thenReturn(tableModel);
        when(tableModel.getRowCount()).thenReturn(0);
        
        // Act
        boolean result = attributeUtilities.hasAttributes(node);
        
        // Assert
        assertThat(result).isFalse();
        verify(tableModel).getRowCount();
    }

    @Test
        public void testHasAttributesWhenModelIsNull() {
            try (MockedStatic<NodeAttributeTableModel> mocked = mockStatic(NodeAttributeTableModel.class)) {
                // Arrange
                NodeModel testNode = new NodeModel("test", null);
                mocked.when(() -> NodeAttributeTableModel.getModel(testNode)).thenReturn(tableModel);
                when(tableModel.getRowCount()).thenReturn(0);
                
                // Act
                boolean result = attributeUtilities.hasAttributes(testNode);
                
                // Assert
                assertThat(result).isFalse();
                mocked.verify(() -> NodeAttributeTableModel.getModel(testNode));
                verify(tableModel).getRowCount();
            }
        }
}