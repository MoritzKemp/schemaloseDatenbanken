--- Pre ----
Vor der Ausführung:
- laufen alle HDFS-Datanodes? Check 10.20.110.61:50071
- laufen alle YARN-Nodemanager? Check 10.20.110.61:8086
- Ausgabeverzeichnis für das MapReduce-Ergebnis darf noch nicht existieren.
  Löschen z.B. mit $~ ./hadoop/bin/hdfs dfs -rm -r /millionSong/result/run1

--- Ausführen auf dem Cluster ---
$~ ./hadoop/bin/hadoop jar <jar-location> <class>

Beispiel (jar liegt auf home):
$~ ./hadoop/bin/hadoop jar mapReduceJars/CompleteToStripped.jar CompleteToStripped

--- Monitoring ---
Dies Jobs werden mit YARN verwaltet. Den Job-Status sieht man
auf der YARN-WebUI unter 10.20.110.61:8086

--- Trouble ---
Wenn der Job stockt, kann er "relativ" geordnet über
die YARN-WebUI ge-killt werden. Unter "Applikations" auf
den "RUNNING"-Job klicken und dann links oben der Button 
"Kill Application".

Oft werden auf HDFS-Datanodes und YARN-Nodemanager wegen überlastung
gekillt. Die müssen dann noch vor einer erneuten ausführen neu gestartet
werden.
Einzelne HDFS-Nodes kann man mit 
"$~ ./hadoop/bin/hadoop-daemon.sh start datanode" 
und 
"$~ ./hadoop/bin/yarn-daemon.sh start nodemanager" 
wieder starten.
Und nicht vergessen, das Ausgabeverzeichnis wieder zu löschen.
