package help.lixin.datasource.keygen;

public abstract class AbstractKeyGenerateService implements IKeyGenerateService {

    protected static final String DEFAULT_FORMAT = "%s";

    private String format;

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        if (null == format) {
            return DEFAULT_FORMAT;
        }
        return format;
    }

    protected String generate0(String... args) {
        return String.format(getFormat(), args);
    }
}
