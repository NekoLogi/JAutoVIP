package com.gmail.nekologi.twitch.utils;

import java.io.*;
import java.time.LocalDate;
import com.google.gson.Gson;

public class Viewer {
    private final String PATH = "users";
    public String Username;
    public LocalDate LastSeen = LocalDate.now();
    public int ActiveStreak;

    public Viewer(String username)
    {
        Username = username;
    }

    public Viewer(String username, LocalDate lastSeen, int activeStreak)
    {
        Username = username;
        LastSeen = lastSeen;
        ActiveStreak = activeStreak;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String value) {
        Username = value;
    }

    public Viewer load(String username)
    {
        init();
        String path = PATH + "/" + username + ".json";
        if (username == null)
            return null;
        if (username.isEmpty())
            return null;

        File file = new File(path);
        if (file.exists())
        {
            try (FileReader reader = new FileReader(file)) {
                String json = new BufferedReader(reader).lines().toString();
                return new Gson().fromJson(json, Viewer.class);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }
        }

        return null;
    }

    public void save()
    {
        init();
        try {
            new Gson().toJson(this, new FileWriter(PATH + "/" + this.Username + ".json"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void streakIncrement()
    {
        ActiveStreak++;
        save();
    }

    public void streakReset()
    {
        ActiveStreak = 1;
        save();
    }

    public void setLastSeen(LocalDate date)
    {
        LastSeen = date;
        save();
    }

    private void init()
    {
        File file = new File(PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
