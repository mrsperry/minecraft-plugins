package io.github.mrsperry.rifts.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.Random;

public class NameUtils {
    private static Random random;

    private static ArrayList<String> names;
    private static ArrayList<String> prefixes;
    private static ArrayList<String> suffixes;
    private static ArrayList<ChatColor> colors;
    private static ChatColor bossNameColor;

    public NameUtils(Random globalRandom, ArrayList<String> namesList, ArrayList<String> prefixList, ArrayList<String> suffixList, ArrayList<ChatColor> colorList, ChatColor nameColor) {
        random = globalRandom;

        names = namesList;
        prefixes = prefixList;
        suffixes = suffixList;
        colors = colorList;
        bossNameColor = nameColor;
    }

    public static String getRandomBossName() {
        return getRandomColor() + getString(prefixes) + " " + bossNameColor + getString(names) + " " + getRandomColor() + getString(suffixes) + ChatColor.RESET;
    }

    private static ChatColor getRandomColor() {
        return colors.get(random.nextInt(colors.size()));
    }

    private static String getString(ArrayList<String> strings) {
        return strings.get(random.nextInt(strings.size()));
    }
}
