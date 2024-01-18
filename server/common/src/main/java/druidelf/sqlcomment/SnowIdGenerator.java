package druidelf.sqlcomment;

import druidelf.util.UtilForData;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import java.io.Serializable;

@SuppressWarnings("unused")
public class SnowIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {

        return UtilForData.getSnowId();
    }

}
