package help.lixin.datasource.keygenerate;

public abstract class AbstractKeyGenerateStrategy implements IKeyGenerateStrategy {

    protected static final String DEFAULT_FORMAT = "%s/%s/%s";

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
