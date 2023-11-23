package pl.lodz.p.it.ssbd2023.ssbd03.interceptors;

import io.quarkus.security.ForbiddenException;
import jakarta.annotation.Priority;
import jakarta.ejb.AccessLocalException;
import jakarta.ejb.EJBAccessException;
import jakarta.ejb.EJBTransactionRolledbackException;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.persistence.NoResultException;
import jakarta.transaction.TransactionalException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;

@Interceptor
@InterceptionBinding
@Priority(0)
public class BasicServiceExceptionInterceptor {
    @AroundInvoke
    public Object intercept(InvocationContext ictx) throws Exception {
        try {
            return ictx.proceed();
        } catch (AppException ae) {
            throw ae;
        } catch (NoResultException nre) {
            throw AppException.createNoResultException(nre.getCause());
        }  catch (EJBTransactionRolledbackException | TransactionalException e) {
            throw AppException.createTransactionRollbackException();
        } catch (EJBAccessException | AccessLocalException | ForbiddenException e) {
            throw AppException.createNotAllowedActionException();
        } catch (Exception e) {
            throw AppException.createAppException(e.getCause());
        }
    }
}
