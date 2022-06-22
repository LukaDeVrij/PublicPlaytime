# PublicPlaytime

PublicPlaytime is a plugin which makes it easy to see playtimes of every player who has ever been on your server. PublicPlaytime uses the standard Statistic from Bukkit to query the playtime of online players, and a database which holds the time for all offline players. The time played is of course how long a certain player has been logged in on your server. It should be the same as the statistic in vanilla Minecraft (unless you've not had Bukkit from the start).


# Commands

* **/playtime \<player> \<outputMode>** 
	* Gets the playtime for that specific player in a certain format
		* \<outputMode> - _days, hours, minutes, seconds, ticks, standard_
			* _standard_ will output the playtime in DD:HH:MM:SS format.
