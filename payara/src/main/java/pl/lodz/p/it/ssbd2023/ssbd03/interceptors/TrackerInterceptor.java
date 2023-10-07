package pl.lodz.p.it.ssbd2023.ssbd03.interceptors;

import jakarta.annotation.Resource;
import jakarta.ejb.SessionContext;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TrackerInterceptor {
    @Resource
    private SessionContext sctx;
    private static final Logger logger = Logger.getLogger(TrackerInterceptor.class.getName());

    @AroundInvoke
    public Object traceInvoke(InvocationContext ictx) throws Exception {
        StringBuilder message = new StringBuilder("Przechwycone wywołanie metody: ");
        Object result;

        try {
            try {
                message.append(ictx.getMethod().toString());
                message.append(" użytkownik: ").append(sctx.getCallerPrincipal().getName());
                message.append(" wartości parametrów: ");
                if (null != ictx.getParameters()) {
                    for (Object param : ictx.getParameters()) {
                        message.append(param).append(" ");
                    }
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Nieoczekiwany wyjątek w kodzie interceptora: ", e);
                throw e;
            }

            result = ictx.proceed();

        } catch (Exception e) {
            message.append(" zakończone wyjątkiem: ").append(e);
            logger.log(Level.SEVERE, message.toString(), e);
            throw e;
        }

        message.append(" wartość zwrócona: ").append(result).append(" ");
        logger.info(message.toString());
        return result;
    }
}
