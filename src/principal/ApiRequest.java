package principal;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.ApiKeyReader;
import entities.CurrencyData;
import entities.CurrencyPair;
import entities.CurrencyRegister;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class ApiRequest {
    private static final String API_KEY;
    static Map<Integer, CurrencyPair>options = new HashMap<>();

    static{
        String apiKey = null;
        try{
            apiKey = ApiKeyReader.keyReader("apiKey.json");
        }catch(FileNotFoundException e){
            System.out.println("Archivo no encontrado");
            apiKey = " ";
        }
        API_KEY = apiKey;
    }

    static {
        options.put(1, new CurrencyPair("USD", "ARS"));
        options.put(2, new CurrencyPair("ARS", "USD"));
        options.put(3, new CurrencyPair("USD", "BRL"));
        options.put(4, new CurrencyPair("BRL", "USD"));
        options.put(5, new CurrencyPair("USD", "COP"));
        options.put(6, new CurrencyPair("COP", "USD"));
    }

    public static void startMenu(){
        int option = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Sea bienvenido al conversor de monedas =]");

        do{
            System.out.println("\nSelecciona una opcion");
            System.out.println("1) Cambio de Moneda");
            System.out.println("2) Historial");
            System.out.println("3) Salir");
            try{
                option = scanner.nextInt();
            }catch(InputMismatchException e){
                System.out.println("** Ingresa un valor númerico** \n");
                scanner.next();
                continue;
            }

            if(option == 1){
                currencyConversionOptions();
            }
            else if(option == 2){
                showList();
            }

        }while(option != 3);

    }

    private static void currencyConversionOptions(){
        int option = 0;
        Scanner scanner = new Scanner(System.in);

        do{
            System.out.println("1) Dólar =>> Peso Argentino");
            System.out.println("2) Peso Argentino =>> Dólar");
            System.out.println("3) Dólar =>> Real brasileño");
            System.out.println("4) Real brasileño =>> Dólar");
            System.out.println("5) Dólar =>> Peso colombiano");
            System.out.println("6) Peso colombiano =>> Dólar");
            System.out.println("7) Salir");
            System.out.println("Elija una opción: ");
            option = scanner.nextInt();


            if(option != 7 && optionIsValid(option) ){
                boolean bandera = true;
                do{
                    System.out.println("Ingrese el valor a convertir: ");
                    double amount = 0;
                    try {
                        amount = scanner.nextDouble();
                        startRequest(option, amount);
                        bandera = false;
                    } catch (InputMismatchException | IOException | InterruptedException e) {
                        System.out.println("Error");
                        scanner.next();
                    }
                }while(bandera);
            }


        }while(option != 7);
    }

    private static boolean optionIsValid(int option){
        return option >= 1 && option <= 6;
    }

    private static void startRequest(int option, double amount) throws IOException, InterruptedException {
        CurrencyPair currencyPair = options.get(option);
        String url = "https://v6.exchangerate-api.com/v6/"+ API_KEY
                    + "/pair/"+currencyPair.baseCurrency()+"/"
                    +currencyPair.targetCurrency()+"/"+amount;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        CurrencyData currencyData = gsonToClass(response);
        addCurrenyRegister(currencyData, amount);
        showResults(currencyData, amount);
    }

    private static CurrencyData gsonToClass(HttpResponse<String>response){
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting()
                .create();

        CurrencyData currencyData = gson.fromJson(response.body(), CurrencyData.class);
        return currencyData;
    }

    private static void showResults(CurrencyData currencyData, double amount){
        System.out.println("El valor " + amount +
                          "["+ currencyData.base_code()+"]"+
                          "corresponde al valor final de =>> "+currencyData.conversion_result() +
                          "["+ currencyData.target_code()+"]");
    }

    private static void addCurrenyRegister(CurrencyData currencyData, double amount){
        CurrencyRegister currencyRegister =
                    new CurrencyRegister(currencyData.base_code(),
                                        currencyData.target_code(),
                                        String.valueOf(amount),
                                        String.valueOf(currencyData.conversion_result()));
        currencyRegister.addCurrency(currencyRegister);
    }

    private static void showList(){

        CurrencyRegister currencyRegister = new CurrencyRegister();
        ArrayList<CurrencyRegister>currencyRecord = new ArrayList<>();
        currencyRecord = currencyRegister.getCurrencyRecord();

        if(!currencyRecord.isEmpty()){
            for (CurrencyRegister c : currencyRecord ) {
                System.out.println(c);
            }
        }else{
            System.out.println("** No hay movimientos **");
        }
    }
}
