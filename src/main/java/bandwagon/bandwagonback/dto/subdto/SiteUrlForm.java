package bandwagon.bandwagonback.dto.subdto;

import lombok.Data;

@Data
public class SiteUrlForm {

    private String siteName;
    private String url;

    public SiteUrlForm() {}

    public SiteUrlForm(String siteName, String url) {
        this.siteName = siteName;
        this.url = url;
    }
}
