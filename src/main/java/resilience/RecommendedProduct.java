package resilience;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RecommendedProduct {

    private final String name;
    private final String teaser;

    public RecommendedProduct(
            @JsonProperty("name") String name,
            @JsonProperty("teaser") String teaser
    ) {
        this.name = name;
        this.teaser = teaser;
    }

    public String getName() {
        return name;
    }

    public String getTeaser() {
        return teaser;
    }
}
