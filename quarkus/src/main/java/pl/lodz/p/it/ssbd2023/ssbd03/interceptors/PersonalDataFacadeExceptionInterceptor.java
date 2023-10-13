package pl.lodz.p.it.ssbd2023.ssbd03.interceptors;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.DataException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;

@PersonalDataBinding
public class PersonalDataFacadeExceptionInterceptor {
    @AroundInvoke
    public Object intercept(InvocationContext ictx) throws Exception {
        try {
            return ictx.proceed();
        } catch (PersistenceException pe) {
            if (pe.getCause() instanceof DataException)
                throw AppException.createPersonalDataConstraintViolationException();
            throw pe;
        }
    }
}
