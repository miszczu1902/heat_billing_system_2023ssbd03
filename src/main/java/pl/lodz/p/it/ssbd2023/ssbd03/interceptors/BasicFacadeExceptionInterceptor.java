package pl.lodz.p.it.ssbd2023.ssbd03.interceptors;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;
import jakarta.persistence.NoResultException;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;

public class BasicFacadeExceptionInterceptor {
    @AroundInvoke
    public Object intercept(InvocationContext ictx) throws Exception {
        try {
            return ictx.proceed();
        } catch (OptimisticLockException ole) {
            throw AppException.createOptimisticLockAppException();
        } catch (PersistenceException | java.sql.SQLException pe) {
            if (pe.getCause() instanceof ConstraintViolationException)
                throw AppException.createAccountExistsException(pe.getCause());
            if (pe.getCause() instanceof NoResultException)
                throw AppException.createNoResultException(pe.getCause());
            throw AppException.createPersistenceException(pe);
        } catch (AppException ae) {
            throw ae;
        } catch (Exception e) {
            throw AppException.createAppException(e.getCause());
        }
    }
}
