package me.lifelessnerd.publicplaytime;

import me.lifelessnerd.publicplaytime.filehandlers.PlaytimeDatabase;
import me.lifelessnerd.publicplaytime.commands.legacy.GetPlaytime;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.*;

public class PlaytimeRanking {

    public HashSet<String> playerNames = new HashSet<>();
    public HashMap<String, Integer> rankingInts = new HashMap<String, Integer>();

    //Constructor
    public PlaytimeRanking(){

        // all offline players
        FileConfiguration fileConfiguration = PlaytimeDatabase.get();
        Set<String> offlinePlayerNames =  fileConfiguration.getKeys(false);
        playerNames.addAll(offlinePlayerNames);
        //Getting all online players and adding them
        for (Player player: Bukkit.getOnlinePlayers()){
            String playerName = player.getName();
            playerNames.add(playerName);
        }
        // Online values will overwrite the offline values due to nature of HashSet

    }

    public HashSet<String> getPlayerNames(){

        return playerNames;
    }

    public HashMap<String, String> getRanking(Set<String> playerNames, int maxAmount, String outputMode){

        FileConfiguration fileConfiguration = PlaytimeDatabase.get();
        GetPlaytime getPlaytimeObject = new GetPlaytime();

        for (String playerName : playerNames){
            // Yes this is duplicate code, duck me

            int score = 0;
            Player argumentPlayer = null;
            try {
                // Try and get the player out of the online pool of players
                argumentPlayer = Bukkit.getServer().getPlayerExact(playerName);
                String name = argumentPlayer.getName();
                score = argumentPlayer.getStatistic(Statistic.PLAY_ONE_MINUTE);
                rankingInts.put(playerName, score);


            }catch(Exception exception){
                // If we're in here that means the player is not online
                // We try to get the data from the database

                try {
                    score = Integer.parseInt(PlaytimeDatabase.get().getString(playerName));
                    //Keeping this in here to make try catch work
                    // If this fails, i.e. player is not in database, we give up
                    rankingInts.put(playerName, score);



                }catch(Exception exception2){
                    System.out.println("Something went seriously wrong! Contact dev!");
                    System.out.println(exception2.getMessage());

                }

            }

        }
        // We now have a HashMap with playerNames and scores in int-form
        // Let the ranking begin!

        rankingInts = sortByValue(rankingInts);

        // Now we slice the array based on maxAmount & Go from int to String
        HashMap<String, String> slicedRankingStrings = new LinkedHashMap<String, String>();

        int i = 0;
        for (String key : rankingInts.keySet()) {
            if (i < maxAmount){
                int IntValue = rankingInts.get(key);

                int seconds = IntValue / 20;
                Duration playTime = Duration.ofSeconds(seconds);
                long DD = playTime.toDays();
                long HH = playTime.toHoursPart();
                long MM = playTime.toMinutesPart();
                long SS = playTime.toSecondsPart();

                String output = "";
                switch(outputMode){
                    default:
                        output = String.format("%s days, %s hours, %s minutes & %s seconds",DD,HH,MM,SS);
                        break;
                    case "seconds":
                        output = seconds + " seconds";
                        break;
                    case "minutes":
                        output = playTime.toMinutes() + " minutes";
                        break;
                    case "hours":
                        output = playTime.toHours() + " hours";
                        break;
                    case "days":
                        output = playTime.toDays() + " days";
                        break;
                    case "ticks":
                        output = playTime.toSeconds() * 20 + " ticks";
                }

                slicedRankingStrings.put(key, output);

                i++;
            }
        }

        return slicedRankingStrings;
    }

    // function to sort hashmap by values by GeeksForGeeks
    private static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hashMap)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer> > list =
                new LinkedList<Map.Entry<String, Integer> >(hashMap.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,Map.Entry<String, Integer> o2)
            {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

}
