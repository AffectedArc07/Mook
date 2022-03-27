package me.aa07.mook;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import me.aa07.mook.config.sections.Data;

public class DataManager {
    private Data config;
    private Logger logger;

    private ArrayList<Fortune> fortunes;

    public DataManager(Data config, Logger logger) {
        this.config = config;
        this.logger = logger;
        fortunes = new ArrayList<Fortune>();
    }

    public int getFortuneCount() {
        return fortunes.size();
    }

    public Fortune pickFortune() {
        int fortune_id = new Random().ints(0, fortunes.size()).findFirst().getAsInt();
        return fortunes.get(fortune_id);
    }

    public boolean refresh() {
        // Make a backup if this fails.
        ArrayList<Fortune> fortunes_backup = new ArrayList<Fortune>();

        for (Fortune f : fortunes) {
            fortunes_backup.add(f);
        }

        // Clear it
        fortunes.clear();

        // Pull data from google
        try {
            NetHttpTransport transport = new NetHttpTransport.Builder().build();
            JacksonFactory json_factory = JacksonFactory.getDefaultInstance();
            HttpRequestInitializer http_req_init = request -> {
                request.setInterceptor(intercepted -> intercepted.getUrl().set("key", config.apiKey));
            };

            Sheets service = new Sheets.Builder(transport, json_factory, http_req_init).setApplicationName("MookBot").build();

            // Get our initial range
            String initial_range = "Fortunes!B1";
            ValueRange initial_response = service.spreadsheets().values().get(config.spreadsheetId, initial_range).execute();

            if (initial_response.getValues().isEmpty()) {
                logger.warning("No data returned from google!");
                return false;
            }

            List<List<Object>> initial_values = initial_response.getValues();


            // Get our full range
            String full_range = String.format("Fortunes!A2:%s", initial_values.get(0).get(0));
            ValueRange full_response = service.spreadsheets().values().get(config.spreadsheetId, full_range).execute();

            if (full_response.getValues().isEmpty()) {
                logger.warning("No data returned from google!");
                // Restore backup
                fortunes = fortunes_backup;
                return false;
            }

            List<List<Object>> fortune_values = full_response.getValues();
            for (List<Object> fortune_row : fortune_values) {
                Fortune f = new Fortune();
                f.fortuneText = (String) fortune_row.get(0);
                f.imageUrl = (String) fortune_row.get(1);
                fortunes.add(f);
            }


        } catch (Exception e) {
            logger.warning("Error loading fortunes");
            // Restore backup
            fortunes = fortunes_backup;
            return false;
        }

        return true;
    }

    public class Fortune {
        public String fortuneText;
        public String imageUrl;
    }
}
