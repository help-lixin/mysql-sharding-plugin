package help.lixin.datasource.keygenerate;

/**
 * 根据上下文生成key
 */
public interface IKeyGenerateStrategy {
    String generate(Object object);
}
