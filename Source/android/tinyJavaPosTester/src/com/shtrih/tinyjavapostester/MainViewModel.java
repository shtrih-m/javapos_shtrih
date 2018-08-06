package com.shtrih.tinyjavapostester;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableField;

import com.shtrih.fiscalprinter.ShtrihFiscalPrinter;
import com.shtrih.util.StaticContext;
import com.shtrih.util.SysUtils;

import jpos.FiscalPrinter;

public class MainViewModel extends AndroidViewModel {
    private ShtrihFiscalPrinter printer;

    public final ObservableField<String> ScocUpdaterStatus = new ObservableField<>();

    public MainViewModel(Application app){
        super(app);

        StaticContext.setContext(this.getApplication());
        printer = new ShtrihFiscalPrinter(new FiscalPrinter());
    }

    public ShtrihFiscalPrinter getPrinter() {
        return printer;
    }
}
