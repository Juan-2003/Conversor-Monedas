package entities;


import java.time.LocalDateTime;
import java.util.ArrayList;

public class CurrencyRegister {
    private LocalDateTime dateTime;
    private String baseCurrency;
    private String targetCurrency;
    private String amount;
    private String conversion;
    private static ArrayList<CurrencyRegister>CurrencyRecord = new ArrayList<>();

    public CurrencyRegister(){}
    public CurrencyRegister(String baseCurrency, String targetCurrency,
                            String amount, String conversion){
        this.dateTime = dateTime.now();
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.amount = amount;
        this.conversion = conversion;

    }

    public LocalDateTime getDateTime(){
        return dateTime;
    }

    public String getBaseCurrency(){
        return baseCurrency;
    }

    public String getTargetCurrency(){
        return targetCurrency;
    }

    public String getAmount(){
        return amount;
    }

    public String getConversion(){
        return conversion;
    }

    public void addCurrency(CurrencyRegister currencyRegister){
        CurrencyRecord.add(currencyRegister);
    }

    public ArrayList<CurrencyRegister> getCurrencyRecord(){
        return CurrencyRecord;
    }

    @Override
    public String toString() {
        return  "\nHISTORIAL\n"
                +dateTime + "  "
                + amount + "[" + baseCurrency + "]"
                + " =>> " + conversion + "[" + targetCurrency + "]";
    }
}
