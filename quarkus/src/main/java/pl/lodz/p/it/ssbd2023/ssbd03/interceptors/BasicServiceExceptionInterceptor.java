package pl.lodz.p.it.ssbd2023.ssbd03.interceptors;

import jakarta.ejb.AccessLocalException;
import jakarta.ejb.EJBAccessException;
import jakarta.ejb.EJBTransactionRolledbackException;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;
import jakarta.persistence.NoResultException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;

@InterceptionBinding
public class BasicServiceExceptionInterceptor {
    @AroundInvoke
    public Object intercept(InvocationContext ictx) throws Exception {
        try {
            return ictx.proceed();
        } catch (AppException ae) {
            throw ae;
        } catch (NoResultException nre) {
            throw AppException.createNoResultException(nre.getCause());
        }  catch (EJBTransactionRolledbackException e) {
            throw AppException.createTransactionRollbackException();
        } catch (EJBAccessException | AccessLocalException e) {
            throw AppException.createNotAllowedActionException();
        } catch (Exception e) {
            throw AppException.createAppException(e.getCause());
        }
    }
}
