package help.lixin.datasource.keygen;

/**
 * 根据上下文生成key
 */
public interface IKeyGenerateService {
    String generate(Object object);
}
