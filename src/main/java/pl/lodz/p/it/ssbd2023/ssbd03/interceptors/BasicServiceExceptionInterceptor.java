package pl.lodz.p.it.ssbd2023.ssbd03.interceptors;

import jakarta.ejb.AccessLocalException;
import jakarta.ejb.EJBAccessException;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;

public class BasicServiceExceptionInterceptor {
    @AroundInvoke
    public Object intercept(InvocationContext ictx) throws Exception {
        try {
            return ictx.proceed();
        } catch (AppException ae) {
            throw ae;
        } catch (EJBAccessException | AccessLocalException e) {
            throw AppException.createNotAllowedActionException();
        } catch (Exception e) {
            throw AppException.createAppException(e.getCause());
        }
    }
}
