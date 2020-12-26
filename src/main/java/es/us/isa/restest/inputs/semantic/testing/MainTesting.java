package es.us.isa.restest.inputs.semantic.testing;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import es.us.isa.restest.configuration.pojos.Operation;
import es.us.isa.restest.configuration.pojos.TestConfigurationObject;
import es.us.isa.restest.specification.OpenAPISpecification;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static es.us.isa.restest.configuration.TestConfigurationIO.loadConfiguration;
import static es.us.isa.restest.inputs.semantic.testing.api.ApiBasketball.*;
import static es.us.isa.restest.inputs.semantic.testing.api.ApiFootball.*;
import static es.us.isa.restest.inputs.semantic.testing.api.Asos.*;
import static es.us.isa.restest.inputs.semantic.testing.api.CarbonFootprint.carbonFootprint_PM;
import static es.us.isa.restest.inputs.semantic.testing.api.CoronavirusMap.coronavirusMap_region;
import static es.us.isa.restest.inputs.semantic.testing.api.CountriesCities.*;
import static es.us.isa.restest.inputs.semantic.testing.api.FlightData.*;
import static es.us.isa.restest.inputs.semantic.testing.api.GreatCircleMapper.greatCircleMapper_aircraft_iataIcao;
import static es.us.isa.restest.inputs.semantic.testing.api.MovieDatabase.movieDatabase_imdbId;
import static es.us.isa.restest.inputs.semantic.testing.api.PublicHoliday.publicHoliday_countryCode;
import static es.us.isa.restest.inputs.semantic.testing.api.PublicHoliday.publicHoliday_year;
import static es.us.isa.restest.inputs.semantic.testing.api.Skyscanner.*;
import static es.us.isa.restest.inputs.semantic.testing.api.TrueWayGeocoding.trueWayGeocoding_language;
import static es.us.isa.restest.inputs.semantic.testing.api.UsRestaurantMenus.usRestaurantMenus_restaurantsState_state;
import static es.us.isa.restest.inputs.semantic.testing.api.WeatherForecast14Days.*;
import static es.us.isa.restest.util.PropertyManager.readProperty;


public class MainTesting {

    // Parámetros a cambiar
    private static String propertiesPath = "/semantic/asos.properties";
    private static String operationPath = "/categories/list";
    private static String semanticParameterName = "country";
    private static Integer limit = Integer.MAX_VALUE;
    private static String apiKey = "---";

    // Parámetros derivados
    private static OpenAPISpecification spec;
    private static String confPath;
    private static String OAISpecPath;
    private static Operation operation;
    private static String host;
    private static TestConfigurationObject conf;

    public static void main(String[] args) throws IOException, InterruptedException {
        setParameters(readProperty("evaluation.properties.dir") + propertiesPath);

        String csvPath = getCsvPath();
        List<String> semanticInputs = readCsv(csvPath);


        System.out.println("Number of inputs " + semanticInputs.size());

        Integer maxCut = (limit < 20) ? limit : 20;

        Collections.shuffle(semanticInputs);

        // Select 20 random values
        List<String> randomSubList = semanticInputs.subList(0, Math.min(maxCut, semanticInputs.size()));


        // API Calls
        int i = 1;
        for(String semanticInput: randomSubList){
            try {

                System.out.println(semanticInput);

                System.out.println("Iteración número " + i + "/" + maxCut);

                asos_categoriesList_country(semanticInput, apiKey, host);

                i++;


            }catch (Exception e){
                System.out.println(e);
            }

            TimeUnit.SECONDS.sleep(6);

        }


    }

    private static void setParameters(String propertyPath){
        OAISpecPath = readProperty(propertyPath, "oaispecpath");
        confPath = readProperty(propertyPath, "confpath");
        spec = new OpenAPISpecification(OAISpecPath);

        conf = loadConfiguration(confPath, spec);

        operation = conf.getTestConfiguration().getOperations().stream().filter(x -> x.getTestPath().equals(operationPath)).findFirst().get();
        host = operation.getTestParameters().stream().filter(x-> x.getName().equals("X-RapidAPI-Host")).findFirst().get().getGenerator().getGenParameters().get(0).getValues().get(0);

    }

    private static String getCsvPath(){
        return operation.getTestParameters().stream()
                .filter(x-> x.getName().equals(semanticParameterName))
                .findFirst().get()
                .getGenerator()
                .getGenParameters().get(0).getValues().get(0);
    }

    public static List<String> readCsv(String csvFile) {

        List<String> res = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            String line = "";
            while((line = br.readLine()) != null) {
                res.add(line);
            }
            br.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return res;
    }


}