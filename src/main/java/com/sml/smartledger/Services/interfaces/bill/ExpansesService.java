package com.sml.smartledger.Services.interfaces.bill;


import com.sml.smartledger.Model.bill.Expanses;

import java.util.List;

public interface ExpansesService {

    public List<Expanses> getAllExpanses(Long businessId);
    public Expanses createExpanses( Expanses expanses);
    public void deleteExpanses(Long expansesId);
}
