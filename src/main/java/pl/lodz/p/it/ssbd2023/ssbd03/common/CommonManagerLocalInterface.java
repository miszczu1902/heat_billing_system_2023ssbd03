package pl.lodz.p.it.ssbd2023.ssbd03.common;

import jakarta.ejb.Local;

@Local
public interface CommonManagerLocalInterface {

    public boolean isLastTransactionRollback();
}
