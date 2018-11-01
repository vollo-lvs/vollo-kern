package nl.vollo.kern.test;

import com.fasterxml.jackson.databind.JsonNode;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class JsonNodeIsEqualToTextMatcher extends BaseMatcher<JsonNode> {

    private String expectedValue;

    private JsonNodeIsEqualToTextMatcher(String expectedValue) {
        this.expectedValue = expectedValue;
    }

    public static Matcher<JsonNode> isTextNode(String text) {
        return new JsonNodeIsEqualToTextMatcher(text);
    }

    @Override
    public boolean matches(Object item) {
        if (item instanceof JsonNode) {
            return ((JsonNode) item).asText().equals(expectedValue);
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expectedValue);
    }
}
