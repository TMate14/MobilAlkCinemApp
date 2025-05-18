package com.example.cinemapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemapp.models.Movie;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class WeeklyMovieActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Spinner weekSelector;
    private int selectedWeekOffset = 0; // 0 = this week, 1 = next week, 2 = two weeks ahead
    private MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_movie);

        recyclerView = findViewById(R.id.recyclerViewMovies);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Movie> movies = getFixedMovieList();
        recyclerView.setAdapter(new MovieAdapter(this, movies));

        weekSelector = findViewById(R.id.weekSelector);

        String[] weeks = new String[3];
        LocalDate today = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            today = LocalDate.now();
        }
        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("MMM dd");
        }

        for (int i = 0; i < 3; i++) {
            LocalDate startOfWeek = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startOfWeek = today.plusWeeks(i).with(DayOfWeek.MONDAY);
            }
            LocalDate endOfWeek = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                endOfWeek = startOfWeek.plusDays(6);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                weeks[i] = startOfWeek.format(formatter) + " - " + endOfWeek.format(formatter);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, weeks);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekSelector.setAdapter(adapter);

        weekSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);
                    selectedWeekOffset = position;
                    updateMovieList();
                } catch (Exception e) {
                    Log.d("Unknown", "Error");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: do nothing
            }
        });
    }

    private void updateMovieList() {
        // Get the movie list (could be filtered by selectedWeekOffset later if needed)
        List<Movie> movies = getFixedMovieList();

        // Update the adapter's data
        if (adapter == null) {
            adapter = new MovieAdapter(this, movies);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setMovieList(movies);
            adapter.notifyDataSetChanged();
        }
    }

    private List<Movie> getFixedMovieList() {
        List<Movie> week1Movies = Arrays.asList(
                new Movie("Midnight Coder", "Akció / Dokumentum", "1ó 45p", "8.2", R.drawable.midnight_coder, "Egy éjszaka. Egy fejlesztő. Nulláról újraírt mobil app. A kávéfőző megadta magát, a git konfliktusok elszabadultak, és a designer egy új feature-t követel hajnalban. Az idő ellen harcolva a fejlesztőnek mindent be kell vetnie: Stack Overflow-t, chatGPT-t, sőt, végső esetben még a saját komment nélküli kódját is értelmeznie kell."),
                new Movie("AI Apocalipse", "Sci-Fi / Horror", "2ó 10p", "8.6", R.drawable.ai_apocalipse, "Miután az emberiség átadta az irányítást a mesterséges intelligenciának, minden tökéletes lett. Túl tökéletes. Az emberek már csak \"felhasználók\", a döntéseiket algoritmusok hozzák, a szabad akarat pedig… béta státuszba került."),
                new Movie("Fire Teacher", "Dráma / Vígjáték", "1ó 40p", "7.4", R.drawable.fire_teacher, "Amikor egy kiégett, szó szerint tűzveszélyes természetű kémiatanár, áthelyezést kap egy problémás külvárosi iskolába, senki sem sejti, hogy hamarosan nemcsak a lombikok, de a diákok szívei is lángra kapnak (néha tényleg).")
        );

        List<Movie> week2Movies = Arrays.asList(
                new Movie("Galactic Command", "Sci-Fi / Akció", "2ó 20p", "8.8", R.drawable.galactic_command, "Amikor egy renegát bolygó meghekkeli a központi csillagrendszert, a Galaktikus Parancsnokság egy csapat félreértett hőst küld, akik jobban értenek a robbanásokhoz, mint a diplomáciához."),
                new Movie("Galactus", "Sci-Fi / Dráma", "2ó 30p", "8.5", R.drawable.galactus, "Egy ismeretlen lény érkezik a galaxis pereméről. Óriási, hangtalan, és mindenről tud. Nem támad. Nem válaszol. Csak figyel. És napról napra közelebb jön.\n" +
                        "\n" +
                        "Az emberek, kormányok és civilizációk eltérően reagálnak: egyesek istenként imádják, mások fegyverkeznek ellene, míg néhányan megpróbálják… megszólítani."),
                new Movie("Math", "Dokumentum / Horror", "1ó 30p", "7.9", R.drawable.math, "Képletek. Számok. Végtelen zárójelek. És egy tanár, aki soha nem magyaráz újra.\n" +
                        "Ez a dokumentumfilm/horror hibrid visszavisz a középiskolai matekterem sötét bugyraiba, ahol a táblán egy egyenlet lassan… megmozdul.")
        );

        List<Movie> week3Movies = Arrays.asList(
                new Movie("Revenge of the Ducks", "Vígjáték / Akció", "1ó 55p", "8.1", R.drawable.revenge_of_the_ducks, "Évtizedeken át az emberek kenyérrel etették őket. Most visszavágnak.\n" +
                        "Egy csendes kisváros élete felfordul, amikor a helyi tó lakói – a kacsák – kollektív tudatra ébrednek és megelégelik a morzsákat... és az emberi arroganciát."),
                new Movie("Romantic Dinner", "Romantikus / Dráma", "1ó 50p", "7.7", R.drawable.romantic_dinner, "Egy Michelin-csillagos séf és egy ételallergiás ételkritikus, vakrandira kerülnek egy luxusétteremben. Ő a habos-babos tálalás mestere, ő pedig a „külön kérném szósz nélkül, vegán, gluténmentesen” királynője."),
                new Movie("Midnight Coder", "Akció / Dokumentum", "1ó 45p", "8.2", R.drawable.midnight_coder, "\"Egy éjszaka. Egy fejlesztő. Nulláról újraírt mobil app. A kávéfőző megadta magát, a git konfliktusok elszabadultak, és a designer egy új feature-t követel hajnalban. Az idő ellen harcolva a fejlesztőnek mindent be kell vetnie: Stack Overflow-t, chatGPT-t, sőt, végső esetben még a saját komment nélküli kódját is értelmeznie kell.\"")
        );

        if (selectedWeekOffset == 0) return week1Movies;
        if (selectedWeekOffset == 1) return week2Movies;
        if (selectedWeekOffset == 2) return week3Movies;

        return week1Movies; // fallback
    }
}