package nl.vollo.kern.test

import com.fasterxml.jackson.databind.JsonNode
import org.hamcrest.BaseMatcher
import org.hamcrest.Description

fun isTextNode(text: String): org.hamcrest.Matcher<JsonNode> = JsonNodeIsEqualToTextMatcher(text)

class JsonNodeIsEqualToTextMatcher(private val expectedValue: String): BaseMatcher<JsonNode>() {

    override fun describeTo(description: Description?) {
        description?.appendValue(expectedValue);
    }

    override fun matches(item: Any?): Boolean {
        return if (item != null && item is JsonNode) {
            item.asText() == expectedValue
        } else {
            false
        }
    }

}