package org.freeplane.features.map;
import static org.freeplane.features.map.NodeModel.CloneType.TREE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Before;
import org.junit.Test;


public class NodeStateMachineTest {
    private MapModel map = null;
    private NodeModel currentNode;
    private NodeModel parentNode;

    // Combined State Definition
    private enum CompositeState {
        NO_SIBLINGS_NO_CHILDREN,    // S1: No sibiling, no children.
        NO_SIBLINGS_HAS_CHILDREN,   // S2: No sibiling, have children
        HAS_SIBLINGS_NO_CHILDREN,   // S3: have sibiling, no children 
        HAS_SIBLINGS_HAS_CHILDREN   // S4: have sibiling, have children
    }

    // Event define
    private enum Event {
        GAIN_SIBLING,   
        LOSE_SIBLING,  
        HAVE_CHILD,     
        LOSE_CHILD      
    }

    private CompositeState currentState;

    @Before
    public void setup() {
        parentNode = root();
        currentNode = new NodeModel("node", map);
        currentState = CompositeState.NO_SIBLINGS_NO_CHILDREN;
    }

    private NodeModel root() {
        final NodeModel parent = new NodeModel("parent", map);
        parent.setClones(new SingleNodeList(parent, TREE));
        return parent;
    }

    
    private CompositeState transition(CompositeState state, Event event) {
        switch (state) {
            case NO_SIBLINGS_NO_CHILDREN:
                switch (event) {
                    case GAIN_SIBLING: return CompositeState.HAS_SIBLINGS_NO_CHILDREN;
                    case HAVE_CHILD: return CompositeState.NO_SIBLINGS_HAS_CHILDREN;
                    default: throw new IllegalStateException("Invalid event for current state");
                }
            case NO_SIBLINGS_HAS_CHILDREN:
                switch (event) {
                    case GAIN_SIBLING: return CompositeState.HAS_SIBLINGS_HAS_CHILDREN;
                    case LOSE_CHILD: return CompositeState.NO_SIBLINGS_NO_CHILDREN;
                    default: throw new IllegalStateException("Invalid event for current state");
                }
            case HAS_SIBLINGS_NO_CHILDREN:
                switch (event) {
                    case LOSE_SIBLING: return CompositeState.NO_SIBLINGS_NO_CHILDREN;
                    case HAVE_CHILD: return CompositeState.HAS_SIBLINGS_HAS_CHILDREN;
                    default: throw new IllegalStateException("Invalid event for current state");
                }
            case HAS_SIBLINGS_HAS_CHILDREN:
                switch (event) {
                    case LOSE_SIBLING: return CompositeState.NO_SIBLINGS_HAS_CHILDREN;
                    case LOSE_CHILD: return CompositeState.HAS_SIBLINGS_NO_CHILDREN;
                    default: throw new IllegalStateException("Invalid event for current state");
                }
            default:
                throw new IllegalStateException("Unknown state");
        }
    }

    private void handleEvent(Event event) {
        CompositeState nextState = transition(currentState, event);
        
        switch (event) {
            case GAIN_SIBLING:
                NodeModel sibling = new NodeModel("sibling", map);
                parentNode.insert(sibling);
                break;
            case LOSE_SIBLING:
                parentNode.remove(parentNode.getChildCount() - 1);
                break;
            case HAVE_CHILD:
                NodeModel child = new NodeModel("child", map);
                currentNode.insert(child);
                break;
            case LOSE_CHILD:
                currentNode.remove(currentNode.getChildCount() - 1);
                break;
        }
        
        currentState = nextState;
    }

    @Test
    public void testInitialState() {
        assertThat(currentState, equalTo(CompositeState.NO_SIBLINGS_NO_CHILDREN));
        assertThat(currentNode.getChildCount(), equalTo(0));
        assertThat(parentNode.getChildCount(), equalTo(0));
    }

    @Test
    public void testGainSibling() {
        handleEvent(Event.GAIN_SIBLING);
        assertThat(currentState, equalTo(CompositeState.HAS_SIBLINGS_NO_CHILDREN));
        assertThat(parentNode.getChildCount(), equalTo(1));
    }

    @Test
    public void testHaveChild() {
        handleEvent(Event.HAVE_CHILD);
        assertThat(currentState, equalTo(CompositeState.NO_SIBLINGS_HAS_CHILDREN));
        assertThat(currentNode.getChildCount(), equalTo(1));
    }

    @Test
    public void testCompleteStateTransition() {
        // S1 -> S3 -> S4 -> S2
        handleEvent(Event.GAIN_SIBLING);
        assertThat(currentState, equalTo(CompositeState.HAS_SIBLINGS_NO_CHILDREN));

        handleEvent(Event.HAVE_CHILD);
        assertThat(currentState, equalTo(CompositeState.HAS_SIBLINGS_HAS_CHILDREN));

        handleEvent(Event.LOSE_SIBLING);
        assertThat(currentState, equalTo(CompositeState.NO_SIBLINGS_HAS_CHILDREN));
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidTransition() {
        handleEvent(Event.LOSE_CHILD); // Attempt to remove child nodes from the initial state
    }
}