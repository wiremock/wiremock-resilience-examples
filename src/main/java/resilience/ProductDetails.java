package resilience;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductDetails {

    private final String name;
    private final String description;
    private final String imageUrl;

    public ProductDetails(
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("imageUrl") String imageUrl
    ) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
