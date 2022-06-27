![enter image description here](https://i.imgur.com/YcSSgNX.png)
# PublicPlaytime 

PublicPlaytime is a plugin which makes it easy to see playtimes of every player who has ever been on your server. PublicPlaytime uses the standard Statistic from Bukkit* to query the playtime of online players, and a database which holds the time for all offline players. The time played is of course how long a certain player has been logged in on your server. It should be the same as the statistic in vanilla Minecraft that people can access via the statistics menu. **This also means, that when you switch the main world of your server, the playtime will be reset.**

*Any forks of Bukkit will suffice (i.e. Spigot, Paper etc.)

## Commands
PublicPlaytime several functions.

* **/playtime get \<player> \<outputMode>** 
	* Gets the playtime for that specific player in a certain format
		* \<outputMode> - _days, hours, minutes, seconds, ticks, standard_
			* _standard_ will output the playtime in DD:HH:MM:SS format.

Output when executing: */playtime get LifelessNerd standard*
![Playtime command preview](https://i.imgur.com/0egFpdI.png)

* **/playtime ranking \<outputMode> \<maxAmount>** 
	* Outputs a leaderboard of playtime in your chat in standard formatting. 
		* \<maxAmount> - _any integer_ 
			* The amount of places that the printed leaderboard will hold. I.e. _5_ will get you the top 5 players.
		* \<outputMode> - _days, hours, minutes, seconds, ticks, standard_
			* _standard_ will output the playtime in DD:HH:MM:SS format.

Output when executing: */playtime ranking standard 10*
![Playtimeranking command preview](https://i.imgur.com/RnNj3Mb.png)

* **/playtime scoreboard \<show/hide> \<outputMode> \<maxAmount>** 
	* Shows or hides a scoreboard of the players with the highest amount of playtime.
		* \<show/hide> _'show' or 'hide'_
			* Whether to show or hide a scoreboard.
		* \<maxAmount> - _any integer_ 
			* The amount of places that the printed leaderboard will hold. I.e. _5_ will get you the top 5 players.
		* \<outputMode> - _days, hours, minutes, seconds, ticks_
			* _standard_ is not permitted here, since it does not fit in a scoreboard.

> Note: This command has options in the config, for whether to refresh the scoreboard and at what interval. See 'Files'.

Output when executing: */playtime scoreboard show minutes 10*
![Playtimescoreboard command preview](https://i.imgur.com/qcpobwm.png)

* **/playtime help**
	* Shows a list of all commands that the plugin has.

* **/playtime about**
	* Shows information about the plugin, where to find it and more.

## Files 
Upon first starting the plugin, 2 YAML files will be created, a config and a database. The config will hold options for the plugin for you to edit. There are 2 settings that can be changed, whether to allow scoreboards to refresh, and if so, at what interval they should do so. Anything below 20 ticks (1 second) will not have an effect, since the statistics themselves are done with seconds.

The database is not to be edited. It stores playtimes as ticks per player. You can add players, although this is already done by the plugin when people first join, and any values will get overwritten when a player quits the server. 

## Upcoming feature(s) (hopefully) 
- Ways to carry over data from old worlds to new worlds
- PlaceholderAPI support

## Known issues
Currently no known issues.

If there are any problems, create a message below, add them on GitHub, [here](https://github.com/LifelessNerd/PublicPlaytime), or DM me on [Twitter](https://twitter.com/nerdlifeless).
