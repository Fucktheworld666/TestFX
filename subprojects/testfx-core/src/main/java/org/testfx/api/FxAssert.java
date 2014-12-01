package org.testfx.api;

import javafx.scene.Node;

import com.google.common.annotations.Beta;
import com.google.common.base.Predicate;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.testfx.matcher.predicate.PredicateMatchers;

@Beta
public class FxAssert {

    //---------------------------------------------------------------------------------------------
    // CONSTANTS.
    //---------------------------------------------------------------------------------------------

    private static final String EMPTY_STRING = "";

    //---------------------------------------------------------------------------------------------
    // STATIC FIELDS.
    //---------------------------------------------------------------------------------------------

    private static FxAssertContext context;

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    public static <T extends Node> void verifyThat(String reason,
                                                   T node,
                                                   Matcher<T> nodeMatcher) {
        verifyThatImpl(reason, node, nodeMatcher);
    }

    public static <T extends Node> void verifyThat(T node,
                                                   Matcher<T> nodeMatcher) {
        verifyThatImpl(emptyReason(), node, nodeMatcher);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Node> void verifyThat(String reason,
                                                   String nodeQuery,
                                                   Matcher<T> nodeMatcher) {
        verifyThatImpl(reason, (T) toNode(nodeQuery), nodeMatcher);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Node> void verifyThat(String nodeQuery,
                                                   Matcher<T> nodeMatcher) {
        verifyThatImpl(emptyReason(), (T) toNode(nodeQuery), nodeMatcher);
    }

    public static <T extends Node> void verifyThat(String reason,
                                                   T node,
                                                   Predicate<T> nodePredicate) {
        verifyThatImpl(reason, node, toNodeMatcher(nodePredicate));
    }

    public static <T extends Node> void verifyThat(T node,
                                                   Predicate<T> nodePredicate) {
        verifyThatImpl(emptyReason(), node, toNodeMatcher(nodePredicate));
    }

    public static FxAssertContext assertContext() {
        if (context == null) {
            context = new FxAssertContext();
        }
        return context;
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    private static <T extends Node> void verifyThatImpl(String reason,
                                                        T node,
                                                        Matcher<T> nodeMatcher) {
        try {
            MatcherAssert.assertThat(reason, node, nodeMatcher);
        }
        catch (AssertionError error) {
            // TODO: Save screenshot to file.
            throw new AssertionError(error.getMessage());
        }
    }

    private static String emptyReason() {
        return EMPTY_STRING;
    }

    private static Node toNode(String nodeQuery) {
        try {
            return assertContext().getNodeFinder().node(nodeQuery);
        }
        catch (Exception ignore) {
            return null;
        }
    }

    private static <T extends Node> Matcher<T> toNodeMatcher(final Predicate<T> nodePredicate) {
        return PredicateMatchers.nodeMatcher("applies on Predicate", nodePredicate);
    }

}
