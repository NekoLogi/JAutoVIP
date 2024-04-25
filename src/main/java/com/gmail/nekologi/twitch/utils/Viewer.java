package com.gmail.nekologi.twitch.utils;

import java.io.*;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.omg.CORBA.DynAnyPackage.Invalid;

public class Viewer {
    private final String PATH = "users";
    private String Username;
    private LocalDate LastSeen = LocalDate.now();
    private int ActiveStreak = 1;

    public Viewer(String username)
    {
        constructUsername(username);
    }

    public Viewer(String username, LocalDate lastSeen, int activeStreak)
    {
        constructUsername(username);

        if (lastSeen == null)
            throw new NullPointerException("LastSeen can't be null");
        LastSeen = lastSeen;

        if (activeStreak < 1)
            throw new ArithmeticException("ActiveStreak can't be lower than 1");
        ActiveStreak = activeStreak;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String value) {
        Username = value;
    }

    public Viewer load(String username) {
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

    public void save() {
        init();
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(this, new FileWriter(PATH + "/" + this.Username + ".json"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public int getActiveStreak() {
        return ActiveStreak;
    }

    public void streakIncrement() {
        ActiveStreak++;
        save();
    }

    public void streakReset() {
        ActiveStreak = 1;
        save();
    }

    public LocalDate getLastSeen() {
        return LastSeen;
    }

    public void setLastSeen(LocalDate date) {
        LastSeen = date;
        save();
    }

    private void constructUsername(String username) {
        if (username == null)
            throw new NullPointerException("Username can't be null");
        if (username.isEmpty())
            throw new NullPointerException("Username can't be empty");
        if (username.contains(" "))
            throw new InvalidParameterException("Username can't contain whitespaces");
        Username = username;
    }

    @Override
    public String toString() {
        return "Viewer{" +
                "PATH='" + PATH + '\'' +
                ", Username='" + Username + '\'' +
                ", LastSeen=" + LastSeen +
                ", ActiveStreak=" + ActiveStreak +
                '}';
    }

    private void init() {
        File file = new File(PATH);
        if (!file.exists()) {
            try {
                if (file.createNewFile())
                    System.out.println("Failed to create directory: " + PATH);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
